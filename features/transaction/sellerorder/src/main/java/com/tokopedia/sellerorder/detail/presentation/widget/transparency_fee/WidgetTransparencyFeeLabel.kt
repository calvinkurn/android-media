package com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee

import android.view.View
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemDetailTransparencyFeeLabelBinding
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeLabelUiModel

class WidgetTransparencyFeeLabel(
    view: View
) : BaseWidgetTransparencyFeeAttribute<TransparencyFeeLabelUiModel> {

    companion object {
        val LAYOUT = R.layout.item_detail_transparency_fee_label
    }

    private val binding = ItemDetailTransparencyFeeLabelBinding.bind(view)

    override fun bind(data: TransparencyFeeLabelUiModel) {
        setupLabel(data)
    }

    private fun setupLabel(element: TransparencyFeeLabelUiModel) {
        binding.detailIncomeLabel.run {
            setLabel(element.label)
        }
    }

}
