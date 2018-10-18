package com.tokopedia.train.search.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.common.travel.widget.DepartureTripLabelView;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.common.util.TrainFlowExtraConstant;
import com.tokopedia.train.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.train.passenger.presentation.activity.TrainBookingPassengerActivity;
import com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef;
import com.tokopedia.train.search.di.DaggerTrainSearchComponent;
import com.tokopedia.train.search.presentation.activity.TrainSearchActivity;
import com.tokopedia.train.search.presentation.activity.TrainSearchReturnActivity;
import com.tokopedia.train.search.presentation.contract.TrainSearchReturnContract;
import com.tokopedia.train.search.presentation.model.TrainScheduleBookingPassData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.train.search.presentation.presenter.TrainSearchReturnPresenter;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 3/19/18.
 */

public class TrainSearchReturnFragment extends TrainSearchFragment
        implements TrainSearchReturnContract.View {
    @Inject
    TrainSearchReturnPresenter presenterReturn;

    private DepartureTripLabelView departureTripLabelView;
    private TrainScheduleBookingPassData trainScheduleBookingPassData;

    public static TrainSearchReturnFragment newInstance(TrainSearchPassDataViewModel trainSearchPassDataViewModel,
                                                        String idSchedule,
                                                        TrainScheduleBookingPassData scheduleBookingPassData,
                                                        String arrivalScheduleSelected) {
        TrainSearchReturnFragment fragment = new TrainSearchReturnFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TrainSearchActivity.EXTRA_SEARCH_PASS_DATA, trainSearchPassDataViewModel);
        bundle.putString(TrainSearchReturnActivity.EXTRA_SEARCH_ID_SCHEDULE, idSchedule);
        bundle.putParcelable(TrainSearchReturnActivity.EXTRA_SCHEDULE_BOOKING, scheduleBookingPassData);
        bundle.putString(TrainSearchReturnActivity.EXTRA_ARRIVAL_TIME_SCHEDULE_SELECTED, arrivalScheduleSelected);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void getDataFromFragment() {
        trainScheduleBookingPassData = getArguments().getParcelable(TrainSearchReturnActivity.EXTRA_SCHEDULE_BOOKING);
        trainSearchPassDataViewModel = getArguments().getParcelable(TrainSearchActivity.EXTRA_SEARCH_PASS_DATA);
        dateDeparture = trainSearchPassDataViewModel.getReturnDate();
        adultPassenger = trainSearchPassDataViewModel.getAdult();
        infantPassenger = trainSearchPassDataViewModel.getInfant();
        originCode = trainSearchPassDataViewModel.getDestinationStationCode();
        originCity = trainSearchPassDataViewModel.getDestinationCityName();
        destinationCode = trainSearchPassDataViewModel.getOriginStationCode();
        destinationCity = trainSearchPassDataViewModel.getOriginCityName();
    }

    @Override
    protected int getScheduleVariant() {
        return TrainScheduleTypeDef.RETURN_SCHEDULE;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        departureTripLabelView = view.findViewById(R.id.departure_trip_label);

        presenterReturn.getDetailSchedules(getArguments().getString(TrainSearchReturnActivity.EXTRA_SEARCH_ID_SCHEDULE));
    }

    @Override
    public void selectSchedule(TrainScheduleViewModel trainScheduleViewModel) {
        trainScheduleBookingPassData.setReturnScheduleId(trainScheduleViewModel.getIdSchedule());
        startActivityForResult(TrainBookingPassengerActivity.callingIntent(getActivity(), trainScheduleBookingPassData), NEXT_STEP_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NEXT_STEP_REQUEST_CODE:
                if (data != null) {
                    if (data.getIntExtra(TrainFlowExtraConstant.EXTRA_FLOW_DATA, -1) != -1) {
                        trainFlowUtil.actionSetResultAndClose(
                                getActivity(),
                                getActivity().getIntent(),
                                data.getIntExtra(TrainFlowExtraConstant.EXTRA_FLOW_DATA, 0)
                        );
                    }
                }
                break;
        }
    }

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_train_return_search;
    }

    @Override
    public void loadDetailSchedule(TrainScheduleViewModel viewModel) {
        departureTripLabelView.setVisibility(View.VISIBLE);
        departureTripLabelView.setValueName(viewModel.getTrainName());

        String dateDepartureString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_SEARCH,
                TrainDateUtil.DEFAULT_VIEW_FORMAT, trainSearchPassDataViewModel.getDepartureDate());
        departureTripLabelView.setValueTitle(String.format("%s - %s", getString(R.string.train_search_departure_title), dateDepartureString));

        String timeDepartureString = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_TIME, viewModel.getDepartureTimestamp());
        String timeArrivalString = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_TIME, viewModel.getArrivalTimestamp());

        String departureHour = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_DAY, viewModel.getDepartureTimestamp());
        String arrivalHour = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_DAY, viewModel.getArrivalTimestamp());
        int deviationDay = Integer.parseInt(arrivalHour) - Integer.parseInt(departureHour);
        String deviationDayString = deviationDay > 0 ? " (+" + deviationDay + "h)" : "";
        departureTripLabelView.setValueDepartureTime(String.format(" | %s - %s %s", timeDepartureString, timeArrivalString, deviationDayString));
    }

    @Override
    public void hideDetailDepartureSchedule(Throwable e) {
        departureTripLabelView.setVisibility(View.GONE);
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        if (trainSearchComponent == null) {
            trainSearchComponent = DaggerTrainSearchComponent.builder()
                    .trainComponent(TrainComponentUtils.getTrainComponent(getActivity().getApplication())).build();
        }
        trainSearchComponent
                .inject(this);
        presenterReturn.attachView(this);
    }
}
