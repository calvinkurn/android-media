package com.tokopedia.purchase_platform.features.cart.view

import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartDigitalProduct
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartResponse
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.RecentView
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartShopHolderData
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

    fun getActivityObject(): FragmentActivity?

    fun getCartId(): String

    fun getInsuranceCartShopData(): ArrayList<InsuranceCartDigitalProduct>?

    fun showProgressLoading()

    fun hideProgressLoading()

    fun renderInitialGetCartListDataSuccess(cartListData: CartListData?)

    fun renderErrorInitialGetCartListData(message: String)

    fun renderToShipmentFormSuccess(eeCheckoutData: Map<String, Any>,
                                    cartItemDataList: List<CartItemData>,
                                    checkoutProductEligibleForCashOnDelivery: Boolean,
                                    condition: Int)

    fun renderErrorToShipmentForm(message: String)

    fun renderDetailInfoSubTotal(qty: String, subtotalPrice: String, selectAllItem: Boolean, unselectAllItem: Boolean, hasAvailableItems: Boolean)

    fun updateCashback(cashback: Double)

    fun showToastMessageRed(message: String)

    fun showToastMessageGreen(message: String)

    fun renderLoadGetCartData()

    fun renderLoadGetCartDataFinish()

    fun onDeleteCartDataSuccess(deletedCartIds: List<Int>)

    fun goToCouponList()

    fun goToDetailPromoStacking(promoStackingData: PromoStackingData)

    fun stopCartPerformanceTrace()

    fun stopAllCartPerformanceTrace()

    fun onSuccessClearPromoStack(shopIndex: Int)

    fun onSuccessCheckPromoFirstStep(responseGetPromoStackUiModel: ResponseGetPromoStackUiModel)

    fun onFailedClearPromoStack(ignoreAPIResponse: Boolean)

    fun showMerchantVoucherListBottomsheet(shopGroupAvailableData: ShopGroupAvailableData)

    fun onClashCheckPromo(clashingInfoDetailUiModel: ClashingInfoDetailUiModel, type: String)

    fun onSuccessClearPromoStackAfterClash()

    fun renderRecentView(recentViewList: List<RecentView>?)

    fun renderWishlist(wishlist: List<Wishlist>?)

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

    fun goToLite(url: String)
}
