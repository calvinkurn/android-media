package com.tokopedia.play.widget.ui.adapter.viewholder.medium

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.custom.PlayWidgetCardChannelMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel

/**
 * Created by mzennis on 05/10/20.
 */
class PlayWidgetCardMediumChannelViewHolder(
        itemView: View,
        private val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    private val playWidgetCardChannelMediumView: PlayWidgetCardChannelMediumView = itemView as PlayWidgetCardChannelMediumView
    private val widgetCardMediumListener = object : PlayWidgetCardChannelMediumView.Listener {

        override fun onChannelClicked(view: View, item: PlayWidgetMediumChannelUiModel) {
            listener.onChannelClicked(view, item, adapterPosition)
        }

        override fun onToggleReminderChannelClicked(item: PlayWidgetMediumChannelUiModel, reminderType: PlayWidgetReminderType) {
            listener.onToggleReminderChannelClicked(item, reminderType, adapterPosition)
        }

        override fun onMenuActionButtonClicked(view: View, item: PlayWidgetMediumChannelUiModel) {
            listener.onMenuActionButtonClicked(view, item, adapterPosition)
        }
    }

    init {
        playWidgetCardChannelMediumView.setListener(widgetCardMediumListener)
    }

    fun bind(item: PlayWidgetMediumChannelUiModel) {
        itemView.addOnImpressionListener(item.impressHolder) {
            listener.onChannelImpressed(itemView, item, adapterPosition)
        }
        playWidgetCardChannelMediumView.setModel(item)
    }

    companion object {
        @LayoutRes val layoutRes = R.layout.item_play_widget_card_channel_medium

        const val KEY_EXTRA_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"
        const val KEY_EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"

        const val KEY_PLAY_WIDGET_REQUEST_CODE = 2567
    }

    interface Listener {

        fun onChannelImpressed(
                view: View,
                item: PlayWidgetMediumChannelUiModel,
                position: Int
        )

        fun onChannelClicked(
                view: View,
                item: PlayWidgetMediumChannelUiModel,
                position: Int
        )

        fun onToggleReminderChannelClicked(
                item: PlayWidgetMediumChannelUiModel,
                reminderType: PlayWidgetReminderType,
                position: Int
        )

        fun onMenuActionButtonClicked(
                view: View,
                item: PlayWidgetMediumChannelUiModel,
                position: Int
        )
    }
}