package com.tokopedia.tkpd.flashsale.presentation.detail.adapter.campaigndetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemTimelineBinding
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.TimelineStepModel

class TimelineProcessAdapter: RecyclerView.Adapter<TimelineViewHolder>() {

    private var data: List<TimelineStepModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val binding = StfsItemTimelineBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return TimelineViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    fun setDataList(newData: List<TimelineStepModel>) {
        val diffUtil = TimelineDiffUtil(data, newData)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        data = newData
        diffResult.dispatchUpdatesTo(this)
    }

    inner class TimelineDiffUtil(
        private val oldItems: List<TimelineStepModel>,
        private val newItems: List<TimelineStepModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldItems[oldItemPosition]
            val new = newItems[newItemPosition]
            return old.title == new.title
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldItems[oldItemPosition]
            val new = newItems[newItemPosition]
            return old == new
        }
    }
}
