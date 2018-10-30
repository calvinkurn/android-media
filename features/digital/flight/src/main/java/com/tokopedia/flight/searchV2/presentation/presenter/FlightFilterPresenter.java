package com.tokopedia.flight.searchV2.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.searchV2.presentation.FlightFilterCountView;
import com.tokopedia.flight.searchV2.presentation.model.resultstatistics.FlightSearchStatisticModel;
import com.tokopedia.flight.searchV2.domain.usecase.FlightSearchCountUseCase;
import com.tokopedia.flight.searchV2.domain.usecase.FlightSearchStatisticsUseCase;
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightFilterPresenter extends BaseDaggerPresenter<FlightFilterCountView> {

    private final FlightSearchCountUseCase flightFilterCountUseCase;
    private FlightSearchStatisticsUseCase flightSearchStatisticUseCase;

    @Inject
    public FlightFilterPresenter(FlightSearchCountUseCase flightFilterCountUseCase, FlightSearchStatisticsUseCase flightSearchStatisticUseCase) {
        this.flightFilterCountUseCase = flightFilterCountUseCase;
        this.flightSearchStatisticUseCase = flightSearchStatisticUseCase;
    }

    public void getFlightCount(boolean isReturning, boolean isFromCache, FlightFilterModel flightFilterModel) {
        flightFilterCountUseCase.execute(flightFilterCountUseCase.createRequestParams(flightFilterModel),
                getSubscriberFlightCount());
    }

    @Override
    public void detachView() {
        super.detachView();
        flightFilterCountUseCase.unsubscribe();
    }

    private Subscriber<Integer> getSubscriberFlightCount() {
        return new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onErrorGetCount(e);
            }

            @Override
            public void onNext(Integer integer) {
                getView().onSuccessGetCount(integer);
            }
        };
    }

    public void getFilterStatisticData() {
        getView().showGetFilterStatisticLoading();
        FlightFilterModel flightFilterModel = new FlightFilterModel();
        flightSearchStatisticUseCase.execute(flightSearchStatisticUseCase.createRequestParams(flightFilterModel),
                getSubscriberSearchStatisticFlight());
    }


    public Subscriber<FlightSearchStatisticModel> getSubscriberSearchStatisticFlight() {
        return new Subscriber<FlightSearchStatisticModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().hideGetFilterStatisticLoading();
                getView().showErrorGetFilterStatistic(e);
            }

            @Override
            public void onNext(FlightSearchStatisticModel statisticModel) {
                getView().hideGetFilterStatisticLoading();
                getView().onSuccessGetStatistic(statisticModel);
            }
        };
    }
}
