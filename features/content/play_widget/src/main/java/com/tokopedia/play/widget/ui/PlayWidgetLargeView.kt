package com.tokopedia.play.widget.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.analytic.large.PlayWidgetLargeAnalyticListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetLargeListener
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.widget.large.adapter.PlayWidgetLargeAdapter
import com.tokopedia.play.widget.ui.widget.large.adapter.PlayWidgetLargeViewHolder

/**
 * @author by astidhiyaa on 11/01/22
 */
class PlayWidgetLargeView : FrameLayout, IPlayWidgetView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val recyclerViewItem: RecyclerView

    private var mWidgetListener: PlayWidgetLargeListener? = null
    private var mAnalyticListener: PlayWidgetLargeAnalyticListener? = null
    private var mWidgetInternalListener: PlayWidgetInternalListener? = null

    private val layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.play_widget_large_span_count))

    private val channelCardListener = object : PlayWidgetLargeViewHolder.Channel.Listener {
        override fun onChannelImpressed(
            view: View,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {
            mAnalyticListener?.onImpressChannelCard(
                view = this@PlayWidgetLargeView,
                item = item,
                channelPositionInList = position,
                isAutoPlay = mIsAutoPlay
            )
        }

        override fun onChannelClicked(
            view: View,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {
            mAnalyticListener?.onClickChannelCard(
                view = this@PlayWidgetLargeView,
                item = item,
                channelPositionInList = position,
                isAutoPlay = mIsAutoPlay
            )
            mWidgetListener?.onWidgetOpenAppLink(view, item.appLink)
        }

        override fun onToggleReminderChannelClicked(
            item: PlayWidgetChannelUiModel,
            reminderType: PlayWidgetReminderType,
            position: Int
        ) {
            mAnalyticListener?.onClickToggleReminderChannel(
                view = this@PlayWidgetLargeView,
                item = item,
                channelPositionInList = position,
                isRemindMe = reminderType.reminded
            )
            mWidgetListener?.onToggleReminderClicked(
                this@PlayWidgetLargeView,
                item.channelId,
                reminderType,
                position
            )
        }

    }

    private val bannerCardListener = object : PlayWidgetLargeViewHolder.Banner.Listener {
        override fun onBannerImpressed(view: View, item: PlayWidgetBannerUiModel, position: Int) {
            mAnalyticListener?.onImpressBannerCard(
                view = this@PlayWidgetLargeView,
                item = item,
                channelPositionInList = position
            )
        }

        override fun onBannerClicked(
            view: View,
            item: PlayWidgetBannerUiModel,
            position: Int
        ) {
            mAnalyticListener?.onClickBannerCard(
                view = this@PlayWidgetLargeView,
                item = item,
                channelPositionInList = position
            )
        }
    }

    private val adapter = PlayWidgetLargeAdapter(
        cardChannelListener = channelCardListener,
        cardBannerListener = bannerCardListener,
    )

    private var mIsAutoPlay: Boolean = false

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_play_widget_large, this)
        recyclerViewItem = view.findViewById(R.id.play_widget_recycler_view)
        setupView()
    }

    fun setWidgetListener(listener: PlayWidgetLargeListener?) {
        mWidgetListener = listener
    }

    fun setAnalyticListener(listener: PlayWidgetLargeAnalyticListener?) {
        mAnalyticListener = listener
    }

    /**
     * Setup view
     */
    private fun setupView() {
        recyclerViewItem.layoutManager = layoutManager
        recyclerViewItem.adapter = adapter
//        recyclerViewItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    mWidgetInternalListener?.onWidgetCardsScrollChanged(recyclerView)
//                }
//            }
//        })
    }

    fun setData(data: PlayWidgetUiModel) {
        adapter.setItemsAndAnimateChanges(data.items)

        mIsAutoPlay = data.config.autoPlay
    }

    override fun setWidgetInternalListener(listener: PlayWidgetInternalListener?) {
        this.mWidgetInternalListener = listener
    }
}