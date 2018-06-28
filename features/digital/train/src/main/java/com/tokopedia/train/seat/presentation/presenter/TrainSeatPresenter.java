package com.tokopedia.train.seat.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.seat.domain.TrainChangeSeatUseCase;
import com.tokopedia.train.seat.domain.TrainGetSeatsUseCase;
import com.tokopedia.train.seat.domain.model.request.ChangeSeatMapRequest;
import com.tokopedia.train.seat.presentation.contract.TrainSeatContract;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerSeatViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class TrainSeatPresenter extends BaseDaggerPresenter<TrainSeatContract.View> implements TrainSeatContract.Presenter {
    private TrainGetSeatsUseCase trainGetSeatsUseCase;
    private TrainChangeSeatUseCase trainChangeSeatUseCase;

    @Inject
    public TrainSeatPresenter(TrainGetSeatsUseCase trainGetSeatsUseCase, TrainChangeSeatUseCase trainChangeSeatUseCase) {
        this.trainGetSeatsUseCase = trainGetSeatsUseCase;
        this.trainChangeSeatUseCase = trainChangeSeatUseCase;
    }


    @Override
    public void getSeatMaps() {
        getView().showLoading();
        getView().hidePage();
        trainGetSeatsUseCase.execute(RequestParams.create(), new Subscriber<List<TrainWagonViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().showErrorGetSeatMaps(e.getMessage());
                    getView().hideLoading();
                }
            }

            @Override
            public void onNext(List<TrainWagonViewModel> trainWagonViewModels) {
                getView().showPage();
                getView().hideLoading();
                getView().renderWagon(trainWagonViewModels);
                getView().renderExpireDateCountdown(TrainDateUtil.stringToDate(TrainDateUtil.FORMAT_DATE_API, getView().getExpireDate()));
            }
        });
    }

    @Override
    public void onRunningOutOfTime() {
        getView().backToHomePage();
    }

    @Override
    public void onWagonChooserClicked() {

    }

    @Override
    public void onSubmitButtonClicked() {
        getView().hidePage();
        getView().showLoading();
        List<ChangeSeatMapRequest> requests = transformSeatRequest("",
                getView().getOriginalPassenger(),
                getView().getPassengers());
        if (requests.size() > 0) {
            trainGetSeatsUseCase.execute(
                    trainChangeSeatUseCase.createRequest(requests),
                    new Subscriber<List<TrainWagonViewModel>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            if (isViewAttached()) {
                                getView().hideLoading();
                                getView().showPage();

                            }
                        }

                        @Override
                        public void onNext(List<TrainWagonViewModel> trainWagonViewModels) {
                            getView().hideLoading();
                            getView().showPage();
                            List<TrainSeatPassengerViewModel> passengers = getView().getOriginalPassenger();
                            passengers = getView().getPassengers();
                            getView().navigateToReview(passengers);
                        }
                    });
        } else {
            getView().navigateToReview(getView().getOriginalPassenger());

        }
    }

    private List<ChangeSeatMapRequest> transformSeatRequest(String bookCode, List<TrainSeatPassengerViewModel> originalPassenger, List<TrainSeatPassengerViewModel> passengers) {
        List<ChangeSeatMapRequest> requests = new ArrayList<>();
        ChangeSeatMapRequest request;
        for (TrainSeatPassengerViewModel passenger : originalPassenger) {
            for (TrainSeatPassengerViewModel changedPassenger : passengers) {
                if (passenger.getName().equalsIgnoreCase(changedPassenger.getName())) {
                    TrainSeatPassengerSeatViewModel originSeat = passenger.getSeatViewModel();
                    TrainSeatPassengerSeatViewModel changeSeat = changedPassenger.getSeatViewModel();

                    if (!originSeat.equals(changeSeat)) {
                        request = new ChangeSeatMapRequest();
                        request.setBookCode(bookCode);
                        request.setName(passenger.getName());
                        request.setSeat(passenger.getSeatViewModel().getRow() + passenger.getSeatViewModel().getColumn());
                        request.setWagonCode(passenger.getSeatViewModel().getWagonCode());
                        requests.add(request);
                    }
                    break;
                }
            }
        }
        return requests;
    }
}
