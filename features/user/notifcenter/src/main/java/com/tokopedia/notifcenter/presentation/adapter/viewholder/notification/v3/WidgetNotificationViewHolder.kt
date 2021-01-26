package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.notification.TrackHistory
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.unifyprinciples.Typography

class WidgetNotificationViewHolder(
        itemView: View?, listener: NotificationItemListener?
) : BaseNotificationViewHolder(itemView, listener) {

    private val historyBtn: Typography? = itemView?.findViewById(R.id.tp_history)
    private val historyTimeLine: RecyclerView? = itemView?.findViewById(R.id.rv_history)
    private val historyAdapter: HistoryAdapter = HistoryAdapter()

    init {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        historyTimeLine?.apply {
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = historyAdapter
        }
    }

    override fun bind(element: NotificationUiModel) {
        super.bind(element)
        bindTrackHistory(element)
        bindHistoryBtnClick(element)
    }

    private fun bindTrackHistory(element: NotificationUiModel) {
        historyAdapter.updateHistories(element)
    }

    private fun bindHistoryBtnClick(element: NotificationUiModel) {
        historyBtn?.setOnClickListener {
            toggleTimeLineVisibility()
        }
    }

    private fun toggleTimeLineVisibility() {
        if (historyTimeLine?.isVisible == true) {
            historyTimeLine.hide()
        } else {
            historyTimeLine?.show()
        }
    }

    override fun onViewRecycled() {
        historyAdapter.histories.clear()
        historyAdapter.notifyDataSetChanged()
    }

    companion object {
        val LAYOUT = R.layout.item_notification_widget_order_history
    }
}

class HistoryAdapter: RecyclerView.Adapter<TimeLineViewHolder>() {

    val histories: ArrayList<TrackHistory> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
                TimeLineViewHolder.LAYOUT, parent, false
        )
        return TimeLineViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return histories.size
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        holder.bind(histories[position])
    }

    fun updateHistories(element: NotificationUiModel) {
        histories.addAll(element.trackHistory)
        notifyDataSetChanged()
    }
}

class TimeLineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(trackHistory: TrackHistory) {

    }

    companion object {
        val LAYOUT = R.layout.item_notification_timeline_order_history
    }
}