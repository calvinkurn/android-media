package com.tokopedia.home_component.delegate

import com.tokopedia.home_component.delegate.OrigamiListenerDelegate.Companion.EVENT_ACTION_PAYLOAD
import com.tokopedia.home_component.delegate.OrigamiListenerDelegate.Companion.PARTIAL_POSITION_WIDGET_PAYLOAD
import com.tokopedia.home_component.listener.CampaignWidgetComponentListener
import com.tokopedia.home_component.visitable.OrigamiSDUIDataModel
import org.json.JSONObject

interface CampaignWidgetListenerDelegate {

    fun campaignWidgetClicked(
        trackerPayload: JSONObject?,
        model: OrigamiSDUIDataModel?,
        position: Int
    )

    fun campaignWidgetImpressed(
        trackerPayload: JSONObject?,
        model: OrigamiSDUIDataModel?,
        position: Int
    )
}

class CampaignWidgetListenerDelegateImpl constructor(
    private val listener: CampaignWidgetComponentListener
) : CampaignWidgetListenerDelegate {

    override fun campaignWidgetClicked(
        trackerPayload: JSONObject?,
        model: OrigamiSDUIDataModel?,
        position: Int
    ) {
        val payload = trackerPayload ?: return
        val channel = model?.channel ?: return

        when (payload.get(EVENT_ACTION_PAYLOAD)) {
            "click campaign card" -> listener.onProductCardClicked(
                channel.channelModel,
                channel.channelModel.channelGrids[payload.getInt(PARTIAL_POSITION_WIDGET_PAYLOAD)],
                position,
                payload.getInt(PARTIAL_POSITION_WIDGET_PAYLOAD),
                ""
            )

            "click see all header" -> listener.onSeeAllBannerClicked(
                channel.channelModel,
                ""
            )

            "click see all card" -> listener.onSeeMoreCardClicked(
                channel.channelModel,
                ""
            )
        }
    }

    override fun campaignWidgetImpressed(
        trackerPayload: JSONObject?,
        model: OrigamiSDUIDataModel?,
        position: Int
    ) {
        val payload = trackerPayload ?: return
        val channel = model?.channel ?: return

        when (payload.get(EVENT_ACTION_PAYLOAD)) {
            "view campaign card" -> listener.onProductCardImpressed(
                channel.channelModel,
                channel.channelModel.channelGrids[payload.getInt(PARTIAL_POSITION_WIDGET_PAYLOAD)],
                position,
                payload.getInt(PARTIAL_POSITION_WIDGET_PAYLOAD)
            )
        }
    }
}
