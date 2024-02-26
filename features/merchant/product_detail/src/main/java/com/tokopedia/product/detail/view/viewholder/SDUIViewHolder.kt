package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.SDUIDataModel
import com.tokopedia.product.detail.databinding.ItemSduiContainerBinding
import com.tokopedia.product.detail.view.fragment.delegate.BasicComponentEvent
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.product.detail.view.viewholder.dynamic_oneliner.delegate.DynamicOneLinerCallback
import com.tokopedia.product.detail.view.viewholder.dynamic_oneliner.event.DynamicOneLinerEvent
import com.tokopedia.sdui.SDUIManager
import com.tokopedia.sdui.interfaces.SDUITrackingInterface
import org.json.JSONObject

class SDUIViewHolder(
    view: View,
    private val callback: DynamicOneLinerCallback
) : ProductDetailPageViewHolder<SDUIDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_sdui_container
    }

    private val context = view.context
    private val binding = ItemSduiContainerBinding.bind(view)

    private val actionHandler by generateActionHandler()

    private val sduiManager = lazy {
        SDUIManager().apply {
            initSDUI(context, actionHandler)
        }
    }

    private var element: SDUIDataModel? = null

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

        binding.sduiViewContainer.removeAllViews()
        binding.sduiViewContainer.addView(view)
    }

    private fun generateActionHandler() = lazy {
        object : SDUITrackingInterface {

            override fun onViewClick(trackerPayload: JSONObject?) {
                val element = element ?: return
                callback.event(
                    DynamicOneLinerEvent.OnDynamicOneLinerClicked(
                    "", "", getComponentTrackData(element)
                ))
            }

            override fun onViewVisible(trackerPayload: JSONObject?) {
                // No Op
            }
        }
    }
}
