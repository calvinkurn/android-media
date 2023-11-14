package com.tokopedia.product.detail.view.fragment

import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.viewholder.review.delegate.ReviewCallback

/**
 * Created by yovi.putra on 14/11/23"
 * Project name: android-tokopedia-core
 **/

interface PdpCallbackDelegate {

    val review: ReviewCallback

    fun registerCallback(mediator: PdpComponentCallbackMediator)
}

class PdpCallbackDelegateImpl : PdpCallbackDelegate {

    private lateinit var _mediator: PdpComponentCallbackMediator

    override fun registerCallback(mediator: PdpComponentCallbackMediator) {
        _mediator = mediator
    }

    override val review by lazy { ReviewCallback(mediator = _mediator) }
}
