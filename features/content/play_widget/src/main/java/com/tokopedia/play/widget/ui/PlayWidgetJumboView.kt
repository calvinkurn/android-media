package com.tokopedia.play.widget.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.adapter.PlayWidgetCardJumboAdapter
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboChannelViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboTranscodeViewHolder
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetJumboListener
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play_common.util.blur.ImageBlurUtil

/**
 * @author by astidhiyaa on 12/01/22
 */
class PlayWidgetJumboView : ConstraintLayout, IPlayWidgetView {
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

    private var mWidgetListener: PlayWidgetJumboListener? = null

    private var mWidgetInternalListener: PlayWidgetInternalListener? = null

    private val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    private val channelCardListener = object : PlayWidgetCardJumboChannelViewHolder.Listener {
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
                && (item.channelType == PlayWidgetChannelType.Live
                        || item.channelType == PlayWidgetChannelType.Vod
                        || item.channelType == PlayWidgetChannelType.Upcoming
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
                this@PlayWidgetJumboView,
                item.channelId,
                reminderType,
                position
            )
        }

    }

    private val bannerCardListener = object : PlayWidgetCardJumboBannerViewHolder.Listener {
        override fun onBannerClicked(
            view: View,
            item: PlayWidgetBannerUiModel,
            position: Int
        ) {
        }
    }
    private val transcodeCardListener = object : PlayWidgetCardJumboTranscodeViewHolder.Listener {
        override fun onFailedTranscodingChannelDeleteButtonClicked(view: View, item: PlayWidgetChannelUiModel, position: Int) {
            mWidgetListener?.onDeleteFailedTranscodingChannel(this@PlayWidgetJumboView, item.channelId)
        }
    }


    private val adapter = PlayWidgetCardJumboAdapter(
        imageBlurUtil = ImageBlurUtil(context),
        channelCardListener = channelCardListener,
        bannerCardListener = bannerCardListener,
        transcodeListener = transcodeCardListener
    )

    private var mIsAutoPlay: Boolean = false

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_play_widget_jumbo, this)

        itemContainer = view.findViewById(R.id.play_widget_container)

        recyclerViewItem = view.findViewById(R.id.play_widget_recycler_view)

        setupView()
    }

    fun setWidgetListener(listener: PlayWidgetJumboListener?) {
        mWidgetListener = listener
    }

    /**
     * Setup view
     */
    private fun setupView() {
        recyclerViewItem.layoutManager = layoutManager
        recyclerViewItem.adapter = adapter
    }

    fun setData(data: PlayWidgetUiModel) {
        adapter.setItemsAndAnimateChanges(data.items)

        mIsAutoPlay = data.config.autoPlay
    }
}