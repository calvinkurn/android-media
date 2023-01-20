package com.tokopedia.tkpd.flashsale.common.bottomsheet.sse_submission_error.adapter.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductSseSubmissionErrorBinding
import com.tokopedia.tkpd.flashsale.common.bottomsheet.sse_submission_error.adapter.viewholder.ProductSseSubmissionErrorItemViewHolder
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionProgress

class FlashSaleProductListSseSubmissionErrorItemDelegate :
    DelegateAdapter<FlashSaleProductSubmissionProgress.CampaignProductError, ProductSseSubmissionErrorItemViewHolder>(
        FlashSaleProductSubmissionProgress.CampaignProductError::class.java
    ) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = StfsItemProductSseSubmissionErrorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductSseSubmissionErrorItemViewHolder(binding)
    }

    override fun bindViewHolder(
        item: FlashSaleProductSubmissionProgress.CampaignProductError,
        viewHolder: ProductSseSubmissionErrorItemViewHolder
    ) {
        viewHolder.bind(item)
    }

}
