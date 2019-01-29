package com.tokopedia.train.seat.presentation.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.passenger.data.TrainBookingPassenger;
import com.tokopedia.train.passenger.domain.model.TrainPaxPassenger;
import com.tokopedia.train.passenger.domain.model.TrainSeat;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.passenger.domain.model.TrainTrip;
import com.tokopedia.train.seat.domain.TrainChangeSeatUseCase;
import com.tokopedia.train.seat.domain.TrainGetSeatsUseCase;
import com.tokopedia.train.seat.domain.model.TrainPassengerSeat;
import com.tokopedia.train.seat.domain.model.request.ChangeSeatMapRequest;
import com.tokopedia.train.seat.presentation.contract.TrainSeatContract;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerSeatViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatViewModel;
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
        RequestParams requestParams = buildGetSeatRequests(getView().isReturning(), getView().getTrainSoftbook());
        trainGetSeatsUseCase.execute(requestParams, new Subscriber<List<TrainWagonViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().showPage();
                    getView().showErrorGetSeatMaps(e.getMessage());
                    getView().stopTrace();
                }
            }

            @Override
            public void onNext(List<TrainWagonViewModel> trainWagonViewModels) {
                getView().showPage();
                getView().hideLoading();
                getView().renderWagon(trainWagonViewModels, calculateMaxRow(trainWagonViewModels));
                if (getView().getExpireDate() != null && !getView().getExpireDate().isEmpty()) {
                    getView().renderExpireDateCountdown(TrainDateUtil.stringToDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, getView().getExpireDate()));
                }
                getView().stopTrace();
            }
        });
    }

    private int calculateMaxRow(List<TrainWagonViewModel> trainWagonViewModels) {
        int max = 0;
        for (TrainWagonViewModel wagon :
                trainWagonViewModels) {
            if (max < wagon.getMaxRow()) {
                max = wagon.getMaxRow();
            }
        }
        return max;
    }

    private RequestParams buildGetSeatRequests(boolean returning, TrainSoftbook trainSoftbook) {
        if (!returning) {
            if (trainSoftbook.getDepartureTrips().size() > 0) {
                TrainTrip trainTrip = trainSoftbook.getDepartureTrips().get(0);

                String departureTime = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, "yyyyMMdd", trainTrip.getDepartureTimestamp());
                return trainGetSeatsUseCase.createRequestParam(
                        departureTime,
                        trainTrip.getOrg(),
                        trainTrip.getDes(),
                        trainTrip.getTrainNo(),
                        trainTrip.getSubclass()
                );
            }
        } else {
            if (trainSoftbook.getReturnTrips().size() > 0) {
                TrainTrip trainTrip = trainSoftbook.getReturnTrips().get(0);

                String departureTime = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, "yyyyMMdd", trainTrip.getDepartureTimestamp());
                return trainGetSeatsUseCase.createRequestParam(
                        departureTime,
                        trainTrip.getOrg(),
                        trainTrip.getDes(),
                        trainTrip.getTrainNo(),
                        trainTrip.getSubclass()
                );
            }
        }
        return null;
    }

    @Override
    public void onRunningOutOfTime() {
        if (isViewAttached()) {
            getView().showExpiredPaymentDialog();
        }
    }

    @Override
    public void onWagonChooserClicked() {
        getView().showWagonChooser();
    }

    @Override
    public void onChangePassengerConfirmDialogAccepted() {
        getView().hidePage();
        getView().showLoading();
        List<ChangeSeatMapRequest> requests = transformSeatRequest(getBookCode(),
                getView().getOriginalPassenger(),
                getView().getPassengers(), true);
        trainChangeSeatUseCase.execute(
                trainChangeSeatUseCase.createRequest(requests), new Subscriber<List<TrainPassengerSeat>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isViewAttached()) {
                            getView().hideLoading();
                            getView().showPage();
                            getView().showErrorChangeSeat(e.getMessage());
                            onViewCreated();
                            getView().clearSeatMaps();
                            getSeatMaps();
                        }
                    }

                    @Override
                    public void onNext(List<TrainPassengerSeat> trainPassengerSeats) {
                        getView().hideLoading();
                        getView().showPage();
                        List<TrainSeatPassengerViewModel> passengers = getView().getOriginalPassenger();
                        passengers = getView().getPassengers();
                        navigateToReview(passengers);
                    }
                });
    }

    @Override
    public void onSubmitButtonClicked() {
        getView().hidePage();
        getView().showLoading();
        List<ChangeSeatMapRequest> requests = transformSeatRequest(getBookCode(),
                getView().getOriginalPassenger(),
                getView().getPassengers(), false);
        if (requests.size() > 0) {
            getView().showPage();
            getView().hideLoading();
            getView().showConfirmChangePassengersDialog(getView().getPassengers());
        } else {
            getView().showPage();
            getView().hideLoading();
            getView().navigateToReview(getView().getTrainSoftbook());
        }
    }

    private void navigateToReview(List<TrainSeatPassengerViewModel> passengers) {
        if (!getView().isReturning()) {
            List<TrainPaxPassenger> paxPassengers = new ArrayList<>();
            List<TrainPaxPassenger> oldPaxPassengers = getView().getTrainSoftbook().getDepartureTrips().get(0).getPaxPassengers();
            buildNewPaxPassengers(passengers, paxPassengers, oldPaxPassengers);
            getView().getTrainSoftbook().getDepartureTrips().get(0).setPaxPassengers(paxPassengers);
            getView().navigateToReview(getView().getTrainSoftbook());
        } else {
            List<TrainPaxPassenger> paxPassengers = new ArrayList<>();
            List<TrainPaxPassenger> oldPaxPassengers = getView().getTrainSoftbook().getReturnTrips().get(0).getPaxPassengers();
            buildNewPaxPassengers(passengers, paxPassengers, oldPaxPassengers);
            getView().getTrainSoftbook().getReturnTrips().get(0).setPaxPassengers(paxPassengers);
            getView().navigateToReview(getView().getTrainSoftbook());
        }
    }

    private void buildNewPaxPassengers(List<TrainSeatPassengerViewModel> passengers, List<TrainPaxPassenger> paxPassengers, List<TrainPaxPassenger> oldPaxPassengers) {
        TrainPaxPassenger paxPassenger;
        for (TrainPaxPassenger oldPaxPassenger : oldPaxPassengers) {
            if (oldPaxPassenger.getPaxType() == TrainBookingPassenger.ADULT){
                for (TrainSeatPassengerViewModel passenger : passengers) {
                    if (oldPaxPassenger.getName().equalsIgnoreCase(passenger.getName())) {
                        paxPassenger = new TrainPaxPassenger();
                        paxPassenger.setIdNumber(oldPaxPassenger.getIdNumber());
                        paxPassenger.setName(passenger.getName());
                        paxPassenger.setPaxType(oldPaxPassenger.getPaxType());
                        TrainSeat trainSeat = new TrainSeat();
                        trainSeat.setColumn(passenger.getSeatViewModel().getColumn());
                        trainSeat.setRow(passenger.getSeatViewModel().getRow());
                        trainSeat.setWagonNo(passenger.getSeatViewModel().getWagonCode());
                        paxPassenger.setSeat(trainSeat);
                        paxPassengers.add(paxPassenger);
                        break;
                    }
                }
            }else {
                paxPassenger = new TrainPaxPassenger();
                paxPassenger.setIdNumber(oldPaxPassenger.getIdNumber());
                paxPassenger.setName(oldPaxPassenger.getName());
                paxPassenger.setPaxType(oldPaxPassenger.getPaxType());
                paxPassenger.setSeat(oldPaxPassenger.getSeat());
                paxPassengers.add(paxPassenger);
            }
        }
    }

    @Override
    public void onViewCreated() {
        buildPassengers(getView().isReturning(), getView().getTrainSoftbook());
        getView().setToolbarSubTitle(buildToolbarSubtitle(getView().isReturning(), getView().getTrainSoftbook()));
    }

    private String buildToolbarSubtitle(boolean returning, TrainSoftbook trainSoftbook) {
        if (!returning) {
            if (trainSoftbook.getDepartureTrips().size() > 0) {
                TrainTrip trainTrip = trainSoftbook.getDepartureTrips().get(0);
                return getView().getString(R.string.train_seat_prefix_toolbar_title) + " (" + trainTrip.getOrg() + " - " + trainTrip.getDes() + ")";
            }
        } else {
            if (trainSoftbook.getReturnTrips() != null && trainSoftbook.getReturnTrips().size() > 0) {
                TrainTrip trainTrip = trainSoftbook.getReturnTrips().get(0);
                return getView().getString(R.string.train_seat_prefix_toolbar_return_trip_title) + " (" + trainTrip.getOrg() + " - " + trainTrip.getDes() + ")";
            }
        }
        return "";
    }

    @Override
    public void onPassengerSeatChange(TrainSeatPassengerViewModel passenger, TrainSeatViewModel seat, String wagonCode) {
        for (TrainSeatPassengerViewModel passengerSeat : getView().getPassengers()) {
            if (passengerSeat.getName().equalsIgnoreCase(passenger.getName())) {
                TrainSeatPassengerSeatViewModel seatViewModel = new TrainSeatPassengerSeatViewModel();
                seatViewModel.setWagonCode(wagonCode);
                seatViewModel.setRow(String.valueOf(seat.getRow()));
                seatViewModel.setColumn(seat.getColumn());
                passengerSeat.setSeatViewModel(seatViewModel);
                break;
            }
        }
        getView().updateSelectedWagon();
    }

    private void buildPassengers(boolean returning, TrainSoftbook trainSoftbook) {
        if (!returning) {
            if (trainSoftbook.getDepartureTrips().size() > 0) {
                List<TrainSeatPassengerViewModel> originPassengers = new ArrayList<>();
                List<TrainSeatPassengerViewModel> passengers = new ArrayList<>();
                List<TrainPaxPassenger> paxPassengers = trainSoftbook.getDepartureTrips().get(0).getPaxPassengers();
                prepareTrainSeatPassengerViewModel(originPassengers, passengers, paxPassengers);
                getView().setOriginPassenger(originPassengers);
                getView().setPassengers(passengers);
            }
        } else {
            if (trainSoftbook.getReturnTrips().size() > 0) {
                List<TrainSeatPassengerViewModel> originPassengers = new ArrayList<>();
                List<TrainSeatPassengerViewModel> passengers = new ArrayList<>();
                List<TrainPaxPassenger> paxPassengers = trainSoftbook.getReturnTrips().get(0).getPaxPassengers();
                prepareTrainSeatPassengerViewModel(originPassengers, passengers, paxPassengers);
                getView().setOriginPassenger(originPassengers);
                getView().setPassengers(passengers);
            }
        }
    }

    private void prepareTrainSeatPassengerViewModel(List<TrainSeatPassengerViewModel> originPassengers,
                                                    List<TrainSeatPassengerViewModel> passengers,
                                                    List<TrainPaxPassenger> paxPassengers) {
        TrainSeatPassengerViewModel viewModel;
        TrainSeatPassengerViewModel cloneViewModel;
        for (int i = 0; i < paxPassengers.size(); i++) {
            TrainPaxPassenger trainPaxPassenger = paxPassengers.get(i);
            if (trainPaxPassenger.getPaxType() == TrainBookingPassenger.ADULT){
                viewModel = transformTrainPaxToPassengerWithSeat(i, trainPaxPassenger);
                cloneViewModel = transformTrainPaxToPassengerWithSeat(i, trainPaxPassenger);
                originPassengers.add(viewModel);
                passengers.add(cloneViewModel);
            }
        }
    }

    @NonNull
    private TrainSeatPassengerViewModel transformTrainPaxToPassengerWithSeat(int i, TrainPaxPassenger trainPaxPassenger) {
        TrainSeatPassengerViewModel cloneViewModel;
        cloneViewModel = new TrainSeatPassengerViewModel();
        cloneViewModel.setName(trainPaxPassenger.getName());
        cloneViewModel.setNumber(trainPaxPassenger.getIdNumber());
        cloneViewModel.setPassengerNumber(i + 1);
        TrainSeatPassengerSeatViewModel cloneSeatViewModel = new TrainSeatPassengerSeatViewModel();
        cloneSeatViewModel.setColumn(trainPaxPassenger.getSeat().getColumn());
        cloneSeatViewModel.setRow(trainPaxPassenger.getSeat().getRow());
        cloneSeatViewModel.setWagonCode(trainPaxPassenger.getSeat().getWagonNo());
        cloneViewModel.setSeatViewModel(cloneSeatViewModel);
        return cloneViewModel;
    }

    private List<ChangeSeatMapRequest> transformSeatRequest(String bookCode,
                                                            List<TrainSeatPassengerViewModel> originalPassenger,
                                                            List<TrainSeatPassengerViewModel> passengers,
                                                            boolean includeOriginalPassenger) {
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
                        request.setSeat(changeSeat.getRow() + changeSeat.getColumn());
                        request.setWagonCode(changeSeat.getWagonCode());
                        requests.add(request);
                    } else {
                        if (includeOriginalPassenger){
                            request = new ChangeSeatMapRequest();
                            request.setBookCode(bookCode);
                            request.setName(passenger.getName());
                            request.setSeat(originSeat.getRow() + originSeat.getColumn());
                            request.setWagonCode(originSeat.getWagonCode());
                            requests.add(request);
                        }
                    }
                    break;
                }
            }
        }
        return requests;
    }

    @Override
    public void detachView() {
        trainGetSeatsUseCase.unsubscribe();
        trainChangeSeatUseCase.unsubscribe();
        super.detachView();
    }

    private String getBookCode() {
        if (!getView().isReturning()) {
            return getView().getTrainSoftbook().getDepartureTrips().get(0).getBookCode();
        } else {
            return getView().getTrainSoftbook().getReturnTrips().get(0).getBookCode();
        }
    }
}
