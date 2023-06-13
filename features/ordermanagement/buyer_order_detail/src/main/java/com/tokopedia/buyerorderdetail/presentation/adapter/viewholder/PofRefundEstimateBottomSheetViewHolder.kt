package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.PofUtils.setupHyperlinkText
import com.tokopedia.buyerorderdetail.databinding.ItemPofEstimateRefundBottomsheetBinding
import com.tokopedia.buyerorderdetail.presentation.model.EstimateInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofRefundEstimateBottomSheetUiModel

class PofRefundEstimateBottomSheetViewHolder(
    view: View,
    private val listener: Listener
) : AbstractViewHolder<PofRefundEstimateBottomSheetUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_estimate_refund_bottomsheet
    }

    private val binding = ItemPofEstimateRefundBottomsheetBinding.bind(itemView)

    override fun bind(element: PofRefundEstimateBottomSheetUiModel) {
        with(binding) {
            setRefundEstimateLabel(element.refundEstimateLabel)
            setRefundEstimateValue(element.refundEstimateValue)
            setRefundEstimateFooter(element.pofFooterInfo)
            setRefundEstimateIcon(element.estimateInfoUiModel)
        }
    }

    private fun ItemPofEstimateRefundBottomsheetBinding.setRefundEstimateIcon(estimateInfoUiModel: EstimateInfoUiModel) {
        icEstimateRefundInfo.setOnClickListener {
            listener.onRefundEstimateInfoClicked(estimateInfoUiModel)
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
        fun onRefundEstimateInfoClicked(estimateInfoUiModel: EstimateInfoUiModel)

        fun onFooterHyperlinkClicked(linkUrl: String)
    }
}
