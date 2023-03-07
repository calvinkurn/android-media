package com.tokopedia.tkpd.flashsale.presentation.detail.adapter.campaigndetail

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemTimelineBinding
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.TimelineStepModel

class TimelineViewHolder(private val binding: StfsItemTimelineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(itemModel: TimelineStepModel) {
        val context = binding.root.context
        setupTitleText(itemModel)
        setupPeriodText(itemModel, context)
        setupIconBackground(itemModel, context)
        setupIcon(itemModel)
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

    private fun setupIcon(itemModel: TimelineStepModel) {
        binding.iconProcess.setImage(newIconId = itemModel.icon)
    }
}
