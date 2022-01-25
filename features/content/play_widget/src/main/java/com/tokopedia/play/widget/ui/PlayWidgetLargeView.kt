package com.tokopedia.play.widget.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.adapter.PlayWidgetCardLargeAdapter
import com.tokopedia.play.widget.ui.adapter.viewholder.large.PlayWidgetCardLargeBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.large.PlayWidgetCardLargeChannelViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.large.PlayWidgetCardLargeTranscodeViewHolder
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetLargeListener
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.snaphelper.PlayWidgetSnapHelper
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play_common.util.blur.ImageBlurUtil

/**
 * @author by astidhiyaa on 11/01/22
 */
class PlayWidgetLargeView : ConstraintLayout, IPlayWidgetView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setWidgetInternalListener(listener: PlayWidgetInternalListener?) {
        mWidgetInternalListener = listener
    }

    private val itemContainer: FrameLayout

    private val recyclerViewItem: RecyclerView

    private val snapHelper: SnapHelper = PlayWidgetSnapHelper(context)

    private var mWidgetListener: PlayWidgetLargeListener? = null
    private var mWidgetInternalListener: PlayWidgetInternalListener? = null

    private val layoutManager = GridLayoutManager(context, 2)

    private val channelCardListener = object : PlayWidgetCardLargeChannelViewHolder.Listener {
        override fun onChannelImpressed(
            view: View,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {
        }

        override fun onChannelClicked(
            view: View,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {
            if (mWidgetListener != null
                && (item.widgetType == PlayWidgetChannelType.Live
                        || item.widgetType == PlayWidgetChannelType.Vod
                        || item.widgetType == PlayWidgetChannelType.Upcoming
                        || GlobalConfig.isSellerApp())
            ) {
                mWidgetListener?.onWidgetOpenAppLink(view, item.appLink)
            } else {
                RouteManager.route(context, item.appLink)
            }
        }

        override fun onToggleReminderChannelClicked(
            item: PlayWidgetChannelUiModel,
            reminderType: PlayWidgetReminderType,
            position: Int
        ) {
            mWidgetListener?.onToggleReminderClicked(
                this@PlayWidgetLargeView,
                item.channelId,
                reminderType,
                position
            )
        }

    }

    private val bannerCardListener = object : PlayWidgetCardLargeBannerViewHolder.Listener {
        override fun onBannerClicked(
            view: View,
            item: PlayWidgetBannerUiModel,
            position: Int
        ) {
        }
    }

    private val transcodeCardListener = object : PlayWidgetCardLargeTranscodeViewHolder.Listener {
        override fun onFailedTranscodingChannelDeleteButtonClicked(view: View, item: PlayWidgetChannelUiModel, position: Int) {
            mWidgetListener?.onDeleteFailedTranscodingChannel(this@PlayWidgetLargeView, item.channelId)
        }
    }

    private val adapter = PlayWidgetCardLargeAdapter(
        imageBlurUtil = ImageBlurUtil(context),
        channelCardListener = channelCardListener,
        bannerCardListener = bannerCardListener,
        transcodeCardListener = transcodeCardListener
    )

    private var mIsAutoPlay: Boolean = false


    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_play_widget_large, this)

        itemContainer = view.findViewById(R.id.play_widget_container)

        recyclerViewItem = view.findViewById(R.id.play_widget_recycler_view)

        setupView()
    }

    fun setWidgetListener(listener: PlayWidgetLargeListener?) {
        mWidgetListener = listener
    }

    /**
     * Setup view
     */
    private fun setupView() {
        recyclerViewItem.layoutManager = layoutManager
        recyclerViewItem.adapter = adapter

        snapHelper.attachToRecyclerView(recyclerViewItem)

        recyclerViewItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mWidgetInternalListener?.onWidgetCardsScrollChanged(recyclerView)
                }
            }
        })
    }

    fun setData(data: PlayWidgetUiModel) {
        recyclerViewItem.addOneTimeGlobalLayoutListener {
            mWidgetInternalListener?.onWidgetCardsScrollChanged(recyclerViewItem)
        }

        adapter.setItemsAndAnimateChanges(data.items)

        mIsAutoPlay = data.config.autoPlay
    }
}