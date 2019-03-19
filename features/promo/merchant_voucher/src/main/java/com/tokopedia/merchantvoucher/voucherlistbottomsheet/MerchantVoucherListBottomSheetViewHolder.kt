package com.tokopedia.merchantvoucher.voucherlistbottomsheet

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import kotlinx.android.synthetic.main.item_merchant_voucher_used.view.*

/**
 * Created by fwidjaja on 10/03/19.
 */
class MerchantVoucherListBottomSheetViewHolder(itemView: View,
                                               onMerchantVoucherViewListener: MerchantVoucherView.OnMerchantVoucherViewListener?)
    : AbstractViewHolder<MerchantVoucherViewModel>(itemView) {

    companion object {
        val ITEM_MERCHANT_VOUCHER = R.layout.item_merchant_voucher_used
    }

    init {
        itemView.merchantVoucherView.onMerchantVoucherViewListener = onMerchantVoucherViewListener
    }

    override fun bind(element: MerchantVoucherViewModel?) {
        itemView.merchantVoucherView.setData(element)
    }

}