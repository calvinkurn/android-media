package com.tokopedia.checkout.view.feature.cartlist;

import java.util.Map;

/**
 * @author anggaprasetiyo on 28/08/18.
 */
public interface ICartListAnalyticsListener {

    // 5 condition EE Checkout Step 1 as eventLabel
    // at 15 feb add more condition for eligible COD, request by logistic tribe for fetures cod, Lourent
    //======================================================================
    void sendAnalyticsOnSuccessToCheckoutDefault(Map<String, Object> eeData);

    void sendAnalyticsOnSuccessToCheckoutCheckAll(Map<String, Object> eeData);

    void sendAnalyticsOnSuccessToCheckoutPartialShop(Map<String, Object> eeData);

    void sendAnalyticsOnSuccessToCheckoutPartialProduct(Map<String, Object> eeData);

    void sendAnalyticsOnSuccessToCheckoutPartialShopAndProduct(Map<String, Object> eeData);

    //==========

    void sendAnalyticsOnSuccessToCheckoutDefaultEligibleCod(Map<String, Object> eeData);

    void sendAnalyticsOnSuccessToCheckoutCheckAllEligibleCod(Map<String, Object> eeData);

    void sendAnalyticsOnSuccessToCheckoutPartialShopEligibleCod(Map<String, Object> eeData);

    void sendAnalyticsOnSuccessToCheckoutPartialProductEligibleCod(Map<String, Object> eeData);

    void sendAnalyticsOnSuccessToCheckoutPartialShopAndProductEligibleCod(Map<String, Object> eeData);
    //=======================================================================

    void sendAnalyticsOnButtonCheckoutClicked();

    void sendAnalyticsOnClickBackArrow();

    void sendAnalyticsOnClickRemoveButtonHeader();

    void sendAnalyticsOnClickConfirmationRemoveCartSelectedWithAddToWishList(Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnClickRemoveIconCartItem();

    void sendAnalyticsOnClickButtonPlusCartItem();

    void sendAnalyticsOnClickButtonMinusCartItem();

    void sendAnalyticsOnClickProductNameCartItem(String productName);

    void sendAnalyticsOnClickShopNameCartItem(String shopName);

    void sendAnalyticsOnClickUsePromoCodeAndCoupon();

    void sendAnalyticsOnClickCancelPromoCodeAndCouponBanner();

    void sendAnalyticsOnClickRemoveCartConstrainedProduct(Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductWithAddToWishList(Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductNoAddToWishList(Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnClickQuantityCartItemInput(String quantity);

    void sendAnalyticsOnClickCreateNoteCartItem();

    void sendAnalyticsOnDataCartIsEmpty();

    void sendAnalyticsOnClickShoppingNowCartEmptyState();

    void sendAnalyticsOnClickAddFromWishListCartEmptyState();

    void sendAnalyticsScreenName(String screenName);

    void sendAnalyticsOnButtonCheckoutClickedFailed();

    void sendAnalyticsOnButtonSelectAllChecked();

    void sendAnalyticsOnButtonSelectAllUnchecked();

    void sendAnalyticsOnViewPromoAutoApply();

    void sendAnalyticsOnViewPromoManualApply(String type);

}
