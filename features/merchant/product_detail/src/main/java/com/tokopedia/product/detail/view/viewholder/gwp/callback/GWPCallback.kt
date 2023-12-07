package com.tokopedia.product.detail.view.viewholder.gwp.callback

import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.fragment.delegate.BaseComponentCallback
import com.tokopedia.product.detail.view.viewholder.gwp.event.GWPEvent

/**
 * Created by yovi.putra on 30/11/23"
 * Project name: android-tokopedia-core
 **/

class GWPCallback(
    mediator: PdpComponentCallbackMediator
) : BaseComponentCallback<GWPEvent>(mediator = mediator) {

    override fun onEvent(event: GWPEvent) {
        when (event) {
            is GWPEvent.OnClickComponent -> {
                event.data.action.navigate()
            }
            is GWPEvent.OnClickProduct -> {
                event.data.action.navigate()
            }
            is GWPEvent.OnClickShowMore -> {
                event.data.action.navigate()
            }

            else -> {
                // no-ops
            }
        }
    }

    override fun onTracking(event: GWPEvent) {
        when (event) {
            is GWPEvent.OnClickComponent -> {
            }
            is GWPEvent.OnClickProduct -> {
            }
            is GWPEvent.OnClickShowMore -> {
            }
            is GWPEvent.OnCardImpress -> {
            }
        }
    }
}
