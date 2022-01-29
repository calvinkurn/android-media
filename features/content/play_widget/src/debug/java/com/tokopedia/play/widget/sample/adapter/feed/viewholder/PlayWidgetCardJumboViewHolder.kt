package com.tokopedia.play.widget.sample.adapter.feed.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.widget.jumbo.PlayWidgetCardJumboView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType

/**
 * @author by astidhiyaa on 12/01/22
 */
class PlayWidgetCardJumboViewHolder(
    itemView: View,
    private val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    private val widgetCardJumboView: PlayWidgetCardJumboView = itemView as PlayWidgetCardJumboView
    private val widgetCardJumboListener = object : PlayWidgetCardJumboView.Listener {

        override fun onChannelClicked(view: View, item: PlayWidgetChannelUiModel) {
            listener.onChannelClicked(view, item, adapterPosition)
        }

        override fun onToggleReminderChannelClicked(item: PlayWidgetChannelUiModel, reminderType: PlayWidgetReminderType) {
            listener.onToggleReminderChannelClicked(item, reminderType, adapterPosition)
        }
    }

    init {
        widgetCardJumboView.setListener(widgetCardJumboListener)
    }

    fun bind(item: PlayWidgetChannelUiModel) {
        itemView.addOnImpressionListener(item.impressHolder) {
            listener.onChannelImpressed(itemView, item, adapterPosition)
        }
        widgetCardJumboView.setModel(item)
    }

    companion object {
        @LayoutRes
        val layoutRes = R.layout.item_play_widget_card_jumbo
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