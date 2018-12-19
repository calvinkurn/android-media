package com.tokopedia.flight.cancellation.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.cancellation.domain.FlightCancellationGetReasonsUseCase;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationChooseReasonContract;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by furqan on 30/10/18.
 */

public class FlightCancellationChooseReasonPresenter extends BaseDaggerPresenter<FlightCancellationChooseReasonContract.View>
        implements FlightCancellationChooseReasonContract.Presenter {

    private FlightCancellationGetReasonsUseCase flightCancellationGetReasonsUseCase;

    @Inject
    public FlightCancellationChooseReasonPresenter(FlightCancellationGetReasonsUseCase flightCancellationGetReasonsUseCase) {
        this.flightCancellationGetReasonsUseCase = flightCancellationGetReasonsUseCase;
    }

    @Override
    public void getReasonList() {
        flightCancellationGetReasonsUseCase.execute(new Subscriber<List<FlightCancellationReasonViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onNext(List<FlightCancellationReasonViewModel> flightCancellationReasonViewModels) {
                getView().renderReasonList((ArrayList<FlightCancellationReasonViewModel>) flightCancellationReasonViewModels);
            }
        });
    }
}
