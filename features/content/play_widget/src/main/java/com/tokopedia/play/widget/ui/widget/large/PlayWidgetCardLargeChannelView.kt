package com.tokopedia.play.widget.ui.widget.large

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.player.PlayVideoPlayer
import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetRatio
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.getWidthAndHeight
import com.tokopedia.play.widget.ui.model.switch
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.play.widget.util.PlayWidgetCompositeTouchDelegate
import com.tokopedia.play_common.util.extension.exhaustive
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.play_common.R as playCommonR
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * @author by astidhiyaa on 11/01/22
 */
class PlayWidgetCardLargeChannelView : FrameLayout, PlayVideoPlayerReceiver {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttrs(attrs)
    }

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
    private val ivGiveaway: ImageView
    private val ivPromoLabel: IconUnify

    private var mPlayer: PlayVideoPlayer? = null
    private var mListener: Listener? = null

    private val compositeTouchDelegate: PlayWidgetCompositeTouchDelegate

    private lateinit var mModel: PlayWidgetChannelUiModel

    private var mRatio = PlayWidgetRatio.Unknown

    init {
        val view = View.inflate(context, R.layout.view_play_widget_card_large_channel, this)
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
        ivGiveaway = view.findViewById(R.id.iv_giveaway)
        ivPromoLabel = llPromoDetail.findViewById(R.id.promo_image)

        compositeTouchDelegate = PlayWidgetCompositeTouchDelegate(view)
        view.touchDelegate = compositeTouchDelegate
    }

    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) return

        val attributeArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.PlayWidgetCardLargeView
        )
        val rawDimensionRatio = attributeArray.getString(R.styleable.PlayWidgetCardLargeView_dimensionRatio).orEmpty()
        mRatio = PlayWidgetRatio.parse(rawDimensionRatio)

        attributeArray.recycle()
    }

    private val playerListener = object : PlayVideoPlayer.VideoPlayerListener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            pvVideo.showWithCondition(isPlaying)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val (newWidthMeasureSpec, newHeightMeasureSpec) = mRatio.getMeasureSpec(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setModel(model: PlayWidgetChannelUiModel) {
        this.mModel = model

        layoutParams = ViewGroup.LayoutParams(
            model.gridType.getWidthAndHeight(context).first,
            model.gridType.getWidthAndHeight(context).second
        )

        thumbnail.setImageUrl(model.video.coverUrl)

        when (model.channelType) {
            PlayWidgetChannelType.Upcoming -> setUpcomingModel()
            else -> setActiveModel(model)
        }

        setPromoType(model.promoType)

        tvTitle.showWithCondition(model.title.isNotEmpty())
        tvAuthor.showWithCondition(model.partner.name.isNotEmpty())

        val startTimeIsShown = model.startTime.isNotEmpty() && model.channelType == PlayWidgetChannelType.Upcoming

        tvStartTime.shouldShowWithAction(startTimeIsShown) {
            tvStartTime.text = model.startTime
        }

        tvAuthor.text = model.partner.name
        tvTitle.text = model.title
        tvTotalView.text = model.totalView.totalViewFmt
        ivGiveaway.showWithCondition(model.hasGame)

        setIconToggleReminder(model.reminderType)
        reminderBadge.setOnClickListener {
            mListener?.onToggleReminderChannelClicked(model, model.reminderType.switch())
        }

        setOnClickListener {
            mListener?.onChannelClicked(it, model)
        }

        ivAction.setOnClickListener {
            mListener?.onMenuActionButtonClicked(it, model)
        }
    }

    private fun setActiveModel(model: PlayWidgetChannelUiModel) {
        val isLiveBadgeShown = model.video.isLive && model.channelType == PlayWidgetChannelType.Live
        ivAction.showWithCondition(model.hasAction)
        liveBadge.showWithCondition(isLiveBadgeShown)
        reminderBadge.gone()
        totalViewBadge.showWithCondition(model.totalView.isVisible)
    }

    private fun setUpcomingModel() {
        ivAction.gone()
        liveBadge.gone()
        reminderBadge.visible()
        totalViewBadge.gone()
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
                llPromoDetail.gone()
                tvOnlyLive.gone()
            }
            is PlayWidgetPromoType.Default -> {
                setPromoLabelIcon(promoType.isRilisanSpesial)
                tvOnlyLive.gone()
                llPromoDetail.visible()

                tvPromoDetail.text = promoType.promoText
            }
            is PlayWidgetPromoType.LiveOnly -> {
                setPromoLabelIcon(promoType.isRilisanSpesial)
                tvOnlyLive.visible()
                llPromoDetail.visible()

                tvPromoDetail.text = promoType.promoText
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setPlayer(null)
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
        )
    }
}
