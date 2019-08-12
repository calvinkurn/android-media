package com.tokopedia.purchase_platform.checkout.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.promocheckout.common.data.entity.request.Promo;
import com.tokopedia.transactiondata.entity.response.cod.Data;

import java.security.PublicKey;

import okhttp3.Interceptor;
import retrofit2.Converter;

/**
 * @author anggaprasetiyo on 28/02/18.
 */

public interface ICheckoutModuleRouter {

    Intent checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartListIntent(
            boolean couponActive, String additionalStringData, int pageTracking,
            String cartString, Promo promo
    );

    Intent checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartShipmentIntent(
            boolean couponActive, String additionalStringData, boolean isOneClickShipment, int pageTracking,
            Promo promo);

    void checkoutModuleRouterResetBadgeCart();

    String checkoutModuleRouterGetAutoApplyCouponBranchUtil();

    Intent checkoutModuleRouterGetShopInfoIntent(String shopId);

    Intent checkoutModuleRouterGetWhislistIntent();

    Interceptor checkoutModuleRouterGetCartCheckoutChuckInterceptor();

    Interceptor checkoutModuleRouterGetCartCheckoutFingerPrintInterceptor();

    Converter.Factory checkoutModuleRouterGetWS4TkpdResponseConverter();

    Converter.Factory checkoutModuleRouterGetStringResponseConverter();

    Intent checkoutModuleRouterGetHomeIntent(Context context);

    Intent getGeolocationIntent(Context context, LocationPass locationPass);

    boolean checkoutModuleRouterGetEnableFingerprintPayment();

    PublicKey checkoutModuleRouterGeneratePublicKey();

    String checkoutModuleRouterGetPublicKey(PublicKey publicKey);

    Intent checkoutModuleRouterGetRecentViewIntent();

    Intent getPromoCheckoutDetailIntentWithCode(String promoCode, boolean promoCouponActive, boolean oneClickShipment, int pageTracking, Promo promo);

    Intent getPromoCheckoutListIntentWithCode(String promoCode, boolean promoCouponActive, boolean oneClickShipment, int pageTracking,
                                              Promo promo);

    Intent getCodPageIntent(Context context, Data data);

}
