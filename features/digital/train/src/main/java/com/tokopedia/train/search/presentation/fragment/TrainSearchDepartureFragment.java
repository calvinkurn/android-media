package com.tokopedia.train.search.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.train.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef;
import com.tokopedia.train.search.presentation.activity.TrainSearchReturnActivity;
import com.tokopedia.train.search.presentation.activity.TrainSearchActivity;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

/**
 * Created by nabillasabbaha on 3/19/18.
 */

public class TrainSearchDepartureFragment extends TrainSearchFragment {

    public static TrainSearchDepartureFragment newInstance(TrainSearchPassDataViewModel trainSearchPassDataViewModel) {
        TrainSearchDepartureFragment fragment = new TrainSearchDepartureFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TrainSearchActivity.EXTRA_SEARCH_PASS_DATA, trainSearchPassDataViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void getDataFromFragment() {
        trainSearchPassDataViewModel = getArguments().getParcelable(TrainSearchActivity.EXTRA_SEARCH_PASS_DATA);
        dateDeparture = trainSearchPassDataViewModel.getDepartureDate();
        adultPassanger = trainSearchPassDataViewModel.getAdult();
        infantPassanger = trainSearchPassDataViewModel.getInfant();
        originCode = trainSearchPassDataViewModel.getOriginStationCode();
        originCity = trainSearchPassDataViewModel.getOriginCityName();
        destinationCode = trainSearchPassDataViewModel.getDestinationStationCode();
        destinationCity = trainSearchPassDataViewModel.getDestinationCityName();
    }

    @Override
    protected int getScheduleVariant() {
        return TrainScheduleTypeDef.DEPARTURE_SCHEDULE;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClicked(TrainScheduleViewModel trainScheduleViewModel) {
        if (!trainSearchPassDataViewModel.isOneWay()) {
            startActivityForResult(TrainSearchReturnActivity.getCallingIntent(getActivity(),
                    trainSearchPassDataViewModel, trainScheduleViewModel.getIdSchedule()), 11);
        }
    }
}
