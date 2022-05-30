package com.tokopedia.shop.flash_sale.presentation.campaign_list.list

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemCampaignBinding
import com.tokopedia.shop.flash_sale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flash_sale.domain.entity.enums.CampaignStatus
import com.tokopedia.unifyprinciples.Typography
import java.util.*

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
        binding.labelRegisteredToAnEvent.isVisible = campaign.thematicParticipation
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
        handleTimer(campaign.status, campaign.startDateCalendar)
        handleCampaignStatusIndicator(campaign.status)
    }

    private fun handleTimer(campaignStatus: CampaignStatus, startDate : Calendar) {
        binding.timer.isVisible = campaignStatus == CampaignStatus.UPCOMING
        if (campaignStatus == CampaignStatus.UPCOMING) {
            binding.timer.targetDate = startDate
        }
    }

    private fun handleCampaignStatusIndicator(campaignStatus: CampaignStatus) {
        when(campaignStatus) {
            CampaignStatus.UPCOMING -> {
                binding.tpgCampaignStatus.setStatus(R.string.sfs_upcoming)
                binding.tpgCampaignStatus.textColor(R.color.Unify_YN400)
                binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_upcoming)
            }
            CampaignStatus.AVAILABLE -> {
                binding.tpgCampaignStatus.setStatus(R.string.sfs_available)
                binding.tpgCampaignStatus.textColor(R.color.Unify_NN600)
                binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_available)
            }
            CampaignStatus.ONGOING -> {
                binding.tpgCampaignStatus.setStatus(R.string.sfs_ongoing)
                binding.tpgCampaignStatus.textColor(R.color.Unify_GN500)
                binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_ongoing)
            }
            CampaignStatus.FINISHED -> {
                binding.tpgCampaignStatus.setStatus(R.string.sfs_finished)
                binding.tpgCampaignStatus.textColor(R.color.Unify_NN400)
                binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_finished)
            }
            CampaignStatus.CANCELLED -> {
                binding.tpgCampaignStatus.setStatus(R.string.sfs_cancelled)
                binding.tpgCampaignStatus.textColor(R.color.Unify_RN500)
                binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_cancelled)
            }
            CampaignStatus.DRAFT -> {}
        }
    }

    private fun Typography.setStatus(@StringRes resourceId: Int) {
        val status = this.context.getString(resourceId)
        this.text = status
    }
    private fun Typography.textColor(@ColorRes resourceId: Int) {
        val color = ContextCompat.getColor(this.context, resourceId)
        this.setTextColor(color)
    }

}