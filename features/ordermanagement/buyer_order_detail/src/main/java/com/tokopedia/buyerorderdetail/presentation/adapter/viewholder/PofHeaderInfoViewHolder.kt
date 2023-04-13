package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.PofUtils.setupHyperlinkText
import com.tokopedia.buyerorderdetail.databinding.ItemFulfilledPartialOrderHeaderBinding
import com.tokopedia.buyerorderdetail.presentation.model.PofHeaderInfoUiModel

class PofHeaderInfoViewHolder(
    view: View,
    private val listener: Listener
) : AbstractViewHolder<PofHeaderInfoUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_fulfilled_partial_order_header
    }

    private val binding = ItemFulfilledPartialOrderHeaderBinding.bind(itemView)

    override fun bind(element: PofHeaderInfoUiModel) {
        binding.setHeaderInfo(element.headerInfoHtmlStr)
    }

    private fun ItemFulfilledPartialOrderHeaderBinding.setHeaderInfo(pofHeaderStr: String) {
        tvPartialOrderFulfillmentHeader.setupHyperlinkText(pofHeaderStr) {
            listener.onHeaderHyperlinkClicked(it)
        }
    }

    interface Listener {
        fun onHeaderHyperlinkClicked(linkUrl: String)
    }
}
