package com.tokopedia.flight.booking.domain;

import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

public class FlightAddToCartUseCase {
    public FlightAddToCartUseCase(FlightRepository flightRepository,
                                  String flightSearchJourneyByIdUseCase) {

    }
    public Observable<CartEntity> createObservable(RequestParams requestParams){
        return null;
    }
}
