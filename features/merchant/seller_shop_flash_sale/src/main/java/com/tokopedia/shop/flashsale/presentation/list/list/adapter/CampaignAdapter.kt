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
import com.tokopedia.shop.flashsale.common.constant.Constant.SIXTY_MINUTE
import com.tokopedia.shop.flashsale.common.extension.removeTimeZone
import com.tokopedia.shop.flashsale.common.extension.toCalendar
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.enums.*
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography


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
            binding.imgMore.isVisible = campaign.status.isActive()
            binding.timer.isVisible = campaign.status.isUpcoming()
            binding.tpgPackageInfo.handlePackageInfo(
                R.string.sfs_placeholder_quota_resource,
                campaign
            )
            binding.tpgEventRegistered.handleEventRegistered(
                R.string.sfs_placeholder_event_registered,
                campaign
            )
            handleCampaignStatusIndicator(campaign)
        }

        private fun handleCampaignStatusIndicator(campaign: CampaignUiModel) {
            when {
                campaign.status.isUpcoming() -> {
                    val statusTextResourceId =
                        if (campaign.thematicParticipation) R.string.sfs_selection else R.string.sfs_upcoming
                    binding.tpgCampaignStatus.setStatus(statusTextResourceId)
                    binding.tpgCampaignStatus.textColor(Unify_YN400)
                    binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_upcoming)
                    startTimer(campaign)
                }
                campaign.status.isAvailable() -> {
                    binding.tpgCampaignStatus.setStatus(R.string.sfs_available)
                    binding.tpgCampaignStatus.textColor(Unify_NN600)
                    binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_available)
                }
                campaign.status.isOngoing() -> {
                    binding.tpgCampaignStatus.setStatus(R.string.sfs_ongoing)
                    binding.tpgCampaignStatus.textColor(Unify_GN500)
                    binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_ongoing)
                }
                campaign.status.isFinished() -> {
                    binding.tpgCampaignStatus.setStatus(R.string.sfs_finished)
                    binding.tpgCampaignStatus.textColor(Unify_NN400)
                    binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_finished)
                }
                campaign.status.isCancelled() -> {
                    binding.tpgCampaignStatus.setStatus(R.string.sfs_cancelled)
                    binding.tpgCampaignStatus.textColor(Unify_RN500)
                    binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_cancelled)
                }
            }
        }

        private fun startTimer(campaign: CampaignUiModel) {
            if (campaign.relativeTimeDifferenceInMinute >= SIXTY_MINUTE) {
                binding.timer.timerFormat = TimerUnifySingle.FORMAT_HOUR
                binding.timer.targetDate = campaign.startDate.removeTimeZone().toCalendar()
            } else {
                binding.timer.timerFormat = TimerUnifySingle.FORMAT_MINUTE
                binding.timer.targetDate = campaign.startDate.toCalendar()
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

        private fun Typography.handlePackageInfo(
            @StringRes resourceId: Int,
            campaign: CampaignUiModel
        ) {
            val packageInfo = campaign.packageInfo.packageName
            this.isVisible = packageInfo.isNotEmpty()
            this.text = this.context.getString(resourceId, packageInfo)
        }

        private fun Typography.handleEventRegistered(
            @StringRes resourceId: Int,
            campaign: CampaignUiModel
        ) {
            val eventName = campaign.thematicInfo.name
            this.isVisible = campaign.thematicParticipation
            this.text = this.context.getString(resourceId, eventName)
        }
    }
}