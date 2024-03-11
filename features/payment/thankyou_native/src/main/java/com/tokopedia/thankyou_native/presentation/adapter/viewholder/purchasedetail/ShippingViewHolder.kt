package com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankPurchaseDetailShippingBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.ShippingUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShippingViewHolder(val view: View) : AbstractViewHolder<ShippingUiModel>(view) {

    private val binding: ThankPurchaseDetailShippingBinding? by viewBinding()

    override fun bind(element: ShippingUiModel) {
        binding?.pdName?.shouldShowWithAction(element.name.isNotEmpty()) {
            binding?.pdName?.text = element.name
        }
        binding?.pdTotalPrice?.shouldShowWithAction(element.totalPriceStr.isNotEmpty()) {
            binding?.pdTotalPrice?.text = element.totalPriceStr
        }
        binding?.pdShippingName?.shouldShowWithAction(element.detail.shippingName.isNotEmpty()) {
            binding?.pdShippingName?.text = element.detail.shippingName
        }
        binding?.pdShippingEta?.shouldShowWithAction(element.detail.shippingEta.isNotEmpty()) {
            binding?.pdShippingEta?.text = element.detail.shippingEta
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_purchase_detail_shipping
    }
}
