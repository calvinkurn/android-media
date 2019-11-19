package com.tokopedia.train.seat.presentation.fragment.viewpager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
    public Fragment getItem(int position) {
        return TrainWagonFragment.newInstance(wagons.get(position), interactionListener);
    }
}
