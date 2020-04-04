package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model

import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.PriceValidation
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.NotEligiblePromoHolderdata
import java.util.ArrayList

sealed class OccState<out T: Any> {
    data class FirstLoad<out T: Any>(val data: T) : OccState<T>()
    data class Success<out T: Any>(val data: T) : OccState<T>()
    object Loading : OccState<Nothing>()
    data class Fail(var isConsumed: Boolean, val throwable: Throwable?, val errorMessage: String): OccState<Nothing>()
}

sealed class OccGlobalEvent {
    object Normal : OccGlobalEvent()
    object Loading : OccGlobalEvent()
    data class Error(val throwable: Throwable? = null, val errorMessage: String = "") : OccGlobalEvent()
    data class CheckoutError(val error: com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout.Error) : OccGlobalEvent()
    data class PriceChangeError(val priceValidation: PriceValidation) : OccGlobalEvent()
    data class TriggerRefresh(val isFullRefresh: Boolean = true, val throwable: Throwable? = null, val errorMessage: String = "") : OccGlobalEvent()
    data class PromoClashing(val notEligiblePromoHolderdataList: ArrayList<NotEligiblePromoHolderdata>) : OccGlobalEvent()
}