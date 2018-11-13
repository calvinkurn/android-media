package com.tokopedia.common.travel.presentation.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.common.travel.domain.GetTravelPassengersUseCase;
import com.tokopedia.common.travel.domain.UpdateTravelPassengerUseCase;
import com.tokopedia.common.travel.presentation.contract.TravelPassengerListContract;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 26/06/18.
 */
public class TravelPassengerListPresenter extends BaseDaggerPresenter<TravelPassengerListContract.View>
        implements TravelPassengerListContract.Presenter {

    private GetTravelPassengersUseCase getTravelPassengersUseCase;
    private UpdateTravelPassengerUseCase updateTravelPassengerUseCase;
    private Context context;

    @Inject
    public TravelPassengerListPresenter(@ApplicationContext Context context,
                                        GetTravelPassengersUseCase getTravelPassengersUseCase,
                                        UpdateTravelPassengerUseCase updateTravelPassengerUseCase) {
        this.getTravelPassengersUseCase = getTravelPassengersUseCase;
        this.updateTravelPassengerUseCase = updateTravelPassengerUseCase;
        this.context = context;
    }

    @Override
    public void getPassengerList(boolean resetPassengerListSelected) {
        getView().showProgressBar();
        getTravelPassengersUseCase.setResetPassengerListSelected(resetPassengerListSelected);
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
    public void updatePassenger(String travelIdPassenger, boolean isSelected) {
        updateTravelPassengerUseCase.execute(updateTravelPassengerUseCase.createRequestParams(travelIdPassenger, isSelected),
                new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                });
    }

    @Override
    public void onDestroyView() {
        detachView();
        getTravelPassengersUseCase.unsubscribe();
        updateTravelPassengerUseCase.unsubscribe();
    }
}
