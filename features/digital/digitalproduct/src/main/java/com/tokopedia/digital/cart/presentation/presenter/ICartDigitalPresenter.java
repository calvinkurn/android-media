package com.tokopedia.digital.cart.presentation.presenter;

import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;

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

    void sendAnalyticsATCSuccess(CartDigitalInfoData cartDigitalInfoData, int extraComeFrom);

    void onClearVoucher();

    void onPaymentSuccess(String categoryId);

    void onFirstTimeLaunched();

    void onLoginResultReceived();
}
