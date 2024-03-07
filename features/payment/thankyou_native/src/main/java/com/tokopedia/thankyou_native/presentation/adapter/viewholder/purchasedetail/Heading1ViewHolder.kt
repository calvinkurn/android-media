package com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankPurchaseDetailHeading1Binding
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.Heading1UiModel
import com.tokopedia.utils.view.binding.viewBinding

class Heading1ViewHolder(val view: View) : AbstractViewHolder<Heading1UiModel>(view) {

    private val binding: ThankPurchaseDetailHeading1Binding? by viewBinding()

    override fun bind(element: Heading1UiModel) {
        binding?.pdName?.shouldShowWithAction(element.name.isNotEmpty()) {
            binding?.pdName?.text = element.name
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_purchase_detail_heading_1
    }
}
