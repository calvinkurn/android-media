package com.tokopedia.attachvoucher.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachvoucher.R
import com.tokopedia.attachvoucher.data.Voucher

class AttachVoucherViewHolder(itemView: View?, val listener: Listener) : AbstractViewHolder<Voucher>(itemView) {

    interface Listener {

    }

    override fun bind(element: Voucher?) {
        if (element == null) return
    }

    companion object {
        val LAYOUT = R.layout.item_attach_voucher
    }
}