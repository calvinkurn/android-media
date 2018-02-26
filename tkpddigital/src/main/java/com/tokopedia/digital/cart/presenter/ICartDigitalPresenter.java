package com.tokopedia.digital.cart.presenter;

/**
 * @author anggaprasetiyo on 2/24/17.
 */

public interface ICartDigitalPresenter {

    void processGetCartData(String digitalCategoryId);

    void processGetCartDataAfterCheckout(String digitalCategoryId);

    void processAddToCart();

    void processCheckVoucher(String voucherCode, String digitalCategoryId);

    void processToCheckout();

    void processToInstantCheckout();

    void processPatchOtpCart(String digitalCategoryId);

    void autoApplyCouponIfAvailable(String digitalCategoryId);

    void callPermissionCheckSuccess();

    void callPermissionCheckFail();

}
