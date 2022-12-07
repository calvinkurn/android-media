package com.tokopedia.tkpd.flashsale.common.bottomsheet.sse_submission_error.adapter.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsProductListSubmissionErrorShimmeringLayoutBinding
import com.tokopedia.tkpd.flashsale.common.bottomsheet.sse_submission_error.adapter.item.ProductSseSubmissionErrorShimmeringItem
import com.tokopedia.tkpd.flashsale.common.bottomsheet.sse_submission_error.adapter.viewholder.ProductSseSubmissionErrorItemShimmeringViewHolder

class FlashSaleProductListSseSubmissionErrorItemShimmeringDelegate :
    DelegateAdapter<ProductSseSubmissionErrorShimmeringItem, ProductSseSubmissionErrorItemShimmeringViewHolder>(
        ProductSseSubmissionErrorShimmeringItem::class.java
    ) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = StfsProductListSubmissionErrorShimmeringLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductSseSubmissionErrorItemShimmeringViewHolder(binding)
    }

    override fun bindViewHolder(
        item: ProductSseSubmissionErrorShimmeringItem,
        viewHolder: ProductSseSubmissionErrorItemShimmeringViewHolder
    ) {}

}
