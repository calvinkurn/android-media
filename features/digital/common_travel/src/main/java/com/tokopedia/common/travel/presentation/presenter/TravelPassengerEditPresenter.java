package com.tokopedia.common.travel.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common.travel.domain.DeleteTravelPassengerUseCase;
import com.tokopedia.common.travel.domain.GetTravelPassengersUseCase;
import com.tokopedia.common.travel.domain.provider.TravelProvider;
import com.tokopedia.common.travel.presentation.contract.TravelPassengerEditContract;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 13/11/18.
 */
public class TravelPassengerEditPresenter extends BaseDaggerPresenter<TravelPassengerEditContract.View>
        implements TravelPassengerEditContract.Presenter {

    private GetTravelPassengersUseCase getTravelPassengersUseCase;
    private DeleteTravelPassengerUseCase deleteTravelPassengerUseCase;
    private CompositeSubscription compositeSubscription;
    private TravelProvider travelProvider;

    @Inject
    public TravelPassengerEditPresenter(GetTravelPassengersUseCase getTravelPassengersUseCase,
                                        DeleteTravelPassengerUseCase deleteTravelPassengerUseCase,
                                        TravelProvider travelProvider) {
        this.getTravelPassengersUseCase = getTravelPassengersUseCase;
        this.deleteTravelPassengerUseCase = deleteTravelPassengerUseCase;
        this.compositeSubscription = new CompositeSubscription();
        this.travelProvider = travelProvider;
    }

    @Override
    public void getPassengerList() {
        getView().showProgressBar();
        getTravelPassengersUseCase.setResetPassengerListSelected(false);
        getTravelPassengersUseCase.setTravelPassengerSelected(getView().getTravelPassengerBooking().getIdPassenger());
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
                    }
                })
        );
    }

    @Override
    public void deletePassenger(String idPassenger, String id, int travelId) {
        getView().showProgressBar();
        deleteTravelPassengerUseCase.setIdPassengerSelected(idPassenger);
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(DeleteTravelPassengerUseCase.ID_PASSENGER, id);
        requestParams.putObject(DeleteTravelPassengerUseCase.TRAVEL_ID, travelId);
        compositeSubscription.add(deleteTravelPassengerUseCase.createObservable(
                deleteTravelPassengerUseCase.create(requestParams, getView().getTravelPlatformType()))
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
                            getView().hideProgressBar();
                            getView().showMessageErrorInSnackBar(e);
                        }
                    }

                    @Override
                    public void onNext(Boolean isDeleted) {
                        getView().hideProgressBar();
                        if (isDeleted) {
                            getView().successDeletePassenger();
                        }
                    }
                })
        );
    }

    @Override
    public void onDestroyView() {
        detachView();
        if (getTravelPassengersUseCase != null) getTravelPassengersUseCase.unsubscribe();

        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
    }
}
