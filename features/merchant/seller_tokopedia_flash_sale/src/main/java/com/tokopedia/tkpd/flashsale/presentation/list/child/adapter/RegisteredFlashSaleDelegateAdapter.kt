package com.tokopedia.tkpd.flashsale.presentation.list.child.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.campaign.utils.constant.ImageUrlConstant
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.toCalendar
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemRegisteredFlashSaleBinding
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.RegisteredFlashSaleItem
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.Date

class RegisteredFlashSaleDelegateAdapter: DelegateAdapter<RegisteredFlashSaleItem, RegisteredFlashSaleDelegateAdapter.ViewHolder>
    (RegisteredFlashSaleItem::class.java) {

    companion object{
        private const val TWENTY_FOUR_HOURS = 24
    }

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = StfsItemRegisteredFlashSaleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: RegisteredFlashSaleItem, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding : StfsItemRegisteredFlashSaleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RegisteredFlashSaleItem) {
            binding.tpgCampaignName.text = item.name
            binding.tpgCampaignStatus.setStatus(item)
            binding.imgCampaignStatusIndicator.loadRibbonImage(item)
            binding.btnAddProduct.setButtonAppearance(item)
            binding.imgFlashSale.loadImage(item.imageUrl)
            binding.tpgCampaignName.text = item.name
            binding.tpgPeriod.text = binding.tpgPeriod.context.getString(
                R.string.stfs_placeholder_period,
                item.formattedStartDate,
                item.formattedEndDate
            )
            binding.tpgDescription.setDescription(item)
            binding.timer.setTimer(item)
        }

        private fun TextView.setDescription(item: RegisteredFlashSaleItem) {
            val description: String = when (item.status) {
                FlashSaleStatus.NO_REGISTERED_PRODUCT -> context.getString(R.string.stfs_registration_ends_in)
                FlashSaleStatus.WAITING_FOR_SELECTION -> context.getString(R.string.stfs_selection_starts_in)
                FlashSaleStatus.ON_SELECTION_PROCESS -> context.getString(R.string.stfs_selection_ends_in)
                FlashSaleStatus.SELECTION_FINISHED -> context.getString(R.string.stfs_campaign_starts_in)
                else -> ImageUrlConstant.IMAGE_URL_RIBBON_GREY
            }

            this.text = description
        }

        private fun UnifyButton.setButtonAppearance(item: RegisteredFlashSaleItem) {
            when (item.status) {
                FlashSaleStatus.NO_REGISTERED_PRODUCT -> {
                    buttonType = UnifyButton.Type.ALTERNATE
                    buttonVariant = UnifyButton.Variant.FILLED
                    text = context.getString(R.string.stfs_add_product)
                }
                FlashSaleStatus.WAITING_FOR_SELECTION -> {
                    buttonType = UnifyButton.Type.MAIN
                    buttonVariant = UnifyButton.Variant.GHOST
                    text = context.getString(R.string.stfs_change_product)
                }
                FlashSaleStatus.ON_SELECTION_PROCESS -> {
                    buttonType = UnifyButton.Type.ALTERNATE
                    buttonVariant = UnifyButton.Variant.GHOST
                    text = context.getString(R.string.stfs_view_detail)
                }
                FlashSaleStatus.SELECTION_FINISHED -> {
                    buttonType = UnifyButton.Type.ALTERNATE
                    buttonVariant = UnifyButton.Variant.GHOST
                    text = context.getString(R.string.stfs_view_detail)
                }
                else -> {
                    buttonType = UnifyButton.Type.ALTERNATE
                    buttonVariant = UnifyButton.Variant.GHOST
                    text = context.getString(R.string.stfs_view_detail)
                }
            }

        }

        private fun TextView.setStatus(item: RegisteredFlashSaleItem) {
            val textColorResourceId = when (item.status) {
                FlashSaleStatus.NO_REGISTERED_PRODUCT -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
                FlashSaleStatus.WAITING_FOR_SELECTION -> com.tokopedia.unifyprinciples.R.color.Unify_YN400
                FlashSaleStatus.ON_SELECTION_PROCESS -> com.tokopedia.unifyprinciples.R.color.Unify_BN500
                FlashSaleStatus.SELECTION_FINISHED -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
                else -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            }

            this.text = item.statusText
            this.setTextColorCompat(textColorResourceId)
        }

        private fun ImageView.loadRibbonImage(item: RegisteredFlashSaleItem) {
            val ribbonImageUrl: String = when (item.status) {
                FlashSaleStatus.NO_REGISTERED_PRODUCT -> ImageUrlConstant.IMAGE_URL_RIBBON_GREY
                FlashSaleStatus.WAITING_FOR_SELECTION -> ImageUrlConstant.IMAGE_URL_RIBBON_ORANGE
                FlashSaleStatus.ON_SELECTION_PROCESS -> ImageUrlConstant.IMAGE_URL_RIBBON_BLUE
                FlashSaleStatus.SELECTION_FINISHED -> ImageUrlConstant.IMAGE_URL_RIBBON_GREEN
                else -> ImageUrlConstant.IMAGE_URL_RIBBON_GREY
            }

            this.loadImage(ribbonImageUrl)
        }


        private fun TimerUnifySingle.setTimer(item: RegisteredFlashSaleItem) {
            when (item.status) {
                FlashSaleStatus.NO_REGISTERED_PRODUCT -> startTimer(this, item.distanceHoursToReviewStartDate,  item.reviewStartDate)
                FlashSaleStatus.WAITING_FOR_SELECTION -> startTimer(this, item.distanceHoursToReviewStartDate,  item.reviewStartDate)
                FlashSaleStatus.ON_SELECTION_PROCESS -> startTimer(this, item.distanceHoursToReviewEndDate,  item.reviewEndDate)
                FlashSaleStatus.SELECTION_FINISHED -> startTimer(this, item.distanceHourToStartDate,  item.startDate)
                else -> {}
            }
        }

        private fun startTimer(timer: TimerUnifySingle, distanceHourToEndDate: Int, endDate: Date) {
            when {
                distanceHourToEndDate < Int.ZERO -> {
                    timer.invisible()
                    binding.tpgDescription.text = binding.tpgDescription.context.getString(R.string.stfs_status_registration_already_closed)
                }
                distanceHourToEndDate in Int.ZERO..TWENTY_FOUR_HOURS -> {
                    timer.timerFormat = TimerUnifySingle.FORMAT_HOUR
                    timer.timerVariant = TimerUnifySingle.VARIANT_GENERAL
                    timer.targetDate = endDate.toCalendar()
                }
                distanceHourToEndDate > TWENTY_FOUR_HOURS -> {
                    timer.timerFormat = TimerUnifySingle.FORMAT_DAY
                    timer.targetDate = endDate.toCalendar()
                }
                else -> {}
            }
        }
    }



}