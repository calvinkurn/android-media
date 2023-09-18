package com.tokopedia.checkout.revamp.view.viewholder

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.view.isVisible
import com.tokopedia.checkout.databinding.ItemCheckoutButtonPaymentBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.kotlin.extensions.view.setOnClickDebounceListener

object CheckoutButtonPaymentItemView {

    fun renderButtonPayment(buttonPayment: CheckoutButtonPaymentModel, binding: ItemCheckoutButtonPaymentBinding, listener: CheckoutAdapterListener) {
        if (buttonPayment.useInsurance) {
            val text = "Dengan melanjutkan, kamu menyetujui S&K Asuransi & Proteksi."
            val span = SpannableString(text)
            span.apply {
                setSpan(
                    object : ClickableSpan() {
                        override fun onClick(p0: View) {
                            listener.onInsuranceTncClicked()
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            ds.isUnderlineText = true
                        }
                    },
                    text.indexOf("S&K"),
                    text.lastIndex,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
            binding.tvCheckoutTerms.text = span
            binding.tvCheckoutTerms.isVisible = true
            binding.tvCheckoutTerms.setOnClickListener {
                if (binding.root.visibility == View.VISIBLE) {
                    listener.onInsuranceTncClicked()
                }
            }
        } else {
            binding.tvCheckoutTerms.isVisible = false
        }

        binding.btnCheckoutPay.isEnabled = buttonPayment.enable
        binding.btnCheckoutPay.setOnClickDebounceListener {
            if (binding.root.visibility == View.VISIBLE) {
                listener.onProcessToPayment()
            }
        }
    }
}
