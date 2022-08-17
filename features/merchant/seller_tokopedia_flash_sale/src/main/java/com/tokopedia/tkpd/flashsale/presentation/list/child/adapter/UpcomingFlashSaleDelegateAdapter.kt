package com.tokopedia.tkpd.flashsale.presentation.list.child.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.campaign.utils.constant.ImageUrlConstant
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.toCalendar
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemUpcomingFlashSaleBinding
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.UpcomingFlashSaleItem
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.Date

class UpcomingFlashSaleDelegateAdapter : DelegateAdapter<UpcomingFlashSaleItem, UpcomingFlashSaleDelegateAdapter.UpcomingFlashSaleViewHolder>(
    UpcomingFlashSaleItem::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = StfsItemUpcomingFlashSaleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UpcomingFlashSaleViewHolder(binding)
    }

    override fun bindViewHolder(item: UpcomingFlashSaleItem, viewHolder: UpcomingFlashSaleViewHolder) {
        viewHolder.bind(item)
    }

    inner class UpcomingFlashSaleViewHolder(private val binding : StfsItemUpcomingFlashSaleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UpcomingFlashSaleItem) {
            binding.tpgCampaignName.text = item.name
            binding.imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREY)
            binding.imgFlashSale.loadImage(item.imageUrl)
            binding.tpgCampaignStatus.text = when (item.status) {
                UpcomingFlashSaleItem.Status.REGISTRATION_OPEN -> binding.tpgCampaignStatus.context.getString(R.string.stfs_status_registration_ended)
                UpcomingFlashSaleItem.Status.REGISTRATION_CLOSED -> binding.tpgCampaignStatus.context.getString(R.string.stfs_status_registration_closed)
            }
            binding.tpgCampaignName.text = item.name
            binding.tpgPeriod.text = binding.tpgPeriod.context.getString(
                R.string.stfs_placeholder_period,
                item.formattedStartDate,
                item.formattedEndDate
            )

            binding.progressBar.setValue(item.quotaUsagePercentage)
            renderQuotaUsageDescription(item)
            handleTimer(item.status, item.distanceDaysToReviewStartDate, item.reviewStartDate)
        }

        private fun renderQuotaUsageDescription(item: UpcomingFlashSaleItem) {
            when {
                item.quotaUsagePercentage < 50 -> {
                    binding.tpgRemainingQuota.text = binding.tpgRemainingQuota.context.getString(
                        R.string.stfs_placeholder_original_quota,
                        item.maxProductSubmission.splitByThousand()
                    )
                }
                item.quotaUsagePercentage in 50..75 -> {
                    binding.tpgRemainingQuota.text = binding.tpgRemainingQuota.context.getString(
                        R.string.stfs_placeholder_remaining_quota,
                        item.remainingQuota
                    )
                }
                item.quotaUsagePercentage in 76..99 -> {
                    //add fire icon
                    //binding.progressBar.setProgressIcon()
                    binding.tpgRemainingQuota.text = binding.tpgRemainingQuota.context.getString(
                        R.string.stfs_placeholder_remaining_quota,
                        item.remainingQuota
                    )
                }
                item.quotaUsagePercentage == 100 -> {
                    //change quota progressbar to red color
                    binding.tpgRemainingQuota.text = binding.tpgRemainingQuota.context.getString(R.string.stfs_full_quota)
                }
            }
        }

        private fun handleTimer(
            status: UpcomingFlashSaleItem.Status,
            distanceDaysToReviewStartDate: Int,
            reviewStartDate: Date
        ) {
            if (status == UpcomingFlashSaleItem.Status.REGISTRATION_CLOSED) {
                binding.timer.gone()
            } else {
                binding.timer.visible()
                startTimer(distanceDaysToReviewStartDate, reviewStartDate)
            }
        }

        private fun startTimer(distanceDaysToReviewStartDate: Int, reviewStartDate: Date) {
            if (distanceDaysToReviewStartDate > 1) {
                binding.timer.timerFormat = TimerUnifySingle.FORMAT_DAY
                binding.timer.targetDate = reviewStartDate.toCalendar()
            } else {
                binding.timer.timerFormat = TimerUnifySingle.FORMAT_HOUR
                binding.timer.targetDate = reviewStartDate.toCalendar()
            }
        }
    }

}