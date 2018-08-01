package com.tokopedia.flight.review.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by furqan on 26/06/18.
 */

public class FlightCancelVoucherUseCase extends UseCase<Boolean> {

    private FlightRepository flightRepository;

    @Inject
    public FlightCancelVoucherUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return flightRepository.cancelVoucherApply();
    }

    public RequestParams createEmptyParams() {
        return RequestParams.EMPTY;
    }
}
