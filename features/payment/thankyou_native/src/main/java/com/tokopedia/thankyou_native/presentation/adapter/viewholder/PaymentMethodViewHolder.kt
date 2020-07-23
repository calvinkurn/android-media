package com.tokopedia.thankyou_native.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.PaymentMethodModel
import kotlinx.android.synthetic.main.thank_item_invoice_payment_method.view.*

class PaymentMethodViewHolder(val view: View) : AbstractViewHolder<PaymentMethodModel>(view) {


    override fun bind(element: PaymentMethodModel?) {
        element?.let {
            view.tvPaymentMethodName.text = element.paymentMethodStr
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_item_invoice_payment_method
    }
}