package com.tokopedia.product.detail.view.viewholder.promo_price.delegate

import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.fragment.delegate.BaseComponentCallback
import com.tokopedia.product.detail.view.viewholder.promo_price.event.ProductPriceEvent

class ProductPriceCallback(
    mediator: PdpComponentCallbackMediator
) : BaseComponentCallback<ProductPriceEvent>(mediator = mediator) {
    override fun onEvent(event: ProductPriceEvent) {
    }

}