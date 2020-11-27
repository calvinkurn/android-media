package com.tokopedia.play.widget.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.player.PlayVideoPlayer
import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by mzennis on 26/10/20.
 */
class PlayWidgetCardChannelMediumView : ConstraintLayout, PlayVideoPlayerReceiver {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val thumbnail: AppCompatImageView
    private val pvVideo: PlayerView
    private val reminderBadge: AppCompatImageView
    private val liveBadge: View
    private val totalViewBadge: View
    private val promoBadge: View
    private val tvStartTime: TextView
    private val tvTitle: TextView
    private val tvAuthor: TextView
    private val tvTotalView: TextView

    private var originalReminderState = false

    private var mPlayer: PlayVideoPlayer? = null
    private var mListener: Listener? = null

    private lateinit var mModel: PlayWidgetMediumChannelUiModel

    init {
        val view = View.inflate(context, R.layout.view_play_widget_card_channel_medium, this)
        thumbnail = view.findViewById(R.id.play_widget_thumbnail)
        pvVideo = view.findViewById(R.id.play_widget_player_view)
        reminderBadge = view.findViewById(R.id.play_widget_iv_reminder)
        liveBadge = view.findViewById(R.id.play_widget_badge_live)
        totalViewBadge = view.findViewById(R.id.play_widget_badge_total_view)
        promoBadge = view.findViewById(R.id.play_widget_badge_promo)
        tvStartTime = view.findViewById(R.id.play_widget_channel_date)
        tvTitle = view.findViewById(R.id.play_widget_channel_title)
        tvAuthor = view.findViewById(R.id.play_widget_channel_name)
        tvTotalView = view.findViewById(R.id.viewer)
    }

    private val playerListener = object : PlayVideoPlayer.VideoPlayerListener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) pvVideo.visible() else pvVideo.gone()
        }
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun revertToOriginalReminderState() {
        setIconToggleReminder(originalReminderState)
    }

    fun setTotalView(totalView: String) {
        tvTotalView.text = totalView
    }

    fun setModel(model: PlayWidgetMediumChannelUiModel) {
        this.mModel = model

        thumbnail.loadImage(model.video.coverUrl)

        promoBadge.visibility = if (model.hasPromo) View.VISIBLE else View.GONE
        totalViewBadge.visibility = if (model.totalViewVisible && model.channelType != PlayWidgetChannelType.Upcoming) View.VISIBLE else View.GONE
        liveBadge.visibility = if (model.video.isLive && model.channelType == PlayWidgetChannelType.Live) View.VISIBLE else View.GONE
        reminderBadge.visibility = if (model.channelType == PlayWidgetChannelType.Upcoming) View.VISIBLE else View.GONE

        tvTitle.visibility = if (model.title.isNotEmpty()) View.VISIBLE else View.GONE
        tvAuthor.visibility = if (model.partner.name.isNotEmpty()) View.VISIBLE else View.GONE
        tvStartTime.visibility = if (model.startTime.isNotEmpty() && model.channelType == PlayWidgetChannelType.Upcoming) View.VISIBLE else View.GONE

        tvAuthor.text = model.partner.name
        tvTitle.text = model.title
        tvStartTime.text = model.startTime
        tvTotalView.text = model.totalView

        originalReminderState = model.activeReminder

        setIconToggleReminder(model.activeReminder)
        reminderBadge.setOnClickListener {
            model.activeReminder = !model.activeReminder
            mListener?.onToggleReminderChannelClicked(model, model.activeReminder)
            setIconToggleReminder(model.activeReminder)
        }

        setOnClickListener {
            mListener?.onChannelClicked(it, model)
        }
    }

    private fun setIconToggleReminder(active: Boolean) {
        val drawableIconReminder = if (active) R.drawable.ic_play_reminder else R.drawable.ic_play_reminder_non_active
        reminderBadge.setImageDrawable(
                ContextCompat.getDrawable(context, drawableIconReminder)
        )
    }

    private fun shouldStartVideo() = mModel.channelType == PlayWidgetChannelType.Live
            || mModel.channelType == PlayWidgetChannelType.Vod

    override fun setPlayer(player: PlayVideoPlayer?) {
        mPlayer?.listener = null
        mPlayer = player
        pvVideo.player = player?.getPlayer()
        if (player == null) {
            pvVideo.gone()
        } else {
            if (::mModel.isInitialized && shouldStartVideo()) {
                player.videoUrl = mModel.video.videoUrl
                player.start()
            }
            player.listener = playerListener
        }
    }

    override fun getPlayer(): PlayVideoPlayer? {
        return mPlayer
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setPlayer(null)
    }

    interface Listener {

        fun onChannelClicked(
                view: View,
                item: PlayWidgetMediumChannelUiModel
        )

        fun onToggleReminderChannelClicked(
                item: PlayWidgetMediumChannelUiModel,
                remind: Boolean
        )
    }
}