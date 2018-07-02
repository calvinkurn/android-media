package com.tokopedia.train.reviewdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rizky on 02/07/18.
 */
public class TrainReviewDetailFragment extends BaseListFragment<TrainSeatPassengerViewModel, TrainPassengerAdapterTypeFactory> {

    public TrainReviewDetailFragment newInstance(List<TrainSeatPassengerViewModel> trainSeatPassengerViewModels) {
        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList((ArrayList<TrainSeatPassengerViewModel>) trainSeatPassengerViewModels);
        fragment.setArguments(bundle);
        return new TrainReviewDetailFragment();
    }

    @Override
    public void loadData(int page) {

    }

    @Override
    protected TrainPassengerAdapterTypeFactory getAdapterTypeFactory() {
        return new TrainPassengerAdapterTypeFactory();
    }

    @Override
    public void onItemClicked(TrainSeatPassengerViewModel trainSeatPassengerViewModel) {

    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

}