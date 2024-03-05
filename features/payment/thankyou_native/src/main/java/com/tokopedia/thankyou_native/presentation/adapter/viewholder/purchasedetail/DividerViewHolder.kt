package com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankPurchaseDetailDividerBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.SeparatorUiModel
import com.tokopedia.thankyou_native.presentation.fragment.setMargin
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class DividerViewHolder(val view: View) : AbstractViewHolder<SeparatorUiModel>(view) {

    private val binding: ThankPurchaseDetailDividerBinding? by viewBinding()

    override fun bind(element: SeparatorUiModel) {
        binding?.pdDivider?.layoutParams?.height = element.height.toPx()
        if (element.applyMargin) {
            binding?.pdDivider?.setMargin(H_MARGIN.toPx(), TOP_MARGIN.toPx(), H_MARGIN.toPx(), BOTTOM_MARGIN.toPx())
        } else {
            binding?.pdDivider?.setMargin(Int.ZERO, TOP_MARGIN.toPx(), Int.ZERO, BOTTOM_MARGIN.toPx())
        }
        binding?.pdDivider?.requestLayout()

    }

    companion object {
        val LAYOUT_ID = R.layout.thank_purchase_detail_divider
        private const val H_MARGIN = 16
        private const val TOP_MARGIN = 4
        private const val BOTTOM_MARGIN = 12
    }
}
