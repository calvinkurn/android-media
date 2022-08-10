package com.tokopedia.tkpd.flashsale.presentation.list.adapter.campaigndetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemTimelineViewBinding

class TimelineProcessAdapter: RecyclerView.Adapter<TimelineProcessAdapter.TimelineViewHolder>() {

    var data: List<String> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val binding = StfsItemTimelineViewBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return TimelineViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    inner class TimelineViewHolder(private val binding: StfsItemTimelineViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String) {
            binding.tfProcess.text = title
            binding.tfPeriod.text = title
        }
    }
}