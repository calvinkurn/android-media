package com.tokopedia.shop.home.view.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalViewUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel

/**
 * Created by mzennis on 13/10/20.
 */
class CarouselPlayWidgetViewHolder(
        private val playWidgetViewHolder: PlayWidgetViewHolder
) : AbstractViewHolder<CarouselPlayWidgetUiModel>(playWidgetViewHolder.itemView) {

    override fun bind(element: CarouselPlayWidgetUiModel) {
        playWidgetViewHolder.bind(element.widgetUiModel, this)
    }

    override fun bind(element: CarouselPlayWidgetUiModel?, payloads: MutableList<Any>) {
        if (element == null || payloads.size <= 0) return
        val payload = payloads[0]
        val widgetUiModel = element.widgetUiModel
        if (widgetUiModel !is PlayWidgetUiModel.Medium) return

        if (payload is PlayWidgetReminderUiModel) {
            playWidgetViewHolder.bind(updateToggleReminder(payload, widgetUiModel), payloads)
        } else if (payload is PlayWidgetTotalViewUiModel) {
            playWidgetViewHolder.bind(updateTotalView(payload, widgetUiModel), payloads)
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
        val LAYOUT = R.layout.item_shop_home_play_widget
    }
}