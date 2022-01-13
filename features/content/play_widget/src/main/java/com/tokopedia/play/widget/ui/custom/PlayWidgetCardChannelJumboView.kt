package com.tokopedia.play.widget.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.player.PlayVideoPlayer
import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver
import com.tokopedia.play.widget.ui.model.PlayWidgetJumboChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.switch
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.play.widget.util.PlayWidgetCompositeTouchDelegate
import com.tokopedia.play_common.util.extension.exhaustive
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify

/**
 * @author by astidhiyaa on 12/01/22
 */
class PlayWidgetCardChannelJumboView : ConstraintLayout, PlayVideoPlayerReceiver {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val thumbnail: ImageUnify
    private val pvVideo: PlayerView
    private val reminderBadge: View
    private val ivReminder: IconUnify
    private val liveBadge: View
    private val totalViewBadge: View
    private val tvOnlyLive: TextView
    private val tvPromoDetail: TextView
    private val llPromoDetail: ViewGroup
    private val tvStartTime
            : TextView
    private val tvTitle: TextView
    private val tvAuthor: TextView
    private val tvTotalView: TextView
    private val llLoadingContainer: LinearLayout
    private val loaderLoading: LoaderUnify
    private val ivGiveaway: ImageView

    private var mPlayer: PlayVideoPlayer? = null
    private var mListener: Listener? = null

    private val compositeTouchDelegate: PlayWidgetCompositeTouchDelegate

    private lateinit var mModel: PlayWidgetJumboChannelUiModel

    init {
        val view = View.inflate(context, R.layout.view_play_widget_card_channel_jumbo, this)
        thumbnail = view.findViewById(R.id.play_widget_thumbnail)
        pvVideo = view.findViewById(R.id.play_widget_player_view)
        reminderBadge = view.findViewById(R.id.view_reminder)
        ivReminder = view.findViewById(R.id.play_widget_iv_reminder)
        liveBadge = view.findViewById(R.id.play_widget_badge_live)
        totalViewBadge = view.findViewById(R.id.play_widget_badge_total_view)
        tvOnlyLive = view.findViewById(R.id.tv_only_live)
        tvPromoDetail = view.findViewById(R.id.tv_promo_detail)
        llPromoDetail = view.findViewById(R.id.ll_promo_detail)
        tvStartTime = view.findViewById(R.id.play_widget_channel_date)
        tvTitle = view.findViewById(R.id.play_widget_channel_title)
        tvAuthor = view.findViewById(R.id.play_widget_channel_name)
        tvTotalView = view.findViewById(R.id.viewer)
        llLoadingContainer = view.findViewById(R.id.ll_loading_container)
        loaderLoading = view.findViewById(R.id.loader_loading)
        ivGiveaway = view.findViewById(R.id.iv_giveaway)

        compositeTouchDelegate = PlayWidgetCompositeTouchDelegate(view)
        view.touchDelegate = compositeTouchDelegate
    }

    private val playerListener = object : PlayVideoPlayer.VideoPlayerListener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            pvVideo.showWithCondition(isPlaying)
        }
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setModel(model: PlayWidgetJumboChannelUiModel) {
        this.mModel = model

        thumbnail.setImageUrl(model.video.coverUrl)

        when (model.channelType) {
            PlayWidgetChannelType.Deleting -> setDeletingModel(model)
            PlayWidgetChannelType.Upcoming -> setUpcomingModel()
            else -> setActiveModel(model)
        }

        setPromoType(model.promoType)

        tvTitle.showWithCondition(model.title.isNotEmpty())
        tvAuthor.showWithCondition(model.partner.name.isNotEmpty())

        val startTimeIsShown = model.startTime.isNotEmpty() && model.channelType == PlayWidgetChannelType.Upcoming

        tvStartTime.shouldShowWithAction(startTimeIsShown){
            tvStartTime.text = model.startTime
        }

        tvAuthor.text = model.partner.name
        tvTitle.text = model.title
        tvTotalView.text = model.totalView
        ivGiveaway.showWithCondition(model.hasGiveaway)

        setIconToggleReminder(model.reminderType)
        reminderBadge.setOnClickListener {
            mListener?.onToggleReminderChannelClicked(model, model.reminderType.switch())
        }

        setOnClickListener {
            mListener?.onChannelClicked(it, model)
        }
    }

    private fun setActiveModel(model: PlayWidgetJumboChannelUiModel) {
        val isLiveBadgeShown = model.video.isLive && model.channelType == PlayWidgetChannelType.Live
        liveBadge.showWithCondition(isLiveBadgeShown)
        reminderBadge.gone()
        totalViewBadge.showWithCondition(model.totalViewVisible)
        llLoadingContainer.gone()
    }

    private fun setUpcomingModel() {
        liveBadge.gone()
        reminderBadge.visible()
        totalViewBadge.gone()
        llLoadingContainer.gone()
    }

    private fun setDeletingModel(model: PlayWidgetJumboChannelUiModel) {
        val isLiveBadgeShown = model.video.isLive && model.channelType == PlayWidgetChannelType.Live
        liveBadge.showWithCondition(isLiveBadgeShown)
        reminderBadge.gone()
        totalViewBadge.showWithCondition(model.totalViewVisible)
        llLoadingContainer.visible()
    }

    private fun setIconToggleReminder(reminderType: PlayWidgetReminderType) {
        val iconId = when (reminderType) {
            PlayWidgetReminderType.Reminded -> IconUnify.BELL_FILLED
            PlayWidgetReminderType.NotReminded -> IconUnify.BELL
        }
        ivReminder.setImage(newIconId = iconId)
    }

    private fun setPromoType(promoType: PlayWidgetPromoType) {
        when (promoType) {
            PlayWidgetPromoType.NoPromo, PlayWidgetPromoType.Unknown -> {
                llPromoDetail.gone()
                tvOnlyLive.gone()
            }
            is PlayWidgetPromoType.Default -> {
                tvOnlyLive.gone()
                llPromoDetail.visible()

                tvPromoDetail.text = promoType.promoText
            }
            is PlayWidgetPromoType.LiveOnly -> {
                tvOnlyLive.visible()
                llPromoDetail.visible()

                tvPromoDetail.text = promoType.promoText
            }
        }.exhaustive
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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        loaderLoading.visibility = loaderLoading.visibility
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setPlayer(null)
    }

    interface Listener {
        fun onChannelClicked(
            view: View,
            item: PlayWidgetJumboChannelUiModel
        )

        fun onToggleReminderChannelClicked(
            item: PlayWidgetJumboChannelUiModel,
            reminderType: PlayWidgetReminderType
        )
    }
}