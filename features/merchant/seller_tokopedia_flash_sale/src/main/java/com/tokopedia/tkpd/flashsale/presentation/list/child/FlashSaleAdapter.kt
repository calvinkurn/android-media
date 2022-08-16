package com.tokopedia.tkpd.flashsale.presentation.list.child

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.utils.constant.ImageUrlConstant
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemRegisteredFlashSaleBinding
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.seller_tokopedia_flash_sale.R

class FlashSaleAdapter : RecyclerView.Adapter<FlashSaleAdapter.RegisteredFlashSaleViewHolder>() {

    private var onItemClicked: (FlashSale) -> Unit = {}
    private var isLoading = false

    companion object {

    }
    private val differCallback = object : DiffUtil.ItemCallback<FlashSale>() {
        override fun areItemsTheSame(
            oldItem: FlashSale,
            newItem: FlashSale
        ): Boolean {
            return oldItem.campaignId == newItem.campaignId
        }

        override fun areContentsTheSame(
            oldItem: FlashSale,
            newItem: FlashSale
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisteredFlashSaleViewHolder {
        val binding = StfsItemRegisteredFlashSaleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RegisteredFlashSaleViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: RegisteredFlashSaleViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        viewHolder.bind(position, currentItem, onItemClicked, isLoading)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    fun setOnItemClicked(onItemClicked: (FlashSale) -> Unit) {
        this.onItemClicked = onItemClicked
    }


    fun submit(newItems: List<FlashSale>) {
        differ.submitList(newItems)
    }

    fun snapshot(): List<FlashSale> {
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

    inner class RegisteredFlashSaleViewHolder(private val binding: StfsItemRegisteredFlashSaleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnCreateCampaign.setOnClickListener {  }
        }

        fun bind(
            position: Int,
            flashSale: FlashSale,
            onCampaignClicked: (FlashSale) -> Unit,
            isLoading: Boolean
        ) {
            binding.imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREY)
            binding.imgFlashSale.loadImage(flashSale.coverImage)
            binding.tpgCampaignStatus.text = flashSale.statusText
            binding.tpgCampaignName.text = flashSale.name
            binding.tpgPeriod.text = binding.tpgPeriod.context.getString(
                R.string.stfs_placeholder_period,
                flashSale.formattedDate.startDate,
                flashSale.formattedDate.endDate
            )

            //handleCampaignStatusIndicator(campaign)
        }


        private fun Typography.setPeriod(@StringRes resourceId: Int) {


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