package com.tokopedia.cartrevamp.view.adapter.cart

import android.widget.ImageView
import com.tokopedia.cart.data.model.response.shopgroupsimplified.Action
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData

class CartItemAdapter {
    interface ActionListener {
        fun onCartItemDeleteButtonClicked(cartItemHolderData: CartItemHolderData)
        fun onCartItemQuantityPlusButtonClicked()
        fun onCartItemQuantityMinusButtonClicked()
        fun onCartItemProductClicked(cartItemHolderData: CartItemHolderData)
        fun onCartItemQuantityInputFormClicked(qty: String)
        fun onCartItemLabelInputRemarkClicked()
        fun onCartItemCheckChanged(position: Int, cartItemHolderData: CartItemHolderData)
        fun onBundleItemCheckChanged(cartItemHolderData: CartItemHolderData)
        fun onWishlistCheckChanged(productId: String, cartId: String, imageView: ImageView, isError: Boolean, errorType: String)
        fun onNeedToRefreshSingleShop(cartItemHolderData: CartItemHolderData, itemPosition: Int)
        fun onNeedToRefreshWeight(cartItemHolderData: CartItemHolderData)
        fun onNeedToRecalculate()
        fun onCartItemQuantityChanged(cartItemHolderData: CartItemHolderData, newQuantity: Int)
        fun onCartItemShowRemainingQty(productId: String)
        fun onCartItemShowInformationLabel(productId: String, informationLabel: String)
        fun onEditBundleClicked(cartItemHolderData: CartItemHolderData)
        fun onTobaccoLiteUrlClicked(url: String, data: CartItemHolderData, action: Action)
        fun onShowTickerTobacco()
        fun onSimilarProductUrlClicked(data: CartItemHolderData)
        fun onShowActionSeeOtherProduct(productId: String, errorType: String)
        fun onFollowShopClicked(shopId: String, errorType: String)
        fun onVerificationClicked(applink: String)
        fun onCartShopNameClicked(shopId: String?, shopName: String?, isTokoNow: Boolean)
        fun onProductAddOnClicked(addOnId: CartItemHolderData)
    }
}
