package com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankPurchaseDetailHeading2Binding
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.Heading2UiModel
import com.tokopedia.utils.view.binding.viewBinding

class Heading2ViewHolder(val view: View) : AbstractViewHolder<Heading2UiModel>(view) {

    private val binding: ThankPurchaseDetailHeading2Binding? by viewBinding()

    override fun bind(element: Heading2UiModel) {
        binding?.pdName?.shouldShowWithAction(element.name.isNotEmpty()) {
            binding?.pdName?.text = element.name
        }
        binding?.pdTotalPrice?.shouldShowWithAction(element.totalPriceStr.isNotEmpty()) {
            binding?.pdTotalPrice?.text = element.totalPriceStr
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_purchase_detail_heading_2
    }
}
