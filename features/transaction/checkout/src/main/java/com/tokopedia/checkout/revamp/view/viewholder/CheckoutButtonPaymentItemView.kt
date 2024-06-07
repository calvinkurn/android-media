package com.tokopedia.checkout.revamp.view.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutButtonPaymentBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.setOnClickDebounceListener
import com.tokopedia.purchase_platform.common.R as purchase_platformcommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

object CheckoutButtonPaymentItemView {

    fun renderButtonPayment(buttonPayment: CheckoutButtonPaymentModel, binding: ItemCheckoutButtonPaymentBinding, listener: CheckoutAdapterListener) {
        if (buttonPayment.useInsurance) {
            binding.tvCheckoutTerms.text = MethodChecker.fromHtml(buttonPayment.terms)
            binding.tvCheckoutTerms.isVisible = true
            binding.tvCheckoutTerms.setOnClickListener {
                if (binding.root.visibility == View.VISIBLE) {
                    listener.onInsuranceTncClicked()
                }
            }
        } else {
            binding.tvCheckoutTerms.isVisible = false
        }

        if (buttonPayment.useDirectPayment) {
            val drawable = getIconUnifyDrawable(binding.root.context, IconUnify.PROTECTION_CHECK, ContextCompat.getColor(binding.root.context, unifyprinciplesR.color.Unify_Static_White))
            binding.btnCheckoutPay.setDrawable(drawable)
            binding.btnCheckoutPay.text = binding.root.resources.getString(purchase_platformcommonR.string.label_pay_now)
            binding.btnCheckoutPay.contentDescription = binding.root.resources.getString(R.string.content_desc_tv_pay_now)
        } else {
            binding.btnCheckoutPay.text = binding.root.resources.getString(purchase_platformcommonR.string.label_choose_payment)
            binding.btnCheckoutPay.contentDescription = binding.root.resources.getString(R.string.content_desc_tv_select_payment_method)
        }
        binding.btnCheckoutPay.isEnabled = buttonPayment.enable
        binding.btnCheckoutPay.setOnClickDebounceListener {
            if (binding.root.visibility == View.VISIBLE) {
                listener.onProcessToPayment()
            }
        }
    }
}
