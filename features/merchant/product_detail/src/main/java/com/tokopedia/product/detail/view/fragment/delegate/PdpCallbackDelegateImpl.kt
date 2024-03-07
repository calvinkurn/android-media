package com.tokopedia.product.detail.view.fragment.delegate

import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.viewholder.promo_price.delegate.ProductPriceCallback
import com.tokopedia.product.detail.view.viewholder.gwp.callback.GWPCallback
import com.tokopedia.product.detail.view.viewholder.review.delegate.ReviewCallback

@Suppress("LateinitUsage")
class PdpCallbackDelegateImpl : PdpCallbackDelegate {

    private lateinit var _mediator: PdpComponentCallbackMediator

    override fun registerCallback(mediator: PdpComponentCallbackMediator) {
        _mediator = mediator
    }

    override val review by callback { ReviewCallback(mediator = _mediator) }

    override val gwp by callback { GWPCallback(mediator = _mediator) }

    override val productPrice by lazy { ProductPriceCallback(mediator = _mediator) }

    //Fragment Level
    override val productRecomm by lazy { PartialRecommendationManager(mediator = _mediator) }
}
