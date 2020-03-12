package com.tokopedia.common_digital.cart.domain.usecase;

import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.common_digital.cart.domain.IDigitalCartRepository;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class DigitalGetCartUseCase extends UseCase<CartDigitalInfoData> {

    private final static String PARAM_REQUEST_BODY_ATC_DIGITAL = "PARAM_REQUEST_BODY_ATC_DIGITAL";
    private final static String PARAM_IDEM_POTENCY_KEY = "PARAM_IDEM_POTENCY_KEY";

    private IDigitalCartRepository digitalCartRepository;

    public DigitalGetCartUseCase(IDigitalCartRepository digitalCartRepository) {
        this.digitalCartRepository = digitalCartRepository;
    }

    @Override
    public Observable<CartDigitalInfoData> createObservable(RequestParams requestParams) {
        RequestBodyAtcDigital requestBodyAtcDigital = (RequestBodyAtcDigital) requestParams
                .getObject(PARAM_REQUEST_BODY_ATC_DIGITAL);
        String idemPotencyKeyHeader = requestParams.getString(PARAM_IDEM_POTENCY_KEY, "");
        return digitalCartRepository.getCart(requestBodyAtcDigital, idemPotencyKeyHeader);
    }
}
