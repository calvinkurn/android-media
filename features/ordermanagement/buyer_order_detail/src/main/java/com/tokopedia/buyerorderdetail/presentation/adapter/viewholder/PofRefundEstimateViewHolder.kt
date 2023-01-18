package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.PofUtils.setupHyperlinkText
import com.tokopedia.buyerorderdetail.databinding.ItemPofEstimateRefundBottomsheetBinding
import com.tokopedia.buyerorderdetail.presentation.model.PofRefundEstimateUiModel

class PofRefundEstimateViewHolder(
    view: View,
    private val listener: Listener
) : CustomPayloadViewHolder<PofRefundEstimateUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_estimate_refund_bottomsheet
    }

    private val binding = ItemPofEstimateRefundBottomsheetBinding.bind(itemView)

    override fun bind(element: PofRefundEstimateUiModel) {
        with(binding) {
            setRefundEstimateLabel(element.refundEstimateLabel)
            setRefundEstimateValue(element.refundEstimateValue)
            setRefundEstimateFooter(element.pofFooterInfo)
            setRefundEstimateIcon()
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let { (oldItem, newItem) ->
            if (oldItem is PofRefundEstimateUiModel && newItem is PofRefundEstimateUiModel) {
                binding.setRefundEstimateIcon()
                if (oldItem.refundEstimateLabel != newItem.refundEstimateLabel) {
                    binding.setRefundEstimateLabel(newItem.refundEstimateLabel)
                }
                if (oldItem.refundEstimateValue != newItem.refundEstimateValue) {
                    binding.setRefundEstimateValue(newItem.refundEstimateValue)
                }
                if (oldItem.pofFooterInfo != newItem.pofFooterInfo) {
                    binding.setRefundEstimateFooter(newItem.pofFooterInfo)
                }
            }
        }
    }

    private fun ItemPofEstimateRefundBottomsheetBinding.setRefundEstimateIcon() {
        icEstimateRefundInfo.setOnClickListener {
            listener.onRefundEstimateInfoClicked()
        }
    }

    private fun ItemPofEstimateRefundBottomsheetBinding.setRefundEstimateLabel(label: String) {
        tvPofEstimateRefundLabel.text = label
    }

    private fun ItemPofEstimateRefundBottomsheetBinding.setRefundEstimateValue(value: String) {
        tvPofEstimateRefundValue.text = value
    }

    private fun ItemPofEstimateRefundBottomsheetBinding.setRefundEstimateFooter(footerInfo: String) {
        tvPofEstimateRefundFooterInfo.setupHyperlinkText(footerInfo) {
            listener.onFooterHyperlinkClicked(it)
        }
    }

    interface Listener {
        fun onRefundEstimateInfoClicked()

        fun onFooterHyperlinkClicked(linkUrl: String)
    }
}
