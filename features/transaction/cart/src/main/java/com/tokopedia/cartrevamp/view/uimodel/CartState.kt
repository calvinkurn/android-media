package com.tokopedia.cartrevamp.view.uimodel

import android.widget.ImageView
import androidx.lifecycle.LiveData
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductUiModel
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.GetWishlistV2Response

sealed class CartState<out T : Any> {
    data class Success<out T : Any>(val data: T) : CartState<T>()
    data class Failed(val throwable: Throwable) : CartState<Nothing>()
}

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(
        val exception: Exception,
        val errorMessage: String
    ) : Result<Nothing>()
}

sealed class CartGlobalEvent {
    data class ItemLoading(val isLoading: Boolean) : CartGlobalEvent()
    data class ProgressLoading(val isLoading: Boolean) : CartGlobalEvent()
    object LoadGetCartData : CartGlobalEvent()
    data class CartCounterUpdated(val counter: Int) : CartGlobalEvent()
    object SuccessClearRedPromosThenGoToPromo : CartGlobalEvent()
    object SuccessClearRedPromosThenGoToCheckout : CartGlobalEvent()
    data class UpdateAndReloadCartFailed(val throwable: Throwable) : CartGlobalEvent()
    data class SubTotalUpdated(
        val subtotalCashback: Double,
        val qty: String,
        val subtotalPrice: Double,
        val noAvailableItems: Boolean
    ) : CartGlobalEvent()

    data class AdapterItemChanged(
        val position: Int
    ) : CartGlobalEvent()

    data class CheckGroupShopCartTicker(
        val cartGroupHolderData: CartGroupHolderData
    ) : CartGlobalEvent()

    data class UpdateCartShopGroupTicker(
        val cartGroupHolderData: CartGroupHolderData
    ) : CartGlobalEvent()
}

sealed interface AddToCartEvent {
    data class Success(
        val addToCartDataModel: AddToCartDataModel,
        val productModel: Any
    ) : AddToCartEvent

    data class Failed(val throwable: Throwable) : AddToCartEvent
}

sealed interface CartTrackerEvent {
    data class ATCTrackingURLRecent(val productModel: CartRecentViewItemHolderData) :
        CartTrackerEvent

    data class ATCTrackingURLRecommendation(val recommendationItem: RecommendationItem) :
        CartTrackerEvent

    data class ATCTrackingURLBanner(val bannerShop: BannerShopProductUiModel) : CartTrackerEvent
}

sealed interface DeleteCartEvent {
    data class Success(
        val toBeDeletedCartIds: ArrayList<String>,
        val removeAllItems: Boolean,
        val forceExpandCollapsedUnavailableItems: Boolean,
        val addWishList: Boolean,
        val isFromGlobalCheckbox: Boolean,
        val isFromEditBundle: Boolean
    ) : DeleteCartEvent

    data class Failed(
        val forceExpandCollapsedUnavailableItems: Boolean,
        val throwable: Throwable
    ) : DeleteCartEvent
}

sealed interface UndoDeleteEvent {
    object Success : UndoDeleteEvent

    data class Failed(val throwable: Throwable) : UndoDeleteEvent
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

sealed class UpdateCartAndGetLastApplyEvent {
    data class Success(val promoUiModel: PromoUiModel) : UpdateCartAndGetLastApplyEvent()
    data class Failed(val throwable: Throwable) : UpdateCartAndGetLastApplyEvent()
}

sealed interface AddToWishlistV2Event {
    data class Success(
        val data: AddToWishlistV2Response.Data.WishlistAddV2,
        val productId: String,
        val isLastItem: Boolean,
        val source: String,
        val wishlistIcon: IconUnify,
        val animatedWishlistImage: ImageView
    ) : AddToWishlistV2Event

    data class Failed(val throwable: Throwable) : AddToWishlistV2Event
}

sealed interface LoadWishlistV2State {
    data class Success(
        val wishlists: List<GetWishlistV2Response.Data.WishlistV2.Item>,
        val forceReload: Boolean
    ) : LoadWishlistV2State

    object Failed : LoadWishlistV2State
}

sealed interface LoadRecommendationState {
    data class Success(val recommendationWidgets: List<RecommendationWidget>) :
        LoadRecommendationState

    object Failed : LoadRecommendationState
}

sealed interface LoadRecentReviewState {
    data class Success(val recommendationWidgets: List<RecommendationWidget>) :
        LoadRecentReviewState

    data class Failed(val throwable: Throwable) : LoadRecentReviewState
}

sealed interface UpdateCartPromoState {
    object Success : UpdateCartPromoState

    data class Failed(val throwable: Throwable) : UpdateCartPromoState
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
