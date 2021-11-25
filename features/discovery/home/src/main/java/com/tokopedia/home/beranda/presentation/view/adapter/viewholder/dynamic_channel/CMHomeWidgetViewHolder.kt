package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCloseClickListener
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CMHomeWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeChannelWidgetUtil
import kotlinx.android.synthetic.main.home_dc_cm_home_widget_item.view.*

class CMHomeWidgetViewHolder(
    val view: View,
    private val listener: CMHomeWidgetCloseClickListener
) :
    AbstractViewHolder<CMHomeWidgetDataModel>(view) {

    override fun bind(dataModel: CMHomeWidgetDataModel?) {
        dataModel?.let {
            val cmHomeWidget = itemView.cm_home_widget
            it.cmHomeWidgetData?.let { cmHomeWidgetData ->
                cmHomeWidget.visibility = View.VISIBLE
                cmHomeWidget.setOnCMHomeWidgetCloseClickListener(object :
                    CMHomeWidgetCloseClickListener {
                    override fun onCMHomeWidgetDismissClick(parentID: Long, campaignID: Long) {
                        cmHomeWidget.visibility = View.GONE
                        listener.onCMHomeWidgetDismissClick(0, 0)
                    }
                })
                cmHomeWidget.loadCMHomeWidgetData(cmHomeWidgetData)
            }
            setChannelDivider(it.channel)
        }
    }

    override fun bind(element: CMHomeWidgetDataModel?, payloads: MutableList<Any>) {
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
}