package com.tokopedia.train.search.presentation.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.train.search.domain.FilterParam;
import com.tokopedia.train.search.domain.GetAvailabilityScheduleUseCase;
import com.tokopedia.train.search.domain.GetFilteredAndSortedScheduleUseCase;
import com.tokopedia.train.search.domain.GetScheduleUseCase;
import com.tokopedia.train.search.presentation.contract.TrainSearchContract;
import com.tokopedia.train.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;

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
    public void getTrainSchedules(final int scheduleVariant) {
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
                            for (AvailabilityKeySchedule available : availabilityKeySchedules) {
                                getAvailabilitySchedule(available.getIdTrain(), scheduleVariant);
                            }
                        }
                    }
                });
    }

    private void getAvailabilitySchedule(final String idTrain, int scheduleVariant) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(GetAvailabilityScheduleUseCase.TRAIN_ID_KEY, idTrain);
        getAvailabilityScheduleUseCase.setScheduleVariant(scheduleVariant);
        getAvailabilityScheduleUseCase.execute(requestParams, new Subscriber<List<TrainScheduleViewModel>>() {
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
            public void onNext(List<TrainScheduleViewModel> trainScheduleViewModels) {
                Log.d(TAG, idTrain);
                if (trainScheduleViewModels != null) {
                    getView().showLayoutTripInfo();
                    getView().addPaddingSortAndFilterSearch();
                    getView().showFilterAndSortButtonAction();
                    getView().renderList(trainScheduleViewModels);
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