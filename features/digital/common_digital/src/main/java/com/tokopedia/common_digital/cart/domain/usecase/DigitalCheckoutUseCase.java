package com.tokopedia.common_digital.cart.domain.usecase;

import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.common_digital.cart.data.repository.DigitalCartRepository;
import com.tokopedia.common_digital.cart.view.model.CheckoutDigitalData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by Rizky on 28/08/18.
 */
public class DigitalCheckoutUseCase extends UseCase<CheckoutDigitalData> {

    private static final String PARAM_REQUEST_BODY_CHECKOUT = "PARAM_REQUEST_BODY_CHECKOUT";

    private DigitalCartRepository digitalCartRepository;

    public DigitalCheckoutUseCase(DigitalCartRepository digitalCartRepository) {
        this.digitalCartRepository = digitalCartRepository;
    }

    @Override
    public Observable<CheckoutDigitalData> createObservable(RequestParams requestParams) {
        RequestBodyCheckout requestBodyCheckout = (RequestBodyCheckout) requestParams.getObject(PARAM_REQUEST_BODY_CHECKOUT);
        return digitalCartRepository.checkoutCart(requestBodyCheckout);
    }

    public RequestParams createRequestParams(RequestBodyCheckout requestBodyCheckout) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_REQUEST_BODY_CHECKOUT, requestBodyCheckout);
        return requestParams;
    }

}
