package com.tokopedia.purchase_platform.features.cart.view;

import java.util.Map;

/**
 * @author anggaprasetiyo on 28/08/18.
 */
public interface ICartListAnalyticsListener {

    void sendAnalyticsOnClickBackArrow();

    void sendAnalyticsOnClickRemoveButtonHeader();

    void sendAnalyticsOnClickConfirmationRemoveCartSelectedWithAddToWishList(Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnClickRemoveIconCartItem();

    void sendAnalyticsOnClickButtonPlusCartItem();

    void sendAnalyticsOnClickButtonMinusCartItem();

    void sendAnalyticsOnClickProductNameCartItem(String productName);

    void sendAnalyticsOnClickShopNameCartItem(String shopName);

    void sendAnalyticsOnClickShopCartItem(String shopId, String shopName);

    void sendAnalyticsOnClickCancelPromoCodeAndCouponBanner();

    void sendAnalyticsOnClickRemoveCartConstrainedProduct(Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductWithAddToWishList(Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductNoAddToWishList(Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnClickQuantityCartItemInput(String quantity);

    void sendAnalyticsOnClickCreateNoteCartItem();

    void sendAnalyticsOnDataCartIsEmpty();

    void sendAnalyticsScreenName(String screenName);

    void sendAnalyticsOnButtonCheckoutClickedFailed();

    void sendAnalyticsOnButtonSelectAllChecked();

    void sendAnalyticsOnButtonSelectAllUnchecked();

    void sendAnalyticsOnViewPromoAutoApply();

    void sendAnalyticsOnViewPromoManualApply(String type);

    void sendAnalyticsOnViewProductRecommendation(Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnClickProductRecommendation(String position, Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnViewProductWishlist(Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnClickProductWishlistOnEmptyCart(String position, Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnClickProductWishlistOnCartList(String position, Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnViewProductRecentView(Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnClickProductRecentViewOnEmptyCart(String position, Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnClickProductRecentViewOnCartList(String position, Map<String, Object> eeDataLayerCart);

    void sendAnalyticsOnGoToShipmentFailed(String errorMessage);

}
