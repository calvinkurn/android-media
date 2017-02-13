package com.tokopedia.tkpd.home.banner.listener;

import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import java.util.ArrayList;

public class BannerPageChangeListener implements ViewPager.OnPageChangeListener {
    private ArrayList<ImageView> pointViews;
    private int[] pageIndicatorId;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    public BannerPageChangeListener(ArrayList<ImageView> pointViews, int pageIndicatorId[]){
        this.pointViews=pointViews;
        this.pageIndicatorId = pageIndicatorId;
    }
    @Override
    public void onPageScrollStateChanged(int state) {
        if(onPageChangeListener != null)onPageChangeListener.onPageScrollStateChanged(state);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(onPageChangeListener != null)onPageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int index) {
        for (int i = 0; i < pointViews.size(); i++) {
            pointViews.get(index).setImageResource(pageIndicatorId[1]);
            if (index != i) {
                pointViews.get(i).setImageResource(pageIndicatorId[0]);
            }
        }
        if(onPageChangeListener != null)onPageChangeListener.onPageSelected(index);

    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }
}
