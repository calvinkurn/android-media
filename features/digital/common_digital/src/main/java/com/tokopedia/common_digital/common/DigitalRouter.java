package com.tokopedia.common_digital.common;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;

import java.util.Map;

import okhttp3.Interceptor;

/**
 * Created by Rizky on 13/08/18.
 */
public interface DigitalRouter {
    int REQUEST_CODE_CART_DIGITAL = 216;
    String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    String MULTICHECKOUT_CART_REMOTE_CONFIG = "mainapp_digital_enable_multicheckout_cart";
    int PAYMENT_SUCCESS = 5;

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);

    Interceptor getChuckInterceptor();

    boolean isSupportApplink(String url);

    Intent getIntentDeepLinkHandlerActivity();

    String getGeneratedOverrideRedirectUrlPayment(String url);

    Map<String,String> getGeneratedOverrideRedirectHeaderUrlPayment(String urlFinal);

    Intent instanceIntentCartDigitalProduct(DigitalCheckoutPassData passData);

    Intent getHomeIntent(Context context);

}