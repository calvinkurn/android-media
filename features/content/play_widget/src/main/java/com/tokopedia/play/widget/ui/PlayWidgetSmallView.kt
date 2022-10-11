package com.tokopedia.play.widget.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.analytic.small.PlayWidgetSmallAnalyticListener
import com.tokopedia.play.widget.ui.itemdecoration.PlayWidgetCardSmallItemDecoration
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetSmallListener
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.snaphelper.PlayWidgetSnapHelper
import com.tokopedia.play.widget.ui.widget.small.adapter.PlayWidgetSmallAdapter
import com.tokopedia.play.widget.ui.widget.small.adapter.PlayWidgetSmallViewHolder
import com.tokopedia.play_common.util.extension.changeConstraint

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetSmallView : FrameLayout, IPlayWidgetView {

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

    init {
        LayoutInflater.from(context).inflate(R.layout.view_play_widget_small, this)
    }

    private val root: ConstraintLayout = findViewById(R.id.cl_root)
    private val rvWidgetCardSmall: RecyclerView = findViewById(R.id.rv_widget_card_small)

    private var header: View = findViewById(R.id.view_play_widget_header)

    private val snapHelper: SnapHelper = PlayWidgetSnapHelper(context)

    private var mWidgetListener: PlayWidgetSmallListener? = null
    private var mAnalyticListener: PlayWidgetSmallAnalyticListener? = null
    private var mWidgetInternalListener: PlayWidgetInternalListener? = null

    private var mModel: PlayWidgetUiModel = PlayWidgetUiModel.Empty

    private val cardBannerListener = object : PlayWidgetSmallViewHolder.Banner.Listener {

        override fun onBannerImpressed(view: View) {
            mAnalyticListener?.onImpressBannerCard(this@PlayWidgetSmallView)
        }

        override fun onBannerClicked(view: View) {
            mAnalyticListener?.onClickBannerCard(this@PlayWidgetSmallView)
        }
    }

    private val cardChannelListener = object : PlayWidgetSmallViewHolder.Channel.Listener {

        override fun onChannelImpressed(view: View, item: PlayWidgetChannelUiModel, position: Int) {
            mAnalyticListener?.onImpressChannelCard(
                view = this@PlayWidgetSmallView,
                item = item,
                config = mModel.config,
                channelPositionInList = position,
            )
        }

        override fun onChannelClicked(view: View, item: PlayWidgetChannelUiModel, position: Int) {
            mWidgetListener?.onWidgetOpenAppLink(view, item.appLink)
            mAnalyticListener?.onClickChannelCard(
                view = this@PlayWidgetSmallView,
                item = item,
                config = mModel.config,
                channelPositionInList = position,
            )
        }
    }

    private var mIsAutoPlay: Boolean = false

    private val adapter = PlayWidgetSmallAdapter(
        cardChannelListener = cardChannelListener,
        cardBannerListener = cardBannerListener,
    )

    init {
        setupView()
    }

    override fun setWidgetInternalListener(listener: PlayWidgetInternalListener?) {
        mWidgetInternalListener = listener
    }

    fun setCustomHeader(header: View) {
        require(header is ViewGroup)
        requireNotNull(header.findViewById(R.id.tv_play_widget_title))
        requireNotNull(header.findViewById(R.id.tv_play_widget_action))

        val currentHeader = getHeader()
        header.id = currentHeader.id

        root.removeView(currentHeader)
        root.addView(header)

        this.header = header

        setupHeader(data = mModel)
    }

    fun getHeader(): View {
        return header
    }

    fun setWidgetListener(listener: PlayWidgetSmallListener?) {
        mWidgetListener = listener
    }

    fun setAnalyticListener(listener: PlayWidgetSmallAnalyticListener?) {
        mAnalyticListener = listener
    }

    fun setData(data: PlayWidgetUiModel) {
        val prevModel = mModel
        mModel = data

        setupHeader(prevModel, data)

        val isNewChannelAdded = isNewItemAdded(data.items)

        rvWidgetCardSmall.addOneTimeGlobalLayoutListener {
            if(isNewChannelAdded && adapter.itemCount > 0) rvWidgetCardSmall.smoothScrollToPosition(0)
            mWidgetInternalListener?.onWidgetCardsScrollChanged(rvWidgetCardSmall)
        }

        adapter.setItemsAndAnimateChanges(data.items)

        mIsAutoPlay = data.config.autoPlay
    }

    private fun setupView() {
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

    private fun setupHeader(
        prevData: PlayWidgetUiModel = PlayWidgetUiModel.Empty,
        data: PlayWidgetUiModel,
    ) {
        val tvAction = getWidgetAction()
        val tvTitle = getWidgetTitle()

        if (prevData.hasAction != data.hasAction && data.hasAction) {
            tvAction.addOneTimeGlobalLayoutListener {
                mAnalyticListener?.onImpressViewAll(this)
            }
        }

        tvTitle.text = data.title

        tvAction.visibility = if (data.hasAction) View.VISIBLE else View.GONE
        tvAction.text = data.actionTitle
        tvAction.setOnClickListener {
            mAnalyticListener?.onClickViewAll(this)
            RouteManager.route(context, data.actionAppLink)
        }
    }

    private fun getWidgetTitle(): TextView {
        return header.findViewById(R.id.tv_play_widget_title)
    }

    private fun getWidgetAction(): TextView {
        return header.findViewById(R.id.tv_play_widget_action)
    }

    private fun isNewItemAdded(items: List<PlayWidgetItemUiModel>): Boolean {
        return when {
            adapter.itemCount == 0 -> false
            items.firstOrNull() !is PlayWidgetChannelUiModel -> false
            else -> !adapter.areItemsTheSame(items.first(), adapter.getItem(0))
        }
    }
}