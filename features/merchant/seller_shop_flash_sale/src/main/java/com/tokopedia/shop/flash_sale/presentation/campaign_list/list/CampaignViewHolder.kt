package com.tokopedia.shop.flash_sale.presentation.campaign_list.list

import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemCampaignBinding
import com.tokopedia.shop.flash_sale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flash_sale.domain.entity.enums.CampaignStatus
import com.tokopedia.unifyprinciples.Typography

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
        handleCampaignStatusIndicator(campaign.status)
    }

    private fun handleCampaignStatusIndicator(campaignStatus: CampaignStatus) {
        when(campaignStatus) {
            CampaignStatus.SCHEDULED -> {
                binding.tpgCampaignStatus.setStatus(R.string.sfs_upcoming)
                binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_upcoming)
            }
            CampaignStatus.DRAFT -> {
                binding.tpgCampaignStatus.setStatus(R.string.sfs_available)
                binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_available)
            }
            CampaignStatus.PUBLISHED -> {
                binding.tpgCampaignStatus.setStatus(R.string.sfs_ongoing)
                binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_ongoing)
            }
            CampaignStatus.FINISHED -> {
                binding.tpgCampaignStatus.setStatus(R.string.sfs_finished)
                binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_finished)
            }
            CampaignStatus.CANCELLED -> {
                binding.tpgCampaignStatus.setStatus(R.string.sfs_cancelled)
                binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_cancelled)
            }
        }
    }

    private fun Typography.setStatus(@StringRes resourceId: Int) {
        val status = this.context.getString(resourceId)
        this.text = status
    }


}