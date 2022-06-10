package com.tokopedia.shop.flashsale.presentation.list.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.R.color.*
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemCampaignBinding
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.extension.toCalendar
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.unifyprinciples.Typography
import java.util.*


class CampaignAdapter(
    private val onCampaignClicked: (CampaignUiModel, Int) -> Unit,
    private val onOverflowMenuClicked: (CampaignUiModel) -> Unit
) : RecyclerView.Adapter<CampaignAdapter.CampaignViewHolder>() {

    private var campaigns: MutableList<CampaignUiModel> = mutableListOf()
    private var isLoading = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignViewHolder {
        val binding =
            SsfsItemCampaignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CampaignViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return campaigns.size
    }

    override fun onBindViewHolder(holder: CampaignViewHolder, position: Int) {
        val campaign = campaigns[position]
        val isLoading = isLoading && (position == campaigns.lastIndex)
        holder.bind(
            position,
            campaign,
            onCampaignClicked,
            onOverflowMenuClicked,
            isLoading
        )
    }

    fun clearAll() {
        this.campaigns = mutableListOf()
        notifyItemRangeRemoved(Constant.FIRST_PAGE, campaigns.size)
    }

    fun submit(newCampaigns: List<CampaignUiModel>) {
        val oldItemsSize = campaigns.size
        campaigns.addAll(newCampaigns)
        notifyItemRangeChanged(oldItemsSize, campaigns.size)
        hideLoading()
    }

    fun showLoading() {
        if (itemCount.isMoreThanZero()) {
            isLoading = true
            notifyItemChanged(campaigns.lastIndex)
        }
    }

    fun hideLoading() {
        isLoading = false
        if (itemCount.isMoreThanZero()) {
            notifyItemChanged(campaigns.lastIndex)
        }
    }

    inner class CampaignViewHolder(private val binding: SsfsItemCampaignBinding) :
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
            binding.tpgStartDate.text = campaign.startDateFormatted
            binding.tpgStartTime.text = campaign.startTime
            binding.tpgEndDate.text = campaign.endDateFormatted
            binding.tpgEndTime.text = campaign.endTime
            binding.tpgProductCount.text = campaign.summary.totalItem.toString()
            binding.tpgReminderCount.text = campaign.notifyMeCount.toString()
            binding.tpgSoldCount.text = campaign.summary.soldItem.toString()
            binding.loader.isVisible = isLoading
            binding.imgMore.isVisible = shouldEnableMoreMenu(campaign.status)
            handleTimer(campaign.status, campaign.startDate)
            handleCampaignStatusIndicator(campaign.status)
        }

        private fun handleTimer(campaignStatus: CampaignStatus, startDate: Date) {
            binding.timer.isVisible = campaignStatus == CampaignStatus.UPCOMING
            if (campaignStatus == CampaignStatus.UPCOMING) {
                binding.timer.targetDate = startDate.toCalendar()
            }
        }

        private fun handleCampaignStatusIndicator(campaignStatus: CampaignStatus) {
            when (campaignStatus) {
                CampaignStatus.UPCOMING -> {
                    binding.tpgCampaignStatus.setStatus(R.string.sfs_upcoming)
                    binding.tpgCampaignStatus.textColor(Unify_YN400)
                    binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_upcoming)
                }
                CampaignStatus.IN_SUBMISSION, CampaignStatus.IN_REVIEW, CampaignStatus.READY -> {
                    binding.tpgCampaignStatus.setStatus(R.string.sfs_available)
                    binding.tpgCampaignStatus.textColor(Unify_NN600)
                    binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_available)
                }
                CampaignStatus.ONGOING -> {
                    binding.tpgCampaignStatus.setStatus(R.string.sfs_ongoing)
                    binding.tpgCampaignStatus.textColor(Unify_GN500)
                    binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_ongoing)
                }
                CampaignStatus.FINISHED -> {
                    binding.tpgCampaignStatus.setStatus(R.string.sfs_finished)
                    binding.tpgCampaignStatus.textColor(Unify_NN400)
                    binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_finished)
                }
                CampaignStatus.CANCELLED, CampaignStatus.ONGOING_CANCELLATION -> {
                    binding.tpgCampaignStatus.setStatus(R.string.sfs_cancelled)
                    binding.tpgCampaignStatus.textColor(Unify_RN500)
                    binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_cancelled)
                }
                CampaignStatus.DRAFT -> {
                }
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

        private fun shouldEnableMoreMenu(campaignStatus: CampaignStatus): Boolean {
            return campaignStatus == CampaignStatus.UPCOMING || campaignStatus == CampaignStatus.IN_SUBMISSION || campaignStatus == CampaignStatus.IN_REVIEW || campaignStatus == CampaignStatus.READY || campaignStatus == CampaignStatus.ONGOING
        }

    }
}