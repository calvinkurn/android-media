package com.tokopedia.digital.newcart.domain.usecase;

import com.tokopedia.device.info.DeviceInfo;
import com.tokopedia.digital.newcart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.digital.newcart.domain.ICheckoutRepository;
import com.tokopedia.digital.newcart.domain.model.CheckoutDigitalData;
import com.tokopedia.track.TrackApp;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func1;

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
        return Observable.just(requestBodyCheckout)
                .map(requestBodyCheckout1 -> {
                    requestBodyCheckout1.getAttributes().getAppsFlyer().setDeviceId(
                            TrackApp.getInstance().getAppsFlyer().getGoogleAdId());
                    return requestBodyCheckout1;
                })
                .flatMap((Func1<RequestBodyCheckout, Observable<CheckoutDigitalData>>) requestBodyCheckout2 ->
                        checkoutRepository.checkoutCart(requestBodyCheckout2)
                );
    }

    public RequestParams createRequestParams(RequestBodyCheckout requestBodyCheckout) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_REQUEST_BODY_CHECKOUT, requestBodyCheckout);
        return requestParams;
    }

}
