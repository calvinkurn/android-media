package com.tokopedia.checkout.router;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;

import java.security.PublicKey;
import java.util.HashMap;

import retrofit2.Converter;

/**
 * @author anggaprasetiyo on 28/02/18.
 */

public interface ICheckoutModuleRouter {

    int LOYALTY_REQUEST_CODE = 77;

    Intent checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartListIntent(
            Context context, boolean couponActive, String additionalStringData, String defaultSelectedTab);

    Intent checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartShipmentIntent(
            Context context, boolean couponActive, String additionalStringData, String defaultSelectedTab);

    Intent checkoutModuleRouterGetProductDetailIntent(
            Context context, ProductPass productPass
    );

    Intent checkoutModuleRouterGetShopInfoIntent(
            Context context, String shopId
    );

    Intent checkoutModuleRouterGetWhislistIntent();

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

    Intent getHomePageIntent(Context context);

    boolean checkoutModuleRouterGetEnableFingerprintPayment();

    PublicKey checkoutModuleRouterGeneratePublicKey();

    String checkoutModuleRouterGetPublicKey(PublicKey publicKey);

    void goToPurchasePage(Activity activity);
}
