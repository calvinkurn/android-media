package com.tokopedia.play.widget.ui.adapter.viewholder.medium

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by mzennis on 05/10/20.
 */
class PlayWidgetCardMediumChannelViewHolder(
        itemView: View,
        private val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    private val thumbnail: AppCompatImageView = itemView.findViewById(R.id.play_widget_thumbnail)

    private val videoPlayerView: PlayerView = itemView.findViewById(R.id.play_widget_player_view)

    private val reminderBadge: AppCompatImageView = itemView.findViewById(R.id.play_widget_iv_reminder)
    private val liveBadge: View = itemView.findViewById(R.id.play_widget_badge_live)
    private val totalViewBadge: View = itemView.findViewById(R.id.play_widget_badge_total_view)
    private val promoBadge: View = itemView.findViewById(R.id.play_widget_badge_promo)

    private val tvStartTime: TextView = itemView.findViewById(R.id.play_widget_channel_date)
    private val tvTitle: TextView = itemView.findViewById(R.id.play_widget_channel_title)
    private val tvAuthor: TextView = itemView.findViewById(R.id.play_widget_channel_name)
    private val tvTotalView: TextView = itemView.findViewById(R.id.viewer)

    private var originalReminderState = false

    fun bind(item: PlayWidgetMediumChannelUiModel) {
        setData(item)
        itemView.setOnClickListener {
            if (item.channelType == PlayWidgetChannelType.Live || item.channelType ==  PlayWidgetChannelType.Vod) {
                listener.onCardChannelClick(item.appLink)
            } else {
                RouteManager.route(it.context, item.appLink)
            }
        }
    }

    private fun setData(item: PlayWidgetMediumChannelUiModel) {
        thumbnail.loadImage(item.video.coverUrl)

        promoBadge.visibility = if (item.hasPromo) View.VISIBLE else View.GONE
        totalViewBadge.visibility = if (item.totalViewVisible) View.VISIBLE else View.GONE
        liveBadge.visibility = if (item.video.isLive) View.VISIBLE else View.GONE
        reminderBadge.visibility = if (item.channelType == PlayWidgetChannelType.Upcoming) View.VISIBLE else View.GONE

        tvTitle.visibility = if (item.title.isNotEmpty()) View.VISIBLE else View.GONE
        tvAuthor.visibility = if (item.partner.name.isNotEmpty()) View.VISIBLE else View.GONE
        tvStartTime.visibility = if (item.startTime.isNotEmpty() && item.channelType == PlayWidgetChannelType.Upcoming) View.VISIBLE else View.GONE

        tvAuthor.text = item.partner.name
        tvTitle.text = item.title
        tvStartTime.text = item.startTime
        tvTotalView.text = item.totalView

        originalReminderState = item.activeReminder

        setIconToggleReminder(item.activeReminder)
        reminderBadge.setOnClickListener {
            item.activeReminder = !item.activeReminder
            listener.onToggleReminderClick(item.channelId, item.activeReminder, adapterPosition)
            setIconToggleReminder(item.activeReminder)
        }
    }

    private fun setIconToggleReminder(active: Boolean) {
        val drawableIconReminder = if (active) com.tokopedia.play_common.R.drawable.ic_play_reminder else com.tokopedia.play_common.R.drawable.ic_play_reminder_non_active
        reminderBadge.setImageDrawable(
                ContextCompat.getDrawable(itemView.context, drawableIconReminder)
        )
    }

    fun revertToOriginalReminderState() {
        setIconToggleReminder(originalReminderState)
    }

    fun setTotalView(totalView: String) {
        tvTotalView.text = totalView
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
        fun onCardChannelClick(appLink: String)
        fun onToggleReminderClick(
                channelId: String,
                remind: Boolean,
                position: Int
        )
    }
}