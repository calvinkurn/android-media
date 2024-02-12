package com.tokopedia.product.detail.view.fragment.delegate

import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.viewholder.gwp.callback.GWPCallback
import com.tokopedia.product.detail.view.viewholder.review.delegate.ReviewCallback

/**
 * Created by yovi.putra on 14/11/23"
 * Project name: android-tokopedia-core
 **/

interface PdpCallbackDelegate {

    val review: ReviewCallback

    val gwp: GWPCallback

    fun registerCallback(mediator: PdpComponentCallbackMediator)
}
