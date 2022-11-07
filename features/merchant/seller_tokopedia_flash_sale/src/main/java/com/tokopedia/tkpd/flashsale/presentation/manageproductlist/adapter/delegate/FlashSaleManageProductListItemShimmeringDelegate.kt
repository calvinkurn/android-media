package com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsManageProductListShimmeringLayoutBinding
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.item.FlashSaleManageProductListShimmeringItem
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.viewholder.FlashSaleManageProductListShimmeringItemViewHolder

class FlashSaleManageProductListItemShimmeringDelegate :
    DelegateAdapter<FlashSaleManageProductListShimmeringItem, FlashSaleManageProductListShimmeringItemViewHolder>(
        FlashSaleManageProductListShimmeringItem::class.java
    ) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = StfsManageProductListShimmeringLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FlashSaleManageProductListShimmeringItemViewHolder(binding)
    }

    override fun bindViewHolder(
        item: FlashSaleManageProductListShimmeringItem,
        viewHolder: FlashSaleManageProductListShimmeringItemViewHolder
    ) {}

}