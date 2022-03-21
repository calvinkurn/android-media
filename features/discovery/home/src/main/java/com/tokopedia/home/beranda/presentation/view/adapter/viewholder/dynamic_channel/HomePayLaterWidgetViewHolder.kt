package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gopayhomewidget.presentation.listener.PayLaterWidgetListener
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomePayLaterWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeChannelWidgetUtil
import com.tokopedia.home.beranda.presentation.view.listener.HomePayLaterWidgetListener
import com.tokopedia.home.databinding.HomeDcPaylaterWidgetBinding
import com.tokopedia.utils.view.binding.viewBinding

class HomePayLaterWidgetViewHolder (
    val view: View,
    private val callback: HomePayLaterWidgetListener
) : AbstractViewHolder<HomePayLaterWidgetDataModel>(view), PayLaterWidgetListener {

    private val binding: HomeDcPaylaterWidgetBinding? by viewBinding()

    override fun bind(dataModel: HomePayLaterWidgetDataModel) {
        binding?.run {
            dataModel.payLaterWidgetData?.let { payLaterWidgetData ->
                paylaterHomeWidget.setData(payLaterWidgetData)
                paylaterHomeWidget.setPayLaterWidgetListener(this@HomePayLaterWidgetViewHolder)
                setChannelDivider(dataModel.channel)
            } ?: run {
                callback.getPayLaterWidgetData()
            }
        }
    }

    override fun bind(element: HomePayLaterWidgetDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    /**
     * Setting widget divider
     * @param channel : Fintech widget
     * @author minion-yoda
     */
    private fun setChannelDivider(channel: DynamicHomeChannel.Channels) {
        binding?.run {
            HomeChannelWidgetUtil.validateHomeComponentDivider(
                channelModel = channel,
                dividerTop = paylaterHomeWidgetDividerHeader,
                dividerBottom = paylaterHomeWidgetDividerFooter
            )
        }
    }


    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_paylater_widget
    }

    override fun onClosePayLaterWidget() {
        callback.deletePayLaterWidget()
    }
}