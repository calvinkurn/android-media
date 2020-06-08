package com.tokopedia.purchase_platform.features.cart.view

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceProductApplicationDetails
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartDigitalProduct
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartShops
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndValidateUseData
import com.tokopedia.purchase_platform.features.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.uimodel.CartShopHolderData
import com.tokopedia.purchase_platform.features.cart.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.purchase_platform.features.promo.data.request.validate_use.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.ValidateUsePromoRevampUiModel
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

    fun processDeleteCartItem(allCartItemData: List<CartItemData>, removedCartItems: List<CartItemData>, addWishList: Boolean, removeInsurance: Boolean)

    fun processUpdateCartData(fireAndForget: Boolean)

    fun processToUpdateAndReloadCartData(cartId: String)

    fun processUpdateCartCounter()

    fun reCalculateSubTotal(dataList: List<CartShopHolderData>, insuranceCartShopsArrayList: ArrayList<InsuranceCartShops>)

    fun generateDeleteCartDataAnalytics(cartItemDataList: List<CartItemData>): Map<String, Any>

    fun generateRecommendationImpressionDataAnalytics(cartRecommendationItemHolderDataList: List<CartRecommendationItemHolderData>, isEmptyCart: Boolean): Map<String, Any>

    fun generateRecommendationDataOnClickAnalytics(recommendationItem: RecommendationItem, isEmptyCart: Boolean, position: Int): Map<String, Any>

    fun generateRecentViewProductClickDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData, position: Int): Map<String, Any>

    fun generateRecentViewProductClickEmptyCartDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData, position: Int): Map<String, Any>

    fun generateWishlistProductClickDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData, position: Int): Map<String, Any>

    fun generateWishlistProductClickEmptyCartDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData, position: Int): Map<String, Any>

    fun generateWishlistDataImpressionAnalytics(cartWishlistItemHolderDataList: List<CartWishlistItemHolderData>, isEmptyCart: Boolean): Map<String, Any>

    fun generateRecentViewDataImpressionAnalytics(cartRecentViewItemHolderDataList: List<CartRecentViewItemHolderData>, isEmptyCart: Boolean): Map<String, Any>

    fun processAddToWishlist(productId: String, userId: String, wishListActionListener: WishListActionListener)

    fun processRemoveFromWishlist(productId: String, userId: String, wishListActionListener: WishListActionListener)

    fun setHasPerformChecklistChange(hasChangeState: Boolean)

    fun getHasPerformChecklistChange(): Boolean

    fun dataHasChanged(): Boolean

    fun processGetRecentViewData(allProductIds: List<String>)

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

    fun doUpdateCartForPromo()

    fun doValidateUse(promoRequest: ValidateUsePromoRequest)

    fun doUpdateCartAndValidateUse(promoRequest: ValidateUsePromoRequest)

    fun doClearRedPromosBeforeGoToCheckout(promoCodeList: ArrayList<String>)

    fun getValidateUseLastResponse(): ValidateUsePromoRevampUiModel?

    fun setValidateUseLastResponse(response: ValidateUsePromoRevampUiModel?)

    fun getUpdateCartAndValidateUseLastResponse(): UpdateAndValidateUseData?

    fun setUpdateCartAndValidateUseLastResponse(response: UpdateAndValidateUseData?)

    fun isLastApplyValid(): Boolean

    fun setLastApplyNotValid()

    fun setLastApplyValid()
}
