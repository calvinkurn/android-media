package com.tokopedia.common_digital.cart.domain.usecase;

import com.tokopedia.common_digital.cart.domain.IDigitalCartRepository;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class DigitalGetCartUseCase extends UseCase<CartDigitalInfoData> {

    private final static String PARAM_CATEGORY_ID = "category_id";
    private final static String PARAM_USER_ID = "user_id";
    private final static String PARAM_DEVICE_ID = "device_id";
    private final static String PARAM_OS_TYPE = "os_type";

    private IDigitalCartRepository digitalCartRepository;

    public DigitalGetCartUseCase(IDigitalCartRepository digitalCartRepository) {
        this.digitalCartRepository = digitalCartRepository;
    }

    @Override
    public Observable<CartDigitalInfoData> createObservable(RequestParams requestParams) {
        return digitalCartRepository.getCart(requestParams);
    }

    public RequestParams createRequestParams(String categoryId, String userId, String deviceId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_CATEGORY_ID, categoryId);
        requestParams.putString(PARAM_USER_ID, userId);
        requestParams.putString(PARAM_DEVICE_ID, deviceId);
        requestParams.putString(PARAM_OS_TYPE, "1");
        return requestParams;
    }
}
