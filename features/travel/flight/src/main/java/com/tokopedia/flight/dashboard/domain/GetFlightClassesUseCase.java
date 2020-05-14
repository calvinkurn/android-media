package com.tokopedia.flight.dashboard.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 10/30/17.
 */

public class GetFlightClassesUseCase extends UseCase<List<FlightClassEntity>> {
    private FlightRepository flightRepository;

    public GetFlightClassesUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<List<FlightClassEntity>> createObservable(RequestParams requestParams) {
        return flightRepository.getFlightClasses();
    }

    public RequestParams createRequestParam() {
        return RequestParams.EMPTY;
    }
}
