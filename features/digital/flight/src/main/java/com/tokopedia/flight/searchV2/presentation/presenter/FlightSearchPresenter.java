package com.tokopedia.flight.searchV2.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.search.domain.FlightAirlineHardRefreshUseCase;
import com.tokopedia.flight.searchV2.domain.FlightSearchV2UseCase;
import com.tokopedia.flight.searchV2.domain.FlightSortAndFilterUseCase;
import com.tokopedia.flight.searchV2.presentation.contract.FlightSearchContract;
import com.tokopedia.flight.searchV2.presentation.model.FlightJourneyViewModel;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by furqan on 01/10/18.
 */

public class FlightSearchPresenter extends BaseDaggerPresenter<FlightSearchContract.View>
        implements FlightSearchContract.Presenter{

    private FlightSearchV2UseCase flightSearchV2UseCase;
    private FlightSortAndFilterUseCase flightSortAndFilterUseCase;
    private FlightAirlineHardRefreshUseCase flightAirlineHardRefreshUseCase;

    @Inject
    public FlightSearchPresenter(FlightSearchV2UseCase flightSearchV2UseCase,
                                 FlightSortAndFilterUseCase flightSortAndFilterUseCase,
                                 FlightAirlineHardRefreshUseCase flightAirlineHardRefreshUseCase) {
        this.flightSearchV2UseCase = flightSearchV2UseCase;
        this.flightSortAndFilterUseCase = flightSortAndFilterUseCase;
        this.flightAirlineHardRefreshUseCase = flightAirlineHardRefreshUseCase;
    }

    @Override
    public void initialize() {
        if (!getView().isReturning()) {
            flightAirlineHardRefreshUseCase.execute(RequestParams.EMPTY, new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    if (isViewAttached()) {
                    }
                }

                @Override
                public void onNext(Boolean aBoolean) {

                }
            });
        } else {

        }
    }

    @Override
    public void onSeeDetailItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {

    }

    @Override
    public void onSearchItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {

    }

    @Override
    public void onSearchItemClicked(FlightJourneyViewModel journeyViewModel) {

    }

    @Override
    public void onSuccessDateChanged(int year, int month, int dayOfMonth) {

    }

    @Override
    public void setDelayHorizontalProgress() {

    }
}
