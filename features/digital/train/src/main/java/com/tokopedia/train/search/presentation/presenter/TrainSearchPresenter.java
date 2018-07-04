package com.tokopedia.train.search.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.train.search.domain.FilterParam;
import com.tokopedia.train.search.domain.GetAvailabilityScheduleUseCase;
import com.tokopedia.train.search.domain.GetFilteredAndSortedScheduleUseCase;
import com.tokopedia.train.search.domain.GetScheduleUseCase;
import com.tokopedia.train.search.presentation.contract.TrainSearchContract;
import com.tokopedia.train.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 3/13/18.
 */

public class TrainSearchPresenter extends BaseDaggerPresenter<TrainSearchContract.View> implements
        TrainSearchContract.Presenter {

    private static final String TAG = TrainSearchPresenter.class.getSimpleName();

    private GetScheduleUseCase getScheduleUseCase;
    private GetAvailabilityScheduleUseCase getAvailabilityScheduleUseCase;
    private GetFilteredAndSortedScheduleUseCase getFilteredAndSortedScheduleUseCase;

    @Inject
    public TrainSearchPresenter(GetScheduleUseCase getScheduleUseCase,
                                GetAvailabilityScheduleUseCase getAvailabilityScheduleUseCase,
                                GetFilteredAndSortedScheduleUseCase getFilteredAndSortedScheduleUseCase) {
        this.getScheduleUseCase = getScheduleUseCase;
        this.getAvailabilityScheduleUseCase = getAvailabilityScheduleUseCase;
        this.getFilteredAndSortedScheduleUseCase = getFilteredAndSortedScheduleUseCase;
    }

    @Override
    public void getTrainSchedules(final int scheduleVariant, String arrivalTimeDepartureTripSelected) {
        getScheduleUseCase.setScheduleVariant(scheduleVariant);
        getScheduleUseCase.execute(getView().getRequestParam(),
                new Subscriber<List<AvailabilityKeySchedule>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideLayoutTripInfo();
                        getView().showGetListError(e);
                    }

                    @Override
                    public void onNext(List<AvailabilityKeySchedule> availabilityKeySchedules) {
                        getView().clearAdapterData();
                        if (availabilityKeySchedules.isEmpty()) {
                            getView().showEmptyResult();
                        } else {
                            getAvailabilitySchedule(availabilityKeySchedules, scheduleVariant, arrivalTimeDepartureTripSelected);
                        }
                    }
                });
    }

    private void getAvailabilitySchedule(List<AvailabilityKeySchedule> availabilityKeySchedules, int scheduleVariant,
                                         String arrivalTimeDepartureTripSelected) {
        getAvailabilityScheduleUseCase.setAvailabilityKeySchedules(availabilityKeySchedules);
        getAvailabilityScheduleUseCase.setArrivalTimeDepartureTripSelected(arrivalTimeDepartureTripSelected);
        getAvailabilityScheduleUseCase.setScheduleVariant(scheduleVariant);
        getAvailabilityScheduleUseCase.execute(RequestParams.EMPTY, new Subscriber<List<List<TrainScheduleViewModel>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().hideLayoutTripInfo();
                getView().showGetListError(e);
            }

            @Override
            public void onNext(List<List<TrainScheduleViewModel>> trainScheduleViewModels) {
                if (trainScheduleViewModels != null) {
                    getView().showLayoutTripInfo();
                    getView().addPaddingSortAndFilterSearch();
                    getView().showFilterAndSortButtonAction();

                    List<TrainScheduleViewModel> resultTrainSchedules = new ArrayList<>();
                    for (List<TrainScheduleViewModel> trainScheduleModelList : trainScheduleViewModels) {
                        for (TrainScheduleViewModel trainScheduleViewModel : trainScheduleModelList) {
                            resultTrainSchedules.add(trainScheduleViewModel);
                        }
                    }
                    getView().renderList(resultTrainSchedules);

                }
            }
        });
    }

    @Override
    public void getFilteredAndSortedSchedules(long minPrice, long maxPrice, List<String> trainClass,
                                              List<String> trains, List<String> departureTrains, final int sortOptionId) {
        FilterParam filterParam = new FilterParam.Builder()
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .trains(trains)
                .trainClass(trainClass)
                .departureTimeList(departureTrains)
                .build();
        RequestParams requestParams = getFilteredAndSortedScheduleUseCase.createRequestParam(filterParam, sortOptionId);

        getFilteredAndSortedScheduleUseCase.execute(requestParams, new Subscriber<List<TrainScheduleViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showGetListError(e);
            }

            @Override
            public void onNext(List<TrainScheduleViewModel> trainSchedulesViewModel) {
                if (trainSchedulesViewModel != null && !trainSchedulesViewModel.isEmpty()) {
                    getView().showLayoutTripInfo();
                    getView().showDataScheduleFromCache(trainSchedulesViewModel);
                } else {
                    getView().showEmptyResult();
                    getView().showFilterAndSortButtonAction();
                }
                getView().setSortOptionId(sortOptionId);
            }
        });
    }

}