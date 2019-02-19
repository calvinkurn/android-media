package com.tokopedia.flight.orderlist.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by alvarisi on 12/6/17.
 */

public class FlightGetOrderUseCase extends UseCase<FlightOrder> {
    private static final String PARAM_ID = "invoice_id";
    private static final String DEFAULT_EMPTY_VALUE = "";
    private FlightRepository flightRepository;

    @Inject
    public FlightGetOrderUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<FlightOrder> createObservable(RequestParams requestParams) {
        return flightRepository.getOrder(requestParams.getString(PARAM_ID, DEFAULT_EMPTY_VALUE));
    }

    public RequestParams createRequestParams(String id) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_ID, id);
        return requestParams;
    }
}
