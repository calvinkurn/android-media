package com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign.adapter

import androidx.recyclerview.widget.DiffUtil

class PreviousCampaignItemDiffCallback : DiffUtil.ItemCallback<RelatedCampaignItem>() {
    override fun areItemsTheSame(
        oldItem: RelatedCampaignItem,
        newItem: RelatedCampaignItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: RelatedCampaignItem,
        newItem: RelatedCampaignItem
    ): Boolean {
        return oldItem == newItem
    }
}