package com.tokopedia.notifcenter.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.notifcenter.data.entity.notification.TrackHistory
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.view.adapter.diffutil.NotifcenterTimelineHistoryDiffUtilCallback
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.NotifcenterTimelineHistoryViewHolder

class NotifcenterTimelineHistoryAdapter(
    private val type: NotifcenterWidgetHistoryType
) : RecyclerView.Adapter<NotifcenterTimelineHistoryViewHolder>() {

    private var histories: ArrayList<TrackHistory> = arrayListOf()

    /**
     * Last journey used only for widget order
     * If last journey and first item, the line should be different (not green)
     */
    private var isLastJourney = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifcenterTimelineHistoryViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            NotifcenterTimelineHistoryViewHolder.LAYOUT, parent, false
        )
        return NotifcenterTimelineHistoryViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return histories.size
    }

    override fun onBindViewHolder(holder: NotifcenterTimelineHistoryViewHolder, position: Int) {
        val isLastItem = isLastItem(position)
        val isFirstItem = isFirstItem(position)
        holder.bind(histories[position], type, isFirstItem, isLastItem, isLastJourney)
    }

    fun updateHistories(element: NotificationUiModel) {
        val diffResult = DiffUtil.calculateDiff(
            NotifcenterTimelineHistoryDiffUtilCallback(histories, element.trackHistory)
        )
        isLastJourney = element.isLastJourney
        histories.clear()
        histories.addAll(element.trackHistory)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun isLastItem(position: Int): Boolean {
        return histories.isNotEmpty() && position == histories.lastIndex
    }

    private fun isFirstItem(position: Int): Boolean {
        return histories.isNotEmpty() && position == 0
    }
}

enum class NotifcenterWidgetHistoryType {
    ORDER,
    FEED
}
