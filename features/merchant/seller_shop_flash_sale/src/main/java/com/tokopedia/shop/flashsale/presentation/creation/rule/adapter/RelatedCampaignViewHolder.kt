package com.tokopedia.shop.flashsale.presentation.creation.rule.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemRelatedCampaignBinding
import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign

class RelatedCampaignViewHolder(
    private val binding: SsfsItemRelatedCampaignBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(relatedCampaign: RelatedCampaign) {
        binding.root.chipText = relatedCampaign.name
    }
}