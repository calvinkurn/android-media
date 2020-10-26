package com.tokopedia.play.widget.ui.adapter.viewholder.medium

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.custom.PlayWidgetCardChannelMediumView
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

        override fun onChannelClicked(appLink: String) {
            listener.onChannelClicked(appLink)
        }

        override fun onToggleReminderClicked(channelId: String, remind: Boolean) {
            listener.onToggleReminderClicked(channelId, remind, adapterPosition)
        }
    }

    init {
        playWidgetCardChannelMediumView.setListener(widgetCardMediumListener)
    }

    fun bind(item: PlayWidgetMediumChannelUiModel) {
        playWidgetCardChannelMediumView.setModel(item)
    }

    fun revertToOriginalReminderState() {
        playWidgetCardChannelMediumView.revertToOriginalReminderState()
    }

    fun setTotalView(totalView: String) {
        playWidgetCardChannelMediumView.setTotalView(totalView)
    }

    companion object {
        @LayoutRes val layoutRes = R.layout.item_play_widget_card_channel_medium

        const val KEY_CHANNEL_REMINDER = "channel_reminder"
        const val KEY_CHANNEL_TOTAL_VIEW = "channel_total_view"

        const val KEY_EXTRA_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"
        const val KEY_EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"

        const val KEY_PLAY_WIDGET_REQUEST_CODE = 2567
    }

    interface Listener {

        fun onChannelClicked(appLink: String)

        fun onToggleReminderClicked(
                channelId: String,
                remind: Boolean,
                position: Int
        )
    }
}