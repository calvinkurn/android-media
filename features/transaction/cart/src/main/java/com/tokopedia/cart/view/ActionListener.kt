package com.tokopedia.cart.view

import androidx.fragment.app.Fragment
import com.tokopedia.cart.view.uimodel.CartBundlingBottomSheetData
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface ActionListener {

    fun getFragment(): Fragment

    fun onClickShopNow()

    fun onCartGroupNameClicked(appLink: String, shopId: String, shopName: String, isOWOC: Boolean)

    fun onCartShopNameClicked(shopId: String?, shopName: String?, isTokoNow: Boolean)

    fun onShopItemCheckChanged(index: Int, checked: Boolean)

    fun onCartShopGroupTickerClicked(cartGroupHolderData: CartGroupHolderData)

    fun onCartShopGroupTickerRefreshClicked(index: Int, cartGroupHolderData: CartGroupHolderData)

    fun onViewCartShopGroupTicker(cartGroupHolderData: CartGroupHolderData)

    fun checkCartShopGroupTicker(cartGroupHolderData: CartGroupHolderData)

    fun onShowAllItem(appLink: String)

    fun onRemoveWishlistFromWishlist(productId: String)

    fun onWishlistProductClicked(productId: String)

    fun onWishlistImpression()

    fun onRecentViewProductClicked(position: Int, recommendationItem: RecommendationItem)

    fun onRecentViewImpression(recommendationItems: List<RecommendationItem>)

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

    fun onToggleUnavailableItemAccordion()

    fun onDisabledCartItemProductClicked(cartItemHolderData: CartItemHolderData)

    fun onRecentViewProductImpression(position: Int, recommendationItem: RecommendationItem)

    fun onGlobalDeleteClicked()

    fun onClickAddOnCart(productId: String, addOnId: String)

    fun addOnImpression(productId: String)

    fun onViewFreeShippingPlusBadge()

    fun showCartBundlingBottomSheet(data: CartBundlingBottomSheetData)

    fun onAvailableCartItemImpression(availableCartItems: List<CartItemHolderData>)

    fun onChangeAddressClicked()
}
