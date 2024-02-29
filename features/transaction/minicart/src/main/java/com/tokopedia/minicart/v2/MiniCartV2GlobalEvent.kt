package com.tokopedia.minicart.v2

import com.tokopedia.minicart.common.domain.data.MiniCartCheckoutData

sealed interface MiniCartV2GlobalEvent {

    data class FailToLoadMiniCart(val throwable: Throwable) : MiniCartV2GlobalEvent

    object SuccessGoToCheckout : MiniCartV2GlobalEvent

    data class FailGoToCheckout(
        val data: MiniCartCheckoutData? = null,
        val throwable: Throwable? = null
    ) : MiniCartV2GlobalEvent
}
