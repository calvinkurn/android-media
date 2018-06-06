package com.tokopedia.train.search.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef;
import com.tokopedia.train.search.di.DaggerTrainSearchComponent;
import com.tokopedia.train.search.presentation.activity.TrainSearchReturnActivity;
import com.tokopedia.train.search.presentation.activity.TrainSearchActivity;
import com.tokopedia.train.search.presentation.contract.TrainSearchReturnContract;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.train.search.presentation.presenter.TrainReturnSearchPresenter;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 3/19/18.
 */

public class TrainSearchReturnFragment extends TrainSearchFragment
        implements TrainSearchReturnContract.View {

    @Inject
    TrainReturnSearchPresenter presenterReturn;

    private TextView titleDepartureInfoTv;
    private TextView trainNameTv;
    private TextView detailDepartureInfoTv;
    private LinearLayout departureDetailLayout;

    public static TrainSearchReturnFragment newInstance(TrainSearchPassDataViewModel trainSearchPassDataViewModel,
                                                        String idSchedule) {
        TrainSearchReturnFragment fragment = new TrainSearchReturnFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TrainSearchActivity.EXTRA_SEARCH_PASS_DATA, trainSearchPassDataViewModel);
        bundle.putString(TrainSearchReturnActivity.EXTRA_SEARCH_ID_SCHEDULE, idSchedule);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void getDataFromFragment() {
        trainSearchPassDataViewModel = getArguments().getParcelable(TrainSearchActivity.EXTRA_SEARCH_PASS_DATA);
        dateDeparture = trainSearchPassDataViewModel.getReturnDate();
        adultPassanger = trainSearchPassDataViewModel.getAdult();
        infantPassanger = trainSearchPassDataViewModel.getInfant();
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

        titleDepartureInfoTv = view.findViewById(R.id.title_departure_info);
        trainNameTv = view.findViewById(R.id.train_name);
        detailDepartureInfoTv = view.findViewById(R.id.detail_departure_info);
        departureDetailLayout = view.findViewById(R.id.layout_departure_detail);

        presenterReturn.getDetailSchedules(getArguments().getString(TrainSearchReturnActivity.EXTRA_SEARCH_ID_SCHEDULE));
    }

    @Override
    public void onItemClicked(TrainScheduleViewModel trainScheduleViewModel) {

    }

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_train_return_search;
    }

    @Override
    public void loadDetailSchedule(TrainScheduleViewModel viewModel) {
        departureDetailLayout.setVisibility(View.VISIBLE);
        String dateDepartureString = TrainDateUtil.formatDate(TrainDateUtil.DEFAULT_FORMAT,
                TrainDateUtil.DEFAULT_VIEW_FORMAT, trainSearchPassDataViewModel.getDepartureDate());
        titleDepartureInfoTv.setText("Perjalanan Pergi - " + dateDepartureString);
        trainNameTv.setText(viewModel.getTrainName());
        String timeDepartureString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, viewModel.getDepartureTimestamp());
        String timeArrivalString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, viewModel.getArrivalTimestamp());
        detailDepartureInfoTv.setText(" | " + timeDepartureString + " - " + timeArrivalString);
    }

    @Override
    public void hideDetailDepartureSchedule(Throwable e) {
        departureDetailLayout.setVisibility(View.GONE);
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
