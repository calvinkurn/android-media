package com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.campaign.databinding.LayoutGlobalErrorCampaignCommonBinding
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.item.FlashSaleManageProductListGlobalErrorItem
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.viewholder.FlashSaleManageProductListGlobalErrorViewHolder

class FlashSaleManageProductListItemGlobalErrorDelegate(
        private val listener: FlashSaleManageProductListGlobalErrorViewHolder.Listener
) :
    DelegateAdapter<FlashSaleManageProductListGlobalErrorItem, FlashSaleManageProductListGlobalErrorViewHolder>(
        FlashSaleManageProductListGlobalErrorItem::class.java
    ) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = LayoutGlobalErrorCampaignCommonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FlashSaleManageProductListGlobalErrorViewHolder(binding, listener)
    }

    override fun bindViewHolder(
        item: FlashSaleManageProductListGlobalErrorItem,
        viewHolder: FlashSaleManageProductListGlobalErrorViewHolder
    ) {
        viewHolder.bind(item)
    }

}