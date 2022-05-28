package com.tokopedia.shop.flash_sale.presentation.campaign_list.list

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemCampaignBinding
import com.tokopedia.shop.flash_sale.domain.entity.CampaignUiModel

class CampaignViewHolder(private val binding: SsfsItemCampaignBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(
        position: Int,
        campaign: CampaignUiModel,
        onCampaignClicked: (CampaignUiModel, Int) -> Unit,
        onOverflowMenuClicked: (CampaignUiModel) -> Unit,
        isLoading: Boolean
    ) {
        binding.tpgCampaignName.text = campaign.campaignName
        binding.root.setOnClickListener { onCampaignClicked(campaign, position) }
        binding.imgMore.setOnClickListener { onOverflowMenuClicked(campaign) }
        binding.tpgStartDate.text = campaign.startDate
        binding.tpgStartTime.text = campaign.startTime
        binding.tpgEndDate.text = campaign.endDate
        binding.tpgEndTime.text = campaign.endTime
        binding.tpgProductCount.text = campaign.summary.totalItem.toString()
        binding.tpgReminderCount.text = campaign.notifyMeCount.toString()
        binding.tpgSoldCount.text = campaign.summary.soldItem.toString()
        binding.loader.isVisible = isLoading
    }

}