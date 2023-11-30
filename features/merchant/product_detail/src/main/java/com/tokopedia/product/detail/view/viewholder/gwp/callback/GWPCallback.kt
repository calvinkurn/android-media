package com.tokopedia.product.detail.view.viewholder.gwp.callback

import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.fragment.delegate.BaseComponentCallback
import com.tokopedia.product.detail.view.viewholder.gwp.event.GWPComponentEvent

/**
 * Created by yovi.putra on 30/11/23"
 * Project name: android-tokopedia-core
 **/

class GWPCallback(
    mediator: PdpComponentCallbackMediator
) : BaseComponentCallback<GWPComponentEvent>(mediator = mediator) {

    override fun onEvent(event: GWPComponentEvent) {
        when (event) {
            is GWPComponentEvent.OnClickTracking -> {
            }
        }
    }
}
