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
import com.tokopedia.play.widget.ui.itemdecoration.PlayWidgetLargeItemDecoration
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
                config = mModel.config,
                channelPositionInList = position,
            )

            if(item.isUpcoming)
                mAnalyticListener?.onImpressReminderIcon(
                view = this@PlayWidgetLargeView,
                item = item,
                channelPositionInList = position,
                isReminded = item.reminderType == PlayWidgetReminderType.Reminded,
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
                config = mModel.config,
                channelPositionInList = position,
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

        override fun onMenuActionButtonClicked(
            view: View,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {
            mAnalyticListener?.onClickMenuActionChannel(
                this@PlayWidgetLargeView,
                item,
                position,
            )
            mWidgetListener?.onMenuActionButtonClicked(
                this@PlayWidgetLargeView,
                item,
                position,
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

    private val transcodeCardListener = object : PlayWidgetLargeViewHolder.Transcode.Listener {
        override fun onFailedTranscodingChannelDeleteButtonClicked(
            view: View,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {
            mAnalyticListener?.onClickDeleteChannel(this@PlayWidgetLargeView, item, position)
            mWidgetListener?.onDeleteFailedTranscodingChannel(this@PlayWidgetLargeView, item.channelId)
        }
    }

    private val adapter = PlayWidgetLargeAdapter(
        cardChannelListener = channelCardListener,
        cardBannerListener = bannerCardListener,
        cardTranscodeListener = transcodeCardListener,
    )

    private var mModel: PlayWidgetUiModel = PlayWidgetUiModel.Empty

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
        recyclerViewItem.addItemDecoration(PlayWidgetLargeItemDecoration(context))
        recyclerViewItem.layoutManager = layoutManager
        recyclerViewItem.adapter = adapter
    }

    fun setData(data: PlayWidgetUiModel) {
        mModel = data
        adapter.setItemsAndAnimateChanges(data.items)
    }

    override fun setWidgetInternalListener(listener: PlayWidgetInternalListener?) {
        this.mWidgetInternalListener = listener
    }
}
