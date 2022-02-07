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
import kotlinx.android.synthetic.main.home_dc_cm_home_widget_item.view.*

class CMHomeWidgetViewHolder(
    val view: View,
    private val callback: CMHomeWidgetCallback
) :
    AbstractViewHolder<CMHomeWidgetDataModel>(view), CMHomeWidgetCloseClickListener {

    override fun bind(dataModel: CMHomeWidgetDataModel) {
        dataModel.cmHomeWidgetData?.let { cmHomeWidgetData ->
            if(cmHomeWidgetData.cmHomeWidgetProductCardData.isNullOrEmpty()){
                itemView.cm_home_widget.visibility = View.GONE
            }else {
                itemView.cm_home_widget.visibility = View.VISIBLE
                itemView.cm_home_widget.setOnCMHomeWidgetCloseClickListener(this)
                itemView.cm_home_widget.loadCMHomeWidgetData(cmHomeWidgetData)
                setChannelDivider(dataModel.channel)
            }
        } ?: run {
            callback.getCMHomeWidget()
        }
    }

    override fun bind(element: CMHomeWidgetDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setChannelDivider(channel: DynamicHomeChannel.Channels) {
        HomeChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = channel,
            dividerTop = itemView.cm_home_widget_divider_header,
            dividerBottom = itemView.cm_home_widget_divider_footer
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_cm_home_widget_item
    }

    override fun onCMHomeWidgetDismissClick(parentId: String, campaignId: String) {
        itemView.cm_home_widget.visibility = View.GONE
        callback.onCMHomeWidgetDismissClick()
    }
}