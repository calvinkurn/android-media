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
import com.elyeproj.loaderviewlibrary.LoaderImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.analytic.medium.PlayWidgetMediumAnalyticListener
import com.tokopedia.play.widget.ui.adapter.PlayWidgetCardMediumAdapter
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumChannelViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumOverlayViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumTranscodeViewHolder
import com.tokopedia.play.widget.ui.itemdecoration.PlayWidgetCardMediumItemDecoration
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetMediumListener
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.snaphelper.PlayWidgetSnapHelper
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
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

    private val background: LoaderImageView

    private val title: Typography
    private val actionTitle: TextView

    private val itemContainer: FrameLayout
    private val overlay: FrameLayout
    private val overlayBackground: AppCompatImageView
    private val overlayImage: AppCompatImageView

    private val recyclerViewItem: RecyclerView

    private val snapHelper: SnapHelper = PlayWidgetSnapHelper(context)

    private var mWidgetListener: PlayWidgetMediumListener? = null
    private var mWidgetInternalListener: PlayWidgetInternalListener? = null
    private var mAnalyticListener: PlayWidgetMediumAnalyticListener? = null

    private val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

    private val overlayCardListener = object : PlayWidgetCardMediumOverlayViewHolder.Listener {

        override fun onOverlayImpressed(view: View, item: PlayWidgetMediumOverlayUiModel, position: Int) {
            mAnalyticListener?.onImpressOverlayCard(
                    view = this@PlayWidgetMediumView,
                    item = item,
                    channelPositionInList = position
            )
        }

        override fun onOverlayClicked(view: View, item: PlayWidgetMediumOverlayUiModel, position: Int) {
            mAnalyticListener?.onClickOverlayCard(
                    view = this@PlayWidgetMediumView,
                    item = item,
                    channelPositionInList = position
            )
        }
    }

    private val channelCardListener = object : PlayWidgetCardMediumChannelViewHolder.Listener {

        override fun onChannelImpressed(view: View, item: PlayWidgetMediumChannelUiModel, position: Int) {
            mAnalyticListener?.onImpressChannelCard(
                    view = this@PlayWidgetMediumView,
                    item = item,
                    channelPositionInList = position,
                    isAutoPlay = mIsAutoPlay
            )
        }

        override fun onChannelClicked(view: View, item: PlayWidgetMediumChannelUiModel, position: Int) {
            mAnalyticListener?.onClickChannelCard(
                    view = this@PlayWidgetMediumView,
                    item = item,
                    channelPositionInList = position,
                    isAutoPlay = mIsAutoPlay
            )
            if (mWidgetListener != null
                    && (item.channelType == PlayWidgetChannelType.Live
                            || item.channelType == PlayWidgetChannelType.Vod
                            || GlobalConfig.isSellerApp())) {
                mWidgetListener?.onWidgetOpenAppLink(view, item.appLink)
            } else {
                RouteManager.route(context, item.appLink)
            }
        }

        override fun onToggleReminderChannelClicked(item: PlayWidgetMediumChannelUiModel, reminderType: PlayWidgetReminderType, position: Int) {
            mAnalyticListener?.onClickToggleReminderChannel(this@PlayWidgetMediumView, item, position, reminderType.reminded)
            mWidgetListener?.onToggleReminderClicked(this@PlayWidgetMediumView, item.channelId, reminderType, position)
        }

        override fun onMenuActionButtonClicked(view: View, item: PlayWidgetMediumChannelUiModel, position: Int) {
            mAnalyticListener?.onClickMenuActionChannel(this@PlayWidgetMediumView, item, position)
            mWidgetListener?.onMenuActionButtonClicked(this@PlayWidgetMediumView, item, position)
        }
    }

    private val bannerCardListener = object : PlayWidgetCardMediumBannerViewHolder.Listener {

        override fun onBannerClicked(view: View, item: PlayWidgetMediumBannerUiModel, position: Int) {
            mAnalyticListener?.onClickBannerCard(this@PlayWidgetMediumView, item, position)
        }
    }

    private val transcodeCardListener = object : PlayWidgetCardMediumTranscodeViewHolder.Listener {

        override fun onFailedTranscodingChannelDeleteButtonClicked(view: View, item: PlayWidgetMediumChannelUiModel, position: Int) {
            mAnalyticListener?.onClickDeleteChannel(this@PlayWidgetMediumView, item, position)
            mWidgetListener?.onDeleteFailedTranscodingChannel(this@PlayWidgetMediumView, item.channelId)
        }
    }

    private val adapter = PlayWidgetCardMediumAdapter(
            imageBlurUtil = ImageBlurUtil(context),
            overlayCardListener = overlayCardListener,
            channelCardListener = channelCardListener,
            bannerCardListener = bannerCardListener,
            transcodeCardListener = transcodeCardListener
    )

    private var mIsAutoPlay: Boolean = false

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_play_widget_medium, this)
        background = view.findViewById(R.id.play_widget_medium_bg_loader)

        title = view.findViewById(R.id.play_widget_medium_title)
        actionTitle = view.findViewById(R.id.play_widget_medium_action)

        itemContainer = view.findViewById(R.id.play_widget_container)
        overlay = view.findViewById(R.id.play_widget_overlay)
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
                    overlay.translationX = translateX

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

    fun setData(data: PlayWidgetUiModel.Medium) {
        title.text = data.title
        actionTitle.visibility = if (data.isActionVisible) View.VISIBLE else View.GONE
        actionTitle.text = data.actionTitle
        actionTitle.setOnClickListener {
            mAnalyticListener?.onClickViewAll(this)
            RouteManager.route(context, data.actionAppLink)
        }

        configureBackgroundOverlay(data.background)

        recyclerViewItem.addOneTimeGlobalLayoutListener {
            mWidgetInternalListener?.onWidgetCardsScrollChanged(recyclerViewItem)
        }

        adapter.setItemsAndAnimateChanges(data.items)

        mIsAutoPlay = data.config.autoPlay
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mWidgetInternalListener?.onWidgetAttached(recyclerViewItem)
    }

    /**
     * Setup view
     */
    private fun configureBackgroundOverlay(data: PlayWidgetBackgroundUiModel) {
        if (data.overlayImageUrl.isEmpty() || data.overlayImageUrl.isBlank()) background.hide()
        else {
            background.show()
            overlayImage.loadImage(data.overlayImageUrl, object : ImageHandler.ImageLoaderStateListener {
                override fun successLoad() {
                    configureBackground(data)
                    background.hide()
                }

                override fun failedLoad() {
                    configureBackground(data)
                    background.hide()
                }
            })
        }
    }

    private fun configureBackground(data: PlayWidgetBackgroundUiModel) {
        if (data.gradientColors.isNotEmpty()) {
            overlayBackground.setGradientBackground(data.gradientColors)
        } else if (data.backgroundUrl.isNotBlank() && data.backgroundUrl.isNotEmpty()) {
            overlayBackground.loadImage(data.backgroundUrl)
        }
    }
}