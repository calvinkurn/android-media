package com.tokopedia.common.travel.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common.travel.domain.GetTravelPassengersUseCase;
import com.tokopedia.common.travel.presentation.contract.TravelPassengerEditContract;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 13/11/18.
 */
public class TravelPassengerEditPresenter extends BaseDaggerPresenter<TravelPassengerEditContract.View>
        implements TravelPassengerEditContract.Presenter {

    private GetTravelPassengersUseCase getTravelPassengersUseCase;

    @Inject
    public TravelPassengerEditPresenter(GetTravelPassengersUseCase getTravelPassengersUseCase) {
        this.getTravelPassengersUseCase = getTravelPassengersUseCase;
    }

    @Override
    public void getPassengerList() {
        getView().showProgressBar();
        getTravelPassengersUseCase.setResetPassengerListSelected(false);
        getTravelPassengersUseCase.execute(new Subscriber<List<TravelPassenger>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressBar();
                e.printStackTrace();
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
        if (getTravelPassengersUseCase != null) getTravelPassengersUseCase.unsubscribe();
    }
}
