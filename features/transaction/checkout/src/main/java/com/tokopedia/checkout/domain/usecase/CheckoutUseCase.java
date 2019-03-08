package com.tokopedia.checkout.domain.usecase;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.mapper.ICheckoutMapper;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.transactiondata.entity.shared.checkout.CheckoutData;
import com.tokopedia.transactiondata.entity.request.CheckoutRequest;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.security.PublicKey;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author anggaprasetiyo on 26/02/18.
 */

public class CheckoutUseCase extends UseCase<CheckoutData> {
    public static final String PARAM_TRADE_IN_DATA = "PARAM_TRADE_IN_DATA";
    public static final String PARAM_CARTS = "carts";
    private static final String PARAM_OPTIONAL = "optional";
    private static final String PARAM_IS_THANKYOU_NATIVE = "is_thankyou_native";
    private static final String PARAM_IS_THANKYOU_NATIVE_NEW = "is_thankyou_native_new";
    private static final String PARAM_FINGERPRINT_PUBLICKEY = "fingerprint_publickey";
    private static final String PARAM_FINGERPRINT_SUPPORT = "fingerprint_support";
    public static final String PARAM_ONE_CLICK_SHIPMENT = "is_one_click_shipment";
    public static final String PARAM_IS_EXPRESS = "is_express";
    public static final String PARAM_IS_TRADEIN = "is_tradein";
    public static final String PARAM_DEVICE_ID = "dev_id";

    private final ICartRepository cartRepository;
    private final ICheckoutMapper checkoutMapper;
    private final ICheckoutModuleRouter checkoutModuleRouter;

    @Inject
    public CheckoutUseCase(ICartRepository cartRepository,
                           ICheckoutMapper checkoutMapper,
                           ICheckoutModuleRouter checkoutModuleRouter) {
        this.cartRepository = cartRepository;
        this.checkoutMapper = checkoutMapper;
        this.checkoutModuleRouter = checkoutModuleRouter;
    }

    @Override
    public Observable<CheckoutData> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        CheckoutRequest checkoutRequest = (CheckoutRequest) requestParams.getObject(PARAM_CARTS);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String paramCartJson = gson.toJson(checkoutRequest);
        param.put(PARAM_CARTS, paramCartJson);
        param.put(PARAM_OPTIONAL, "0");
        param.put(PARAM_IS_THANKYOU_NATIVE, "1");
        param.put(PARAM_IS_THANKYOU_NATIVE_NEW, "1");
        param.put(PARAM_ONE_CLICK_SHIPMENT, String.valueOf(requestParams.getBoolean(
                PARAM_ONE_CLICK_SHIPMENT, false)));
        param.put(PARAM_IS_EXPRESS, String.valueOf(requestParams.getBoolean(PARAM_IS_EXPRESS, false)));
        param.putAll((Map<String, String>) requestParams.getObject(PARAM_TRADE_IN_DATA));
        param = createParamFingerprint(param);
        return cartRepository.checkout(param)
                .map(checkoutMapper::convertCheckoutData);
    }

    private TKPDMapParam<String, String> createParamFingerprint(TKPDMapParam<String, String> tkpdMapParam) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkoutModuleRouter != null
                && checkoutModuleRouter.checkoutModuleRouterGetEnableFingerprintPayment()) {
            PublicKey publicKey = checkoutModuleRouter.checkoutModuleRouterGeneratePublicKey();
            if (publicKey != null) {
                tkpdMapParam.put(PARAM_FINGERPRINT_PUBLICKEY, checkoutModuleRouter.checkoutModuleRouterGetPublicKey(publicKey));
                tkpdMapParam.put(PARAM_FINGERPRINT_SUPPORT, String.valueOf(true));
            } else {
                tkpdMapParam.put(PARAM_FINGERPRINT_SUPPORT, String.valueOf(false));
            }
        } else {
            tkpdMapParam.put(PARAM_FINGERPRINT_SUPPORT, String.valueOf(false));
        }
        return tkpdMapParam;

    }
}
