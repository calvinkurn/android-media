package com.tokopedia.purchase_platform.features.cart.view

/**
 * @author anggaprasetiyo on 28/08/18.
 */

interface ICartListAnalyticsListener {

    fun sendAnalyticsOnClickBackArrow()

    fun sendAnalyticsOnClickRemoveButtonHeader()

    fun sendAnalyticsOnClickConfirmationRemoveCartSelectedWithAddToWishList(eeDataLayerCart: Map<String, Any>)

    fun sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(eeDataLayerCart: Map<String, Any>)

    fun sendAnalyticsOnClickRemoveIconCartItem()

    fun sendAnalyticsOnClickButtonPlusCartItem()

    fun sendAnalyticsOnClickButtonMinusCartItem()

    fun sendAnalyticsOnClickProductNameCartItem(productName: String)

    fun sendAnalyticsOnClickShopCartItem(shopId: String, shopName: String)

    fun sendAnalyticsOnClickCancelPromoCodeAndCouponBanner()

    fun sendAnalyticsOnClickRemoveCartConstrainedProduct(eeDataLayerCart: Map<String, Any>)

    fun sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductWithAddToWishList(eeDataLayerCart: Map<String, Any>)

    fun sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductNoAddToWishList(eeDataLayerCart: Map<String, Any>)

    fun sendAnalyticsOnClickQuantityCartItemInput(quantity: String)

    fun sendAnalyticsOnClickCreateNoteCartItem()

    fun sendAnalyticsOnDataCartIsEmpty()

    fun sendAnalyticsScreenName(screenName: String)

    fun sendAnalyticsOnButtonCheckoutClickedFailed()

    fun sendAnalyticsOnButtonSelectAllChecked()

    fun sendAnalyticsOnButtonSelectAllUnchecked()

    fun sendAnalyticsOnViewPromoAutoApply()

    fun sendAnalyticsOnViewPromoManualApply(type: String)

    fun sendAnalyticsOnViewProductRecommendation(eeDataLayerCart: Map<String, Any>)

    fun sendAnalyticsOnClickProductRecommendation(position: String, eeDataLayerCart: Map<String, Any>)

    fun sendAnalyticsOnViewProductWishlist(eeDataLayerCart: Map<String, Any>)

    fun sendAnalyticsOnClickProductWishlistOnEmptyCart(position: String, eeDataLayerCart: Map<String, Any>)

    fun sendAnalyticsOnClickProductWishlistOnCartList(position: String, eeDataLayerCart: Map<String, Any>)

    fun sendAnalyticsOnViewProductRecentView(eeDataLayerCart: Map<String, Any>)

    fun sendAnalyticsOnClickProductRecentViewOnEmptyCart(position: String, eeDataLayerCart: Map<String, Any>)

    fun sendAnalyticsOnClickProductRecentViewOnCartList(position: String, eeDataLayerCart: Map<String, Any>)

    fun sendAnalyticsOnGoToShipmentFailed(errorMessage: String)

}
