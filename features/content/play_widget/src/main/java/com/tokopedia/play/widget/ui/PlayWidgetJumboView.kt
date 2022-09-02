package com.tokopedia.play.widget.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.analytic.jumbo.PlayWidgetJumboAnalyticListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetJumboListener
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.widget.jumbo.adapter.PlayWidgetJumboAdapter
import com.tokopedia.play.widget.ui.widget.jumbo.adapter.PlayWidgetJumboViewHolder

/**
 * @author by astidhiyaa on 12/01/22
 */
class PlayWidgetJumboView : FrameLayout, IPlayWidgetView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val recyclerViewItem: RecyclerView

    private var mWidgetListener: PlayWidgetJumboListener? = null
    private var mAnalyticListener: PlayWidgetJumboAnalyticListener? = null
    private var mWidgetInternalListener: PlayWidgetInternalListener? = null

    private val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    private val channelCardListener = object : PlayWidgetJumboViewHolder.Channel.Listener {
        override fun onChannelImpressed(
            view: View,
            item: PlayWidgetChannelUiModel,
            position: Int
        ) {
            mAnalyticListener?.onImpressChannelCard(
                this@PlayWidgetJumboView,
                item,
                mModel.config,
                channelPositionInList = position,
            )

            if(item.isUpcoming)
                mAnalyticListener?.onImpressReminderIcon(
                view = this@PlayWidgetJumboView,
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
                this@PlayWidgetJumboView,
                item,
                mModel.config,
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
                this@PlayWidgetJumboView,
                item,
                channelPositionInList = position,
                isRemindMe = reminderType.reminded,
            )
            mWidgetListener?.onToggleReminderClicked(
                this@PlayWidgetJumboView,
                item.channelId,
                reminderType,
                position
            )
        }
    }

    private val adapter = PlayWidgetJumboAdapter(
        cardChannelListener = channelCardListener,
    )

    private var mModel: PlayWidgetUiModel = PlayWidgetUiModel.Empty

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_play_widget_jumbo, this)

        recyclerViewItem = view.findViewById(R.id.play_widget_recycler_view)

        setupView()
    }

    fun setWidgetListener(listener: PlayWidgetJumboListener?) {
        mWidgetListener = listener
    }

    fun setAnalyticListener(listener: PlayWidgetJumboAnalyticListener?) {
        mAnalyticListener = listener
    }

    /**
     * Setup view
     */
    private fun setupView() {
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