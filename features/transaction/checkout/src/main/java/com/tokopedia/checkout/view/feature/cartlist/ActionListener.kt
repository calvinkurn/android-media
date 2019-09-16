package com.tokopedia.checkout.view.feature.cartlist

import com.tokopedia.checkout.view.common.holderitemdata.CartItemTickerErrorHolderData
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData
import com.tokopedia.topads.sdk.domain.model.Product

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

interface ActionListener {

    fun onClickShopNow()

    fun getDefaultCartErrorMessage(): String

    fun onCartShopNameClicked(cartShopHolderData: CartShopHolderData)

    fun onShopItemCheckChanged(itemPosition: Int, checked: Boolean)

    fun onTopAdsItemClicked(product: Product)

    fun onVoucherMerchantPromoClicked(`object`: Any)

    fun onCancelVoucherMerchantClicked(promoMerchantCode: String, position: Int, ignoreAPIResponse: Boolean)

    fun onCartDataEnableToCheckout()

    fun onCartDataDisableToCheckout(message: String)

    fun onCartItemTickerErrorActionClicked(data: CartItemTickerErrorHolderData, position: Int)

    fun onShowAllItem(appLink: String)

    fun onAddToWishlist(productId: String)

    fun onAddRecommendationToWishlist(productId: String)

    fun onRemoveFromWishlist(productId: String)

    fun onRemoveRecommendationFromWishlist(productId: String)

    fun onWishlistProductClicked(productId: String)

    fun onRecentViewProductClicked(productId: String)

    fun onRecommendationProductClicked(productId: String)

    fun onButtonAddToCartClicked(productModel: Any)

    fun onShowTickerOutOfStock(productId: String)

    fun onSimilarProductUrlClicked(similarProductUrl: String)

    fun onSelectAllClicked()

    fun onShowCartTicker(tickerId: String)
}