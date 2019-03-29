package com.tokopedia.train.search.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
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
    public void getTrainSchedules() {
        getView().showLoadingPage();
        getView().hideFilterAndSortButtonAction();
        getScheduleUseCase.setScheduleVariant(getView().getScheduleVariantSelected());
        getScheduleUseCase.execute(getView().getRequestParam(),
                new Subscriber<List<AvailabilityKeySchedule>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().showGetListError(e);
                            getView().stopTrace();
                        }
                    }

                    @Override
                    public void onNext(List<AvailabilityKeySchedule> availabilityKeySchedules) {
                        if (availabilityKeySchedules.isEmpty()) {
                            getView().clearAdapterData();
                            getView().showEmptyResult();
                            getView().stopTrace();
                        } else {
                            getAvailabilitySchedule(availabilityKeySchedules);
                        }
                    }
                });
    }

    private void getAvailabilitySchedule(List<AvailabilityKeySchedule> availabilityKeySchedules) {
        getAvailabilityScheduleUseCase.setAvailabilityKeySchedules(availabilityKeySchedules);
        getAvailabilityScheduleUseCase.execute(RequestParams.EMPTY, new Subscriber<List<List<TrainScheduleViewModel>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().clearAdapterData();
                    getView().showGetListError(e);
                    getView().stopTrace();
                }
            }

            @Override
            public void onNext(List<List<TrainScheduleViewModel>> trainScheduleViewModels) {
                getView().clearAdapterData();
                if (isViewAttached() && trainScheduleViewModels != null) {
                    getView().addPaddingSortAndFilterSearch();
                    getView().showFilterAndSortButtonAction();

                    getFilteredAndSortedSchedules();
                }
            }
        });
    }

    @Override
    public void getFilteredAndSortedSchedules() {
        getView().clearAdapterData();
        getView().showLoadingPage();
        getView().hideFilterAndSortButtonAction();
        RequestParams requestParams = getFilteredAndSortedScheduleUseCase.createRequestParam(
                getView().getFilterParam(), getView().getSortOptionSelected(), getView().getScheduleVariantSelected());

        getFilteredAndSortedScheduleUseCase.execute(requestParams, new Subscriber<List<TrainScheduleViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                    getView().stopTrace();
                }
            }

            @Override
            public void onNext(List<TrainScheduleViewModel> trainSchedulesViewModel) {
                if (isViewAttached()) {
                    if (trainSchedulesViewModel != null && !trainSchedulesViewModel.isEmpty()) {
                        getView().showDataScheduleFromCache(trainSchedulesViewModel);
                    } else {
                        getView().showEmptyResult();
                        getView().showFilterAndSortButtonAction();
                    }
                    getView().markSortOption();
                    getView().stopTrace();
                }
            }
        });
    }

    @Override
    public void reSearch() {
        getView().clearAdapterData();
        getView().resetFilterAndSortParam();
        getView().resetFilterParam();
        getTrainSchedules();
    }

    @Override
    public void detachView() {
        getAvailabilityScheduleUseCase.unsubscribe();
        getFilteredAndSortedScheduleUseCase.unsubscribe();
        getScheduleUseCase.unsubscribe();
        super.detachView();
    }
}