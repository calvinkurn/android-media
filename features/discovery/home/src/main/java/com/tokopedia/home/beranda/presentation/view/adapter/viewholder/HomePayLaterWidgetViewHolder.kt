package com.tokopedia.home.beranda.presentation.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gopayhomewidget.presentation.listener.PayLaterWidgetListener
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomePayLaterWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeChannelWidgetUtil
import com.tokopedia.home.beranda.presentation.view.listener.HomePayLaterWidgetListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.home_dc_paylater_widget.view.*

class HomePayLaterWidgetViewHolder (
    val view: View,
    private val callback: HomePayLaterWidgetListener
) : AbstractViewHolder<HomePayLaterWidgetDataModel>(view), PayLaterWidgetListener {

    override fun bind(dataModel: HomePayLaterWidgetDataModel) {
        dataModel.payLaterWidgetData?.let { payLaterWidgetData ->
            itemView.paylater_home_widget.setData(payLaterWidgetData)
            itemView.paylater_home_widget.setPayLaterWidgetListener(this)
            setChannelDivider(dataModel.channel)
        } ?: run {
            callback.getPayLaterWidgetData()
        }
    }

    override fun bind(element: HomePayLaterWidgetDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setChannelDivider(channel: DynamicHomeChannel.Channels) {
        HomeChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = channel,
            dividerTop = itemView.paylater_home_widget_divider_header,
            dividerBottom = itemView.paylater_home_widget_divider_footer
        )
    }


    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_paylater_widget
    }

    override fun onClosePayLaterWidget() {
        callback.deletePayLaterWidget()
    }
}