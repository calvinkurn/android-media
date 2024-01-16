package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model

sealed interface UiEffect {
    data class FinishActivity(
        val result: Int
    ) : UiEffect
}
