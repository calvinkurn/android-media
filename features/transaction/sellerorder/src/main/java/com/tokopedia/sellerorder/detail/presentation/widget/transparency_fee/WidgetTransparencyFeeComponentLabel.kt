package com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee

import android.view.View
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemDetailTransparencyFeeComponentLabelBinding
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeComponentLabelUiModel

class WidgetTransparencyFeeComponentLabel(
    view: View
) : BaseWidgetTransparencyFeeAttribute<TransparencyFeeComponentLabelUiModel> {
    companion object {
        val LAYOUT = R.layout.item_detail_transparency_fee_component_label
    }

    private val binding = ItemDetailTransparencyFeeComponentLabelBinding.bind(view)

    override fun bind(data: TransparencyFeeComponentLabelUiModel) {
        binding.root.text = data.text
    }
}
