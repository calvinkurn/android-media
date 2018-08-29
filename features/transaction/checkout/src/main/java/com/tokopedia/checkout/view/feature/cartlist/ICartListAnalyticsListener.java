package com.tokopedia.checkout.view.feature.cartlist;

import java.util.Map;

/**
 * @author anggaprasetiyo on 28/08/18.
 */
public interface ICartListAnalyticsListener {

    //5 condition EE Checkout Step 1 as eventLabel
    void sendAnalyticsOnSuccessToCheckout(Map<String, Object> eeData, String eventLabel);

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

}
