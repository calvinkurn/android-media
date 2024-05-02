package com.tokopedia.cart.view.uimodel

import androidx.lifecycle.LiveData
import com.tokopedia.analytics.byteio.CartClickAnalyticsModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalModel
import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.cartcommon.domain.model.bmgm.response.BmGmGetGroupProductTickerResponse
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopProductUiModel
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.GetWishlistV2Response

sealed class CartState<out T : Any> {
    data class Success<out T : Any>(val data: T) : CartState<T>()
    data class Failed(val throwable: Throwable) : CartState<Nothing>()
}

sealed class CartGlobalEvent {
    data class ItemLoading(val isLoading: Boolean) : CartGlobalEvent()
    object LoadGetCartData : CartGlobalEvent()
    data class CartCounterUpdated(val counter: Int) : CartGlobalEvent()
    object SuccessClearRedPromosThenGoToPromo : CartGlobalEvent()
    object SuccessClearRedPromosThenGoToCheckout : CartGlobalEvent()
    data class UpdateAndReloadCartFailed(val throwable: Throwable) : CartGlobalEvent()

    data class AdapterItemChanged(
        val position: Int
    ) : CartGlobalEvent()

    data class CheckGroupShopCartTicker(
        val cartGroupHolderData: CartGroupHolderData
    ) : CartGlobalEvent()

    data class UpdateCartShopGroupTicker(
        val cartGroupHolderData: CartGroupHolderData
    ) : CartGlobalEvent()

    data class OnNeedUpdateWishlistAdapterData(
        val wishlistHolderData: CartWishlistHolderData,
        val wishlistIndex: Int
    ) : CartGlobalEvent()
}

sealed interface AddCartToWishlistV2Event {
    data class Success(
        val data: AddToWishlistV2Response.Data.WishlistAddV2,
        val productId: String,
        val isLastItem: Boolean,
        val source: String
    ) : AddCartToWishlistV2Event

    data class Failed(val throwable: Throwable) : AddCartToWishlistV2Event
}

sealed interface AddToCartEvent {
    data class Success(
        val addToCartDataModel: AddToCartDataModel,
        val productModel: Any
    ) : AddToCartEvent

    data class Failed(val throwable: Throwable) : AddToCartEvent
}

sealed interface AddToCartExternalEvent {
    data class Success(val model: AddToCartExternalModel) : AddToCartExternalEvent
    data class Failed(val throwable: Throwable) : AddToCartExternalEvent
}

sealed interface CartTrackerEvent {
    data class ATCTrackingURLRecent(val recommendationItem: RecommendationItem) :
        CartTrackerEvent

    data class ATCTrackingURLRecommendation(val recommendationItem: RecommendationItem) :
        CartTrackerEvent

    data class ATCTrackingURLBanner(val bannerShop: BannerShopProductUiModel) : CartTrackerEvent
}

sealed interface DeleteCartEvent {
    data class Success(
        val toBeDeletedCartIds: ArrayList<String>,
        val removeAllItems: Boolean,
        val cartDeleteItemData: CartDeleteItemData
    ) : DeleteCartEvent

    data class Failed(
        val forceExpandCollapsedUnavailableItems: Boolean,
        val throwable: Throwable
    ) : DeleteCartEvent
}

sealed interface FollowShopEvent {
    data class Success(val dataFollowShop: DataFollowShop) : FollowShopEvent
    data class Failed(val throwable: Throwable) : FollowShopEvent
}

sealed interface LoadRecentReviewState {
    data class Success(val recommendationWidgets: List<RecommendationWidget>) :
        LoadRecentReviewState

    data class Failed(val throwable: Throwable) : LoadRecentReviewState
}

sealed interface LoadRecommendationState {
    data class Success(val recommendationWidgets: List<RecommendationWidget>) :
        LoadRecommendationState

    object Failed : LoadRecommendationState
}

sealed interface LoadWishlistV2State {
    data class Success(
        val wishlists: List<GetWishlistV2Response.Data.WishlistV2.Item>,
        val forceReload: Boolean
    ) : LoadWishlistV2State

    object Failed : LoadWishlistV2State
}

sealed interface RemoveFromWishlistEvent {

    data class RemoveWishlistFromCartSuccess(
        val position: Int,
        val message: String,
    ) : RemoveFromWishlistEvent

    data class RemoveWishlistFromCartFailed(
        val throwable: Throwable
    ) : RemoveFromWishlistEvent

    data class Success(
        val data: DeleteWishlistV2Response.Data.WishlistRemoveV2,
        val productId: String
    ) : RemoveFromWishlistEvent

    data class Failed(
        val throwable: Throwable,
        val productId: String
    ) : RemoveFromWishlistEvent
}

sealed interface SeamlessLoginEvent {
    data class Success(val url: String) : SeamlessLoginEvent
    data class Failed(val msg: String) : SeamlessLoginEvent
}

sealed interface UndoDeleteEvent {
    object Success : UndoDeleteEvent

    data class Failed(val throwable: Throwable) : UndoDeleteEvent
}

sealed class UpdateCartAndGetLastApplyEvent {
    data class Success(val promoUiModel: PromoUiModel) : UpdateCartAndGetLastApplyEvent()
    data class Failed(val throwable: Throwable) : UpdateCartAndGetLastApplyEvent()
}

sealed class UpdateCartCheckoutState {
    data class Success(
        val eeCheckoutData: Map<String, Any>,
        val checkoutProductEligibleForCashOnDelivery: Boolean,
        val condition: Int
    ) : UpdateCartCheckoutState()

    data class ErrorOutOfService(val outOfService: OutOfService) : UpdateCartCheckoutState()

    data class UnknownError(val message: String, val ctaText: String) : UpdateCartCheckoutState()

    data class Failed(val throwable: Throwable) : UpdateCartCheckoutState()
}

sealed interface UpdateCartPromoState {
    object Success : UpdateCartPromoState

    data class Failed(val throwable: Throwable) : UpdateCartPromoState
}

data class SubTotalState(
    val subtotalCashback: Double,
    val subtotalBeforeSlashedPrice: Double,
    val qty: String,
    val subtotalPrice: Double,
    val noAvailableItems: Boolean
)

sealed class EntryPointInfoEvent {

    object Loading : EntryPointInfoEvent()

    data class ActiveNew(
        val lastApply: LastApplyUiModel,
        val entryPointInfo: PromoEntryPointInfo,
        val recommendedPromoCodes: List<String>
    ) : EntryPointInfoEvent()

    data class Active(
        val lastApply: LastApplyUiModel,
        val message: String
    ) : EntryPointInfoEvent()

    data class ActiveDefault(
        val appliedPromos: List<String>
    ) : EntryPointInfoEvent()

    data class InactiveNew(
        val lastApply: LastApplyUiModel,
        val isNoItemSelected: Boolean = false,
        val entryPointInfo: PromoEntryPointInfo = PromoEntryPointInfo(),
        val recommendedPromoCodes: List<String>
    ) : EntryPointInfoEvent()

    data class Inactive(
        val message: String = "",
        val isNoItemSelected: Boolean = false
    ) : EntryPointInfoEvent()

    data class AppliedNew(
        val lastApply: LastApplyUiModel,
        val leftIconUrl: String,
        val message: String,
        val recommendedPromoCodes: List<String>
    ) : EntryPointInfoEvent()

    data class Applied(
        val lastApply: LastApplyUiModel,
        val message: String,
        val detail: String
    ) : EntryPointInfoEvent()

    data class Error(
        val lastApply: LastApplyUiModel
    ) : EntryPointInfoEvent()
}

@Suppress("UNCHECKED_CAST")
class CartMutableLiveData<T>(initialValue: T) : LiveData<T>(initialValue) {

    override fun getValue(): T = super.getValue() as T

    public override fun setValue(value: T) {
        super.setValue(value)
    }

    fun notifyObserver() {
        this.value = this.value
    }
}

sealed interface GetBmGmGroupProductTickerState {
    data class Success(val pairOfferIdBmGmTickerResponse: Pair<CartItemHolderData, BmGmGetGroupProductTickerResponse>) :
        GetBmGmGroupProductTickerState

    data class Failed(val pairOfferIdThrowable: Pair<CartItemHolderData, Throwable>) :
        GetBmGmGroupProductTickerState
}
