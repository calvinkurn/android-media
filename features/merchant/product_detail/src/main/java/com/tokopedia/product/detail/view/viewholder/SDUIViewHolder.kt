package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.SDUIDataModel
import com.tokopedia.product.detail.databinding.ItemSduiContainerBinding
import com.tokopedia.product.detail.view.fragment.delegate.BasicComponentEvent
import com.tokopedia.product.detail.view.viewholder.sdui.SDUICallback
import com.tokopedia.product.detail.view.viewholder.sdui.SDUIEvent
import com.tokopedia.sdui.SDUIManager
import com.tokopedia.sdui.extention.ActionHandler
import com.tokopedia.sdui.interfaces.SDUITrackingInterface
import com.tokopedia.sdui.utils.DivActionUtils
import org.json.JSONObject

class SDUIViewHolder(
    view: View,
    private val callback: SDUICallback
) : ProductDetailPageViewHolder<SDUIDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_sdui_container
    }

    private val context = view.context
    private val binding = ItemSduiContainerBinding.bind(view)

    private val actionHandler by generateActionHandler()

    private val sduiManager = lazy {
        SDUIManager().apply {
            initSDUI(
                context = context,
                sduiTrackingInterface = actionHandler
            )
        }
    }

    private var element: SDUIDataModel? = null

    private var sduiView: View? = null

    override fun bind(element: SDUIDataModel) {
        loadSDUIWidget(element)
    }

    private fun loadSDUIWidget(
        element: SDUIDataModel
    ) {
        this.element = element
        createAndAddSDUIView(element)
    }

    private fun createAndAddSDUIView(element: SDUIDataModel) {
        if (sduiView != null) return
        val jsonObject = element.jsonObject ?: return
        val templateJSON = jsonObject.optJSONObject("templates") ?: return
        val cardJson = jsonObject.getJSONObject("card") ?: return

        val view = sduiManager.value.createView(
            context,
            templateJSON,
            cardJson,
            "divKitView"
        ) ?: return

        view.addOnImpressionListener(
            holder = element.impressHolder,
            holders = callback.impressionHolders,
            name = element.name,
            useHolders = callback.isRemoteCacheableActive
        ) {
            val trackerData = getComponentTrackData(element = element)
            callback.event(BasicComponentEvent.OnImpressComponent(trackData = trackerData))
        }

        binding.sduiViewContainer.addView(view)
        this.sduiView = view
    }


    private fun generateActionHandler() = lazy {
        object : SDUITrackingInterface {

            private fun parsePayloadToEventMap(payload: JSONObject): HashMap<String, Any> {
                val trackingData = payload.getJSONObject(ActionHandler.KEY_TRACKING_DATA)
                return DivActionUtils.toMap(trackingData)
            }

            override fun onViewClick(trackerPayload: JSONObject?) {
                val payload = trackerPayload ?: return
                val eventMap = parsePayloadToEventMap(payload)
                callback.event(SDUIEvent.SendTracker(eventMap))
            }

            override fun onViewVisible(trackerPayload: JSONObject?) {
                val payload = trackerPayload ?: return
                val eventMap = parsePayloadToEventMap(payload)
                callback.event(SDUIEvent.SendTracker(eventMap))
            }
        }
    }
}
