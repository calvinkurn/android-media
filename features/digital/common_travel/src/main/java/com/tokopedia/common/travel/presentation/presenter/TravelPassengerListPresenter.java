package com.tokopedia.common.travel.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.domain.GetTravelPassengersUseCase;
import com.tokopedia.common.travel.domain.UpdateTravelPassengerUseCase;
import com.tokopedia.common.travel.domain.provider.TravelProvider;
import com.tokopedia.common.travel.presentation.contract.TravelPassengerListContract;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.utils.typedef.TravelBookingPassenger;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 26/06/18.
 */
public class TravelPassengerListPresenter extends BaseDaggerPresenter<TravelPassengerListContract.View>
        implements TravelPassengerListContract.Presenter {

    private GetTravelPassengersUseCase getTravelPassengersUseCase;
    private UpdateTravelPassengerUseCase updateTravelPassengerUseCase;
    private TravelProvider travelProvider;
    private CompositeSubscription compositeSubscription;

    @Inject
    public TravelPassengerListPresenter(GetTravelPassengersUseCase getTravelPassengersUseCase,
                                        UpdateTravelPassengerUseCase updateTravelPassengerUseCase,
                                        TravelProvider travelProvider) {
        this.getTravelPassengersUseCase = getTravelPassengersUseCase;
        this.updateTravelPassengerUseCase = updateTravelPassengerUseCase;
        this.travelProvider = travelProvider;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getPassengerList(boolean resetPassengerListSelected, int idLocal, String idPassengerSelected) {
        getView().showProgressBar();
        getTravelPassengersUseCase.setResetPassengerListSelected(resetPassengerListSelected);
        getTravelPassengersUseCase.setTravelPassengerSelected(idPassengerSelected);
        compositeSubscription.add(getTravelPassengersUseCase.createObservable(RequestParams.EMPTY)
                .subscribeOn(travelProvider.computation())
                .unsubscribeOn(travelProvider.computation())
                .observeOn(travelProvider.uiScheduler())
                .subscribe(new Subscriber<List<TravelPassenger>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().hideProgressBar();
                            getView().showMessageErrorInSnackBar(e);
                        }
                    }

                    @Override
                    public void onNext(List<TravelPassenger> travelPassengers) {
                        getView().hideProgressBar();
                        getView().renderPassengerList(travelPassengers);

                        for (int i = 0; i < travelPassengers.size(); i++) {
                            if (travelPassengers.get(i).getIdPassenger().equals(idPassengerSelected)) {
                                TravelPassenger travelPassenger = travelPassengers.get(i);
                                travelPassenger.setIdLocal(idLocal);
                                getView().updatePassengerSelected(travelPassenger);
                            }
                        }
                    }
                })
        );

    }

    @Override
    public void selectPassenger(TravelPassenger passengerBooking, TravelPassenger travelPassenger) {
        if (passengerBooking.getPaxType() == travelPassenger.getPaxType()) {
            if (isPassengerDataValid(travelPassenger)) {
                travelPassenger.setIdLocal(passengerBooking.getIdLocal());
                getView().onClickSelectPassenger(travelPassenger);
            } else {
                getView().showActionErrorInSnackBar(travelPassenger,
                        R.string.travel_error_msg_pick_passenger_data_not_valid);
            }
        } else {
            getView().showMessageErrorInSnackBar(
                    getErrorMessageCantPickPassenger(passengerBooking.getPaxType()));
        }
    }

    private int getErrorMessageCantPickPassenger(int paxType) {
        if (paxType == TravelBookingPassenger.ADULT) {
            return R.string.travel_error_msg_choose_passenger_adult;
        } else if (paxType == TravelBookingPassenger.CHILD) {
            return R.string.travel_error_msg_choose_passenger_child;
        } else if (paxType == TravelBookingPassenger.INFANT) {
            return R.string.travel_error_msg_choose_passenger_infant;
        }
        return R.string.travel_error_msg_choose_passenger;
    }

    private boolean isPassengerDataValid(TravelPassenger travelPassenger) {
        boolean isValid = true;
        if (travelPassenger.getName() == null || travelPassenger.getName().length() == 0) {
            isValid = false;
        } else if (travelPassenger.getPaxType() == TravelBookingPassenger.ADULT &&
                (travelPassenger.getIdNumber() == null || travelPassenger.getIdNumber().length() == 0)) {
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void updatePassenger(String travelIdPassenger, boolean isSelected) {
        compositeSubscription.add(
                updateTravelPassengerUseCase.createObservable(
                        updateTravelPassengerUseCase.createRequestParams(travelIdPassenger, isSelected))
                        .subscribeOn(travelProvider.computation())
                        .unsubscribeOn(travelProvider.computation())
                        .observeOn(travelProvider.uiScheduler())
                        .subscribe(new Subscriber<Boolean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().failedUpdatePassengerDb();
                                }
                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                getView().successUpdatePassengerDb();
                            }
                        })
        );
    }

    @Override
    public void onDestroyView() {
        detachView();
        getTravelPassengersUseCase.unsubscribe();
        updateTravelPassengerUseCase.unsubscribe();

        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
    }
}
