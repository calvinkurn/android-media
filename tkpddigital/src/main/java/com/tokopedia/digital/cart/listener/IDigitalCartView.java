package com.tokopedia.digital.cart.listener;

import com.tokopedia.digital.cart.model.CartDigitalInfoData;
import com.tokopedia.digital.cart.model.CheckoutDataParameter;
import com.tokopedia.digital.cart.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.model.InstantCheckoutData;
import com.tokopedia.digital.cart.model.VoucherDigital;

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

//    void renderErrorCheckVoucher(String message);

//    void renderErrorHttpCheckVoucher(String message);

//    void renderErrorNoConnectionCheckVoucher(String message);

//    void renderErrorTimeoutConnectionCheckVoucher(String message);

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

    void interruptRequestTokenVerification(CartDigitalInfoData cartDigitalInfoData);

    void interruptRequestTokenVerification();

    String getUserId();

    String getAccountToken();

    String getWalletRefreshToken();

//    String getDigitalCategoryId();

//    String getVoucherCode();

    CheckoutDataParameter getCheckoutData();

    String getClientNumber();

    boolean isInstantCheckout();

    int getProductId();

    String getIdemPotencyKey();


}
