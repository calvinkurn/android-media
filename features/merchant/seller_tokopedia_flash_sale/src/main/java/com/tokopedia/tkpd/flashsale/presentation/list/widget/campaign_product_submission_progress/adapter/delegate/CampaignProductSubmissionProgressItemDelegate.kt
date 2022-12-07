package com.tokopedia.tkpd.flashsale.presentation.list.widget.campaign_product_submission_progress.adapter.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsWidgetCampaignProductSubmissionProgressLayoutItemBinding
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionProgress
import com.tokopedia.tkpd.flashsale.presentation.list.widget.campaign_product_submission_progress.adapter.viewholder.CampaignProductSubmissionProgressItemViewHolder

class CampaignProductSubmissionProgressItemDelegate(private val listener: CampaignProductSubmissionProgressItemViewHolder.Listener) :
    DelegateAdapter<FlashSaleProductSubmissionProgress.Campaign, CampaignProductSubmissionProgressItemViewHolder>(
        FlashSaleProductSubmissionProgress.Campaign::class.java
    ) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = StfsWidgetCampaignProductSubmissionProgressLayoutItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CampaignProductSubmissionProgressItemViewHolder(binding, listener)
    }

    override fun bindViewHolder(
        item: FlashSaleProductSubmissionProgress.Campaign,
        viewHolder: CampaignProductSubmissionProgressItemViewHolder
    ) {
        viewHolder.bind(item)
    }

}
