package com.tokopedia.cart.view

import androidx.fragment.app.Fragment
import com.tokopedia.cart.view.uimodel.CartBundlingBottomSheetData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.cart.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

interface ActionListener {

    fun getFragment(): Fragment

    fun onClickShopNow()

    fun getDefaultCartErrorMessage(): String

    fun onCartShopNameClicked(shopId: String?, shopName: String?, isTokoNow: Boolean)

    fun onShopItemCheckChanged(index: Int, checked: Boolean)

    fun onCartShopGroupTickerClicked(cartShopHolderData: CartShopHolderData)

    fun onCartShopGroupTickerRefreshClicked(index: Int, cartShopHolderData: CartShopHolderData)

    fun onViewCartShopGroupTicker(cartShopHolderData: CartShopHolderData)

    fun checkCartShopGroupTicker(cartShopHolderData: CartShopHolderData)

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

    fun onDeleteAllDisabledProduct();

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

    fun addOnImpression(productId: String)

    fun onViewFreeShippingPlusBadge()

    fun showCartBundlingBottomSheet(data: CartBundlingBottomSheetData, bundleIds: List<String>)
}
