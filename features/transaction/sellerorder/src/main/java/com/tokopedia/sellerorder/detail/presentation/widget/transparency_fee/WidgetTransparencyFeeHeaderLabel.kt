package com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee

import android.view.View
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemDetailTransparencyFeeHeaderLabelBinding
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeHeaderLabelUiModel

class WidgetTransparencyFeeHeaderLabel(
    view: View
): BaseWidgetTransparencyFeeAttribute<TransparencyFeeHeaderLabelUiModel> {
    companion object {
        val LAYOUT = R.layout.item_detail_transparency_fee_header_label
    }

    private val binding = ItemDetailTransparencyFeeHeaderLabelBinding.bind(view)

    override fun bind(data: TransparencyFeeHeaderLabelUiModel) {
        binding.root.text = data.text
    }
}
