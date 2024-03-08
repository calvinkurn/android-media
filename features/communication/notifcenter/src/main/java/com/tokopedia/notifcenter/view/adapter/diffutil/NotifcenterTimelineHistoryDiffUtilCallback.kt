package com.tokopedia.notifcenter.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.notifcenter.data.entity.notification.TrackHistory

class NotifcenterTimelineHistoryDiffUtilCallback(
    private val oldList: List<TrackHistory>,
    private val newList: List<TrackHistory>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
