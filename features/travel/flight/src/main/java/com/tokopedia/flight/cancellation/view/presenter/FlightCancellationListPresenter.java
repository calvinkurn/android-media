package com.tokopedia.flight.cancellation.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.cancellation.domain.FlightCancellationGetCancellationListUseCase;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationListContract;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by furqan on 30/04/18.
 */

public class FlightCancellationListPresenter extends BaseDaggerPresenter<FlightCancellationListContract.View>
        implements FlightCancellationListContract.Presenter {

    private FlightCancellationGetCancellationListUseCase flightCancellationGetCancellationListUseCase;

    @Inject
    public FlightCancellationListPresenter(FlightCancellationGetCancellationListUseCase flightCancellationGetCancellationListUseCase) {
        this.flightCancellationGetCancellationListUseCase = flightCancellationGetCancellationListUseCase;
    }

    @Override
    public void onViewCreated() {
        flightCancellationGetCancellationListUseCase.execute(
                flightCancellationGetCancellationListUseCase.createRequestParams(
                        getView().getInvoiceId()
                ),
                new Subscriber<List<? extends FlightCancellationListViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<? extends FlightCancellationListViewModel> flightCancellationListViewModels) {
                        getView().setFlightCancellationList((List<FlightCancellationListViewModel>) flightCancellationListViewModels);
                        getView().renderList();
                    }
                });
    }
}
