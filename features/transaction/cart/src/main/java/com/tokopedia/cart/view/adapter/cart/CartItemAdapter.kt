package com.tokopedia.cart.view.adapter.cart

import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.cart.data.model.response.shopgroupsimplified.Action
import com.tokopedia.cart.view.uimodel.CartDeleteButtonSource
import com.tokopedia.cart.view.uimodel.CartDetailInfo
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.iconunify.IconUnify

class CartItemAdapter {
    interface ActionListener {
        fun onCartItemDeleteButtonClicked(cartItemHolderData: CartItemHolderData, deleteSource: CartDeleteButtonSource)
        fun onCartItemQuantityPlusButtonClicked()
        fun onCartItemQuantityMinusButtonClicked()
        fun onCartItemProductClicked(cartItemHolderData: CartItemHolderData)
        fun onCartItemQuantityInputFormClicked(qty: String)
        fun onCartItemCheckChanged(position: Int, cartItemHolderData: CartItemHolderData)
        fun onBundleItemCheckChanged(cartItemHolderData: CartItemHolderData)
        fun onWishlistCheckChanged(cartItemHolderData: CartItemHolderData, wishlistIcon: IconUnify, animatedWishlistImage: ImageView, position: Int)
        fun onNoteClicked(cartItemHolderData: CartItemHolderData, noteIcon: ImageView, noteLottieIcon: LottieAnimationView, position: Int)
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
        fun onProductAddOnClicked(cartItemData: CartItemHolderData)
        fun onAddOnsProductWidgetImpression(addOnType: Int, productId: String)
        fun onClickAddOnsProductWidgetCart(addOnType: Int, productId: String)
        fun sendRemoveCartFromSubtractButtonAnalytic(cartItemHolderData: CartItemHolderData)
        fun onAvailableCartItemImpression(availableCartItems: List<CartItemHolderData>)
        fun onBmGmChevronRightClicked(offerLandingPageLink: String, offerId: Long, widgetCaption: String, shopId: String)
        fun onBmGmTickerReloadClicked()
        fun onCartItemCheckboxClickChanged(position: Int, cartItemHolderData: CartItemHolderData, isChecked: Boolean)
        fun onCartViewBmGmTicker(offerId: Long, widgetCaption: String, shopId: String)
        fun onSwipeToDeleteClosed(productId: String)
        fun clearAllFocus()
        fun onViewPurchaseBenefit(
            item: CartItemHolderData,
            cartDetailInfo: CartDetailInfo,
            tierProductData: CartDetailInfo.BmGmTierProductData
        )
        fun onClickPurchaseBenefitActionListener(
            item: CartItemHolderData,
            cartDetailInfo: CartDetailInfo,
            tierProductData: CartDetailInfo.BmGmTierProductData
        )
        fun onClickChangeVariant(
            productId: String,
            shopId: String,
            cartId: String,
            currentQuantity: Int
        )
        fun onViewChangeVariant(cartId: String)
    }
}
