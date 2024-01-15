package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.data.model.datamodel.SDUIDataModel
import com.tokopedia.product.detail.databinding.ItemSduiContainerBinding
import com.tokopedia.product.detail.databinding.ViewShipmentBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.sdui.SDUIManager
import org.json.JSONObject

class SDUIViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<SDUIDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_sdui_container
    }

    private val context = view.context
    private val binding = ItemSduiContainerBinding.bind(view)

    private val sduiManager = lazy {
        SDUIManager().apply {
            initSDUI(context)
        }
    }

    override fun bind(element: SDUIDataModel) {
        loadSduiWidget(element)
    }


    private fun loadSduiWidget(
        element: SDUIDataModel,
    ) {
        element.data?.let { createAndAddSDUIView(it) }
    }

    private fun createAndAddSDUIView(sduiJson: JSONObject){
        val templateJson = sduiJson.optJSONObject("templates")
        val cardJson = sduiJson.getJSONObject("card")

        binding.sduiViewContainer.removeAllViews()
        binding.sduiViewContainer.addView(sduiManager.value.createView(context,
            templateJson, "divKitView", cardJson))
    }

}
