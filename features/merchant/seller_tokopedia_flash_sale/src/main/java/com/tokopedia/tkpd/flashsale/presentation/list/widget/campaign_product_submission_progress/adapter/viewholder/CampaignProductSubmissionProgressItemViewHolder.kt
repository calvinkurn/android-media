package com.tokopedia.tkpd.flashsale.presentation.list.widget.campaign_product_submission_progress.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsWidgetCampaignProductSubmissionProgressLayoutItemBinding
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionProgress

class CampaignProductSubmissionProgressItemViewHolder(
    private val binding: StfsWidgetCampaignProductSubmissionProgressLayoutItemBinding,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

    interface Listener {
        fun onCampaignItemClicked(campaignData: FlashSaleProductSubmissionProgress.Campaign)
    }

    fun bind(uiModel: FlashSaleProductSubmissionProgress.Campaign) {
        binding.apply {
            imageCampaign.loadImage(uiModel.campaignPicture)
            campaignName.text = uiModel.campaignName
        }
        itemView.setOnClickListener { listener.onCampaignItemClicked(uiModel) }
    }

}
