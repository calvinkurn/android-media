package com.tokopedia.train.seat.presentation.fragment.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.train.seat.presentation.fragment.TrainWagonFragment;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;

import java.util.List;

public class TrainWagonsPagerAdapter extends FragmentStatePagerAdapter {

    private List<TrainWagonViewModel> wagons;
    private TrainWagonFragment.OnFragmentInteraction interactionListener;

    public TrainWagonsPagerAdapter(FragmentManager fm, List<TrainWagonViewModel> wagons, TrainWagonFragment.OnFragmentInteraction interactionListener) {
        super(fm);
        this.wagons = wagons;
        this.interactionListener = interactionListener;
    }

    /**
     * @return the number of pages to display
     */
    @Override
    public int getCount() {
        return wagons.size();
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
        return TrainWagonFragment.newInstance(wagons.get(position), interactionListener);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }
}
