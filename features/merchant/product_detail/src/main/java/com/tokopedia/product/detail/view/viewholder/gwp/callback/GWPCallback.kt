package com.tokopedia.product.detail.view.viewholder.gwp.callback

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

    override fun onEvent(event: GWPEvent) {
        when (event) {
            is GWPEvent.OnClickComponent -> onClickComponent(event)
            is GWPEvent.OnClickProduct -> onClickProduct(event = event)
            is GWPEvent.OnClickShowMore -> onClickShowMore(event = event)
            is GWPEvent.OnCardImpress -> onCardImpress(event = event)
        }
    }

    private fun onClickComponent(event: GWPEvent.OnClickComponent) {
        event.data.action.navigate()

        GWPTracker.onShowMore(
            title = event.data.title,
            componentTrackDataModel = event.data.trackData,
            commonTracker = getCommonTracker()
        )
    }

    private fun onClickProduct(event: GWPEvent.OnClickProduct) {
        event.data.action.navigate()

        GWPTracker.onCardClicked(
            trackingQueue = queueTracker,
            cardTrackData = event.data.trackData,
            commonTracker = getCommonTracker()
        )
    }

    private fun onClickShowMore(event: GWPEvent.OnClickShowMore) {
        event.data.action.navigate()

        val trackData = event.data.trackData
        GWPTracker.onShowMore(
            title = trackData.componentTitle,
            componentTrackDataModel = trackData.parentTrackData,
            commonTracker = getCommonTracker()
        )
    }

    private fun onCardImpress(event: GWPEvent.OnCardImpress) {
        GWPTracker.onCardImpression(
            trackingQueue = queueTracker,
            cardTrackData = event.card.trackData,
            commonTracker = getCommonTracker()
        )
    }
}
