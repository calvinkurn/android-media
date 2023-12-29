package com.tokopedia.play.widget.ui.widget.medium

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.player.PlayVideoPlayer
import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.switch
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.play.widget.util.PlayWidgetCompositeTouchDelegate
import com.tokopedia.play_common.util.extension.exhaustive
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.play_common.R as playCommonR
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by kenny.hadisaputra on 24/01/22
 */
class PlayWidgetCardMediumChannelView : FrameLayout, PlayVideoPlayerReceiver {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val thumbnail: ImageUnify
    private val pvVideo: PlayerView
    private val reminderBadge: View
    private val ivReminder: IconUnify
    private val ivAction: IconUnify
    private val liveBadge: View
    private val totalViewBadge: View
    private val tvOnlyLive: TextView
    private val tvPromoDetail: TextView
    private val llPromoDetail: ViewGroup
    private val tvStartTime: TextView
    private val tvTitle: TextView
    private val tvAuthor: TextView
    private val tvTotalView: TextView
    private val llLoadingContainer: LinearLayout
    private val loaderLoading: LoaderUnify
    private val ivGiveaway: ImageView
    private val ivPromoLabel: IconUnify

    private var mPlayer: PlayVideoPlayer? = null
    private var mListener: Listener? = null

    private val compositeTouchDelegate: PlayWidgetCompositeTouchDelegate

    private lateinit var mModel: PlayWidgetChannelUiModel

    init {
        val view = View.inflate(context, R.layout.view_play_widget_card_medium_channel, this)
        thumbnail = view.findViewById(R.id.play_widget_thumbnail)
        pvVideo = view.findViewById(R.id.play_widget_player_view)
        reminderBadge = view.findViewById(R.id.view_reminder)
        ivReminder = view.findViewById(R.id.play_widget_iv_reminder)
        ivAction = view.findViewById(R.id.play_widget_iv_action)
        liveBadge = view.findViewById(R.id.play_widget_badge_live)
        totalViewBadge = view.findViewById(R.id.play_widget_badge_total_view)
        tvOnlyLive = view.findViewById(R.id.tv_only_live)
        tvPromoDetail = view.findViewById(R.id.tv_promo_detail)
        llPromoDetail = view.findViewById(R.id.ll_promo_detail)
        tvStartTime = view.findViewById(R.id.play_widget_channel_date)
        tvTitle = view.findViewById(R.id.play_widget_channel_title)
        tvAuthor = view.findViewById(R.id.play_widget_channel_name)
        tvTotalView = view.findViewById(playCommonR.id.viewer)
        llLoadingContainer = view.findViewById(R.id.ll_loading_container)
        loaderLoading = view.findViewById(R.id.loader_loading)
        ivGiveaway = view.findViewById(R.id.iv_giveaway)
        ivPromoLabel = llPromoDetail.findViewById(R.id.promo_image)

        compositeTouchDelegate = PlayWidgetCompositeTouchDelegate(view)
        view.touchDelegate = compositeTouchDelegate

        setupView(view)
    }

    private val playerListener = object : PlayVideoPlayer.VideoPlayerListener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) pvVideo.visible() else pvVideo.gone()
        }
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setData(data: PlayWidgetChannelUiModel) {
        mModel = data

        thumbnail.setImageUrl(data.video.coverUrl)

        when (data.channelType) {
            PlayWidgetChannelType.Deleting -> setDeletingModel(data)
            PlayWidgetChannelType.Upcoming -> setUpcomingModel()
            else -> setActiveModel(data)
        }

        setPromoType(data.promoType)

        tvTitle.visibility = if (data.title.isNotEmpty()) View.VISIBLE else View.GONE
        tvAuthor.visibility = if (data.partner.name.isNotEmpty()) View.VISIBLE else View.GONE
        tvStartTime.visibility = if (data.startTime.isNotEmpty() && data.channelType == PlayWidgetChannelType.Upcoming) {
            View.VISIBLE
        } else {
            View.GONE
        }

        tvAuthor.text = data.partner.name
        tvTitle.text = data.title
        tvStartTime.text = data.startTime
        tvTotalView.text = data.totalView.totalViewFmt
        ivGiveaway.visibility = if (data.hasGame) View.VISIBLE else View.GONE

        setIconToggleReminder(data.reminderType)
        reminderBadge.setOnClickListener {
            mListener?.onToggleReminderChannelClicked(data, data.reminderType.switch())
        }

        setOnClickListener {
            mListener?.onChannelClicked(it, data)
        }

        ivAction.setOnClickListener {
            mListener?.onMenuActionButtonClicked(this, data)
        }
    }

    private fun setActiveModel(model: PlayWidgetChannelUiModel) {
        ivAction.visibility = if (model.hasAction) View.VISIBLE else View.GONE
        liveBadge.visibility = if (model.video.isLive && model.channelType == PlayWidgetChannelType.Live) {
            View.VISIBLE
        } else {
            View.GONE
        }
        reminderBadge.visibility = View.GONE
        totalViewBadge.visibility = if (model.totalView.isVisible) View.VISIBLE else View.GONE
        llLoadingContainer.visibility = View.GONE
    }

    private fun setUpcomingModel() {
        ivAction.visibility = View.GONE
        liveBadge.visibility = View.GONE
        reminderBadge.visibility = View.VISIBLE
        totalViewBadge.visibility = View.GONE
        llLoadingContainer.visibility = View.GONE
    }

    private fun setDeletingModel(model: PlayWidgetChannelUiModel) {
        ivAction.visibility = View.GONE
        liveBadge.visibility = if (model.video.isLive && model.channelType == PlayWidgetChannelType.Live) {
            View.VISIBLE
        } else {
            View.GONE
        }
        reminderBadge.visibility = View.GONE
        totalViewBadge.visibility = if (model.totalView.isVisible) View.VISIBLE else View.GONE
        llLoadingContainer.visibility = View.VISIBLE
    }

    private fun setIconToggleReminder(reminderType: PlayWidgetReminderType) {
        when (reminderType) {
            PlayWidgetReminderType.Reminded -> ivReminder.setImage(newIconId = IconUnify.BELL_FILLED, newDarkEnable = MethodChecker.getColor(context, unifyR.color.Unify_GN500), newLightEnable = MethodChecker.getColor(context, unifyR.color.Unify_GN500))
            PlayWidgetReminderType.NotReminded -> ivReminder.setImage(newIconId = IconUnify.BELL, newDarkEnable = MethodChecker.getColor(context, unifyR.color.Unify_Static_White), newLightEnable = MethodChecker.getColor(context, unifyR.color.Unify_Static_White))
        }
    }

    private fun setPromoType(promoType: PlayWidgetPromoType) {
        when (promoType) {
            PlayWidgetPromoType.NoPromo, PlayWidgetPromoType.Unknown -> {
                llPromoDetail.visibility = View.GONE
                tvOnlyLive.visibility = View.GONE
            }
            is PlayWidgetPromoType.Default -> {
                setPromoLabelIcon(promoType.isRilisanSpesial)
                tvOnlyLive.visibility = View.GONE

                tvPromoDetail.text = promoType.promoText
                llPromoDetail.visibility = View.VISIBLE
            }
            is PlayWidgetPromoType.LiveOnly -> {
                setPromoLabelIcon(promoType.isRilisanSpesial)
                tvOnlyLive.visibility = View.VISIBLE

                tvPromoDetail.text = promoType.promoText
                llPromoDetail.visibility = View.VISIBLE
            }
        }.exhaustive
    }

    private fun setPromoLabelIcon(isRilisanSpesial: Boolean) {
        if (isRilisanSpesial) {
            ivPromoLabel.setImage(newIconId = IconUnify.ROCKET, newLightEnable = MethodChecker.getColor(context, unifyR.color.Unify_Static_White), newDarkEnable = MethodChecker.getColor(context, unifyR.color.Unify_Static_White))
        } else {
            ivPromoLabel.setImage(newIconId = IconUnify.PROMO, newLightEnable = MethodChecker.getColor(context, unifyR.color.Unify_Static_White), newDarkEnable = MethodChecker.getColor(context, unifyR.color.Unify_Static_White))
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
                player.isLive = mModel.video.isLive
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
            item: PlayWidgetChannelUiModel
        )

        fun onToggleReminderChannelClicked(
            item: PlayWidgetChannelUiModel,
            reminderType: PlayWidgetReminderType
        )

        fun onMenuActionButtonClicked(
            view: View,
            item: PlayWidgetChannelUiModel
        ) {}
    }
}
