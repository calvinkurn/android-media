package com.tokopedia.campaignlist.page.presentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaignlist.databinding.CampaignStatusItemLayoutBinding
import com.tokopedia.campaignlist.page.presentation.model.CampaignStatusSelection
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show

class CampaignStatusViewHolder(
    private val binding: CampaignStatusItemLayoutBinding,
    private val clickListener: OnListItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    interface OnListItemClickListener {
        fun onListItemClicked(position: Int)
    }

    init {
        binding.root.setOnClickListener {
            val isChecked = binding.ivGreenCheck.isVisible
            if (isChecked) {
                binding.ivGreenCheck.hide()
            } else {
                binding.ivGreenCheck.show()
            }
            clickListener.onListItemClicked(adapterPosition)
        }
    }

    fun bindData(campaignStatusSelection: CampaignStatusSelection) {
        binding.tpgCampaignStatusName.text = campaignStatusSelection.statusText
        if (campaignStatusSelection.isSelected) binding.ivGreenCheck.show()
        else binding.ivGreenCheck.hide()
    }
}