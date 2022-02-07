package com.tokopedia.campaignlist.page.presentation.viewholder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaignlist.databinding.CampaignTypeItemLayoutBinding
import com.tokopedia.campaignlist.page.presentation.model.CampaignTypeSelection
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show

class CampaignTypeViewHolder(
        private val binding: CampaignTypeItemLayoutBinding,
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
                clickListener.onListItemClicked(adapterPosition)
            }
        }
    }

    fun bindData(campaignTypeSelection: CampaignTypeSelection) {
        // disable the click listener until there are more than one selection
        binding.root.isEnabled = false
        // bind campaign type name
        binding.tpgCampaignTypeName.text = campaignTypeSelection.campaignTypeName
        // bind campaign status
        if (campaignTypeSelection.statusText.isNotBlank()) {
            binding.labelStatusText.text = campaignTypeSelection.statusText
            binding.labelStatusText.show()
            // disable selecting future selection
            binding.root.context?.run {
                binding.tpgCampaignTypeName.setTextColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN400))
            }

        } else binding.labelStatusText.hide()
        // bind selection state
        if (campaignTypeSelection.isSelected) binding.ivGreenCheck.show()
        else binding.ivGreenCheck.hide()
    }
}