package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.analytics.v2.HomePlayWidgetAnalyticListener
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CarouselPlayWidgetDataModel
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by mzennis on 19/10/20.
 */
class CarouselPlayWidgetViewHolder(
        private val playWidgetViewHolder: PlayWidgetViewHolder,
        val homeCategoryListener: HomeCategoryListener
) : AbstractViewHolder<CarouselPlayWidgetDataModel>(playWidgetViewHolder.itemView) {

    private val playWidgetAnalyticListener = HomePlayWidgetAnalyticListener(
            trackingQueue = homeCategoryListener.getTrackingQueueObj(),
            userId = homeCategoryListener.userId
    )

    init {
        playWidgetViewHolder.coordinator.setAnalyticListener(playWidgetAnalyticListener)
    }

    override fun bind(element: CarouselPlayWidgetDataModel?) {
        element?.let {
            setupAnalyticVariable(element)
            playWidgetViewHolder.bind(element.widgetUiModel)
        }
    }

    override fun bind(element: CarouselPlayWidgetDataModel?, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setupAnalyticVariable(element: CarouselPlayWidgetDataModel) {
        if (element.widgetUiModel is PlayWidgetUiModel.Medium) {
            playWidgetAnalyticListener.widgetId = element.homeChannel.id
            playWidgetAnalyticListener.widgetName = element.widgetUiModel.title
            playWidgetAnalyticListener.setBusinessWidgetPosition(element.widgetUiModel.config.businessWidgetPosition)
        }
    }

    companion object {
        @LayoutRes val LAYOUT = PlayWidgetViewHolder.layout
    }
}