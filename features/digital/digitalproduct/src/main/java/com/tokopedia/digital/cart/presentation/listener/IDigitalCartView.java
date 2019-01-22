package com.tokopedia.digital.cart.presentation.listener;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.digital.cart.presentation.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.presentation.model.VoucherDigital;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public interface IDigitalCartView extends IBaseView {

    void renderLoadingAddToCart();

    void renderAddToCartData(CartDigitalInfoData cartDigitalInfoData);

    void renderErrorAddToCart(String message);

    void renderErrorHttpAddToCart(String message);

    void renderErrorNoConnectionAddToCart(String message);

    void renderErrorTimeoutConnectionAddToCart(String message);

    void renderLoadingGetCartInfo();

    void renderCartDigitalInfoData(CartDigitalInfoData cartDigitalInfoData);

    void renderErrorGetCartData(String message);

    void renderErrorHttpGetCartData(String message);

    void renderErrorNoConnectionGetCartData(String message);

    void renderErrorTimeoutConnectionGetCartData(String message);

    void renderVoucherInfoData(VoucherDigital voucherDigital);

    void renderCouponInfoData(VoucherDigital voucherDigital);

    void renderToTopPay(CheckoutDigitalData checkoutDigitalData);

    void renderErrorCheckout(String message);

    void renderErrorHttpCheckout(String message);

    void renderErrorNoConnectionCheckout(String message);

    void renderErrorTimeoutConnectionCheckout(String message);

    void renderToInstantCheckoutPage(InstantCheckoutData instantCheckoutData);

    void renderErrorInstantCheckout(String message);

    void renderErrorHttpInstantCheckout(String message);

    void renderErrorNoConnectionInstantCheckout(String message);

    void renderErrorTimeoutConnectionInstantCheckout(String message);

    void closeViewWithMessageAlert(String message);

    void setCartDigitalInfo(CartDigitalInfoData cartDigitalInfo);

    void interruptRequestTokenVerification();

    String getUserId();

    String getAccountToken();

    String getWalletRefreshToken();

    CheckoutDataParameter getCheckoutData();

    String getClientNumber();

    boolean isInstantCheckout();

    int getProductId();

    String getIdemPotencyKey();

    void checkCallPermissionForNOTP();

    Context getApplicationContext();

    Activity getActivity();

    DigitalCheckoutPassData getPassData();

    void showProgressLoading(String title,String message);

    CartDigitalInfoData getCartDataInfo();

    void navigateToLoggedInPage();

    void showPostPaidDialog(String title, String content, String confirmButtonTitle);

    boolean isAlreadyShowPostPaid();
}
