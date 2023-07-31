package com.tokopedia.merchantvoucher.voucherlistbottomsheet

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherViewUsed

/**
 * Created by fwidjaja on 10/03/19.
 */
class MerchantVoucherListBottomSheetViewHolder(itemView: View,
                                               onMerchantVoucherViewListener: MerchantVoucherViewUsed.OnMerchantVoucherViewListener?)
    : AbstractViewHolder<MerchantVoucherViewModel>(itemView) {

    var merchantVoucherViewUsed: MerchantVoucherViewUsed ?= null
    companion object {
        val ITEM_MERCHANT_VOUCHER = R.layout.item_merchant_voucher_used
    }

    init {
        merchantVoucherViewUsed = itemView.findViewById(R.id.merchantVoucherView)
        merchantVoucherViewUsed?.onMerchantVoucherViewListener = onMerchantVoucherViewListener
    }

    override fun bind(element: MerchantVoucherViewModel?) {
        merchantVoucherViewUsed?.setData(element)
    }

}