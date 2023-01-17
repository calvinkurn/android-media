package com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductListItemBinding
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.item.FlashSaleManageProductListItem
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.viewholder.FlashSaleManageProductListItemViewHolder

class FlashSaleManageProductListItemDelegate(
    private val listener: FlashSaleManageProductListItemViewHolder.Listener
) :
    DelegateAdapter<FlashSaleManageProductListItem, FlashSaleManageProductListItemViewHolder>(
        FlashSaleManageProductListItem::class.java
    ) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = LayoutCampaignManageProductListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FlashSaleManageProductListItemViewHolder(binding, listener)
    }

    override fun bindViewHolder(
        item: FlashSaleManageProductListItem,
        viewHolder: FlashSaleManageProductListItemViewHolder
    ) {
        viewHolder.bind(item)
    }

}