package com.tokopedia.merchantvoucher.voucherList.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import kotlinx.android.synthetic.main.item_merchant_voucher.view.*

/**
 * Created by hendry on 01/10/18.
 */
class MerchantVoucherViewHolder(itemView: View,
                                onMerchantVoucherViewListener: MerchantVoucherView.OnMerchantVoucherViewListener?)
    : AbstractViewHolder<MerchantVoucherViewModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_merchant_voucher
        val HORIZONTAL_LAYOUT = R.layout.item_merchant_voucher_horizontal
    }

    init {
        itemView.merchantVoucherView.onMerchantVoucherViewListener = onMerchantVoucherViewListener
    }

    override fun bind(element: MerchantVoucherViewModel?) {
        itemView.merchantVoucherView.setData(element)
    }

}
