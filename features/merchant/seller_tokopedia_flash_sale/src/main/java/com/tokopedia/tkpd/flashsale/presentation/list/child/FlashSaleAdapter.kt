package com.tokopedia.tkpd.flashsale.presentation.list.child

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemRegisteredFlashSaleBinding
import com.tokopedia.tkpd.flashsale.domain.entity.Campaign


class FlashSaleAdapter : RecyclerView.Adapter<FlashSaleAdapter.CampaignViewHolder>() {

    private var onItemClicked: (Campaign) -> Unit = {}
    private var isLoading = false

    private val differCallback = object : DiffUtil.ItemCallback<Campaign>() {
        override fun areItemsTheSame(
            oldItem: Campaign,
            newItem: Campaign
        ): Boolean {
            return oldItem.campaignId == newItem.campaignId
        }

        override fun areContentsTheSame(
            oldItem: Campaign,
            newItem: Campaign
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignViewHolder {
        val binding = StfsItemRegisteredFlashSaleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CampaignViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: CampaignViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        viewHolder.bind(position, currentItem, onItemClicked, isLoading)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    fun setOnItemClicked(onItemClicked: (Campaign) -> Unit) {
        this.onItemClicked = onItemClicked
    }


    fun submit(newItems: List<Campaign>) {
        differ.submitList(newItems)
    }

    fun snapshot(): List<Campaign> {
        return differ.currentList
    }

    fun showLoading() {
        if (itemCount.isMoreThanZero()) {
            isLoading = true
            notifyItemChanged(differ.currentList.lastIndex)
        }
    }

    fun hideLoading() {
        isLoading = false
        if (itemCount.isMoreThanZero()) {
            notifyItemChanged(differ.currentList.lastIndex)
        }
    }

    inner class CampaignViewHolder(private val binding: StfsItemRegisteredFlashSaleBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(
            position: Int,
            campaign: Campaign,
            onCampaignClicked: (Campaign) -> Unit,
            isLoading: Boolean
        ) {
            binding.imgFlashSale.loadImage(campaign.coverImage)
            binding.tpgCampaignStatus.text = campaign.statusText
            binding.tpgCampaignName.text = campaign.name

            //handleCampaignStatusIndicator(campaign)
        }

        /*private fun handleCampaignStatusIndicator(campaign: Campaign) {
            when {
                campaign.status.isUpcoming() -> {
                    val statusTextResourceId = if (campaign.thematicParticipation) R.string.sfs_selection else R.string.sfs_upcoming
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

        private fun startTimer(campaign: Campaign) {
            if (campaign.relativeTimeDifferenceInMinute >= SIXTY_MINUTE) {
                binding.timer.timerFormat = TimerUnifySingle.FORMAT_HOUR
                binding.timer.targetDate = campaign.startDate.removeTimeZone().toCalendar()
            } else {
                binding.timer.timerFormat =  TimerUnifySingle.FORMAT_MINUTE
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
        }*/
    }
}