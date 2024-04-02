package com.tokopedia.home_component.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentOrigamiSduiBinding
import com.tokopedia.home_component.delegate.OrigamiListenerDelegate
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.visitable.OrigamiSDUIDataModel
import com.tokopedia.sdui.SDUIManager
import com.tokopedia.sdui.extention.CustomActionInterface
import com.tokopedia.sdui.interfaces.SDUITrackingInterface
import com.tokopedia.utils.view.binding.viewBinding
import org.json.JSONObject

class V2OrigamiSDUIViewHolder(
    itemView: View,
    private val listenerDelegate: OrigamiListenerDelegate,
    val homeComponentListener: HomeComponentListener
) : AbstractViewHolder<OrigamiSDUIDataModel>(itemView),
    SDUITrackingInterface,
    CustomActionInterface {

    private var binding: HomeComponentOrigamiSduiBinding? by viewBinding()
    private var origamiSDUIDataModel: OrigamiSDUIDataModel? = null

    private val mManager by lazy(LazyThreadSafetyMode.NONE) {
        SDUIManager().apply {
            initSDUI(
                context = itemView.context,
                sduiTrackingInterface = this@V2OrigamiSDUIViewHolder,
                customActionInterface = this@V2OrigamiSDUIViewHolder
            )
        }
    }

    override fun bind(element: OrigamiSDUIDataModel?) {
        if (element == null) return

        this.origamiSDUIDataModel = element

        val dataSDUIJson = JSONObject(element.origamiData)
        val templateJson = dataSDUIJson.optJSONObject("templates") ?: return
        val cardJson = dataSDUIJson.getJSONObject("card")

        binding?.homeComponentOrigamiContainer?.removeAllViews()
        binding?.homeComponentOrigamiContainer?.addView(
            mManager.createView(
                itemView.context,
                templateJson, cardJson, ""
            )
        )
    }

    override fun bind(element: OrigamiSDUIDataModel?, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun onViewClick(trackerPayload: JSONObject?) {
        listenerDelegate.setDelegate(trackerPayload, origamiSDUIDataModel)
        listenerDelegate.onViewClicked(bindingAdapterPosition)
    }

    override fun onViewVisible(trackerPayload: JSONObject?) {
        listenerDelegate.setDelegate(trackerPayload, origamiSDUIDataModel)
        listenerDelegate.onViewImpressed(bindingAdapterPosition)
    }

    override fun onHandleCustomAction(queryParameter: String?) {
        // custom handling when timer from SDUI widget is expired then delete the viewholder from position
        if (queryParameter?.equals("timer_ended") == true) {
            val model = origamiSDUIDataModel ?: return

            model.channelModel?.let { channelModel ->
                homeComponentListener.onChannelExpired(
                    channelModel,
                    channelModel.verticalPosition,
                    model
                )
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.home_component_origami_sdui
    }
}

