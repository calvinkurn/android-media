package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CarouselPlayWidgetDataModel
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalViewUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel


/**
 * Created by mzennis on 19/10/20.
 */
class CarouselPlayWidgetViewHolder(
        private val widgetViewHolder: PlayWidgetViewHolder
) : AbstractViewHolder<CarouselPlayWidgetDataModel>(widgetViewHolder.itemView) {


    override fun bind(element: CarouselPlayWidgetDataModel?) {
        element?.let { item ->
            widgetViewHolder.bind(item.widgetUiModel)
        }
    }

    override fun bind(element: CarouselPlayWidgetDataModel?, payloads: MutableList<Any>) {
        if (element == null || payloads.size <= 0) return
        val payload = payloads[0]
        val widgetUiModel = element.widgetUiModel
        if (widgetUiModel !is PlayWidgetUiModel.Medium) return

        if (payload is PlayWidgetReminderUiModel) {
            widgetViewHolder.bind(updateToggleReminder(payload, widgetUiModel))
        } else if (payload is PlayWidgetTotalViewUiModel) {
            widgetViewHolder.bind(updateTotalView(payload, widgetUiModel))
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

    companion object {
        @LayoutRes val LAYOUT = PlayWidgetViewHolder.layout
    }
}