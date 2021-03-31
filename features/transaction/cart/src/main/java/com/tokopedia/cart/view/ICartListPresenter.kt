package com.tokopedia.cart.view

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.domain.model.updatecart.UpdateAndValidateUseData
import com.tokopedia.cart.view.uimodel.*
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
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

    fun processDeleteCartItem(allCartItemData: List<CartItemData>,
                              removedCartItems: List<CartItemData>,
                              addWishList: Boolean,
                              forceExpandCollapsedUnavailableItems: Boolean = false,
                              isFromGlobalCheckbox: Boolean = false)

    fun processUndoDeleteCartItem(cartIds: List<String>);

    fun processUpdateCartData(fireAndForget: Boolean)

    fun processToUpdateAndReloadCartData(cartId: String)

    fun processUpdateCartCounter()

    fun reCalculateSubTotal(dataList: List<CartShopHolderData>)

    fun generateDeleteCartDataAnalytics(cartItemDataList: List<CartItemData>): Map<String, Any>

    fun generateRecommendationImpressionDataAnalytics(position: Int, cartRecommendationItemHolderDataList: List<CartRecommendationItemHolderData>, isEmptyCart: Boolean): Map<String, Any>

    fun generateRecommendationDataOnClickAnalytics(recommendationItem: RecommendationItem, isEmptyCart: Boolean, position: Int): Map<String, Any>

    fun generateRecentViewProductClickDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData, position: Int): Map<String, Any>

    fun generateRecentViewProductClickEmptyCartDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData, position: Int): Map<String, Any>

    fun generateWishlistProductClickDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData, position: Int): Map<String, Any>

    fun generateWishlistProductClickEmptyCartDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData, position: Int): Map<String, Any>

    fun generateWishlistDataImpressionAnalytics(cartWishlistItemHolderDataList: List<CartWishlistItemHolderData>, isEmptyCart: Boolean): Map<String, Any>

    fun generateRecentViewDataImpressionAnalytics(cartRecentViewItemHolderDataList: List<CartRecentViewItemHolderData>, isEmptyCart: Boolean): Map<String, Any>

    fun processAddToWishlist(productId: String, userId: String, wishListActionListener: WishListActionListener)

    fun processRemoveFromWishlist(productId: String, userId: String, wishListActionListener: WishListActionListener)

    fun processAddCartToWishlist(productId: String, cartId: String, isLastItem: Boolean, source: String, forceExpandCollapsedUnavailableItems: Boolean = false)

    fun setHasPerformChecklistChange(hasChangeState: Boolean)

    fun getHasPerformChecklistChange(): Boolean

    fun dataHasChanged(): Boolean

    fun processGetRecentViewData(allProductIds: List<String>)

    fun processGetWishlistData()

    fun processGetRecommendationData(page: Int, allProductIds: List<String>)

    fun processAddToCart(productModel: Any)

    fun processAddToCartExternal(productId: Long)

    fun generateAddToCartEnhanceEcommerceDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData, addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean): Map<String, Any>

    fun generateAddToCartEnhanceEcommerceDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData, addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean): Map<String, Any>

    fun generateAddToCartEnhanceEcommerceDataLayer(cartRecommendationItemHolderData: CartRecommendationItemHolderData, addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean): Map<String, Any>

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

    fun saveCheckboxState(cartItemDataList: List<CartItemHolderData>)

    fun followShop(shopId: String)

    fun doClearAllPromo()
}
