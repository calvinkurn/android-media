package com.tokopedia.product.detail.view.fragment.delegate

import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.viewholder.review.delegate.ReviewCallback

@Suppress("LateinitUsage")
class PdpCallbackDelegateImpl : PdpCallbackDelegate {

    private lateinit var _mediator: PdpComponentCallbackMediator

    override fun registerCallback(mediator: PdpComponentCallbackMediator) {
        _mediator = mediator
    }

    override val review by lazy { ReviewCallback(mediator = _mediator) }
}
