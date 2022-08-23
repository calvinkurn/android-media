package com.tokopedia.tkpd.flashsale.presentation.list.child.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.campaign.utils.constant.ImageUrlConstant
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemOngoingFlashSaleBinding
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.OngoingFlashSaleItem

class OngoingFlashSaleDelegateAdapter : DelegateAdapter<OngoingFlashSaleItem, OngoingFlashSaleDelegateAdapter.OngoingFlashSaleViewHolder>(
    OngoingFlashSaleItem::class.java) {
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

        fun bind(item: OngoingFlashSaleItem) {
            binding.tpgCampaignName.text = item.name
            binding.imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREY)
            /*binding.imgFlashSale.loadImage(item.coverImage)
            binding.tpgCampaignStatus.text = item.statusText
            binding.tpgCampaignName.text = item.name
            binding.tpgPeriod.text = binding.tpgPeriod.context.getString(
                R.string.stfs_placeholder_period,
                item.formattedDate.startDate,
                item.formattedDate.endDate
            )*/

        }

    }

}