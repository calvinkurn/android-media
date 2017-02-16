package com.tokopedia.tkpd.home.banner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import com.tokopedia.tkpd.home.banner.holder.BannerViewHolderCreator;
import com.tokopedia.tkpd.home.banner.holder.Holder;
import com.tokopedia.tkpd.home.banner.view.BannerLoopViewPager;

import java.util.List;

public class BannerPageAdapter<T> extends FragmentStatePagerAdapter {
    protected List<T> mDatas;
    protected BannerViewHolderCreator holderCreator;
    private boolean canLoop = true;
    private BannerLoopViewPager viewPager;
    private Holder holder;
    private final int MULTIPLE_COUNT = 300;

    public int toRealPosition(int position) {
        int realCount = getRealCount();
        if (realCount == 0)
            return 0;
        int realPosition = position % realCount;
        return realPosition;
    }

    @Override
    public int getCount() {
        return canLoop ? getRealCount() * MULTIPLE_COUNT : getRealCount();
    }

    public int getRealCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    @Override
    public Fragment getItem(int position) {
        int realPosition = toRealPosition(position);
        return getFragment(realPosition);
    }


    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
        int position = viewPager.getCurrentItem();
        if (position == 0) {
            position = viewPager.getFristItem();
        } else if (position == getCount() - 1) {
            position = viewPager.getLastItem();
        }
        try {
            viewPager.setCurrentItem(position, false);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }

    public void setViewPager(BannerLoopViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public BannerPageAdapter(FragmentManager fm, BannerViewHolderCreator holderCreator, List<T> mDatas) {
        super(fm);
        this.holderCreator = holderCreator;
        this.mDatas = mDatas;
        this.holder = (Holder) holderCreator.createHolder();
    }


    public Fragment getFragment(int position) {
        return holder.createFragment(mDatas.get(position));
    }

}
