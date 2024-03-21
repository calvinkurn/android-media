package com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankPurchaseDetailDiscountBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.DiscountUiModel
import com.tokopedia.utils.view.binding.viewBinding

class DiscountViewHolder(val view: View) : AbstractViewHolder<DiscountUiModel>(view) {

    private val binding: ThankPurchaseDetailDiscountBinding? by viewBinding()

    override fun bind(element: DiscountUiModel) {
        binding?.pdName?.shouldShowWithAction(element.name.isNotEmpty()) {
            binding?.pdName?.text = element.name
        }
        binding?.pdTotalPrice?.shouldShowWithAction(element.totalPriceStr.isNotEmpty()) {
            binding?.pdTotalPrice?.text = element.totalPriceStr
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_purchase_detail_discount
    }
}
