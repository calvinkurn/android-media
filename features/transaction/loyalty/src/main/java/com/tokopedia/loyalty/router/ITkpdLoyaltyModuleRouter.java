package com.tokopedia.loyalty.router;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;

import retrofit2.Converter;
import rx.Observable;


/**
 * @author anggaprasetiyo on 28/02/18.
 */

public interface ITkpdLoyaltyModuleRouter {

    ChuckInterceptor loyaltyModuleRouterGetCartCheckoutChuckInterceptor();

    FingerprintInterceptor loyaltyModuleRouterGetCartCheckoutFingerPrintInterceptor();

    Converter.Factory loyaltyModuleRouterGetStringResponseConverter();
}
