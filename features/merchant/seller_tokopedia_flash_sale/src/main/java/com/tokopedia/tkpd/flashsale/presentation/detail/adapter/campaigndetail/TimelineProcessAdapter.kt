package com.tokopedia.tkpd.flashsale.presentation.detail.adapter.campaigndetail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemTimelineBinding
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.TimelineStepModel

class TimelineProcessAdapter: RecyclerView.Adapter<TimelineProcessAdapter.TimelineViewHolder>() {

    var data: List<TimelineStepModel> = mutableListOf()
        set(value) {
            val diffUtil = TimelineDiffUtil(field, value)
            val diffResult = DiffUtil.calculateDiff(diffUtil)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

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

    inner class TimelineViewHolder(private val binding: StfsItemTimelineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemModel: TimelineStepModel) {
            val context = binding.root.context
            setupTitleText(itemModel)
            setupPeriodText(itemModel, context)
            setupIconBackground(itemModel, context)
        }

        private fun setupTitleText(itemModel: TimelineStepModel) {
            binding.tfProcess.text = itemModel.title
            binding.tfProcessCenter.text = itemModel.title
            binding.tfProcess.isVisible = itemModel.period.isNotEmpty()
            binding.tfProcessCenter.isVisible = itemModel.period.isEmpty()
        }

        private fun setupPeriodText(itemModel: TimelineStepModel, context: Context) {
            if (itemModel.isEnded) {
                binding.tfPeriod.text = context.getString(R.string.campaigndetail_campaign_ended)
            } else {
                binding.tfPeriod.text = itemModel.period
            }
        }

        private fun setupIconBackground(itemModel: TimelineStepModel, context: Context) {
            binding.iconProcess.background = if (itemModel.isActive) {
                context.getDrawable(R.drawable.stfs_bg_circle_timeline)
            } else {
                context.getDrawable(R.drawable.stfs_bg_circle_timeline_inactive)
            }
        }
    }
}