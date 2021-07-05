package com.tokopedia.play.widget.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.analytic.small.PlayWidgetSmallAnalyticListener
import com.tokopedia.play.widget.ui.adapter.PlayWidgetCardSmallAdapter
import com.tokopedia.play.widget.ui.adapter.viewholder.small.PlayWidgetCardSmallBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.small.PlayWidgetCardSmallChannelViewHolder
import com.tokopedia.play.widget.ui.itemdecoration.PlayWidgetCardSmallItemDecoration
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetSmallListener
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.snaphelper.PlayWidgetSnapHelper

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetSmallView : ConstraintLayout, IPlayWidgetView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val tvTitle: TextView
    private val tvSeeAll: TextView
    private val rvWidgetCardSmall: RecyclerView

    private val snapHelper: SnapHelper = PlayWidgetSnapHelper(context)

    private var mWidgetListener: PlayWidgetSmallListener? = null
    private var mAnalyticListener: PlayWidgetSmallAnalyticListener? = null
    private var mWidgetInternalListener: PlayWidgetInternalListener? = null

    private val smallBannerListener = object : PlayWidgetCardSmallBannerViewHolder.Listener {

        override fun onBannerClicked(view: View) {
            mAnalyticListener?.onClickBannerCard(this@PlayWidgetSmallView)
        }
    }

    private val channelCardListener = object : PlayWidgetCardSmallChannelViewHolder.Listener {

        override fun onChannelImpressed(view: View, item: PlayWidgetSmallChannelUiModel, position: Int) {
            mAnalyticListener?.onImpressChannelCard(
                    view = this@PlayWidgetSmallView,
                    item = item,
                    channelPositionInList = position,
                    isAutoPlay = mIsAutoPlay
            )
        }

        override fun onChannelClicked(view: View, item: PlayWidgetSmallChannelUiModel, position: Int) {
            mWidgetListener?.onWidgetOpenAppLink(view, item.appLink)
            mAnalyticListener?.onClickChannelCard(
                    view = this@PlayWidgetSmallView,
                    item = item,
                    channelPositionInList = position,
                    isAutoPlay = mIsAutoPlay
            )
        }
    }

    private var mIsAutoPlay: Boolean = false

    private val adapter = PlayWidgetCardSmallAdapter(
            bannerCardListener = smallBannerListener,
            channelCardListener = channelCardListener
    )

    init {
        val view = View.inflate(context, R.layout.view_play_widget_small, this)
        tvTitle = view.findViewById(R.id.tv_title)
        tvSeeAll = view.findViewById(R.id.tv_see_all)
        rvWidgetCardSmall = view.findViewById(R.id.rv_widget_card_small)

        setupView(view)
    }

    override fun setWidgetInternalListener(listener: PlayWidgetInternalListener?) {
        mWidgetInternalListener = listener
    }

    fun setWidgetListener(listener: PlayWidgetSmallListener?) {
        mWidgetListener = listener
    }

    fun setAnalyticListener(listener: PlayWidgetSmallAnalyticListener?) {
        mAnalyticListener = listener
    }

    fun setData(data: PlayWidgetUiModel.Small) {
        tvTitle.text = data.title

        tvSeeAll.visibility = if (data.isActionVisible) View.VISIBLE else View.GONE
        tvSeeAll.text = data.actionTitle
        tvSeeAll.setOnClickListener {
            mAnalyticListener?.onClickViewAll(this)
            RouteManager.route(context, data.actionAppLink)
        }

        val isNewChannelAdded = isNewItemAdded(data.items)

        rvWidgetCardSmall.addOneTimeGlobalLayoutListener {
            if(isNewChannelAdded && adapter.itemCount > 0) rvWidgetCardSmall.smoothScrollToPosition(0)
            mWidgetInternalListener?.onWidgetCardsScrollChanged(rvWidgetCardSmall)
        }

        adapter.setItemsAndAnimateChanges(data.items)

        mIsAutoPlay = data.config.autoPlay
    }

    private fun setupView(view: View) {
        rvWidgetCardSmall.adapter = adapter
        rvWidgetCardSmall.addItemDecoration(PlayWidgetCardSmallItemDecoration(context))

        snapHelper.attachToRecyclerView(rvWidgetCardSmall)

        rvWidgetCardSmall.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mWidgetInternalListener?.onWidgetCardsScrollChanged(recyclerView)
                }
            }
        })
    }

    private fun isNewItemAdded(items: List<PlayWidgetSmallItemUiModel>): Boolean {
        return when {
            adapter.itemCount == 0 -> false
            items.firstOrNull() !is PlayWidgetSmallChannelUiModel -> false
            else -> !adapter.areItemsTheSame(items.first(), adapter.getItem(0))
        }
    }
}