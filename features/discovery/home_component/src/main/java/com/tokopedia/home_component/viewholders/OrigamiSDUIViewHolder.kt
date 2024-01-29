package com.tokopedia.home_component.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.visitable.OrigamiSDUIDataModel
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentOrigamiSduiBinding
import com.tokopedia.home_component.listener.CampaignWidgetComponentListener
import com.tokopedia.sdui.SDUIManager
import com.tokopedia.sdui.interfaces.SDUITrackingInterface
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import com.tokopedia.utils.view.binding.viewBinding
import org.json.JSONObject

/*
temporary solution for tracker :
hardcoded the tracker for campaign widget for POC purpose
 */
open class OrigamiSDUIViewHolder(
    itemView: View,
    val listenerTracker: CampaignWidgetComponentListener
) : AbstractViewHolder<OrigamiSDUIDataModel>(itemView), SDUITrackingInterface {
    companion object {
        val LAYOUT: Int = R.layout.home_component_origami_sdui
    }

    private var binding: HomeComponentOrigamiSduiBinding? by viewBinding()
    private var origamiSDUIDataModel: OrigamiSDUIDataModel? = null

    private val sduiManager = lazy {
        SDUIManager().apply {
            initSDUI(itemView.context, this@OrigamiSDUIViewHolder)
        }
    }

    override fun bind(element: OrigamiSDUIDataModel?) {
        this.origamiSDUIDataModel = element
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

    override fun onViewClick(trackerPayload: JSONObject?) {
        trackerPayload?.let {
            origamiSDUIDataModel?.channel?.let {
                when(trackerPayload.get("eventAction")){
                    "click campaign card" -> listenerTracker.onProductCardClicked(
                        it.channelModel,
                        it.channelModel.channelGrids[trackerPayload.getInt("position")],
                        adapterPosition,
                        trackerPayload.getInt("position"),
                        ""
                    )
                    "click see all header" -> listenerTracker.onSeeAllBannerClicked(it.channelModel, "")
                    "click see all card" -> listenerTracker.onSeeMoreCardClicked(it.channelModel, "")
                }
            }
        }
    }

    override fun onViewVisible(trackerPayload: JSONObject?) {
        trackerPayload?.let {
            origamiSDUIDataModel?.channel?.let {
                when(trackerPayload.get("eventAction")){
                    "view campaign card" -> listenerTracker.onProductCardImpressed(
                        it.channelModel,
                        it.channelModel.channelGrids[trackerPayload.getInt("position")],
                        adapterPosition,
                        trackerPayload.getInt("position")
                    )
                }
            }
        }
    }

}
