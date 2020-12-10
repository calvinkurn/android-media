package com.tokopedia.cart.view

import android.widget.ImageView
import com.tokopedia.cart.domain.model.cartlist.ActionData
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cart.view.uimodel.DisabledCartItemHolderData
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

    fun onAddDisabledItemToWishlist(data: DisabledCartItemHolderData, imageView: ImageView)

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

    fun onShowActionSeeOtherProduct(productId: String, errorType: String)

    fun onSimilarProductUrlClicked(similarProductUrl: String)

    fun onFollowShopClicked(shopId: String, errorType: String)

    fun onDeleteAllDisabledProduct();

    fun onDeleteDisabledItem(data: DisabledCartItemHolderData)

    fun onSeeErrorProductsClicked()

    fun onTobaccoLiteUrlClicked(url: String, data: DisabledCartItemHolderData, actionData: ActionData)

    fun onShowTickerTobacco()

    fun onAccordionClicked(data: DisabledAccordionHolderData, buttonWording: String)

    fun onDisabledCartItemProductClicked(cartItemData: CartItemData)

    fun onGlobalCheckboxCheckedChange(isChecked: Boolean, isCheckUncheckDirectAction: Boolean)

    fun onGlobalDeleteClicked()
}