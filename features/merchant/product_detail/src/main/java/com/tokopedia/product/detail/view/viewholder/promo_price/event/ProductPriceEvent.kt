package com.tokopedia.product.detail.view.viewholder.promo_price.event

import com.tokopedia.product.detail.view.fragment.delegate.BaseComponentEvent

sealed interface ProductPriceEvent : BaseComponentEvent {

    data class OnPromoPriceClicked(
        val url: String
    ) : ProductPriceEvent
}