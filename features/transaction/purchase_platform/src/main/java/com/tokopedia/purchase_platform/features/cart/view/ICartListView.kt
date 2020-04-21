package com.tokopedia.purchase_platform.features.cart.view

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartDigitalProduct
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartResponse
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.RecentView
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.view.uimodel.CartShopHolderData
import com.tokopedia.purchase_platform.features.promo.data.request.validate_use.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.PromoUiModel
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.ValidateUsePromoRevampUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist
import java.util.*

/**
 * @author anggaprasetiyo on 18/01/18.
 */

interface ICartListView : CustomerView {

    fun getAllShopDataList(): List<CartShopHolderData>

    fun getAllCartDataList(): List<CartItemData>

    fun getAllAvailableCartDataList(): List<CartItemData>

    fun getAllSelectedCartDataList(): List<CartItemData>?

    fun getCartId(): String

    fun getInsuranceCartShopData(): ArrayList<InsuranceCartDigitalProduct>?

    fun showProgressLoading()

    fun hideProgressLoading()

    fun renderInitialGetCartListDataSuccess(cartListData: CartListData?)

    fun renderErrorInitialGetCartListData(throwable: Throwable)

    fun renderToShipmentFormSuccess(eeCheckoutData: Map<String, Any>,
                                    cartItemDataList: List<CartItemData>,
                                    checkoutProductEligibleForCashOnDelivery: Boolean,
                                    condition: Int)

    fun renderErrorToShipmentForm(message: String)

    fun renderErrorToShipmentForm(throwable: Throwable)

    fun renderDetailInfoSubTotal(qty: String, subtotalPrice: String, selectAllItem: Boolean, unselectAllItem: Boolean, noAvailableItems: Boolean)

    fun updateCashback(cashback: Double)

    fun showToastMessageRed(message: String)

    fun showToastMessageRed(throwable: Throwable)

    fun showToastMessageGreen(message: String)

    fun renderLoadGetCartData()

    fun renderLoadGetCartDataFinish()

    fun onDeleteCartDataSuccess(deletedCartIds: List<String>)

    fun stopCartPerformanceTrace()

    fun stopAllCartPerformanceTrace()

    fun renderRecentView(recentViewList: List<RecentView>?)

    fun renderWishlist(wishlists: List<Wishlist>?)

    fun renderRecommendation(recommendationWidget: RecommendationWidget?)

    fun showItemLoading()

    fun hideItemLoading()

    fun notifyBottomCartParent()

    fun setHasTriedToLoadWishList()

    fun setHasTriedToLoadRecentView()

    fun setHasTriedToLoadRecommendation()

    fun triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataResponseModel: AddToCartDataModel, productModel: Any)

    fun renderInsuranceCartData(insuranceCartResponse: InsuranceCartResponse?, isRecommendation: Boolean)

    fun removeInsuranceProductItem(productId: List<Long>)

    fun showMessageRemoveInsuranceProductSuccess()

    fun showMessageUpdateInsuranceProductSuccess()

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

    fun sendATCTrackingURL(clickUrl: String)
}
