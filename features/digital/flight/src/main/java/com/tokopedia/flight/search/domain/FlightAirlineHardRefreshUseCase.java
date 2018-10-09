package com.tokopedia.flight.search.domain;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 3/15/18.
 */

public class FlightAirlineHardRefreshUseCase extends UseCase<Boolean> {
    private FlightRepository flightRepository;

    @Inject
    public FlightAirlineHardRefreshUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return flightRepository.refreshAirlines().map(new Func1<List<FlightAirlineDB>, Boolean>() {
            @Override
            public Boolean call(List<FlightAirlineDB> flightAirlineDBS) {
                return true;
            }
        });
    }
}
