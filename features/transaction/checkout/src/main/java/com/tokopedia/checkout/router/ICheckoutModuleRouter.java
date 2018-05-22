package com.tokopedia.checkout.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;

import java.util.HashMap;

import retrofit2.Converter;

/**
 * @author anggaprasetiyo on 28/02/18.
 */

public interface ICheckoutModuleRouter {

    int LOYALTY_REQUEST_CODE = 77;

    Intent checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartListIntent(Context context, boolean couponActive);

    Intent checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartShipmentIntent(
            Context context, String additionalDataString, boolean couponActive);

    Intent checkoutModuleRouterGetProductDetailIntent(
            Context context, ProductPass productPass
    );

    Intent checkoutModuleRouterGetShopInfoIntent(
            Context context, String shopId
    );

    Intent checkoutModuleRouterGetInsuranceTncActivityIntent();

    Intent checkoutModuleRouterGetPickupPointActivityFromCartMultipleAddressIntent(Activity activity,
                                                                                   int cartPosition,
                                                                                   String districtName,
                                                                                   HashMap<String, String> params);

    Intent checkoutModuleRouterGetPickupPointActivityFromCartSingleAddressIntent(Activity activity, String districtName,
                                                                                 HashMap<String, String> params);

    ChuckInterceptor checkoutModuleRouterGetCartCheckoutChuckInterceptor();

    FingerprintInterceptor checkoutModuleRouterGetCartCheckoutFingerPrintInterceptor();

    Converter.Factory checkoutModuleRouterGetWS4TkpdResponseConverter();

    Converter.Factory checkoutModuleRouterGetStringResponseConverter();

    Intent getHomeFeedIntent(Context context);
}
