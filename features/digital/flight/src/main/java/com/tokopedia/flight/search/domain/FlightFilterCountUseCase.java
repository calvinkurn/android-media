package com.tokopedia.flight.search.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightFilterCountUseCase extends UseCase<Integer> {
    private final FlightRepository flightRepository;

    @Inject
    public FlightFilterCountUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<Integer> createObservable(RequestParams requestParams) {
        return flightRepository.getFlightSearchCount(requestParams);
    }

}
