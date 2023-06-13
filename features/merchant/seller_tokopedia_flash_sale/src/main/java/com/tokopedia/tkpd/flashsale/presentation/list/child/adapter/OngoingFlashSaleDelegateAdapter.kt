package com.tokopedia.tkpd.flashsale.presentation.list.child.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.campaign.utils.constant.ImageUrlConstant
import com.tokopedia.campaign.utils.constant.ImageUrlConstant.IMAGE_URL_RIBBON_GREEN
import com.tokopedia.campaign.utils.constant.ImageUrlConstant.IMAGE_URL_RIBBON_RED
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.toCalendar
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemOngoingFlashSaleBinding
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.OngoingFlashSaleItem
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.Date

class OngoingFlashSaleDelegateAdapter(private val onFlashSaleClicked : (Int) -> Unit) : DelegateAdapter<OngoingFlashSaleItem, OngoingFlashSaleDelegateAdapter.OngoingFlashSaleViewHolder>(
    OngoingFlashSaleItem::class.java) {

    private val now = Date()

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = StfsItemOngoingFlashSaleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OngoingFlashSaleViewHolder(binding)
    }

    override fun bindViewHolder(item: OngoingFlashSaleItem, viewHolder: OngoingFlashSaleViewHolder) {
        viewHolder.bind(item)
    }

    inner class OngoingFlashSaleViewHolder(private val binding : StfsItemOngoingFlashSaleBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onFlashSaleClicked(adapterPosition) }
        }

        fun bind(item: OngoingFlashSaleItem) {
            binding.tpgCampaignName.text = item.name
            binding.tpgCampaignStatus.setStatus(item)
            binding.imgCampaignStatusIndicator.loadRibbonImage(item)
            binding.tpgSoldStockCount.setSoldCount(item)
            binding.imgFlashSale.loadImage(item.imageUrl)
            binding.tpgCampaignName.text = item.name
            binding.tpgPeriod.text = binding.tpgPeriod.context.getString(
                R.string.stfs_placeholder_period,
                item.formattedStartDate,
                item.formattedEndDate
            )
            binding.timer.setTimer(item)
        }

        private fun TextView.setStatus(item: OngoingFlashSaleItem) {
            val textColorResourceId = when (item.status) {
                FlashSaleStatus.ONGOING -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
                FlashSaleStatus.REJECTED -> com.tokopedia.unifyprinciples.R.color.Unify_RN400
                else -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            }

            this.text = when (item.status) {
                FlashSaleStatus.ONGOING -> context.getString(R.string.stfs_status_ongoing)
                FlashSaleStatus.REJECTED -> item.statusText
                else -> item.statusText
            }

            this.setTextColorCompat(textColorResourceId)
        }

        private fun TextView.setSoldCount(item: OngoingFlashSaleItem) {
            this.text = when (item.status) {
                FlashSaleStatus.ONGOING -> MethodChecker.fromHtml(context.getString(R.string.stfs_placeholder_sold_count, item.totalStockSold, item.totalProductStock))
                FlashSaleStatus.REJECTED -> context.getString(R.string.stfs_all_product_rejected)
                else -> ""
            }

            if (item.status == FlashSaleStatus.REJECTED) {
                this.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_RN500)
            }
        }


        private fun ImageView.loadRibbonImage(item: OngoingFlashSaleItem) {
            val ribbonImageUrl: String = when (item.status) {
                FlashSaleStatus.ONGOING -> IMAGE_URL_RIBBON_GREEN
                FlashSaleStatus.REJECTED -> IMAGE_URL_RIBBON_RED
                else -> ImageUrlConstant.IMAGE_URL_RIBBON_GREY
            }

            this.loadImage(ribbonImageUrl)
        }


        private fun TimerUnifySingle.setTimer(item: OngoingFlashSaleItem) {
            if (now.after(item.endDate)) {
                invisible()
                binding.tpgDescription.text = binding.tpgDescription.context.getString(R.string.stfs_status_registration_already_closed)
            } else {
                binding.timer.visible()
                binding.timer.timerFormat = TimerUnifySingle.FORMAT_AUTO
                binding.timer.timerVariant = TimerUnifySingle.VARIANT_GENERAL
                binding.timer.targetDate = item.endDate.toCalendar()
            }
        }
    }

}
