package com.tokopedia.home_component.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.visitable.OrigamiSDUIDataModel
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentOrigamiSduiBinding
import com.tokopedia.sdui.SDUIManager
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import com.tokopedia.utils.view.binding.viewBinding
import org.json.JSONObject

class OrigamiSDUIViewHolder(
    itemView: View
) : AbstractViewHolder<OrigamiSDUIDataModel>(itemView) {
    companion object {
        val LAYOUT: Int = R.layout.home_component_origami_sdui
    }

    private var binding: HomeComponentOrigamiSduiBinding? by viewBinding()

    private val sduiManager = lazy {
        SDUIManager().apply {
            initSDUI(itemView.context)
        }
    }

    override fun bind(element: OrigamiSDUIDataModel?) {
        val dataSDUIJson = JSONObject(element?.origamiData)
        val templateJson = dataSDUIJson.optJSONObject("templates")
        val cardJson = dataSDUIJson.getJSONObject("card")

        binding?.homeComponentOrigamiContainer?.removeAllViews()
        binding?.homeComponentOrigamiContainer?.addView(
            sduiManager.value.createView(
                itemView.context,
                templateJson, "", cardJson
            )
        )
    }

}
