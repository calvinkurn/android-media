package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel


/**
 * Created by mzennis on 13/10/20.
 */
class CarouselPlayWidgetViewHolder(
        itemView: View,
        private val playWidgetCoordinator: PlayWidgetCoordinator
) : AbstractViewHolder<CarouselPlayWidgetUiModel>(itemView) {

    private val playWidgetView = itemView as PlayWidgetView

    init {
        playWidgetCoordinator.controlWidget(playWidgetView)
    }

    override fun bind(element: CarouselPlayWidgetUiModel) {
        playWidgetCoordinator.connect(playWidgetView, element.widgetUiModel)
    }

    override fun bind(element: CarouselPlayWidgetUiModel?, payloads: MutableList<Any>) {
        if (element == null || payloads.size <= 0) return
        val reminderUiModel = payloads[0]
        if (reminderUiModel !is PlayWidgetReminderUiModel) return

        val newWidgetUiModel = element.widgetUiModel
        if (newWidgetUiModel is PlayWidgetUiModel.Medium && newWidgetUiModel.items.size > reminderUiModel.position) {
            newWidgetUiModel.items.mapIndexed { index, item ->
                if (index == reminderUiModel.position && item is PlayWidgetMediumChannelUiModel) {
                    item.activeReminder = reminderUiModel.remind
                }
                item
            }
        }

        playWidgetView.setModel(newWidgetUiModel)
    }

    companion object {
        val LAYOUT = R.layout.item_shop_home_play_widget
    }
}