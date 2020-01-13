package com.tokopedia.attachvoucher.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.attachvoucher.R
import com.tokopedia.attachvoucher.data.Voucher
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import kotlinx.android.synthetic.main.item_attach_voucher.view.*

class AttachVoucherViewHolder(itemView: View?, val listener: Listener) : AbstractViewHolder<Voucher>(itemView) {

    interface Listener {

    }

    override fun bind(voucher: Voucher?) {
        if (voucher == null) return
        bindVoucherTitle(voucher)
        bindVoucherStatus(voucher)
        bindVoucherView(voucher)
    }

    private fun bindVoucherTitle(voucher: Voucher) {
        itemView.title?.text = voucher.voucherName
    }

    private fun bindVoucherStatus(voucher: Voucher) {
        val amount = voucher.availableAmount
        val validDate = DateFormatUtils.getFormattedDate(voucher.validThru, "dd MMM yyyy")
        val status = itemView.context?.getString(R.string.desc_attachvoucher_status, validDate, amount)

        itemView.validStatus?.text = status
    }

    private fun bindVoucherView(voucher: Voucher) {
        val voucherModel = MerchantVoucherViewModel(voucher)
        itemView.voucher?.setData(voucherModel)
    }

    companion object {
        val LAYOUT = R.layout.item_attach_voucher
    }
}