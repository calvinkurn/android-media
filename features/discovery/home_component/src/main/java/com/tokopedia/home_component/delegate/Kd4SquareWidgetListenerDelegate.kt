package com.tokopedia.home_component.delegate

import com.tokopedia.home_component.delegate.OrigamiListenerDelegate.Companion.EVENT_ACTION_PAYLOAD
import com.tokopedia.home_component.delegate.OrigamiListenerDelegate.Companion.PARTIAL_POSITION_WIDGET_PAYLOAD
import com.tokopedia.home_component.listener.Kd4SquareWidgetListener
import com.tokopedia.home_component.visitable.OrigamiSDUIDataModel
import org.json.JSONObject

interface Kd4SquareWidgetListenerDelegate {

    fun kd4SquareClicked(
        trackerPayload: JSONObject?,
        model: OrigamiSDUIDataModel?,
        position: Int
    )

    fun kd4SquareImpressed(
        trackerPayload: JSONObject?,
        model: OrigamiSDUIDataModel?,
        position: Int
    )
}

class Kd4SquareWidgetListenerDelegateImpl constructor(
    private val listener: Kd4SquareWidgetListener
) : Kd4SquareWidgetListenerDelegate {

    override fun kd4SquareClicked(
        trackerPayload: JSONObject?,
        model: OrigamiSDUIDataModel?,
        position: Int
    ) {
        val payload = trackerPayload ?: return
        val model = model?.channelModel ?: return

        when (payload.get(EVENT_ACTION_PAYLOAD)) {
            "click product" -> {
                listener.onCardClicked(
                    model,
                    model.channelGrids[payload.getInt(PARTIAL_POSITION_WIDGET_PAYLOAD)],
                    position
                )
            }

            "click chevron" -> {
                listener.onViewAllChevronClicked(model)
            }
        }
    }

    override fun kd4SquareImpressed(
        trackerPayload: JSONObject?,
        model: OrigamiSDUIDataModel?,
        position: Int
    ) {
        val payload = trackerPayload ?: return
        val model = model?.channelModel ?: return

        when (payload.get(EVENT_ACTION_PAYLOAD)) {
            "view product" -> listener.onWidgetImpressed(model, position)
        }
    }
}
