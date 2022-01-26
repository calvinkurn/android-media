package com.tokopedia.play.widget.ui.widget.small

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.player.PlayVideoPlayer
import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalView
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Created by kenny.hadisaputra on 24/01/22
 */
class PlayWidgetCardSmallChannelView : FrameLayout, PlayVideoPlayerReceiver {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val flBorder: FrameLayout
    private val ivCover: ImageUnify
    private val pvVideo: PlayerView
    private val llTotalView: LinearLayout
    private val tvTotalView: TextView
    private val tvLiveBadge: TextView
    private val ivTotalView: ImageView
    private val tvTitle: TextView
    private val tvUpcoming: TextView
    private val tvContextualInfo: TextView
    private val ivGiveaway: ImageView

    private var mListener: Listener? = null

    private var mPlayer: PlayVideoPlayer? = null
    private lateinit var mModel: PlayWidgetChannelUiModel

    init {
        val view = View.inflate(context, R.layout.view_play_widget_card_small_channel, this)
        flBorder = view.findViewById(R.id.fl_border)
        ivCover = view.findViewById(R.id.iv_cover)
        pvVideo = view.findViewById(R.id.pv_video)
        llTotalView = view.findViewById(R.id.ll_total_view)
        tvTotalView = view.findViewById(R.id.tv_total_view)
        tvLiveBadge = view.findViewById(R.id.tv_live_badge)
        ivTotalView = view.findViewById(R.id.iv_total_view)
        tvTitle = view.findViewById(R.id.tv_title)
        tvUpcoming = view.findViewById(R.id.tv_upcoming)
        tvContextualInfo = view.findViewById(R.id.tv_contextual_info)
        ivGiveaway = view.findViewById(R.id.iv_giveaway)
    }

    private val playerListener = object : PlayVideoPlayer.VideoPlayerListener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) pvVideo.visible() else pvVideo.gone()
        }
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
                player.shouldCache = !mModel.video.isLive
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setPlayer(null)
    }

    fun setData(data: PlayWidgetChannelUiModel) {
        mModel = data

        ivCover.setImageUrl(data.video.coverUrl)

        handleType(data.channelType)
        handleTotalView(data.channelType, data.totalView)
        handleGiveaway(data.hasGiveaway)

        tvTitle.text = data.title
        tvUpcoming.text = data.startTime

        tvContextualInfo.text = data.promoType.promoText

        when {
            data.video.isLive -> flBorder.setBackgroundResource(R.drawable.bg_play_widget_small_live_border)
            data.hasPromo -> flBorder.setBackgroundResource(R.drawable.bg_play_widget_small_promo_border)
            else -> flBorder.setBackgroundResource(R.drawable.bg_play_widget_small_default_border)
        }

        when {
            data.hasPromo -> {
                tvContextualInfo.setBackgroundResource(
                    if (data.video.isLive) R.drawable.bg_play_widget_small_live_context
                    else R.drawable.bg_play_widget_small_promo_context
                )
                tvContextualInfo.visible()
            }
            else -> {
                tvContextualInfo.invisible()
            }
        }

        setOnClickListener {
            mListener?.onChannelClicked(this, data)
        }
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    private fun handleType(type: PlayWidgetChannelType) {
        when (type) {
            PlayWidgetChannelType.Live -> {
                tvLiveBadge.visible()
                ivTotalView.gone()
                tvUpcoming.gone()
            }
            PlayWidgetChannelType.Vod -> {
                ivTotalView.visible()
                tvLiveBadge.gone()
                tvUpcoming.gone()
            }
            PlayWidgetChannelType.Upcoming -> {
                ivTotalView.gone()
                tvLiveBadge.gone()
                tvUpcoming.visible()
            }
            else -> {}
        }
    }

    private fun handleTotalView(type: PlayWidgetChannelType, totalView: PlayWidgetTotalView) {
        if (type == PlayWidgetChannelType.Upcoming || type == PlayWidgetChannelType.Unknown) llTotalView.gone()
        else if (totalView.isVisible) {
            llTotalView.visible()
            tvTotalView.text = totalView.totalViewFmt
        }
        else llTotalView.gone()
    }

    private fun handleGiveaway(hasGiveaway: Boolean) {
        if(hasGiveaway) ivGiveaway.visible()
        else ivGiveaway.gone()
    }

    interface Listener {

        fun onChannelClicked(
            view: PlayWidgetCardSmallChannelView,
            model: PlayWidgetChannelUiModel
        )
    }
}