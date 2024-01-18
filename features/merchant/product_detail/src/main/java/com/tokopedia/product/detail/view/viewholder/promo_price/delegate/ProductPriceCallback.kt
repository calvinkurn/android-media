package com.tokopedia.product.detail.view.viewholder.promo_price.delegate

import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.fragment.delegate.BaseComponentCallback
import com.tokopedia.product.detail.view.viewholder.promo_price.event.ProductPriceEvent
import com.tokopedia.product.detail.view.viewholder.promo_price.tracker.PromoPriceTracker

class ProductPriceCallback(
    mediator: PdpComponentCallbackMediator
) : BaseComponentCallback<ProductPriceEvent>(mediator = mediator) {
    override fun onEvent(event: ProductPriceEvent) {
        when (event) {
            is ProductPriceEvent.OnPromoPriceClicked -> onPromoPriceClicked(data = event)
        }
    }

    private fun onPromoPriceClicked(
        data: ProductPriceEvent.OnPromoPriceClicked
    ) {
        PromoPriceTracker.onPromoPriceClicked(
            queueTracker,
            data.subtitle,
            data.defaultPriceFmt,
            data.slashPriceFmt,
            data.promoPriceFmt,
            data.promoId,
            data.trackerData.asCommonTracker()
        )
    }
}
