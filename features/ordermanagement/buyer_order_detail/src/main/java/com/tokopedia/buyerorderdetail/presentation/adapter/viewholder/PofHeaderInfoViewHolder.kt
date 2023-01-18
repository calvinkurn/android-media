package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.PofUtils.setupHyperlinkText
import com.tokopedia.buyerorderdetail.databinding.ItemFulfilledPartialOrderHeaderBinding
import com.tokopedia.buyerorderdetail.databinding.ItemPartialOrderFulfillmentHeaderBinding
import com.tokopedia.buyerorderdetail.presentation.model.PofHeaderInfoUiModel

class PofHeaderInfoViewHolder(
    private val view: View,
    private val listener: Listener
): CustomPayloadViewHolder<PofHeaderInfoUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_fulfilled_partial_order_header
    }

    private val binding = ItemFulfilledPartialOrderHeaderBinding.bind(itemView)

    override fun bind(element: PofHeaderInfoUiModel) {
        binding.setHeaderInfo(element.headerInfoHtmlStr)
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let { (oldItem, newItem) ->
            if (oldItem is PofHeaderInfoUiModel && newItem is PofHeaderInfoUiModel) {
                if (oldItem.headerInfoHtmlStr != newItem.headerInfoHtmlStr) {
                    binding.setHeaderInfo(newItem.headerInfoHtmlStr)
                }
            }
        }
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
