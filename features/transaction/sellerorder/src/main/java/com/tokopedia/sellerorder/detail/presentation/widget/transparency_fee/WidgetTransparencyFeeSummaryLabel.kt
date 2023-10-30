package com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee

import android.view.View
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemDetailTransparencyFeeSummaryLabelBinding
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeSummaryLabelUiModel

class WidgetTransparencyFeeSummaryLabel(
    view: View
) : BaseWidgetTransparencyFeeAttribute<TransparencyFeeSummaryLabelUiModel> {
    companion object {
        val LAYOUT = R.layout.item_detail_transparency_fee_summary_label
    }

    private val binding = ItemDetailTransparencyFeeSummaryLabelBinding.bind(view)

    override fun bind(data: TransparencyFeeSummaryLabelUiModel) {
        binding.root.text = data.text
    }
}
