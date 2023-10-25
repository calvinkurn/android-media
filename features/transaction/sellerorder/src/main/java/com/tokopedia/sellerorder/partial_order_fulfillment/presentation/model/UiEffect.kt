package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model

sealed interface UiEffect {
    object FinishActivity : UiEffect
}
