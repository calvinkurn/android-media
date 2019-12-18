package com.tokopedia.purchase_platform.features.cart.view

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceProductApplicationDetails
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartDigitalProduct
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartShops
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecentViewItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecommendationItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartShopHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartWishlistItemHolderData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.wishlist.common.listener.WishListActionListener
import java.util.*

/**
 * @author anggaprasetiyo on 18/01/18.
 */

interface ICartListPresenter {

    fun getCartListData(): CartListData?

    fun setCartListData(cartListData: CartListData)

    fun attachView(view: ICartListView)

    fun detachView()

    fun processInitialGetCartData(cartId: String, initialLoad: Boolean, isLoadingTypeRefresh: Boolean)

    fun processDeleteCartItem(allCartItemData: List<CartItemData>, removedCartItems: List<CartItemData>, appliedPromoCodeList: ArrayList<String>?, addWishList: Boolean, removeInsurance: Boolean)

    fun processToUpdateCartData(cartItemDataList: List<CartItemData>)

    fun processUpdateCartDataPromoMerchant(cartItemDataList: List<CartItemData>, shopGroupAvailableData: ShopGroupAvailableData)

    fun processUpdateCartDataPromoStacking(cartItemDataList: List<CartItemData>, promoStackingData: PromoStackingData, goToDetail: Int)

    fun processToUpdateAndReloadCartData()

    fun reCalculateSubTotal(dataList: List<CartShopHolderData>, insuranceCartShopsArrayList: ArrayList<InsuranceCartShops>)

    fun processCancelAutoApplyPromoStack(shopIndex: Int, promoCodeList: ArrayList<String>, ignoreAPIResponse: Boolean)

    fun processCancelAutoApplyPromoStackAfterClash(promoStackingGlobalData: PromoStackingData, oldPromoList: ArrayList<String>, newPromoList: ArrayList<ClashingVoucherOrderUiModel>, type: String)

    fun processApplyPromoStackAfterClash(promoStackingGlobalData: PromoStackingData, newPromoList: ArrayList<ClashingVoucherOrderUiModel>, type: String)

    fun generateCartDataAnalytics(cartItemDataList: List<CartItemData>, enhancedECommerceAction: String): Map<String, Any>

    fun generateRecommendationDataAnalytics(cartRecommendationItemHolderDataList: List<CartRecommendationItemHolderData>, isEmptyCart: Boolean): Map<String, Any>

    fun generateRecommendationDataOnClickAnalytics(recommendationItem: RecommendationItem, isEmptyCart: Boolean, position: Int): Map<String, Any>

    fun generateRecentViewProductClickDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData, position: Int): Map<String, Any>

    fun generateRecentViewProductClickEmptyCartDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData, position: Int): Map<String, Any>

    fun generateWishlistProductClickDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData, position: Int): Map<String, Any>

    fun generateWishlistProductClickEmptyCartDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData, position: Int): Map<String, Any>

    fun generateWishlistDataImpressionAnalytics(cartWishlistItemHolderDataList: List<CartWishlistItemHolderData>, isEmptyCart: Boolean): Map<String, Any>

    fun generateRecentViewDataImpressionAnalytics(cartRecentViewItemHolderDataList: List<CartRecentViewItemHolderData>, isEmptyCart: Boolean): Map<String, Any>

    fun processAddToWishlist(productId: String, userId: String, wishListActionListener: WishListActionListener)

    fun processRemoveFromWishlist(productId: String, userId: String, wishListActionListener: WishListActionListener)

    fun setHasPerformChecklistChange()

    fun getHasPerformChecklistChange(): Boolean

    fun dataHasChanged(): Boolean

    fun processGetRecentViewData()

    fun processGetWishlistData()

    fun processGetRecommendationData(page: Int, allProductIds: List<String>)

    fun processAddToCart(productModel: Any)

    fun generateAddToCartEnhanceEcommerceDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData, addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean): Map<String, Any>

    fun generateAddToCartEnhanceEcommerceDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData, addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean): Map<String, Any>

    fun generateAddToCartEnhanceEcommerceDataLayer(cartRecommendationItemHolderData: CartRecommendationItemHolderData, addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean): Map<String, Any>

    fun getInsuranceTechCart()

    fun processDeleteCartInsurance(insuranceCartShopsArrayList: ArrayList<InsuranceCartDigitalProduct>, showToaster: Boolean)

    fun updateInsuranceProductData(insuranceCartShops: InsuranceCartShops, updateInsuranceProductApplicationDetailsArrayList: ArrayList<UpdateInsuranceProductApplicationDetails>)

    fun setAllInsuranceProductsChecked(insuranceCartShopsArrayList: ArrayList<InsuranceCartShops>, isChecked: Boolean)

    fun generateCheckoutDataAnalytics(cartItemDataList: List<CartItemData>, step: String): Map<String, Any>

    fun redirectToLite(url: String)

    fun generateCheckPromoFirstStepParam(promoStackingGlobalData: PromoStackingData): Promo
}
