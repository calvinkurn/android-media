package com.tokopedia.loyalty.router;

import com.tokopedia.network.interceptor.FingerprintInterceptor;

import retrofit2.Converter;


/**
 * @author anggaprasetiyo on 28/02/18.
 */

public interface ITkpdLoyaltyModuleRouter {

    Converter.Factory loyaltyModuleRouterGetStringResponseConverter();
}
