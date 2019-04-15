package com.tokopedia.digital.newcart.domain.usecase;

import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.digital.newcart.domain.ICheckoutRepository;
import com.tokopedia.digital.newcart.domain.model.CheckoutDigitalData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by Rizky on 28/08/18.
 */
public class DigitalCheckoutUseCase extends UseCase<CheckoutDigitalData> {

    private static final String PARAM_REQUEST_BODY_CHECKOUT = "PARAM_REQUEST_BODY_CHECKOUT";

    private ICheckoutRepository checkoutRepository;

    public DigitalCheckoutUseCase(ICheckoutRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
    }

    @Override
    public Observable<CheckoutDigitalData> createObservable(RequestParams requestParams) {
        RequestBodyCheckout requestBodyCheckout = (RequestBodyCheckout) requestParams.getObject(PARAM_REQUEST_BODY_CHECKOUT);
        return checkoutRepository.checkoutCart(requestBodyCheckout);
    }

    public RequestParams createRequestParams(RequestBodyCheckout requestBodyCheckout) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_REQUEST_BODY_CHECKOUT, requestBodyCheckout);
        return requestParams;
    }

}
