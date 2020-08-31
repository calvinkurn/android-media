package com.tokopedia.attachvoucher.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachvoucher.R
import com.tokopedia.attachvoucher.data.EmptyVoucherUiModel

class EmptyVoucherViewHolder(itemView: View?) : AbstractViewHolder<EmptyVoucherUiModel>(itemView) {
    override fun bind(element: EmptyVoucherUiModel?) {

    }

    companion object {
        val LAYOUT = R.layout.item_attachvoucher_empty_filter
    }
}