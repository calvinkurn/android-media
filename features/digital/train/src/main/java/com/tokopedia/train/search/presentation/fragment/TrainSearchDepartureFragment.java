package com.tokopedia.train.search.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.tokopedia.train.common.util.TrainFlowConstant;
import com.tokopedia.train.common.util.TrainFlowExtraConstant;
import com.tokopedia.train.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef;
import com.tokopedia.train.search.presentation.activity.TrainSearchActivity;

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
        adultPassenger = trainSearchPassDataViewModel.getAdult();
        infantPassenger = trainSearchPassDataViewModel.getInfant();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEXT_STEP_REQUEST_CODE) {
            if (data != null) {
                int extraConstantData = data.getIntExtra(TrainFlowExtraConstant.EXTRA_FLOW_DATA, -1);
                switch (extraConstantData) {
                    case TrainFlowConstant.HOME:
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                        break;
                    case TrainFlowConstant.RESEARCH:
                        presenter.reSearch();
                        break;
                }
            }
        }
    }
}
