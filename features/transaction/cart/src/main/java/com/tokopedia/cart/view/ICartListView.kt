package com.tokopedia.cart.view

import android.view.View
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductUiModel
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.GetWishlistV2Response

interface ICartListView : CustomerView {

    fun refreshCartWithSwipeToRefresh()

    fun getAllShopDataList(): List<CartShopHolderData>

    fun getAllCartDataList(): List<CartItemHolderData>

    fun getAllAvailableCartDataList(): List<CartItemHolderData>

    fun getAllSelectedCartDataList(): List<CartItemHolderData>

    fun getCartId(): String

    fun showProgressLoading()

    fun hideProgressLoading()

    fun renderInitialGetCartListDataSuccess(cartData: CartData)

    fun renderErrorInitialGetCartListData(throwable: Throwable)

    fun renderToShipmentFormSuccess(
        eeCheckoutData: Map<String, Any>,
        cartItemDataList: List<CartItemHolderData>,
        checkoutProductEligibleForCashOnDelivery: Boolean,
        condition: Int
    )

    fun renderErrorToShipmentForm(message: String, ctaText: String = "")

    fun renderErrorToShipmentForm(throwable: Throwable)

    fun renderErrorToShipmentForm(outOfService: OutOfService)

    fun renderDetailInfoSubTotal(qty: String, subtotalPrice: Double, noAvailableItems: Boolean)

    fun updateCashback(cashback: Double)

    fun showToastMessageRed(
        message: String, actionText: String = "", ctaClickListener: View.OnClickListener? = null
    )

    fun showToastMessageRed(throwable: Throwable)

    fun showToastMessageRed()

    fun showToastMessageGreen(
        message: String, actionText: String = "", onClickListener: View.OnClickListener? = null
    )

    fun renderLoadGetCartData()

    fun renderLoadGetCartDataFinish()

    fun onDeleteCartDataSuccess(
        deletedCartIds: List<String>,
        removeAllItems: Boolean,
        forceExpandCollapsedUnavailableItems: Boolean,
        isMoveToWishlist: Boolean,
        isFromGlobalCheckbox: Boolean,
        isFromEditBundle: Boolean
    )

    fun onUndoDeleteCartDataSuccess()

    fun onAddCartToWishlistSuccess(
        message: String,
        productId: String,
        cartId: String,
        isLastItem: Boolean,
        source: String,
        forceExpandCollapsedUnavailableItems: Boolean
    )

    fun onAddCartToWishlistV2Success(
        result: AddToWishlistV2Response.Data.WishlistAddV2,
        productId: String,
        cartId: String,
        isLastItem: Boolean,
        source: String,
        forceExpandCollapsedUnavailableItems: Boolean
    )

    fun stopCartPerformanceTrace(isSuccessLoadCart: Boolean)

    fun stopAllCartPerformanceTrace()

    fun renderRecentView(recommendationWidget: RecommendationWidget?)

    fun renderWishlistV2(
        wishlists: List<GetWishlistV2Response.Data.WishlistV2.Item>?, forceReload: Boolean
    )

    fun renderRecommendation(recommendationWidget: RecommendationWidget?)

    fun showItemLoading()

    fun hideItemLoading()

    fun notifyBottomCartParent()

    fun setHasTriedToLoadWishList()

    fun setHasTriedToLoadRecentView()

    fun setHasTriedToLoadRecommendation()

    fun triggerSendEnhancedEcommerceAddToCartSuccess(
        addToCartDataResponseModel: AddToCartDataModel, productModel: Any
    )

    fun getAdsId(): String?

    fun goToLite(url: String)

    fun updateCartCounter(counter: Int)

    fun updatePromoCheckoutStickyButton(promoUiModel: PromoUiModel)

    fun renderPromoCheckoutButtonActiveDefault(listPromoApplied: List<String>)

    fun showPromoCheckoutStickyButtonInactive()

    fun showPromoCheckoutStickyButtonLoading()

    fun onSuccessClearRedPromosThenGoToCheckout()

    fun onSuccessClearRedPromosThenGoToPromo()

    fun navigateToPromoRecommendation()

    fun checkHitValidateUseIsNeeded(params: ValidateUsePromoRequest): Boolean

    fun generateGeneralParamValidateUse(): ValidateUsePromoRequest

    fun resetRecentViewList()

    fun sendATCTrackingURL(recommendationItem: RecommendationItem)

    fun sendATCTrackingURL(bannerShopProductUiModel: BannerShopProductUiModel)

    fun reCollapseExpandedDeletedUnavailableItems()

    fun sendATCTrackingURLRecent(productModel: CartRecentViewItemHolderData)

    fun updateCartShopGroupTicker(cartShopHolderData: CartShopHolderData)
}
