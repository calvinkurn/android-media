package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.PlainHeaderUiModel
import com.tokopedia.unifyprinciples.Typography

class PlainHeaderViewHolder(itemView: View?) : AbstractViewHolder<PlainHeaderUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_plain_header
    }

    override fun bind(element: PlainHeaderUiModel?) {
        element?.let {
            setupHeader(element.header)
        }
    }

    override fun bind(element: PlainHeaderUiModel?, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setupHeader(header: String) {
        (itemView as? Typography)?.text = header
    }
}