package com.tokopedia.checkout.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

<<<<<<< HEAD
=======
import com.tokopedia.core.manage.people.address.model.AddressModel;
import com.tokopedia.core.manage.people.address.model.Token;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
>>>>>>> f6fc85365a36b1ecd77a8fca9183fe71375629c2
import com.tokopedia.topads.sdk.domain.model.Product;

import java.security.PublicKey;

import okhttp3.Interceptor;
import retrofit2.Converter;

/**
 * @author anggaprasetiyo on 28/02/18.
 */

public interface ICheckoutModuleRouter {

    Intent checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartListIntent(
            boolean couponActive, String additionalStringData, String defaultSelectedTab);

    Intent checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartShipmentIntent(
            boolean couponActive, String additionalStringData, String defaultSelectedTab);

    Intent checkoutModuleRouterGetProductDetailIntent(String productId);

    Intent checkoutModuleRouterGetProductDetailIntentForTopAds(Product product);

    Intent checkoutModuleRouterGetTransactionSummaryIntent();

    void checkoutModuleRouterResetBadgeCart();

    String checkoutModuleRouterGetAutoApplyCouponBranchUtil();

    Intent checkoutModuleRouterGetShopInfoIntent(String shopId);

    Intent checkoutModuleRouterGetWhislistIntent();

    Intent checkoutModuleRouterGetInsuranceTncActivityIntent();

    Interceptor checkoutModuleRouterGetCartCheckoutChuckInterceptor();

    Interceptor checkoutModuleRouterGetCartCheckoutFingerPrintInterceptor();

    Converter.Factory checkoutModuleRouterGetWS4TkpdResponseConverter();

    Converter.Factory checkoutModuleRouterGetStringResponseConverter();

    Intent checkoutModuleRouterGetHomeIntent(Context context);
<<<<<<< HEAD
=======

    Intent getAddAddressIntent(Activity activity, @Nullable AddressModel data, Token token, boolean isEdit, boolean isEmptyAddressFirst);
>>>>>>> f6fc85365a36b1ecd77a8fca9183fe71375629c2

    boolean checkoutModuleRouterGetEnableFingerprintPayment();

    PublicKey checkoutModuleRouterGeneratePublicKey();

    String checkoutModuleRouterGetPublicKey(PublicKey publicKey);

    void goToPurchasePage(Activity activity);

    Intent checkoutModuleRouterGetRecentViewIntent();
}
