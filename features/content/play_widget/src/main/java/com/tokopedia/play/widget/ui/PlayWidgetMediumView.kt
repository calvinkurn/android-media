package com.tokopedia.play.widget.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.toDp
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.analytic.medium.PlayWidgetMediumAnalyticListener
import com.tokopedia.play.widget.ui.itemdecoration.PlayWidgetCardMediumItemDecoration
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetMediumListener
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.snaphelper.PlayWidgetSnapHelper
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.widget.medium.adapter.PlayWidgetMediumAdapter
import com.tokopedia.play.widget.ui.widget.medium.adapter.PlayWidgetMediumViewHolder
import com.tokopedia.play.widget.ui.widget.medium.model.PlayWidgetOverlayUiModel
import com.tokopedia.play_common.util.blur.ImageBlurUtil
import com.tokopedia.play_common.view.loadImage
import com.tokopedia.play_common.view.setGradientBackground
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.abs


/**
 * Created by mzennis on 06/10/20.
 */
class PlayWidgetMediumView : ConstraintLayout, IPlayWidgetView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val title: Typography
    private val actionTitle: TextView

    private val itemContainer: FrameLayout
    private val overlayBackground: AppCompatImageView
    private val overlayImage: AppCompatImageView
    private val topContainer: ConstraintLayout

    private val recyclerViewItem: RecyclerView

    private val snapHelper: SnapHelper = PlayWidgetSnapHelper(context)

    private var mWidgetListener: PlayWidgetMediumListener? = null
    private var mWidgetInternalListener: PlayWidgetInternalListener? = null
    private var mAnalyticListener: PlayWidgetMediumAnalyticListener? = null

    private var overlayImpressHolder = ImpressHolder()

    private val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

    private val cardOverlayListener = object : PlayWidgetMediumViewHolder.Overlay.Listener {
        override fun onOverlayImpressed(view: View, position: Int) {
            mAnalyticListener?.onImpressOverlayCard(
                view = this@PlayWidgetMediumView,
                channelPositionInList = position,
                item = mModel.background,
            )
        }

        override fun onOverlayClicked(view: View, position: Int) {
            mAnalyticListener?.onClickOverlayCard(
                view = this@PlayWidgetMediumView,
                channelPositionInList = position,
                item = mModel.background,
            )

            if (mModel.background.overlayImageAppLink.isNotBlank()) {
                RouteManager.route(context, mModel.background.overlayImageAppLink)
            }
        }
    }

    private val cardChannelListener = object : PlayWidgetMediumViewHolder.Channel.Listener {

        override fun onChannelImpressed(view: View, item: PlayWidgetChannelUiModel, position: Int) {
            mAnalyticListener?.onImpressChannelCard(
                view = this@PlayWidgetMediumView,
                item = item,
                channelPositionInList = position,
                isAutoPlay = mIsAutoPlay
            )
        }

        override fun onChannelClicked(view: View, item: PlayWidgetChannelUiModel, position: Int) {
            mAnalyticListener?.onClickChannelCard(
                view = this@PlayWidgetMediumView,
                item = item,
                channelPositionInList = position,
                isAutoPlay = mIsAutoPlay
            )

            if (mWidgetListener != null
                    && (item.channelType == PlayWidgetChannelType.Live
                            || item.channelType == PlayWidgetChannelType.Vod
                            || item.channelType == PlayWidgetChannelType.Upcoming
                            || GlobalConfig.isSellerApp())) {
                mWidgetListener?.onWidgetOpenAppLink(view, item.appLink)
            } else {
                RouteManager.route(context, item.appLink)
            }
        }

        override fun onToggleReminderChannelClicked(item: PlayWidgetChannelUiModel, reminderType: PlayWidgetReminderType, position: Int) {
            mAnalyticListener?.onClickToggleReminderChannel(this@PlayWidgetMediumView, item, position, reminderType.reminded)
            mWidgetListener?.onToggleReminderClicked(this@PlayWidgetMediumView, item.channelId, reminderType, position)
        }

        override fun onMenuActionButtonClicked(view: View, item: PlayWidgetChannelUiModel, position: Int) {
            mAnalyticListener?.onClickMenuActionChannel(this@PlayWidgetMediumView, item, position)
            mWidgetListener?.onMenuActionButtonClicked(this@PlayWidgetMediumView, item, position)
        }
    }

    private val cardBannerListener = object : PlayWidgetMediumViewHolder.Banner.Listener {

        override fun onBannerClicked(view: View, item: PlayWidgetBannerUiModel, position: Int) {
            mAnalyticListener?.onClickBannerCard(this@PlayWidgetMediumView, item, position)
        }
    }

    private val cardTranscodeListener = object : PlayWidgetMediumViewHolder.Transcode.Listener {

        override fun onFailedTranscodingChannelDeleteButtonClicked(view: View, item: PlayWidgetChannelUiModel, position: Int) {
            mAnalyticListener?.onClickDeleteChannel(this@PlayWidgetMediumView, item, position)
            mWidgetListener?.onDeleteFailedTranscodingChannel(this@PlayWidgetMediumView, item.channelId)
        }
    }

    private val adapter = PlayWidgetMediumAdapter(
        imageBlurUtil = ImageBlurUtil(context),
        cardOverlayListener = cardOverlayListener,
        cardChannelListener = cardChannelListener,
        cardBannerListener = cardBannerListener,
        cardTranscodeListener = cardTranscodeListener,
    )

    private var mIsAutoPlay: Boolean = false
    private var mLastOverlayImageUrl: String? = null

    private val spacing16 by lazy { resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4).toDp().toInt() }

    private var mModel: PlayWidgetUiModel = PlayWidgetUiModel.Empty

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_play_widget_medium, this)

        topContainer = view.findViewById(R.id.cl_top)
        title = view.findViewById(R.id.play_widget_medium_title)
        actionTitle = view.findViewById(R.id.play_widget_medium_action)

        itemContainer = view.findViewById(R.id.play_widget_container)
        overlayBackground = view.findViewById(R.id.play_widget_overlay_bg)
        overlayImage = view.findViewById(R.id.play_widget_overlay_image)

        recyclerViewItem = view.findViewById(R.id.play_widget_recycler_view)

        setupView(view)
    }

    override fun setWidgetInternalListener(listener: PlayWidgetInternalListener?) {
        mWidgetInternalListener = listener
    }

    fun setWidgetListener(listener: PlayWidgetMediumListener?) {
        mWidgetListener = listener
    }

    fun setAnalyticListener(listener: PlayWidgetMediumAnalyticListener?) {
        mAnalyticListener = listener
    }

    /**
     * Setup view
     */
    private fun setupView(view: View) {
        recyclerViewItem.addItemDecoration(PlayWidgetCardMediumItemDecoration(context))
        recyclerViewItem.layoutManager = layoutManager
        recyclerViewItem.adapter = adapter

        snapHelper.attachToRecyclerView(recyclerViewItem)

        recyclerViewItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.findFirstVisibleItemPosition() != 0) return

                val firstView = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition())
                firstView?.let {
                    val distanceFromLeft = it.left
                    val translateX = distanceFromLeft * 0.2f
                    overlayImage.translationX = translateX

                    if (distanceFromLeft <= 0) {
                        val itemSize = it.width.toFloat()
                        val alpha = (abs(distanceFromLeft).toFloat() / itemSize * 0.80f)
                        overlayImage.alpha = 1 - alpha
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mWidgetInternalListener?.onWidgetCardsScrollChanged(recyclerView)
                }
            }
        })
    }

    fun setData(data: PlayWidgetUiModel) {
        val prevModel = mModel
        mModel = data

        topContainer.shouldShowWithAction(data.title.isNotEmpty() && data.isActionVisible) {
            title.text = data.title
            actionTitle.text = data.actionTitle
            actionTitle.setOnClickListener {
                mAnalyticListener?.onClickViewAll(this)
                RouteManager.route(context, data.actionAppLink)
            }
        }

        configureOverlay(data.background)

        recyclerViewItem.addOneTimeGlobalLayoutListener {
            mWidgetInternalListener?.onWidgetCardsScrollChanged(recyclerViewItem)
        }

        val modifiedItems = getModifiedItems(prevModel, data)
        adapter.setItemsAndAnimateChanges(modifiedItems)

        mIsAutoPlay = data.config.autoPlay
    }

    /**
     * Setup view
     */
    private fun configureOverlay(data: PlayWidgetBackgroundUiModel) {
        if (shouldLoadOverlayImage(data.overlayImageUrl)) {
            if (shouldScrollToLeftBanner()) recyclerViewItem.smoothScrollToPosition(LEFT_BANNER_POSITION)
            overlayImage.loadImage(data.overlayImageUrl, overlayImageHandler(data))
        } else {
            overlayImage.setImageDrawable(null)
            configureBackgroundOverlay(data)
        }
        recyclerViewItem.setMargin(
                left = 0,
                top = if (shouldAddSpacing(data)) spacing16 else 0,
                right = 0,
                bottom = spacing16,
        )
        mLastOverlayImageUrl = data.overlayImageUrl
    }

    private fun shouldLoadOverlayImage(imageUrl: String) = imageUrl.isNotBlank()

    private fun shouldScrollToLeftBanner() = mLastOverlayImageUrl?.isBlank() ?: false

    private fun shouldAddSpacing(data: PlayWidgetBackgroundUiModel): Boolean {
        return data.overlayImageUrl.isNotBlank()
                && (data.gradientColors.isNotEmpty() || data.backgroundUrl.isNotBlank())
    }

    private fun overlayImageHandler(data: PlayWidgetBackgroundUiModel): ImageHandler.ImageLoaderStateListener {
        return object : ImageHandler.ImageLoaderStateListener {
            override fun successLoad() {
                configureBackgroundOverlay(data)
            }

            override fun failedLoad() {
                configureBackgroundOverlay(data)
            }
        }
    }

    private fun configureBackgroundOverlay(data: PlayWidgetBackgroundUiModel) {
        if (data.gradientColors.isNotEmpty()) {
            overlayBackground.setGradientBackground(data.gradientColors)
        } else if (data.backgroundUrl.isNotBlank() && data.backgroundUrl.isNotEmpty()) {
            overlayBackground.loadImage(data.backgroundUrl)
        } else {
            overlayBackground.setImageDrawable(null)
            overlayBackground.background = null
        }
    }

    private fun getModifiedItems(prevModel: PlayWidgetUiModel, model: PlayWidgetUiModel): List<Any> {
        return if (model.background.overlayImageUrl.isNotBlank()) {
            if (prevModel.background != model.background) {
                overlayImpressHolder = ImpressHolder()
            }
            listOf(PlayWidgetOverlayUiModel(overlayImpressHolder)) + model.items
        } else model.items
    }

    companion object {
        private const val LEFT_BANNER_POSITION = 0
    }
}