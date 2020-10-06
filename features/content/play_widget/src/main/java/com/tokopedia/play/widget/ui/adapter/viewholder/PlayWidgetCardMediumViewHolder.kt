package com.tokopedia.play.widget.ui.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.adapter.PlayWidgetCardMediumAdapter
import com.tokopedia.play.widget.ui.custom.PlayWidgetVideoView
import com.tokopedia.play.widget.ui.model.PlayWidgetCardUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetCardType


/**
 * Created by mzennis on 05/10/20.
 */
class PlayWidgetCardMediumViewHolder(itemView: View, private val listener: PlayWidgetCardMediumAdapter.PlayWidgetCardMediumListener?) : RecyclerView.ViewHolder(itemView) {

    private val thumbnail: AppCompatImageView = itemView.findViewById(R.id.play_widget_thumbnail)

    private val videoView: PlayWidgetVideoView = itemView.findViewById(R.id.play_widget_player)

    private val reminderBadge: AppCompatImageView = itemView.findViewById(R.id.play_widget_iv_reminder)
    private val liveBadge: View = itemView.findViewById(R.id.play_widget_badge_live)
    private val totalViewBadge: View = itemView.findViewById(R.id.play_widget_badge_total_view)
    private val promoBadge: View = itemView.findViewById(R.id.play_widget_badge_promo)

    private val startTime: TextView = itemView.findViewById(R.id.play_widget_channel_date)
    private val title: TextView = itemView.findViewById(R.id.play_widget_channel_title)
    private val author: TextView = itemView.findViewById(R.id.play_widget_channel_name)
    private val totalView: TextView = itemView.findViewById(R.id.viewer)

    private var widgetType: PlayWidgetCardType = PlayWidgetCardType.Unknown

    fun bind(item: PlayWidgetCardUiModel) {
        thumbnail.loadImage(item.video.coverUrl)

        promoBadge.visibility = if (item.hasPromo) View.VISIBLE else View.GONE
        totalViewBadge.visibility = if (item.totalViewVisible) View.VISIBLE else View.GONE
        liveBadge.visibility = if (item.isLive) View.VISIBLE else View.GONE
        reminderBadge.visibility = if (item.widgetType == PlayWidgetCardType.Upcoming) View.VISIBLE else View.GONE

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
        startTime.visibility = if (item.startTime.isNotEmpty() && item.widgetType == PlayWidgetCardType.Upcoming) View.VISIBLE else View.GONE

        setupListener(item)

        videoView.videoUrl = item.video.videoUrl
        videoView.listener = videoListener

        widgetType = item.widgetType
    }

    fun playVideo() {
        videoView.start()
    }

    fun stopVideo() {
        videoView.stop()
    }

    fun release() {
        videoView.release()
    }

    fun getWidgetType(): PlayWidgetCardType = widgetType

    private val videoListener = object : PlayWidgetVideoView.PlayWidgetVideoListener {
        override fun onPlayerStateChanged(state: PlayWidgetVideoView.PlayWidgetVideoState) {
            videoView.visibility = if (state == PlayWidgetVideoView.PlayWidgetVideoState.Ready) View.VISIBLE else View.INVISIBLE
        }

    }

    private fun setupListener(item: PlayWidgetCardUiModel) {
        if (listener == null) return

        itemView.setOnClickListener {
            listener.onItemClickListener(item)
        }
        itemView.addOnImpressionListener(item) {
            listener.onItemImpressListener(item)
        }
    }
}