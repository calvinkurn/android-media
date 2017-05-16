package com.tokopedia.digital.cart.presenter;

/**
 * @author anggaprasetiyo on 2/24/17.
 */

public interface ICartDigitalPresenter {

    void processGetCartData();

    void processGetCartDataAfterCheckout();

    void processAddToCart();

    void processCheckVoucher();

    void processToCheckout();

    void processToInstantCheckout();

    void processPatchOtpCart();

}
