package com.tokopedia.cart.view

import android.os.Bundle
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cart.data.model.response.promo.CartPromoTicker
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.domain.model.cartlist.SummaryTransactionUiModel
import com.tokopedia.cart.domain.model.updatecart.UpdateAndGetLastApplyData
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.cart.view.uimodel.PromoSummaryData
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener

interface ICartListPresenter {

    fun attachView(view: ICartListView)

    fun detachView()

    fun getCartListData(): CartData?

    fun setCartListData(cartListData: CartData)

    fun getSummaryTransactionUiModel(selectedCartItemData: List<CartItemHolderData>): SummaryTransactionUiModel?

    fun getPromoSummaryUiModel(): PromoSummaryData?

    fun updatePromoSummaryData(lastApplyUiModel: LastApplyUiModel)

    fun processInitialGetCartData(cartId: String, initialLoad: Boolean, isLoadingTypeRefresh: Boolean, getCartState: Int = GET_CART_STATE_DEFAULT)

    fun processDeleteCartItem(
        allCartItemData: List<CartItemHolderData>,
        removedCartItems: List<CartItemHolderData>,
        addWishList: Boolean,
        forceExpandCollapsedUnavailableItems: Boolean = false,
        isFromGlobalCheckbox: Boolean = false,
        isFromEditBundle: Boolean = false
    )

    fun processUndoDeleteCartItem(cartIds: List<String>)

    fun processUpdateCartData(fireAndForget: Boolean, onlyTokoNowProducts: Boolean = false)

    fun processToUpdateAndReloadCartData(cartId: String, getCartState: Int = GET_CART_STATE_DEFAULT)

    fun processUpdateCartCounter()

    fun reCalculateSubTotal(dataList: List<CartGroupHolderData>)

    fun generateDeleteCartDataAnalytics(cartItemDataList: List<CartItemHolderData>): Map<String, Any>

    fun generateRecommendationImpressionDataAnalytics(position: Int, cartRecommendationItemHolderDataList: List<CartRecommendationItemHolderData>, isEmptyCart: Boolean): Map<String, Any>

    fun generateRecommendationDataOnClickAnalytics(recommendationItem: RecommendationItem, isEmptyCart: Boolean, position: Int): Map<String, Any>

    fun generateRecentViewProductClickDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData, position: Int): Map<String, Any>

    fun generateRecentViewProductClickEmptyCartDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData, position: Int): Map<String, Any>

    fun generateWishlistProductClickDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData, position: Int): Map<String, Any>

    fun generateWishlistProductClickEmptyCartDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData, position: Int): Map<String, Any>

    fun generateWishlistDataImpressionAnalytics(cartWishlistItemHolderDataList: List<CartWishlistItemHolderData>, isEmptyCart: Boolean): Map<String, Any>

    fun generateRecentViewDataImpressionAnalytics(cartRecentViewItemHolderDataList: List<CartRecentViewItemHolderData>, isEmptyCart: Boolean): Map<String, Any>

    fun generateCartBundlingPromotionsAnalyticsData(bundleDetail: BundleDetailUiModel): List<Bundle>

    fun processAddToWishlistV2(productId: String, userId: String, wishListActionListener: WishlistV2ActionListener)

    fun processRemoveFromWishlistV2(productId: String, userId: String, wishListActionListener: WishlistV2ActionListener)

    fun processAddCartToWishlist(productId: String, cartId: String, isLastItem: Boolean, source: String, forceExpandCollapsedUnavailableItems: Boolean = false)

    fun setHasPerformChecklistChange(hasChangeState: Boolean)

    fun getHasPerformChecklistChange(): Boolean

    fun dataHasChanged(): Boolean

    fun processGetRecentViewData(allProductIds: List<String>)

    fun processGetWishlistV2Data()

    fun processGetRecommendationData(page: Int, allProductIds: List<String>)

    fun processAddToCart(productModel: Any)

    fun processAddToCartExternal(productId: Long)

    fun generateAddToCartEnhanceEcommerceDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData, addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean): Map<String, Any>

    fun generateAddToCartEnhanceEcommerceDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData, addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean): Map<String, Any>

    fun generateAddToCartEnhanceEcommerceDataLayer(cartRecommendationItemHolderData: CartRecommendationItemHolderData, addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean): Map<String, Any>

    fun generateCheckoutDataAnalytics(cartItemDataList: List<CartItemHolderData>, step: String): Map<String, Any>

    fun redirectToLite(url: String)

    fun doUpdateCartForPromo()

    fun doUpdateCartAndGetLastApply(promoRequest: ValidateUsePromoRequest)

    fun doClearRedPromosBeforeGoToCheckout(clearPromoRequest: ClearPromoRequest)

    fun doClearRedPromosBeforeGoToPromo(clearPromoRequest: ClearPromoRequest)

    fun setLastValidateUseRequest(validateUsePromoRequest: ValidateUsePromoRequest)

    fun getLastValidateUseRequest(): ValidateUsePromoRequest?

    fun getValidateUseLastResponse(): ValidateUsePromoRevampUiModel?

    fun setValidateUseLastResponse(response: ValidateUsePromoRevampUiModel?)

    fun getUpdateCartAndGetLastApplyLastResponse(): UpdateAndGetLastApplyData?

    fun setUpdateCartAndGetLastApplyLastResponse(response: UpdateAndGetLastApplyData?)

    fun isLastApplyValid(): Boolean

    fun setLastApplyNotValid()

    fun setLastApplyValid()

    fun saveCheckboxState(cartItemDataList: List<CartItemHolderData>)

    fun followShop(shopId: String)

    fun doClearAllPromo()

    fun setLocalizingAddressData(lca: LocalCacheModel?)

    fun checkCartShopGroupTicker(cartGroupHolderData: CartGroupHolderData)

    fun getPromoFlag(): Boolean

    fun getTickerPromoData(): CartPromoTicker

    fun getShowChoosePromoWidget(): Boolean

    fun clearAllBo(clearPromoOrderData: ClearPromoOrderData)

    fun validateBoPromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel)

    fun checkEnableBundleCrossSell(cartGroupHolderData: CartGroupHolderData): Boolean

    companion object {
        const val GET_CART_STATE_DEFAULT = 0
        const val GET_CART_STATE_AFTER_CHOOSE_ADDRESS = 1
    }
}
