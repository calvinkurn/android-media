package com.tokopedia.common.travel.presentation;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.common.travel.domain.GetTravelPassengersUseCase;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 26/06/18.
 */
public class TravelPassengerBookingListPresenter extends BaseDaggerPresenter<TravelPassengerBookingListContract.View>
        implements TravelPassengerBookingListContract.Presenter {

    private GetTravelPassengersUseCase getTravelPassengersUseCase;
    private Context context;

    @Inject
    public TravelPassengerBookingListPresenter(@ApplicationContext Context context, GetTravelPassengersUseCase getTravelPassengersUseCase) {
        this.getTravelPassengersUseCase = getTravelPassengersUseCase;
        this.context = context;
    }

    @Override
    public void getPassengerList() {
        getView().showProgressBar();
        getTravelPassengersUseCase.execute(new Subscriber<List<TravelPassenger>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideProgressBar();
                    String errorMessage = ErrorHandler.getErrorMessage(context, e);
                    getView().showMessageErrorInSnackBar(errorMessage);
                }
            }

            @Override
            public void onNext(List<TravelPassenger> travelPassengers) {
                getView().hideProgressBar();
                getView().renderPassengerList(travelPassengers);
            }
        });
    }

    @Override
    public void onDestroyView() {
        detachView();
        getTravelPassengersUseCase.unsubscribe();
    }
}
