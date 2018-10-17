package com.tokopedia.flight.searchV2.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;
import com.tokopedia.flight.searchV2.domain.usecase.FlightSearchCountUseCase;
import com.tokopedia.flight.searchV2.domain.usecase.FlightSearchStatisticsUseCase;
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Rizky on 17/10/18.
 */
public class FlightSearchV2FilterPresenter extends BaseDaggerPresenter<FlightSearchV2FilterContract.View>
        implements FlightSearchV2FilterContract.Presenter {

    private FlightSearchStatisticsUseCase flightSearchStatisticsUseCase;
    private FlightSearchCountUseCase flightSearchCountUseCase;

    @Inject
    public FlightSearchV2FilterPresenter(FlightSearchStatisticsUseCase flightSearchStatisticsUseCase,
                                         FlightSearchCountUseCase flightSearchCountUseCase) {
        this.flightSearchStatisticsUseCase = flightSearchStatisticsUseCase;
        this.flightSearchCountUseCase = flightSearchCountUseCase;
    }

    @Override
    public void getFilterStatisticData() {
        flightSearchStatisticsUseCase.execute(
                flightSearchStatisticsUseCase.createRequestParams(new FlightFilterModel()),
                new Subscriber<FlightSearchStatisticModel>() {
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
                }
        );
    }

    @Override
    public void getFlightCount(boolean isReturning, boolean b, FlightFilterModel flightFilterModel) {
        flightSearchCountUseCase.execute(
                flightSearchCountUseCase.createRequestParams(flightFilterModel),
                new Subscriber<Integer>() {
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
                }
        );
    }

}
