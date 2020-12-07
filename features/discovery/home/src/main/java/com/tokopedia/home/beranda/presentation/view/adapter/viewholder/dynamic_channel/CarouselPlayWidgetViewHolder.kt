package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.analytics.v2.HomePlayWidgetAnalyticListener
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CarouselPlayWidgetDataModel
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.model.*

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
        if (element == null) return
        setupAnalyticVariable(element)
        playWidgetViewHolder.bind(element.widgetUiModel)
    }

    override fun bind(element: CarouselPlayWidgetDataModel?, payloads: MutableList<Any>) {
        if (element == null || payloads.size <= 0) return
        val payload = payloads[0]

        val widgetUiModel = element.widgetUiModel
        if (widgetUiModel !is PlayWidgetUiModel.Medium) return

        if (payload is PlayWidgetReminderUiModel) {
            playWidgetViewHolder.bind(updateToggleReminder(payload, widgetUiModel))
        } else if (payload is PlayWidgetTotalViewUiModel) {
            playWidgetViewHolder.bind(updateTotalView(payload, widgetUiModel))
        }
    }

    private fun updateToggleReminder(reminderUiModel: PlayWidgetReminderUiModel, widgetUiModel: PlayWidgetUiModel.Medium): PlayWidgetUiModel {
        if (widgetUiModel.items.size > reminderUiModel.position) {
            val items = widgetUiModel.items.mapIndexed { index, item ->
                if (index == reminderUiModel.position && item is PlayWidgetMediumChannelUiModel) {
                    item.copy(activeReminder = !reminderUiModel.remind)
                } else {
                    item
                }
            }
            return widgetUiModel.copy(items = items)
        }
        return widgetUiModel
    }

    private fun updateTotalView(totalViewUiModel: PlayWidgetTotalViewUiModel, widgetUiModel: PlayWidgetUiModel.Medium): PlayWidgetUiModel {
        val items = widgetUiModel.items.map { item ->
            if (item is PlayWidgetMediumChannelUiModel && totalViewUiModel.channelId == item.channelId) {
                item.copy(totalView = totalViewUiModel.totalView)
            } else {
                item
            }
        }
        return widgetUiModel.copy(items = items)
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