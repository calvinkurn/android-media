package com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankPurchaseDetailHeading3Binding
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.Heading3UiModel
import com.tokopedia.utils.view.binding.viewBinding

class Heading3ViewHolder(val view: View) : AbstractViewHolder<Heading3UiModel>(view) {

    private val binding: ThankPurchaseDetailHeading3Binding? by viewBinding()

    override fun bind(element: Heading3UiModel) {
        binding?.pdName?.shouldShowWithAction(element.name.isNotEmpty()) {
            binding?.pdName?.text = element.name
        }
        binding?.pdNameSubtitle?.shouldShowWithAction(element.detail.nameSubtitle.isNotEmpty()) {
            binding?.pdNameSubtitle?.text = element.detail.nameSubtitle
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_purchase_detail_heading_3
    }
}
