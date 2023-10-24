package com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee

import android.view.View
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemDetailTransparencyFeeSubComponentLabelBinding
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeSubComponentLabelUiModel

class WidgetTransparencyFeeSubComponentLabel(
    view: View
) : BaseWidgetTransparencyFeeAttribute<TransparencyFeeSubComponentLabelUiModel> {
    companion object {
        val LAYOUT = R.layout.item_detail_transparency_fee_sub_component_label
    }

    private val binding = ItemDetailTransparencyFeeSubComponentLabelBinding.bind(view)

    override fun bind(data: TransparencyFeeSubComponentLabelUiModel) {
        binding.root.text = data.text
    }
}
