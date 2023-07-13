package com.tokopedia.cartrevamp.view.uimodel

import androidx.lifecycle.LiveData
import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.wishlistcommon.data.response.GetWishlistV2Response

sealed class CartState<out T : Any> {
    data class Success<out T : Any>(val data: T) : CartState<T>()
    data class Failed(val throwable: Throwable) : CartState<Nothing>()
}

sealed class CartGlobalEvent {
    object Normal : CartGlobalEvent()
    data class ItemLoading(val isLoading: Boolean) : CartGlobalEvent()
    data class ProgressLoading(val isLoading: Boolean) : CartGlobalEvent()
    object LoadGetCartData : CartGlobalEvent()
    data class CartCounterUpdated(val counter: Int) : CartGlobalEvent()
    object SuccessClearRedPromosThenGoToPromo : CartGlobalEvent()
    object SuccessClearRedPromosThenGoToCheckout : CartGlobalEvent()
    data class UpdateAndReloadCartFailed(val throwable: Throwable) : CartGlobalEvent()
    data class SubTotalUpdated(
        val qty: String,
        val subtotalPrice: Double,
        val noAvailableItems: Boolean
    ) : CartGlobalEvent()
}

sealed class UpdateCartCheckoutState {
    object None : UpdateCartCheckoutState()
    data class Success(
        val eeCheckoutData: Map<String, Any>,
        val checkoutProductEligibleForCashOnDelivery: Boolean,
        val condition: Int
    ) : UpdateCartCheckoutState()

    data class ErrorOutOfService(val outOfService: OutOfService) : UpdateCartCheckoutState()

    data class UnknownError(val message: String, val ctaText: String) : UpdateCartCheckoutState()

    data class Failed(val throwable: Throwable) : UpdateCartCheckoutState()
}

sealed class UpdateCartAndGetLastApplyState {
    data class Success(val promoUiModel: PromoUiModel) : UpdateCartAndGetLastApplyState()
    data class Failed(val throwable: Throwable) : UpdateCartAndGetLastApplyState()
}

sealed class LoadWishlistV2State {
    data class Success(
        val wishlists: List<GetWishlistV2Response.Data.WishlistV2.Item>,
        val forceReload: Boolean
    ) : LoadWishlistV2State()

    object Failed : LoadWishlistV2State()
}

sealed class LoadRecommendationState {
    data class Success(val recommendationWidgets: List<RecommendationWidget>) : LoadRecommendationState()
    object Failed : LoadRecommendationState()
}

sealed class LoadRecentReviewState {
    data class Success(val recommendationWidgets: List<RecommendationWidget>) :
        LoadRecentReviewState()

    data class Failed(val throwable: Throwable) : LoadRecentReviewState()
}

sealed class UpdateCartPromoState {
    object None : UpdateCartPromoState()
    object Success : UpdateCartPromoState()

    data class Failed(val throwable: Throwable) : UpdateCartPromoState()
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
