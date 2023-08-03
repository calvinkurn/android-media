package com.tokopedia.checkout.revamp.view.viewholder

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutButtonPaymentBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.kotlin.extensions.view.setOnClickDebounceListener

class CheckoutButtonPaymentViewHolder(private val binding: ItemCheckoutButtonPaymentBinding, private val listener: CheckoutAdapterListener) : RecyclerView.ViewHolder(binding.root) {

    fun bind(buttonPayment: CheckoutButtonPaymentModel) {
        if (buttonPayment.useInsurance) {
            val text = "Dengan melanjutkan, saya menyetujui S&K Asuransi & Proteksi."
            val span = SpannableString(text)
            span.apply {
                setSpan(object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        listener.onInsuranceTncClicked()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isUnderlineText = true
                    }
                }, text.indexOf("S&K"), text.lastIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            }
            binding.tvCheckoutTerms.text = span
            binding.tvCheckoutTerms.isVisible = true
        } else {
            binding.tvCheckoutTerms.isVisible = false
        }

        binding.btnCheckoutPay.setOnClickDebounceListener {
            listener.onProcessToPayment()
        }
    }

    companion object {

        val VIEW_TYPE = R.layout.item_checkout_button_payment
    }
}
