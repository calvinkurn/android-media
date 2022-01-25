package com.tokopedia.play.widget.ui.adapter.viewholder.jumbo

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.custom.PlayWidgetCardChannelJumboView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType

/**
 * @author by astidhiyaa on 12/01/22
 */
class PlayWidgetCardJumboChannelViewHolder(
    itemView: View,
    private val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    private val playWidgetCardChannelJumboView: PlayWidgetCardChannelJumboView = itemView as PlayWidgetCardChannelJumboView
    private val widgetCardJumboListener = object : PlayWidgetCardChannelJumboView.Listener {

        override fun onChannelClicked(view: View, item: PlayWidgetChannelUiModel) {
            listener.onChannelClicked(view, item, adapterPosition)
        }

        override fun onToggleReminderChannelClicked(item: PlayWidgetChannelUiModel, reminderType: PlayWidgetReminderType) {
            listener.onToggleReminderChannelClicked(item, reminderType, adapterPosition)
        }
    }

    init {
        playWidgetCardChannelJumboView.setListener(widgetCardJumboListener)
    }

    fun bind(item: PlayWidgetChannelUiModel) {
//        itemView.addOnImpressionListener(item.impressHolder) {
//            listener.onChannelImpressed(itemView, item, adapterPosition)
//        }
        playWidgetCardChannelJumboView.setModel(item)
    }

    companion object {
        @LayoutRes
        val layoutRes = R.layout.item_play_widget_card_channel_jumbo
    }

    interface Listener {

        fun onChannelImpressed(
            view: View,
            item: PlayWidgetChannelUiModel,
            position: Int
        )

        fun onChannelClicked(
            view: View,
            item: PlayWidgetChannelUiModel,
            position: Int
        )

        fun onToggleReminderChannelClicked(
            item: PlayWidgetChannelUiModel,
            reminderType: PlayWidgetReminderType,
            position: Int
        )
    }
}