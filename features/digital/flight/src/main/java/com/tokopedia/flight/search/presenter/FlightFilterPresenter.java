package com.tokopedia.flight.search.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.domain.FlightFilterCountUseCase;
import com.tokopedia.flight.search.domain.FlightSearchStatisticUseCase;
import com.tokopedia.flight.search.domain.FlightSearchUseCase;
import com.tokopedia.flight.search.view.FlightFilterCountView;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightFilterPresenter extends BaseDaggerPresenter<FlightFilterCountView> {

    private final FlightFilterCountUseCase flightFilterCountUseCase;
    private FlightSearchStatisticUseCase flightSearchStatisticUseCase;

    @Inject
    public FlightFilterPresenter(FlightFilterCountUseCase flightFilterCountUseCase, FlightSearchStatisticUseCase flightSearchStatisticUseCase) {
        this.flightFilterCountUseCase = flightFilterCountUseCase;
        this.flightSearchStatisticUseCase = flightSearchStatisticUseCase;
    }

    public void getFlightCount(boolean isReturning, boolean isFromCache, FlightFilterModel flightFilterModel) {
        flightFilterCountUseCase.execute(FlightSearchUseCase.generateRequestParams(
                null,
                isReturning, isFromCache, flightFilterModel,
                FlightSortOption.NO_PREFERENCE),
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
        flightSearchStatisticUseCase.execute(FlightSearchUseCase.generateRequestParams(
                null,
                getView().isReturning(), true, null, FlightSortOption.NO_PREFERENCE),
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
