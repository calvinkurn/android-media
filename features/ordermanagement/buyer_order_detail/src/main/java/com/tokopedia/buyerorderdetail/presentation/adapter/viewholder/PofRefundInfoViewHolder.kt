package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.PofUtils.setupHyperlinkText
import com.tokopedia.buyerorderdetail.databinding.ItemPartialOrderEstimateRefundedBinding
import com.tokopedia.buyerorderdetail.presentation.model.EstimateInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofRefundInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofRefundSummaryUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class PofRefundInfoViewHolder(
    view: View,
    private val listener: Listener
) : AbstractViewHolder<PofRefundInfoUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_partial_order_estimate_refunded
    }

    private val binding = ItemPartialOrderEstimateRefundedBinding.bind(itemView)

    override fun bind(element: PofRefundInfoUiModel) {
        with(binding) {
            setEstimateInfoLabel(element.totalAmountLabel)
            setEstimateInfoValue(element.totalAmountValue)
            setEstimateInfoIcon(element)
            setRefundSummaryCta(element)
        }
    }

    private fun ItemPartialOrderEstimateRefundedBinding.setEstimateInfoLabel(label: String) {
        tvPofEstimateRefundedLabel.text = label
    }

    private fun ItemPartialOrderEstimateRefundedBinding.setEstimateInfoValue(value: String) {
        tvPofEstimateRefundedValue.text = value
    }

    private fun ItemPartialOrderEstimateRefundedBinding.setEstimateInfoIcon(item: PofRefundInfoUiModel) {
        if (item.isRefunded) {
            icEstimateRefundInfo.hide()
        } else {
            icEstimateRefundInfo.show()
            item.estimateInfoUiModel?.let { estimateInfo ->
                icEstimateRefundInfo.setOnClickListener {
                    listener.estimateRefundInfoClicked(estimateInfo)
                }
            }
        }
    }

    private fun ItemPartialOrderEstimateRefundedBinding.setRefundSummaryCta(item: PofRefundInfoUiModel) {
        if (item.isRefunded) {
            tvPofRefundedCta.show()
            tvPofRefundedCta.setupHyperlinkText(getString(com.tokopedia.buyerorderdetail.R.string.buyer_order_detail_pof_item_detail_refunded_cta)) {
                item.pofRefundSummaryUiModel?.let { refundSummary ->
                    listener.refundSummaryClicked(refundSummary)
                }
            }
        } else {
            tvPofRefundedCta.hide()
        }
    }

    interface Listener {
        fun estimateRefundInfoClicked(estimateInfoUiModel: EstimateInfoUiModel)
        fun refundSummaryClicked(refundSummaryRefundUiModel: PofRefundSummaryUiModel)
    }
}
