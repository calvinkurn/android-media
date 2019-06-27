package com.tokopedia.flight.review.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

public class FlightCheckVoucherCodeUseCase {

    public FlightCheckVoucherCodeUseCase(FlightRepository flightRepository) {
//        this.flightRepository = flightRepository;
    }

    public Observable<AttributesVoucher> createObservable(RequestParams requestParams) {
        return null;
    }

    public RequestParams createRequestParams(String cartId, String voucherCode, String isCoupon){
        return RequestParams.create();
    }
}
