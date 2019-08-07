package com.tokopedia.purchase_platform.cart.view

import com.tokopedia.purchase_platform.cart.view.viewmodel.CartItemTickerErrorHolderData
import com.tokopedia.purchase_platform.cart.view.viewmodel.CartShopHolderData

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

    fun onAddToWishlist(productId: String)

    fun onRemoveFromWishlist(productId: String)

    fun onProductClicked(productId: String)

    fun onButtonAddToCartClicked(productModel: Any)
}