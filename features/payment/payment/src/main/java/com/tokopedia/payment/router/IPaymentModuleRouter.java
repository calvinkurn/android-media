package com.tokopedia.payment.router;

import android.app.Activity;
import android.content.Intent;

import java.util.Map;

/**
 * @author anggaprasetiyo on 7/11/17.
 */

public interface IPaymentModuleRouter {

    String getSchemeAppLinkCancelPayment();

    boolean isSupportedDelegateDeepLink(String appLinkScheme);

    Intent getIntentDeepLinkHandlerActivity();

    String getBaseUrlDomainPayment();

    void actionAppLinkPaymentModule(Activity activity, String appLinkScheme);

    String getGeneratedOverrideRedirectUrlPayment(String originUrl);

    Map<String, String> getGeneratedOverrideRedirectHeaderUrlPayment(String originUrl);
}
