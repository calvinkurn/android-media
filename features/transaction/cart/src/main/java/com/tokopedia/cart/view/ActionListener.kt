package com.tokopedia.cart.view

import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

interface ActionListener {

    fun onClickShopNow()

    fun getDefaultCartErrorMessage(): String

    fun onCartShopNameClicked(shopId: String?, shopName: String?)

    fun onShopItemCheckChanged(itemPosition: Int, checked: Boolean)

    fun onCartDataEnableToCheckout()

    fun onCartDataDisableToCheckout()

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

    fun onWishlistImpression()

    fun onRecentViewProductClicked(productId: String)

    fun onRecentViewImpression()

    fun onRecommendationProductClicked(recommendationItem: RecommendationItem)

    fun onRecommendationProductImpression(recommendationItem: RecommendationItem)

    fun onRecommendationImpression(recommendationItem: CartRecommendationItemHolderData)

    fun onButtonAddToCartClicked(productModel: Any)

    fun onShowTickerOutOfStock(productId: String)

    fun onSimilarProductUrlClicked(similarProductUrl: String)

    fun onSelectAllClicked()

    fun onDeleteAllDisabledProduct();

    fun onDeleteDisabledItem(data: CartItemData)

    fun onSeeErrorProductsClicked()

    fun onTobaccoLiteUrlClicked(url: String)

    fun onShowTickerTobacco()

    fun onCartShopNameChecked(isAllChecked: Boolean)

    fun onAccordionClicked(data: DisabledAccordionHolderData)
}