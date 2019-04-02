package com.tokopedia.gm.subscribe.view.presenter;

/**
 * Created by sebastianuskh on 1/26/17.
 */

public interface GmCheckoutPresenter {

    void getCurrentSelectedProduct(int productId);

    void getExtendSelectedProduct(int currentProductId, int autoSubscribeProductId);

    void checkVoucherCode(String voucherCode, Integer selectedProduct);

    void checkoutGMSubscribe(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode);

    void checkoutWithVoucherCheckGMSubscribe(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode);

    void clearCacheShopInfo();
}
