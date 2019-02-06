package com.tokopedia.common_digital.cart.domain.usecase;

import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.common_digital.cart.domain.IDigitalCartRepository;
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by Rizky on 28/08/18.
 */
public class DigitalInstantCheckoutUseCase extends UseCase<InstantCheckoutData> {

    private static final String PARAM_REQUEST_BODY_CHECKOUT = "PARAM_REQUEST_BODY_CHECKOUT";

    private IDigitalCartRepository digitalCartRepository;

    public DigitalInstantCheckoutUseCase(IDigitalCartRepository digitalCartRepository) {
        this.digitalCartRepository = digitalCartRepository;
    }

    @Override
    public Observable<InstantCheckoutData> createObservable(RequestParams requestParams) {
        RequestBodyCheckout requestBodyCheckout = (RequestBodyCheckout) requestParams.getObject(PARAM_REQUEST_BODY_CHECKOUT);
        return digitalCartRepository.instantCheckout(requestBodyCheckout);
    }

    public RequestParams createRequestParams(RequestBodyCheckout requestBodyCheckout) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_REQUEST_BODY_CHECKOUT, requestBodyCheckout);
        return requestParams;
    }

}
