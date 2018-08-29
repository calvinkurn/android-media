package com.tokopedia.flight.airport.domain.interactor;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 11/21/17.
 */

public class FlightAirportPickerBackgroundUseCase extends UseCase<Boolean> {

    public static final String VERSION_AIRPORT = "version_airport";
    private final FlightRepository flightRepository;

    @Inject
    public FlightAirportPickerBackgroundUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return flightRepository.getAirportListBackground(requestParams.getLong(VERSION_AIRPORT, 0));
    }

    public RequestParams createRequestParams(long versionAirport) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putLong(VERSION_AIRPORT, versionAirport);
        return requestParams;
    }
}
