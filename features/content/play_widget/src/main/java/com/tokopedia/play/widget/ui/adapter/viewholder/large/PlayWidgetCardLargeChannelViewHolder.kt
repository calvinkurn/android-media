package com.tokopedia.play.widget.ui.adapter.viewholder.large

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.custom.PlayWidgetCardChannelLargeView
import com.tokopedia.play.widget.ui.model.PlayWidgetLargeChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType

/**
 * @author by astidhiyaa on 11/01/22
 */
class PlayWidgetCardLargeChannelViewHolder(
    itemView: View,
    private val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    private val playWidgetCardChannelLargeView: PlayWidgetCardChannelLargeView = itemView as PlayWidgetCardChannelLargeView
    private val widgetCardLargeListener = object : PlayWidgetCardChannelLargeView.Listener {

        override fun onChannelClicked(view: View, item: PlayWidgetLargeChannelUiModel) {
            listener.onChannelClicked(view, item, adapterPosition)
        }

        override fun onToggleReminderChannelClicked(item: PlayWidgetLargeChannelUiModel, reminderType: PlayWidgetReminderType) {
            listener.onToggleReminderChannelClicked(item, reminderType, adapterPosition)
        }
    }

    init {
        //if re-use from medium, need to change layout params here
        playWidgetCardChannelLargeView.setListener(widgetCardLargeListener)
    }

    fun bind(item: PlayWidgetLargeChannelUiModel) {
        itemView.addOnImpressionListener(item.impressHolder) {
            listener.onChannelImpressed(itemView, item, adapterPosition)
        }
        playWidgetCardChannelLargeView.setModel(item)
    }

    companion object {
        @LayoutRes
        val layoutRes = R.layout.item_play_widget_card_channel_large
    }

    interface Listener {

        fun onChannelImpressed(
            view: View,
            item: PlayWidgetLargeChannelUiModel,
            position: Int
        )

        fun onChannelClicked(
            view: View,
            item: PlayWidgetLargeChannelUiModel,
            position: Int
        )

        fun onToggleReminderChannelClicked(
            item: PlayWidgetLargeChannelUiModel,
            reminderType: PlayWidgetReminderType,
            position: Int
        )
    }
}