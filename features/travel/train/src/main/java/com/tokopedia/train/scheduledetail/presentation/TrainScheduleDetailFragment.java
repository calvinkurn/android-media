package com.tokopedia.train.scheduledetail.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tokopedia.tkpdtrain.R;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

/**
 * Created by Rizky on 14/05/18.
 */
public class TrainScheduleDetailFragment extends BaseListFragment<TrainScheduleRouteDetailViewModel, TrainScheduleRouteDetailTypeFactory> {

    public static final String EXTRA_TRAIN_SCHEDULE_VIEW_MODEL = "EXTRA_TRAIN_SCHEDULE_VIEW_MODEL";

    private TrainScheduleViewModel trainScheduleViewModel;

    public static Fragment createInstance(TrainScheduleViewModel trainScheduleViewModel) {
        TrainScheduleDetailFragment fragment = new TrainScheduleDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_TRAIN_SCHEDULE_VIEW_MODEL, trainScheduleViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trainScheduleViewModel = getArguments().getParcelable(EXTRA_TRAIN_SCHEDULE_VIEW_MODEL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_train_schedule_detail, container, false);
    }

    @Override
    public void loadData(int page) {
        renderList(trainScheduleViewModel.getScheduleRouteList());
    }

    @Override
    protected TrainScheduleRouteDetailTypeFactory getAdapterTypeFactory() {
        return null;
    }

    @Override
    public void onItemClicked(TrainScheduleRouteDetailViewModel trainScheduleRouteDetailViewModel) {

    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

}
