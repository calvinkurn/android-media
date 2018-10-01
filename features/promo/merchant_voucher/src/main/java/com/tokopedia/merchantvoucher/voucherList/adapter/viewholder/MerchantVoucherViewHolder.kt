package com.tokopedia.merchantvoucher.voucherList.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel

/**
 * Created by hendry on 01/10/18.
 */
class MerchantVoucherViewHolder(val itemView: View) : AbstractViewHolder<MerchantVoucherViewModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_merchant_voucher
    }

    override fun bind(element: MerchantVoucherViewModel?) {
        //TODO
    }

}
