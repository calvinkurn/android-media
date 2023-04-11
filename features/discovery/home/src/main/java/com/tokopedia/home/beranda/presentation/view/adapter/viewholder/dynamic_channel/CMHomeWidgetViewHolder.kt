package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCloseClickListener
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CMHomeWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeChannelWidgetUtil
import com.tokopedia.home.beranda.presentation.view.listener.CMHomeWidgetCallback
import com.tokopedia.home.databinding.HomeDcCmHomeWidgetItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class CMHomeWidgetViewHolder(
    val view: View,
    private val callback: CMHomeWidgetCallback
) :
    AbstractViewHolder<CMHomeWidgetDataModel>(view), CMHomeWidgetCloseClickListener {

    private val binding: HomeDcCmHomeWidgetItemBinding? by viewBinding()

    override fun bind(dataModel: CMHomeWidgetDataModel) {
        binding?.run {
            dataModel.cmHomeWidgetData?.let { cmHomeWidgetData ->
                if(cmHomeWidgetData.cmHomeWidgetProductCardData.isNullOrEmpty() &&
                    cmHomeWidgetData.cMHomeWidgetPaymentData.isNullOrEmpty()) {
                        cmHomeWidget.visibility = View.GONE
                } else {
                    activateCMHomeWidget(dataModel)
                }
            } ?: run {
                callback.getCMHomeWidget()
            }
        }
    }

    private fun activateCMHomeWidget(dataModel: CMHomeWidgetDataModel) {
        binding?.run {
            cmHomeWidget.visibility = View.VISIBLE
            cmHomeWidget.setOnCMHomeWidgetCloseClickListener(this@CMHomeWidgetViewHolder)
            dataModel.cmHomeWidgetData?.let { cmHomeWidget.loadCMHomeWidgetData(it) }
            setChannelDivider(dataModel.channel)
        }
    }

    override fun bind(element: CMHomeWidgetDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setChannelDivider(channel: DynamicHomeChannel.Channels) {
        binding?.run {
            HomeChannelWidgetUtil.validateHomeComponentDivider(
                channelModel = channel,
                dividerTop = cmHomeWidgetDividerHeader,
                dividerBottom = cmHomeWidgetDividerFooter
            )
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_cm_home_widget_item
    }

    override fun onCMHomeWidgetDismissClick(parentId: String, campaignId: String) {
        binding?.cmHomeWidget?.visibility = View.GONE
        callback.onCMHomeWidgetDismissClick()
    }

    override fun onRemoveCmWidgetLocally() {
        binding?.cmHomeWidget?.visibility = View.GONE
        callback.onRemoveCMWidgetLocally()
    }

    override fun updateCmMHomeWidget() {
        callback.getCMHomeWidget()
    }
}
