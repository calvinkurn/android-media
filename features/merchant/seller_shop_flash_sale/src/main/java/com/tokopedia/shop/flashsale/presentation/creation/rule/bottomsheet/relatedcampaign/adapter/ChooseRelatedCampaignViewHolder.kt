package com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemChooseRelatedCampaignBinding
import com.tokopedia.shop.flashsale.common.extension.disable
import com.tokopedia.shop.flashsale.common.extension.enable

class ChooseRelatedCampaignViewHolder(
    val binding: SsfsItemChooseRelatedCampaignBinding,
    private val listener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private val context: Context
        get() = itemView.context

    private val clickListener = View.OnClickListener { onItemClicked() }

    private fun enableClickListener() {
        itemView.setOnClickListener(clickListener)
        binding.checboxRelatedCampaign.setOnClickListener(clickListener)
        binding.checboxRelatedCampaign.enable()
    }

    private fun disableClickListener() {
        itemView.setOnClickListener(null)
        binding.checboxRelatedCampaign.setOnClickListener(null)
        binding.checboxRelatedCampaign.disable()
    }

    private fun onItemClicked() {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            listener.invoke(adapterPosition)
        }
    }

    @SuppressWarnings("ResourcePackage")
    fun bind(data: RelatedCampaignItem) {
        binding.checboxRelatedCampaign.isChecked = data.isSelected
        if (data.isNotSelectable) {
            disableClickListener()
        } else {
            enableClickListener()
        }
        binding.tgRelatedCampaignName.text = data.name
        binding.tgRelatedCampaignCode.text = context.getString(
            R.string.choose_related_campaign_item_campaign_code,
            data.id.toString()
        )
    }
}