package com.tokopedia.common_digital.common;

import android.content.Intent;

import java.util.Map;

import okhttp3.Interceptor;

/**
 * Created by Rizky on 13/08/18.
 */
public interface DigitalRouter {

    Interceptor getChuckInterceptor();

    boolean isSupportedDelegateDeepLink(String url);

    Intent getIntentDeepLinkHandlerActivity();

    String getGeneratedOverrideRedirectUrlPayment(String url);

    Map<String,String> getGeneratedOverrideRedirectHeaderUrlPayment(String urlFinal);

}