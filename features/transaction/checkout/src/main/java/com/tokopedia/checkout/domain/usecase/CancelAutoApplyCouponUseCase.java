package com.tokopedia.checkout.domain.usecase;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Irfan Khoirul on 17/05/18.
 */

public class CancelAutoApplyCouponUseCase extends UseCase<String> {

    public static final String RESPONSE_DATA = "data";
    public static final String RESPONSE_SUCCESS = "success";

    private final ICartRepository cartRepository;
    private Context context;

    @Inject
    public CancelAutoApplyCouponUseCase(ICartRepository cartRepository, Context context) {
        this.cartRepository = cartRepository;
        this.context = context;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return cartRepository.cancelAutoApplyCoupon(AuthUtil.DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_DEVICE,
                AuthUtil.generateParamsNetwork(context, new TKPDMapParam<String, String>()));
    }

}
