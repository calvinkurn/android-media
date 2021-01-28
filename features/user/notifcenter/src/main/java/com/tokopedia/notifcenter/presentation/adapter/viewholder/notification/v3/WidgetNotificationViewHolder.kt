package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.notification.TrackHistory
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.common.NotificationAdapterListener
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.time.TimeHelper

class WidgetNotificationViewHolder constructor(
        itemView: View?,
        listener: NotificationItemListener?,
        private val adapterListener: NotificationAdapterListener?
) : BaseNotificationViewHolder(itemView, listener) {

    private val historyBtn: Typography? = itemView?.findViewById(R.id.tp_history)
    private val progressIndicator: Group? = itemView?.findViewById(
            R.id.group_progress_indicator
    )
    private val historyTimeLine: RecyclerView? = itemView?.findViewById(R.id.rv_history)
    private val historyAdapter: HistoryAdapter = HistoryAdapter()

    init {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        historyTimeLine?.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            setRecycledViewPool(adapterListener?.getWidgetTimelineViewPool())
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = historyAdapter
        }
    }

    override fun bind(element: NotificationUiModel) {
        super.bind(element)
        bindTrackHistory(element)
        bindTimeLineVisibility(element)
        bindProgressIndicator(element)
        bindHistoryBtnClick(element)
        bindProgressIndicator(element)
    }

    private fun bindTrackHistory(element: NotificationUiModel) {
        historyAdapter.updateHistories(element)
    }

    private fun bindHistoryBtnClick(element: NotificationUiModel) {
        historyBtn?.setOnClickListener {
            element.toggleHistoryVisibility()
            bindTimeLineVisibility(element)
            bindProgressIndicator(element)
        }
    }

    private fun bindTimeLineVisibility(element: NotificationUiModel) {
        if (element.isHistoryVisible) {
            historyTimeLine?.show()
        } else {
            historyTimeLine?.hide()
        }
    }

    private fun bindProgressIndicator(element: NotificationUiModel) {
        if (element.isHistoryVisible) {
            progressIndicator?.show()
        } else {
            progressIndicator?.hide()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notification_widget_order_history
    }
}

/**
 * Adapter for [WidgetNotificationViewHolder] only
 */
class HistoryAdapter : RecyclerView.Adapter<TimeLineViewHolder>(), TimeLineViewHolder.Listener {

    val histories: ArrayList<TrackHistory> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
                TimeLineViewHolder.LAYOUT, parent, false
        )
        return TimeLineViewHolder(layout, this)
    }

    override fun getItemCount(): Int {
        return histories.size
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        holder.bind(histories[position])
    }

    fun updateHistories(element: NotificationUiModel) {
        if (histories.isNotEmpty()) {
            histories.clear()
        }
        histories.addAll(element.trackHistory)
        notifyDataSetChanged()
    }

    override fun isLastItem(item: TrackHistory): Boolean {
        return histories.isNotEmpty() && histories.last() == item
    }

    override fun isFirstItem(item: TrackHistory): Boolean {
        return histories.isNotEmpty() && histories.first() == item
    }
}

/**
 * ViewHolder for [TimeLineViewHolder] only
 */
class TimeLineViewHolder(
        itemView: View,
        private val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    interface Listener {
        fun isLastItem(item: TrackHistory): Boolean
        fun isFirstItem(item: TrackHistory): Boolean
    }

    private val title: Typography? = itemView.findViewById(R.id.tp_timeline_title)
    private val desc: Typography? = itemView.findViewById(R.id.tp_timeline_desc)
    private val bottomLine: View? = itemView.findViewById(R.id.view_bottom_line)
    private val topLine: View? = itemView.findViewById(R.id.view_top_line)

    fun bind(trackHistory: TrackHistory) {
        bindTitle(trackHistory)
        bindDesc(trackHistory)
        bindTopLine(trackHistory)
        bindBottomLine(trackHistory)
    }

    private fun bindTitle(trackHistory: TrackHistory) {
        title?.text = trackHistory.title
    }

    private fun bindDesc(trackHistory: TrackHistory) {
        desc?.text = TimeHelper.getRelativeTimeFromNow(trackHistory.createTimeUnixMillis)
    }

    private fun bindTopLine(trackHistory: TrackHistory) {
        if (listener.isFirstItem(trackHistory)) {
            topLine?.setBackgroundResource(com.tokopedia.unifycomponents.R.color.Unify_N100)
        } else {
            topLine?.setBackgroundResource(com.tokopedia.unifycomponents.R.color.Unify_G400)
        }
    }

    private fun bindBottomLine(trackHistory: TrackHistory) {
        bottomLine?.showWithCondition(!listener.isLastItem(trackHistory))
    }

    companion object {
        val LAYOUT = R.layout.item_notification_timeline_order_history
    }
}