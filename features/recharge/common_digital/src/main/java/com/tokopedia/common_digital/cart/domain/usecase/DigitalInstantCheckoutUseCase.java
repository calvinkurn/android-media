package com.tokopedia.common_digital.cart.domain.usecase;

import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.common_digital.cart.domain.IDigitalCartRepository;
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData;
import com.tokopedia.track.TrackApp;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func1;

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
        return Observable.just(requestBodyCheckout)
                .map(requestBodyCheckout1 -> {
                    requestBodyCheckout1.getAttributes().getAppsFlyer().setDeviceId(
                            TrackApp.getInstance().getAppsFlyer().getGoogleAdId());
                    return requestBodyCheckout1;
                })
                .flatMap((Func1<RequestBodyCheckout, Observable<InstantCheckoutData>>) requestBodyCheckout12 ->
                        digitalCartRepository.instantCheckout(requestBodyCheckout12));

    }

    public RequestParams createRequestParams(RequestBodyCheckout requestBodyCheckout) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_REQUEST_BODY_CHECKOUT, requestBodyCheckout);
        return requestParams;
    }

}
