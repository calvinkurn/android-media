package com.tokopedia.train.search.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.train.search.domain.FilterParam;
import com.tokopedia.train.search.presentation.model.FilterSearchData;
import com.tokopedia.train.search.domain.GetFilterSearchParamDataUseCase;
import com.tokopedia.train.search.domain.GetTotalScheduleUseCase;
import com.tokopedia.train.search.presentation.contract.TrainFilterSearchContract;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 3/20/18.
 */

public class TrainFilterSearchPresenter extends BaseDaggerPresenter<TrainFilterSearchContract.View>
        implements TrainFilterSearchContract.Presenter {

    private GetTotalScheduleUseCase getTotalScheduleUseCase;
    private GetFilterSearchParamDataUseCase getFilterSearchParamDataUseCase;

    @Inject
    public TrainFilterSearchPresenter(GetTotalScheduleUseCase getTotalScheduleUseCase, GetFilterSearchParamDataUseCase getFilterSearchParamDataUseCase) {
        this.getTotalScheduleUseCase = getTotalScheduleUseCase;
        this.getFilterSearchParamDataUseCase = getFilterSearchParamDataUseCase;
    }

    @Override
    public void getCountScheduleAvailable(FilterSearchData filterSearchData) {
        getTotalScheduleUseCase.setScheduleVariant(getView().getScheduleVariant());
        getTotalScheduleUseCase.setArrivalTimestampSelected(getView().getArrivalTimestampSelected());
        getTotalScheduleUseCase.execute(getTotalScheduleUseCase.createRequestParam(filterSearchData),
                new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        getView().showCountSchedule(integer);
                    }
                });
    }

    @Override
    public void getFilterSearchData() {
        getView().showLoading();
        FilterParam filterParam = new FilterParam.Builder()
                .minPrice(Integer.MIN_VALUE)
                .maxPrice(Integer.MAX_VALUE)
                .departureTimeList(new ArrayList<>())
                .trainClass(new ArrayList<>())
                .trains(new ArrayList<>())
                .arrivalTimestampSelected(getView().getArrivalTimestampSelected())
                .scheduleVariant(getView().getScheduleVariant())
                .build();
        getFilterSearchParamDataUseCase.setFilterParam(filterParam);
        getFilterSearchParamDataUseCase.setScheduleVariant(getView().getScheduleVariant());
        getFilterSearchParamDataUseCase.execute(RequestParams.EMPTY, new Subscriber<FilterSearchData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideLoading();
                e.printStackTrace();
            }

            @Override
            public void onNext(FilterSearchData filterSearchData) {
                getView().hideLoading();
                FilterSearchData filterSearchDataFromIntent = getView().getFilterSearchDataFromIntent();
                if (filterSearchDataFromIntent != null) {
                    filterSearchData.setSelectedMinPrice(filterSearchDataFromIntent.getSelectedMinPrice());
                    filterSearchData.setSelectedMaxPrice(filterSearchDataFromIntent.getSelectedMaxPrice());
                    filterSearchData.setSelectedDepartureTimeList(filterSearchDataFromIntent.getSelectedDepartureTimeList());
                    filterSearchData.setSelectedTrainClass(filterSearchDataFromIntent.getSelectedTrainClass());
                    filterSearchData.setSelectedTrains(filterSearchDataFromIntent.getSelectedTrains());
                }
                getView().renderFilterSearchData(filterSearchData);
            }
        });
    }
}
