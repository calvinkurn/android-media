package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Irfan Khoirul on 17/05/18.
 */

public class CancelAutoApplyCouponUseCase extends UseCase<String> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING = "PARAM_REQUEST_AUTH_MAP_STRING";

    public static final String RESPONSE_DATA = "data";
    public static final String RESPONSE_SUCCESS = "success";

    private final ICartRepository cartRepository;

    @Inject
    public CancelAutoApplyCouponUseCase(ICartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<String> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING);

        return cartRepository.cancelAutoApplyCoupon(AuthUtil.DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_DEVICE, param);
    }

}
