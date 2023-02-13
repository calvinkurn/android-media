package com.tokopedia.merchantvoucher.voucherList.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.merchantvoucher.R

class MerchantVoucherShimmerViewHolder(itemView:View) : AbstractViewHolder<LoadingModel>(itemView) {
    override fun bind(element: LoadingModel?) {}

    companion object{
        val LAYOUT = R.layout.item_shimmering_merchant_voucher
    }
}