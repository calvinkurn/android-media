package com.tokopedia.cartrevamp.view

import androidx.fragment.app.Fragment
import com.tokopedia.cartrevamp.view.uimodel.CartBundlingBottomSheetData
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartShopBottomHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface ActionListener {

    fun getFragment(): Fragment

    fun onClickShopNow()

    fun onCartGroupNameClicked(appLink: String)

    fun onCartShopNameClicked(shopId: String?, shopName: String?, isTokoNow: Boolean)

    fun onShopItemCheckChanged(index: Int, checked: Boolean)

    fun onCartShopGroupTickerClicked(cartGroupHolderData: CartGroupHolderData)

    fun onCartShopGroupTickerRefreshClicked(index: Int, cartShopBottomHolderData: CartShopBottomHolderData)

    fun onViewCartShopGroupTicker(cartGroupHolderData: CartGroupHolderData)

    fun checkCartShopGroupTicker(cartGroupHolderData: CartGroupHolderData)

    fun onCartDataEnableToCheckout()

    fun onCartDataDisableToCheckout()

    fun onShowAllItem(appLink: String)

    fun onAddLastSeenToWishlist(productId: String)

    fun onAddWishlistToWishlist(productId: String)

    fun onAddRecommendationToWishlist(productId: String)

    fun onRemoveDisabledItemFromWishlist(productId: String)

    fun onRemoveLastSeenFromWishlist(productId: String)

    fun onRemoveWishlistFromWishlist(productId: String)

    fun onRemoveRecommendationFromWishlist(productId: String)

    fun onWishlistProductClicked(productId: String)

    fun onWishlistImpression()

    fun onRecentViewProductClicked(productId: String)

    fun onRecentViewImpression()

    fun onRecommendationProductClicked(recommendationItem: RecommendationItem)

    fun onRecommendationProductImpression(recommendationItem: RecommendationItem)

    fun onRecommendationImpression(recommendationItem: CartRecommendationItemHolderData)

    fun onButtonAddToCartClicked(productModel: Any)

    fun onDeleteAllDisabledProduct()

    fun onSeeErrorProductsClicked()

    fun onCollapseAvailableItem(index: Int)

    fun onExpandAvailableItem(index: Int)

    fun onCollapsedProductClicked(index: Int, cartItemHolderData: CartItemHolderData)

    fun scrollToClickedExpandedProduct(index: Int, offset: Int)

    fun onToggleUnavailableItemAccordion(data: DisabledAccordionHolderData, buttonWording: String)

    fun onDisabledCartItemProductClicked(cartItemHolderData: CartItemHolderData)

    fun onRecentViewProductImpression(element: CartRecentViewItemHolderData)

    fun onGlobalCheckboxCheckedChange(isChecked: Boolean, isCheckUncheckDirectAction: Boolean)

    fun onGlobalDeleteClicked()

    fun onNeedToGoneLocalizingAddressWidget()

    fun onLocalizingAddressUpdatedFromWidget()

    fun onClickAddOnCart(productId: String, addOnId: String)

    fun onClickEpharmacyInfoCart(
        enablerLabel: String,
        shopId: String,
        productUiModelList: MutableList<CartItemHolderData>
    )

    fun addOnImpression(productId: String)

    fun onViewFreeShippingPlusBadge()

    fun showCartBundlingBottomSheet(data: CartBundlingBottomSheetData)
}
