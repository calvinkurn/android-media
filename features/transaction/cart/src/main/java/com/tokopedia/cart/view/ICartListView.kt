package com.tokopedia.cart.view

import android.view.View
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.domain.model.cartlist.OutOfServiceData
import com.tokopedia.cart.domain.model.cartlist.UndoDeleteCartData
import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist
import java.util.*

/**
 * @author anggaprasetiyo on 18/01/18.
 */

interface ICartListView : CustomerView {

    fun refreshCart()

    fun getAllShopDataList(): List<CartShopHolderData>

    fun getAllCartDataList(): List<CartItemData>

    fun getAllAvailableCartDataList(): List<CartItemData>

    fun getAllSelectedCartDataList(): List<CartItemData>?

    fun getCartId(): String

    fun showProgressLoading()

    fun hideProgressLoading()

    fun renderInitialGetCartListDataSuccess(cartListData: CartListData?)

    fun renderErrorInitialGetCartListData(throwable: Throwable)

    fun renderToShipmentFormSuccess(eeCheckoutData: Map<String, Any>,
                                    cartItemDataList: List<CartItemData>,
                                    checkoutProductEligibleForCashOnDelivery: Boolean,
                                    condition: Int)

    fun renderErrorToShipmentForm(message: String, ctaText: String = "")

    fun renderErrorToShipmentForm(throwable: Throwable)

    fun renderErrorToShipmentForm(outOfServiceData: OutOfServiceData)

    fun renderDetailInfoSubTotal(qty: String, subtotalBeforeSlashedPrice: Double, subtotalPrice: Double, selectAllItem: Boolean, unselectAllItem: Boolean, noAvailableItems: Boolean)

    fun updateCashback(cashback: Double)

    fun showToastMessageRed(message: String, actionText: String = "", ctaClickListener: View.OnClickListener? = null)

    fun showToastMessageRed(throwable: Throwable)

    fun showToastMessageGreen(message: String, actionText: String = "", onClickListener: View.OnClickListener? = null)

    fun renderLoadGetCartData()

    fun renderLoadGetCartDataFinish()

    fun onDeleteCartDataSuccess(deletedCartIds: List<String>, removeAllItems: Boolean, forceExpandCollapsedUnavailableItems: Boolean, isMoveToWishlist: Boolean, isFromGlobalCheckbox: Boolean)

    fun onUndoDeleteCartDataSuccess(undoDeleteCartData: UndoDeleteCartData)

    fun onAddCartToWishlistSuccess(message: String, productId: String, cartId: String, isLastItem: Boolean, source: String, forceExpandCollapsedUnavailableItems: Boolean)

    fun stopCartPerformanceTrace()

    fun stopAllCartPerformanceTrace()

    fun renderRecentView(recommendationWidget: RecommendationWidget?)

    fun renderWishlist(wishlists: List<Wishlist>?, forceReload: Boolean)

    fun renderRecommendation(recommendationWidget: RecommendationWidget?)

    fun showItemLoading()

    fun hideItemLoading()

    fun notifyBottomCartParent()

    fun setHasTriedToLoadWishList()

    fun setHasTriedToLoadRecentView()

    fun setHasTriedToLoadRecommendation()

    fun triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataResponseModel: AddToCartDataModel, productModel: Any)

    fun getAdsId(): String?

    fun goToLite(url: String)

    fun updateCartCounter(counter: Int)

    fun updateListRedPromos(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel)

    fun updatePromoCheckoutStickyButton(promoUiModel: PromoUiModel)

    fun renderPromoCheckoutButtonActiveDefault(listPromoApplied: List<String>)

    fun showPromoCheckoutStickyButtonInactive()

    fun showPromoCheckoutStickyButtonLoading()

    fun onSuccessClearRedPromosThenGoToCheckout()

    fun navigateToPromoRecommendation()

    fun checkHitValidateUseIsNeeded(params: ValidateUsePromoRequest): Boolean

    fun generateGeneralParamValidateUse() : ValidateUsePromoRequest

    fun resetRecentViewList()

    fun sendATCTrackingURL(recommendationItem: RecommendationItem)

    fun reCollapseExpandedDeletedUnavailableItems()

    fun sendATCTrackingURLRecent(productModel: CartRecentViewItemHolderData)


}
