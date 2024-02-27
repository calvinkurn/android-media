package com.tokopedia.home_component.delegate

import com.tokopedia.home_component.listener.CampaignWidgetComponentListener
import com.tokopedia.home_component.listener.Kd4SquareWidgetListener
import com.tokopedia.home_component.visitable.OrigamiSDUIDataModel
import org.json.JSONObject

interface OrigamiListenerDelegate {
    fun setDelegate(payload: JSONObject?, origamiModel: OrigamiSDUIDataModel?)
    fun onViewClicked(position: Int)
    fun onViewImpressed(position: Int)

    companion object {
        const val EVENT_ACTION_PAYLOAD = "eventAction"
        const val PARTIAL_POSITION_WIDGET_PAYLOAD = "position"
    }
}

open class OrigamiListenerDelegateImpl constructor(
    campaignListener: CampaignWidgetComponentListener,
    kd4SquareListener: Kd4SquareWidgetListener
) : OrigamiListenerDelegate,
    CampaignWidgetListenerDelegate by CampaignWidgetListenerDelegateImpl(campaignListener),
    Kd4SquareWidgetListenerDelegate by Kd4SquareWidgetListenerDelegateImpl(kd4SquareListener) {

    private var trackerPayload: JSONObject? = null
    private var model: OrigamiSDUIDataModel? = null

    private var isDelegated = false

    override fun setDelegate(payload: JSONObject?, origamiModel: OrigamiSDUIDataModel?) {
        isDelegated = true

        trackerPayload = payload
        model = origamiModel
    }

    override fun onViewClicked(position: Int) {
        checker()

        campaignWidgetClicked(trackerPayload, model, position)
        kd4SquareClicked(trackerPayload, model, position)
    }

    override fun onViewImpressed(position: Int) {
        checker()

        campaignWidgetImpressed(trackerPayload, model, position)
        kd4SquareImpressed(trackerPayload, model, position)
    }

    private fun checker() {
        if (!isDelegated) error("Please call setDelegate() first before hitting the tracker.")
    }
}
