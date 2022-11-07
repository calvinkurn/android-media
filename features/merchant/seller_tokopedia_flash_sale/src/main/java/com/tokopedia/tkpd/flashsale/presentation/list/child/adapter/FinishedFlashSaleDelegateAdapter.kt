package com.tokopedia.tkpd.flashsale.presentation.list.child.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.campaign.utils.constant.ImageUrlConstant
import com.tokopedia.campaign.utils.extension.setHyperlinkText
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemFinishedFlashSaleBinding
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.FinishedFlashSaleItem

class FinishedFlashSaleDelegateAdapter(private val onFlashSaleClicked : (Int) -> Unit) : DelegateAdapter<FinishedFlashSaleItem, FinishedFlashSaleDelegateAdapter.ViewHolder>(
    FinishedFlashSaleItem::class.java) {


    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = StfsItemFinishedFlashSaleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: FinishedFlashSaleItem, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding : StfsItemFinishedFlashSaleBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onFlashSaleClicked(adapterPosition) }
        }

        fun bind(item: FinishedFlashSaleItem) {
            binding.tpgCampaignName.text = item.name
            binding.imgCampaignStatusIndicator.loadRibbonImage(item)
            binding.imgFlashSale.loadImage(item.imageUrl)
            binding.tpgCampaignName.text = item.name
            binding.tpgCampaignStatus.setStatus(item)
            binding.tpgDescription.setDescription(item)
            binding.tpgPeriod.text = binding.tpgPeriod.context.getString(
                R.string.stfs_placeholder_period,
                item.formattedStartDate,
                item.formattedEndDate
            )
        }

        private fun ImageView.loadRibbonImage(item: FinishedFlashSaleItem) {
            val ribbonImageUrl: String = when (item.status) {
                FlashSaleStatus.FINISHED -> ImageUrlConstant.IMAGE_URL_RIBBON_GREEN
                FlashSaleStatus.MISSED -> ImageUrlConstant.IMAGE_URL_RIBBON_RED
                FlashSaleStatus.CANCELLED -> ImageUrlConstant.IMAGE_URL_RIBBON_RED
                FlashSaleStatus.REJECTED -> ImageUrlConstant.IMAGE_URL_RIBBON_RED
                else -> ImageUrlConstant.IMAGE_URL_RIBBON_GREY
            }

            this.loadImage(ribbonImageUrl)
        }


        private fun TextView.setStatus(item: FinishedFlashSaleItem) {
            val textColorResourceId = when (item.status) {
                FlashSaleStatus.FINISHED -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
                FlashSaleStatus.MISSED -> com.tokopedia.unifyprinciples.R.color.Unify_RN500
                FlashSaleStatus.CANCELLED -> com.tokopedia.unifyprinciples.R.color.Unify_RN500
                FlashSaleStatus.REJECTED -> com.tokopedia.unifyprinciples.R.color.Unify_RN500
                else -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            }

            this.text = item.statusText
            this.setTextColorCompat(textColorResourceId)
        }

        private fun TextView.setDescription(item: FinishedFlashSaleItem) {
            when (item.status) {
                FlashSaleStatus.FINISHED -> handleDescription(item)
                FlashSaleStatus.MISSED -> this.text = context.getString(R.string.stfs_missed_reason)
                FlashSaleStatus.CANCELLED -> this.text = context.getString(R.string.stfs_placeholder_cancellation_reason, item.cancellationReason)
                FlashSaleStatus.REJECTED -> displayAllProductAreRejected()
                else -> this.text = ""
            }
        }

        private fun handleDescription(item: FinishedFlashSaleItem) {
            when {
                item.productMeta.totalStockSold == 0 -> displayNoProductSold()
                item.productSoldPercentage > 0 -> displaySoldProductPercentage(item)
                else -> binding.tpgDescription.text = ""
            }
        }

        private fun displayNoProductSold() {
            binding.tpgDescription.text = binding.tpgDescription.context.getString(R.string.stfs_no_product_sold)
        }

        private fun displayAllProductAreRejected() {
            val wording =
                binding.tpgDescription.context.getString(R.string.stfs_all_product_rejected_with_hyperlink)
            val hyperlink = binding.tpgDescription.context.getString(R.string.stfs_check_reason)
            binding.tpgDescription.setHyperlinkText(
                wording,
                hyperlink,
                onHyperlinkClick = { onFlashSaleClicked(adapterPosition) }
            )
        }

        private fun displaySoldProductPercentage(item: FinishedFlashSaleItem) {
            binding.tpgDescription.text = binding.tpgDescription.context.getString(
                R.string.stfs_placeholder_sold_count_on_campaign_finished,
                item.productSoldPercentage,
                item.productMeta.totalStockSold,
                item.productMeta.totalProductStock
            )
        }
    }

}
