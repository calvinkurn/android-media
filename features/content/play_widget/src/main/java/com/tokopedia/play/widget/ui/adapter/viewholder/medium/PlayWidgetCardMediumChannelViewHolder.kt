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
import com.tokopedia.play.widget.player.PlayVideoPlayer
import com.tokopedia.play.widget.ui.adapter.PlayWidgetCardMediumAdapter
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by mzennis on 05/10/20.
 */
class PlayWidgetCardMediumChannelViewHolder(
        itemView: View,
        private val cardMediumListener: PlayWidgetCardMediumAdapter.CardMediumListener
) : RecyclerView.ViewHolder(itemView) {

    private val thumbnail: AppCompatImageView = itemView.findViewById(R.id.play_widget_thumbnail)

    private val videoPlayerView: PlayerView = itemView.findViewById(R.id.play_widget_player_view)

    private val reminderBadge: AppCompatImageView = itemView.findViewById(R.id.play_widget_iv_reminder)
    private val liveBadge: View = itemView.findViewById(R.id.play_widget_badge_live)
    private val totalViewBadge: View = itemView.findViewById(R.id.play_widget_badge_total_view)
    private val promoBadge: View = itemView.findViewById(R.id.play_widget_badge_promo)

    private val startTime: TextView = itemView.findViewById(R.id.play_widget_channel_date)
    private val title: TextView = itemView.findViewById(R.id.play_widget_channel_title)
    private val author: TextView = itemView.findViewById(R.id.play_widget_channel_name)
    private val totalView: TextView = itemView.findViewById(R.id.viewer)

    private var channelType: PlayWidgetChannelType = PlayWidgetChannelType.Unknown

    private var videoUrl: String = ""

    private val videoPlayerListener = object : PlayVideoPlayer.VideoPlayerListener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            videoPlayerView.visibility = if (isPlaying) View.VISIBLE else View.INVISIBLE
        }
    }

    fun bind(item: PlayWidgetMediumChannelUiModel) {
        setData(item)
        itemView.setOnClickListener {
            RouteManager.route(it.context, item.appLink)
        }

//        itemView.setOnClickListener {
//            cardMediumListener.onCardClicked(item, adapterPosition)
//        }
//        itemView.addOnImpressionListener(item.impress) {
//            cardMediumListener.onCardVisible(item, adapterPosition)
//        }

        channelType = item.channelType
    }

    private fun setData(item: PlayWidgetMediumChannelUiModel) {
        thumbnail.loadImage(item.video.coverUrl)

        promoBadge.visibility = if (item.hasPromo) View.VISIBLE else View.GONE
        totalViewBadge.visibility = if (item.totalViewVisible) View.VISIBLE else View.GONE
        liveBadge.visibility = if (item.video.isLive) View.VISIBLE else View.GONE
        reminderBadge.visibility = if (item.channelType == PlayWidgetChannelType.Upcoming) View.VISIBLE else View.GONE

        val iconReminder = if (item.activeReminder) com.tokopedia.play_common.R.drawable.ic_play_reminder else com.tokopedia.play_common.R.drawable.ic_play_reminder_non_active
        reminderBadge.setImageDrawable(
                ContextCompat.getDrawable(itemView.context, iconReminder)
        )

        author.text = item.partner.name
        title.text = item.title
        startTime.text = item.startTime
        totalView.text = item.totalView

        title.visibility = if (item.title.isNotEmpty()) View.VISIBLE else View.GONE
        author.visibility = if (item.partner.name.isNotEmpty()) View.VISIBLE else View.GONE
        startTime.visibility = if (item.startTime.isNotEmpty() && item.channelType == PlayWidgetChannelType.Upcoming) View.VISIBLE else View.GONE

        videoUrl = item.video.videoUrl
    }

    fun setPlayer(playVideoPlayer: PlayVideoPlayer) {
        playVideoPlayer.videoUrl = videoUrl
        playVideoPlayer.listener = videoPlayerListener
        videoPlayerView.player = playVideoPlayer.getPlayer()
    }

    fun getPlayer(): PlayVideoPlayer? {
        return videoPlayerView.player as? PlayVideoPlayer
    }

    fun getChannelType(): PlayWidgetChannelType = channelType

    companion object {
        @LayoutRes val layoutRes = R.layout.item_play_widget_card_channel_medium
    }
}