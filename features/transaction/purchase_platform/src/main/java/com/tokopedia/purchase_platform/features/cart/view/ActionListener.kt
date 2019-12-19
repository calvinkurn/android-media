package com.tokopedia.purchase_platform.features.cart.view

import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemTickerErrorHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartShopHolderData

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

interface ActionListener {

    fun onClickShopNow()

    fun getDefaultCartErrorMessage(): String

    fun onCartShopNameClicked(cartShopHolderData: CartShopHolderData)

    fun onShopItemCheckChanged(itemPosition: Int, checked: Boolean)

    fun onVoucherMerchantPromoClicked(`object`: Any)

    fun onCancelVoucherMerchantClicked(promoMerchantCode: String, position: Int, ignoreAPIResponse: Boolean)

    fun onCartDataEnableToCheckout()

    fun onCartDataDisableToCheckout(message: String)

    fun onCartItemTickerErrorActionClicked(data: CartItemTickerErrorHolderData, position: Int)

    fun onShowAllItem(appLink: String)

    fun onAddDisabledItemToWishlist(productId: String)

    fun onAddLastSeenToWishlist(productId: String)

    fun onAddWishlistToWishlist(productId: String)

    fun onAddRecommendationToWishlist(productId: String)

    fun onRemoveDisabledItemFromWishlist(productId: String)

    fun onRemoveLastSeenFromWishlist(productId: String)

    fun onRemoveWishlistFromWishlist(productId: String)

    fun onRemoveRecommendationFromWishlist(productId: String)

    fun onWishlistProductClicked(productId: String)

    fun onRecentViewProductClicked(productId: String)

    fun onRecommendationProductClicked(productId: String)

    fun onButtonAddToCartClicked(productModel: Any)

    fun onShowTickerOutOfStock(productId: String)

    fun onSimilarProductUrlClicked(similarProductUrl: String)

    fun onSelectAllClicked()

    fun onDeleteAllDisabledProduct();

    fun onDeleteDisabledItem(data: CartItemData)

    fun onSeeErrorProductsClicked()

    fun onTobaccoLiteUrlClicked(url: String)

    fun onShowTickerTobacco()
}