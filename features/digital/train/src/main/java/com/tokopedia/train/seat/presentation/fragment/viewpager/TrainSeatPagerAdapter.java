package com.tokopedia.train.seat.presentation.fragment.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.train.seat.presentation.fragment.TrainWagonFragment;

import java.util.List;

public class TrainSeatPagerAdapter extends FragmentStatePagerAdapter {

    private List<TrainWagonFragment> fragments;

    public TrainSeatPagerAdapter(FragmentManager fm, List<TrainWagonFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    /**
     * @return the number of pages to display
     */
    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Item " + (position + 1);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}