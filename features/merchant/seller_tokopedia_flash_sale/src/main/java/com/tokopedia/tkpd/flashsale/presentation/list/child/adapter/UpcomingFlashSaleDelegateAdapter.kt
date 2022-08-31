package com.tokopedia.tkpd.flashsale.presentation.list.child.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.campaign.utils.constant.ImageUrlConstant
import com.tokopedia.campaign.utils.extension.dimmed
import com.tokopedia.campaign.utils.extension.resetDimmedBackground
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.toCalendar
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemUpcomingFlashSaleBinding
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.UpcomingFlashSaleItem
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.Date

class UpcomingFlashSaleDelegateAdapter(private val onRegisterButtonClicked : (Int) -> Unit) : DelegateAdapter<UpcomingFlashSaleItem, UpcomingFlashSaleDelegateAdapter.UpcomingFlashSaleViewHolder>(
    UpcomingFlashSaleItem::class.java) {

    companion object{
        private const val TWENTY_FOUR_HOURS = 24
        private const val QUOTA_USAGE_HALF_FULL = 50
        private const val QUOTA_USAGE_SEVENTY_FIVE_PERCENT_USED = 75
        private const val QUOTA_USAGE_SEVENTY_SIX_PERCENT_FULL = 76
        private const val QUOTA_USAGE_ALMOST_FULL = 99
        private const val QUOTA_USAGE_FULL = 100
    }

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

        init {
            binding.root.setOnClickListener { onRegisterButtonClicked(adapterPosition) }
        }

        fun bind(item: UpcomingFlashSaleItem) {
            binding.tpgCampaignName.text = item.name
            binding.imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREY)
            binding.imgFlashSale.loadImage(item.imageUrl)
            binding.tpgCampaignName.text = item.name
            binding.tpgPeriod.text = binding.tpgPeriod.context.getString(
                R.string.stfs_placeholder_period,
                item.formattedStartDate,
                item.formattedEndDate
            )

            binding.progressBar.setValue(item.quotaUsagePercentage, isSmooth = false)
            renderQuotaUsage(item)
            startTimer(item.distanceHoursToSubmissionEndDate, item.submissionEndDate)
        }

        private fun renderQuotaUsage(item: UpcomingFlashSaleItem) {
            when {
                item.quotaUsagePercentage < QUOTA_USAGE_HALF_FULL -> {
                    binding.imgFlashSale.resetDimmedBackground()
                    binding.progressBar.progressBarColorType = ProgressBarUnify.COLOR_RED
                    binding.tpgRemainingQuota.text = binding.tpgRemainingQuota.context.getString(
                        R.string.stfs_placeholder_original_quota,
                        item.remainingQuota.splitByThousand()
                    )
                }
                item.quotaUsagePercentage in QUOTA_USAGE_HALF_FULL..QUOTA_USAGE_SEVENTY_FIVE_PERCENT_USED -> {
                    binding.imgFlashSale.resetDimmedBackground()
                    binding.progressBar.progressBarColorType = ProgressBarUnify.COLOR_RED
                    binding.tpgRemainingQuota.text = binding.tpgRemainingQuota.context.getString(
                        R.string.stfs_placeholder_remaining_quota,
                        item.remainingQuota
                    )
                }
                item.quotaUsagePercentage in QUOTA_USAGE_SEVENTY_SIX_PERCENT_FULL..QUOTA_USAGE_ALMOST_FULL -> {
                    binding.imgFlashSale.resetDimmedBackground()
                    binding.progressBar.progressBarColorType = ProgressBarUnify.COLOR_RED
                    binding.tpgRemainingQuota.text = binding.tpgRemainingQuota.context.getString(
                        R.string.stfs_placeholder_remaining_quota,
                        item.remainingQuota
                    )
                    binding.progressBar.setProgressIcon(
                        icon = ContextCompat.getDrawable(
                            binding.progressBar.context,
                            R.drawable.ic_stfs_fire
                        ),
                        width = binding.progressBar.context.resources.getDimension(R.dimen.fire_width).toIntSafely(),
                        height = binding.progressBar.context.resources.getDimension(R.dimen.fire_height).toIntSafely()
                    )
                }
                item.quotaUsagePercentage == QUOTA_USAGE_FULL -> {
                    binding.imgFlashSale.dimmed()
                    binding.btnRegister.buttonType = UnifyButton.Type.ALTERNATE
                    binding.btnRegister.buttonVariant = UnifyButton.Variant.GHOST
                    binding.btnRegister.text = binding.btnRegister.context.getString(R.string.stfs_view_detail)
                    binding.progressBar.progressDrawable.colors = intArrayOf(
                        ContextCompat.getColor(binding.progressBar.context, com.tokopedia.unifyprinciples.R.color.Unify_RN200),
                        ContextCompat.getColor(binding.progressBar.context, com.tokopedia.unifyprinciples.R.color.Unify_RN200)
                    )
                    binding.tpgRemainingQuota.text = binding.tpgRemainingQuota.context.getString(R.string.stfs_full_quota)
                }
            }
        }

        private fun startTimer(distanceHoursToSubmissionEndDate: Int, submissionEndDate: Date) {
            val onTimerFinished = {
                binding.timer.gone()
                binding.tpgCampaignStatus.text = binding.tpgCampaignStatus.context.getString(R.string.stfs_status_registration_closed)
                binding.imgFlashSale.dimmed()
                binding.btnRegister.text = binding.btnRegister.context.getString(R.string.stfs_view_detail)
                binding.btnRegister.buttonType = UnifyButton.Type.ALTERNATE
                binding.btnRegister.buttonVariant = UnifyButton.Variant.GHOST
                binding.progressBar.progressDrawable.colors = intArrayOf(
                    ContextCompat.getColor(binding.progressBar.context, com.tokopedia.unifyprinciples.R.color.Unify_RN200),
                    ContextCompat.getColor(binding.progressBar.context, com.tokopedia.unifyprinciples.R.color.Unify_RN200)
                )
                binding.tpgRemainingQuota.text = binding.tpgRemainingQuota.context.getString(R.string.stfs_status_registration_closed_alternative)
            }

            when {
                distanceHoursToSubmissionEndDate < Int.ZERO -> onTimerFinished()
                distanceHoursToSubmissionEndDate in Int.ZERO..TWENTY_FOUR_HOURS -> {
                    binding.timer.timerFormat = TimerUnifySingle.FORMAT_HOUR
                    binding.timer.timerVariant = TimerUnifySingle.VARIANT_GENERAL
                    binding.timer.targetDate = submissionEndDate.toCalendar()
                    binding.timer.onFinish = onTimerFinished
                }
                distanceHoursToSubmissionEndDate > TWENTY_FOUR_HOURS -> {
                    binding.timer.timerFormat = TimerUnifySingle.FORMAT_DAY
                    binding.timer.timerVariant = TimerUnifySingle.VARIANT_GENERAL
                    binding.timer.targetDate = submissionEndDate.toCalendar()
                    binding.timer.onFinish = onTimerFinished
                }
                else -> onTimerFinished()
            }
        }
    }

}