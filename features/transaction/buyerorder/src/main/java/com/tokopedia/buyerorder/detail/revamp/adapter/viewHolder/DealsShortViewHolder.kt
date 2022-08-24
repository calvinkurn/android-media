package com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder

import android.view.View
import androidx.annotation.LayoutRes
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.VoucherItemCardDealsShortBinding
import com.tokopedia.buyerorder.detail.data.ItemsDealsShort
import com.tokopedia.buyerorder.detail.data.MetaDataInfo

/**
 * created by @bayazidnasir on 23/8/2022
 */

class DealsShortViewHolder(itemView: View) : AbstractViewHolder<ItemsDealsShort>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.voucher_item_card_deals_short
    }

    override fun bind(element: ItemsDealsShort) {
        val binding = VoucherItemCardDealsShortBinding.bind(itemView)
        val metadata = Gson().fromJson(element.item.metaData, MetaDataInfo::class.java)

        binding.tvDealIntro.text = metadata.entityProductName.ifEmpty { element.item.title }
        binding.tvBrandName.text = metadata.entityBrandName
        binding.tvRedeemLocations.text = metadata.entityAddress.name
    }
}