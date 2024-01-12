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

        val tempJosnSdui = "{\"templates\":{\"palugada_one_liner\":{\"type\":\"container\",\"orientation\":\"horizontal\",\"content_alignment_vertical\":\"center\",\"paddings\":{\"bottom\":12,\"top\":12,\"right\":16,\"left\":16},\"width\":{\"type\":\"match_parent\"},\"height\":{\"type\":\"fixed\",\"value\":48},\"background\":[{\"type\":\"solid\",\"color\":\"#FFFFFF\"}],\"actions\":[{\"log_id\":\"palugada_widget_clicked\",\"\$url\":\"palugada_widget_url\"}],\"items\":[{\"type\":\"image\",\"\$image_url\":\"left_icon_url\",\"height\":{\"type\":\"fixed\",\"value\":24},\"width\":{\"type\":\"fixed\",\"value\":24}},{\"type\":\"text\",\"\$text\":\"title\",\"max_lines\":1,\"margins\":{\"left\":4},\"text_color\":\"#000000\",\"extensions\":[{\"id\":\"html-text\"}]},{\"type\":\"image\",\"\$image_url\":\"right_icon_url\",\"height\":{\"type\":\"fixed\",\"value\":24},\"width\":{\"type\":\"fixed\",\"value\":24}}]}},\"card\":{\"log_id\":\"palugada_card\",\"states\":[{\"state_id\":0,\"div\":{\"type\":\"container\",\"items\":[{\"type\":\"palugada_one_liner\",\"left_icon_url\":\"https://images.tokopedia.net/img/pdp/icons/icon-lampu.png\",\"title\":\"<p><b>lorem</b><i>ipsum</i>dolor</p>\",\"right_icon_url\":\"https://images.tokopedia.net/img/pdp/icons/icon-lampu.png\",\"palugada_widget_url\":\"div-action://route?applink=tokopedia://search?q=iphone\"}]}}]}}"

        val templateJson = tempJosnSdui.optJSONObject("templates")
        val cardJson = tempJosnSdui.getJSONObject("card")

        binding.sduiViewContainer.removeAllViews()
        binding.sduiViewContainer.addView(sduiManager.value.createView(context,
            templateJson, "divKitView", cardJson))
    }

}
