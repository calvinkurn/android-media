package com.tokopedia.shop.flashsale.presentation.creation.rule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemRelatedCampaignBinding
import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign

class RelatedCampaignAdapter(
    private val listener: OnRemoveRelatedCampaignListener,
    diffUtil: DiffUtil.ItemCallback<RelatedCampaign> = DEFAULT_DIFF_UTIL,
) : ListAdapter<RelatedCampaign, RelatedCampaignViewHolder>(diffUtil) {
    companion object {
        private val DEFAULT_DIFF_UTIL = object : DiffUtil.ItemCallback<RelatedCampaign>() {
            override fun areItemsTheSame(
                oldItem: RelatedCampaign,
                newItem: RelatedCampaign
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RelatedCampaign,
                newItem: RelatedCampaign
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedCampaignViewHolder {
        val binding = SsfsItemRelatedCampaignBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RelatedCampaignViewHolder(binding).apply {
            binding.root.setOnRemoveListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onRelatedCampaignRemoved(getItem(adapterPosition))
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RelatedCampaignViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}