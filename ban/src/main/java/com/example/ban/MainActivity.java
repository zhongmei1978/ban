package com.example.ban;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    List<ContentFragment> datas;

    ViewGroup group;
    ImageView[] imageViews;
    ImageView imageView;
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager=findViewById(R.id.vp);
        group=findViewById(R.id.viewGroup);
        initPointer();

        datas = new ArrayList<>();
        datas.add(new ContentFragment(R.drawable.t1));
        datas.add(new ContentFragment(R.drawable.t2));
        datas.add(new ContentFragment(R.drawable.t3));

        ContentPagerAdapter adapter= new ContentPagerAdapter(this,datas);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                //判断当前是在那个page，就把对应下标的ImageView原点设置为选中状态的图片
                for (int i = 0; i < imageViews.length; i++) {
                    imageViews[position].setBackgroundResource(R.mipmap.page_indicator_unfocused);
                    if (position != i) {
                        imageViews[i].setBackgroundResource(R.mipmap.page_indicator_focused);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                isRunning = true;
                while(isRunning){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int cur_item=viewPager.getCurrentItem();
                            cur_item=(cur_item+1)%imageViews.length;
                            viewPager.setCurrentItem(cur_item);
                        }
                    });
                }
            }
        }).start();
    }



    //初始化下面的小圆点的方法
   public void initPointer() {
        //有多少个界面就new多长的数组
        imageViews = new ImageView[3];
        for (int i = 0; i < imageViews.length; i++) {
            imageView = new ImageView(this);
            //设置控件的宽高
            imageView.setLayoutParams(new LinearLayoutCompat.LayoutParams(85, 85));
            //设置控件的padding属性
            imageView.setPadding(20, 0, 20, 0);
            imageViews[i] = imageView;
            //初始化第一个page页面的图片的原点为选中状态
            if (i == 0) {
                //表示当前图片
                imageViews[i].setBackgroundResource(R.mipmap.page_indicator_unfocused);
                /**
                 * 在java代码中动态生成ImageView的时候
                 * 要设置其BackgroundResource属性才有效
                 * 设置ImageResource属性无效
                 */

            } else {
                imageViews[i].setBackgroundResource(R.mipmap.page_indicator_focused);
            }
            group.addView(imageViews[i]);
        }
    }
}