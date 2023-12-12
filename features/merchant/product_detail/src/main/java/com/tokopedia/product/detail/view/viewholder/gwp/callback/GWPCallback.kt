package com.tokopedia.product.detail.view.viewholder.gwp.callback

import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.fragment.delegate.BaseComponentCallback
import com.tokopedia.product.detail.view.viewholder.gwp.event.GWPEvent
import com.tokopedia.product.detail.view.viewholder.gwp.tracker.GWPTracker

/**
 * Created by yovi.putra on 30/11/23"
 * Project name: android-tokopedia-core
 **/

class GWPCallback(
    mediator: PdpComponentCallbackMediator
) : BaseComponentCallback<GWPEvent>(mediator = mediator) {

    private val tracker by lazyThreadSafetyNone {
        GWPTracker(trackingQueue = queueTracker)
    }

    override fun onEvent(event: GWPEvent) {
        tracker.tracking(event = event, commonTracker = getCommonTracker())

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

            is GWPEvent.OnCardImpress -> {
                // no-ops
            }
        }
    }
}
