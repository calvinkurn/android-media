package com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemChooseRelatedCampaignBinding

class ChooseRelatedCampaignAdapter(
    private val listener: ChooseRelatedCampaignListener,
    itemDiffer: DiffUtil.ItemCallback<RelatedCampaignItem> = PreviousCampaignItemDiffCallback()
) : ListAdapter<RelatedCampaignItem, ChooseRelatedCampaignViewHolder>(itemDiffer) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChooseRelatedCampaignViewHolder {
        return ChooseRelatedCampaignViewHolder(
            SsfsItemChooseRelatedCampaignBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            ::onItemClickedAtPosition
        )
    }

    private fun onItemClickedAtPosition(position: Int) {
        listener.onRelatedCampaignClicked(getItem(position))
    }

    override fun onBindViewHolder(holder: ChooseRelatedCampaignViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}