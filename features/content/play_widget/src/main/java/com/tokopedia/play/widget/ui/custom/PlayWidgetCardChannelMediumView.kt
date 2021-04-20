package com.tokopedia.play.widget.ui.custom

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.TouchDelegate
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.player.PlayVideoPlayer
import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.switch
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.util.PlayWidgetCompositeTouchDelegate
import com.tokopedia.unifycomponents.LoaderUnify


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
    private val ivAction: AppCompatImageView
    private val liveBadge: View
    private val totalViewBadge: View
    private val promoBadge: View
    private val tvStartTime: TextView
    private val tvTitle: TextView
    private val tvAuthor: TextView
    private val tvTotalView: TextView
    private val llLoadingContainer: LinearLayout
    private val loaderLoading: LoaderUnify

    private var mPlayer: PlayVideoPlayer? = null
    private var mListener: Listener? = null

    private val compositeTouchDelegate: PlayWidgetCompositeTouchDelegate

    private lateinit var mModel: PlayWidgetMediumChannelUiModel

    init {
        val view = View.inflate(context, R.layout.view_play_widget_card_channel_medium, this)
        thumbnail = view.findViewById(R.id.play_widget_thumbnail)
        pvVideo = view.findViewById(R.id.play_widget_player_view)
        reminderBadge = view.findViewById(R.id.play_widget_iv_reminder)
        ivAction = view.findViewById(R.id.play_widget_iv_action)
        liveBadge = view.findViewById(R.id.play_widget_badge_live)
        totalViewBadge = view.findViewById(R.id.play_widget_badge_total_view)
        promoBadge = view.findViewById(R.id.play_widget_badge_promo)
        tvStartTime = view.findViewById(R.id.play_widget_channel_date)
        tvTitle = view.findViewById(R.id.play_widget_channel_title)
        tvAuthor = view.findViewById(R.id.play_widget_channel_name)
        tvTotalView = view.findViewById(R.id.viewer)
        llLoadingContainer = view.findViewById(R.id.ll_loading_container)
        loaderLoading = view.findViewById(R.id.loader_loading)

        compositeTouchDelegate = PlayWidgetCompositeTouchDelegate(view)
        view.touchDelegate = compositeTouchDelegate

        setupView(view)
    }

    private val playerListener = object : PlayVideoPlayer.VideoPlayerListener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) pvVideo.visible() else pvVideo.gone()
        }
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setModel(model: PlayWidgetMediumChannelUiModel) {
        this.mModel = model

        thumbnail.loadImage(model.video.coverUrl)

        when (model.channelType) {
            PlayWidgetChannelType.Deleting -> setDeletingModel(model)
            PlayWidgetChannelType.Upcoming -> setUpcomingModel(model)
            else -> setActiveModel(model)
        }

        tvTitle.visibility = if (model.title.isNotEmpty()) View.VISIBLE else View.GONE
        tvAuthor.visibility = if (model.partner.name.isNotEmpty()) View.VISIBLE else View.GONE
        tvStartTime.visibility = if (model.startTime.isNotEmpty() && model.channelType == PlayWidgetChannelType.Upcoming) View.VISIBLE else View.GONE

        tvAuthor.text = model.partner.name
        tvTitle.text = model.title
        tvStartTime.text = model.startTime
        tvTotalView.text = model.totalView

        setIconToggleReminder(model.reminderType)
        reminderBadge.setOnClickListener {
            mListener?.onToggleReminderChannelClicked(model, model.reminderType.switch())
        }

        setOnClickListener {
            mListener?.onChannelClicked(it, model)
        }

        ivAction.setOnClickListener {
            mListener?.onMenuActionButtonClicked(this, model)
        }
    }

    private fun setActiveModel(model: PlayWidgetMediumChannelUiModel) {
        ivAction.visibility = if (model.hasAction) View.VISIBLE else View.GONE
        liveBadge.visibility = if (model.video.isLive && model.channelType == PlayWidgetChannelType.Live) View.VISIBLE else View.GONE
        reminderBadge.visibility = View.GONE
        totalViewBadge.visibility = if (model.totalViewVisible) View.VISIBLE else View.GONE
        promoBadge.visibility = if (model.hasPromo) View.VISIBLE else View.GONE
        llLoadingContainer.visibility = View.GONE
    }

    private fun setUpcomingModel(model: PlayWidgetMediumChannelUiModel) {
        ivAction.visibility = View.GONE
        liveBadge.visibility = View.GONE
        reminderBadge.visibility = View.VISIBLE
        totalViewBadge.visibility = View.GONE
        promoBadge.visibility = if (model.hasPromo) View.VISIBLE else View.GONE
        llLoadingContainer.visibility = View.GONE
    }

    private fun setDeletingModel(model: PlayWidgetMediumChannelUiModel) {
        ivAction.visibility = View.GONE
        liveBadge.visibility = if (model.video.isLive && model.channelType == PlayWidgetChannelType.Live) View.VISIBLE else View.GONE
        reminderBadge.visibility = View.GONE
        totalViewBadge.visibility = if (model.totalViewVisible) View.VISIBLE else View.GONE
        promoBadge.visibility = if (model.hasPromo) View.VISIBLE else View.GONE
        llLoadingContainer.visibility = View.VISIBLE
    }

    private fun setIconToggleReminder(reminderType: PlayWidgetReminderType) {
        val drawableIconReminder = when (reminderType) {
            PlayWidgetReminderType.Remind -> R.drawable.ic_play_reminder
            PlayWidgetReminderType.UnRemind ->  R.drawable.ic_play_reminder_non_active
        }
        reminderBadge.setImageDrawable(
                ContextCompat.getDrawable(context, drawableIconReminder)
        )
    }

    override fun setPlayer(player: PlayVideoPlayer?) {
        mPlayer?.listener = null
        mPlayer = player
        pvVideo.player = player?.getPlayer()
        if (player == null) {
            pvVideo.gone()
        } else {
            if (::mModel.isInitialized) {
                player.videoUrl = mModel.video.videoUrl
                player.start()
            }
            player.listener = playerListener
        }
    }

    override fun getPlayer(): PlayVideoPlayer? {
        return mPlayer
    }

    override fun isPlayable(): Boolean {
        return mModel.channelType == PlayWidgetChannelType.Live ||
                mModel.channelType == PlayWidgetChannelType.Vod
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        loaderLoading.visibility = loaderLoading.visibility
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setPlayer(null)
    }

    private fun setupView(view: View) {
        ivAction.addOneTimeGlobalLayoutListener {
            val rect = Rect()
            ivAction.getHitRect(rect)
            rect.top = view.top
            rect.right = view.right
            compositeTouchDelegate.addDelegate(TouchDelegate(rect, ivAction))
        }
    }

    interface Listener {

        fun onChannelClicked(
                view: View,
                item: PlayWidgetMediumChannelUiModel
        )

        fun onToggleReminderChannelClicked(
                item: PlayWidgetMediumChannelUiModel,
                reminderType: PlayWidgetReminderType
        )

        fun onMenuActionButtonClicked(
                view: View,
                item: PlayWidgetMediumChannelUiModel
        )
    }
}