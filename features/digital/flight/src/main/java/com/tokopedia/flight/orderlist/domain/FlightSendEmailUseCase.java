package com.tokopedia.flight.orderlist.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.orderlist.data.cloud.entity.SendEmailEntity;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by alvarisi on 12/6/17.
 */

public class FlightSendEmailUseCase extends UseCase<SendEmailEntity> {
    private static final String PARAM_ID = "invoice_id";
    private static final String USER_ID = "uid";
    private static final String PARAM_EMAIL = "email";
    private static final String DEFAULT_EMPTY_VALUE = "";
    private FlightRepository flightRepository;

    public FlightSendEmailUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<SendEmailEntity> createObservable(RequestParams requestParams) {
        return flightRepository.sendEmail(requestParams.getParameters());
    }

    public RequestParams createRequestParams(String orderId, String userId, String email) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_ID, orderId);
        requestParams.putString(USER_ID, userId);
        requestParams.putString(PARAM_EMAIL, email);
        return requestParams;
    }
}
