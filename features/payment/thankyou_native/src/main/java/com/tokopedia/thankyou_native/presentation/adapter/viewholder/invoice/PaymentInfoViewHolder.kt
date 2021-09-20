package com.tokopedia.thankyou_native.presentation.adapter.viewholder.invoice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.PaymentInfo
import com.tokopedia.thankyou_native.presentation.adapter.model.PaymentModeMap
import kotlinx.android.synthetic.main.thank_widget_payment_info.view.*

class PaymentInfoViewHolder(val view: View) : AbstractViewHolder<PaymentInfo>(view) {

    private lateinit var inflater: LayoutInflater

    override fun bind(element: PaymentInfo?) {
        element?.let {
            view.findViewById<TextView>(R.id.tvTotalBillPaidValue)
                    .text = element.totalAmountPaidStr
            addPaymentMode(view.findViewById(R.id.llPaymentModeContainer), element)
        }

    }

    private fun addPaymentMode(container: LinearLayout, paymentInfo: PaymentInfo) {
        container.removeAllViews()
        view.tvTotalBillPaidWith.gone()
        paymentInfo.paymentModeList?.forEach { paymentModeMap ->
            view.tvTotalBillPaidWith.visible()
            val modeView = createPaymentModeView(context = view.context, paymentModeMap = paymentModeMap)
            container.addView(modeView)
        }
    }

    private fun createPaymentModeView(context: Context, paymentModeMap: PaymentModeMap): View {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(context)
        val paymentModeItemView = inflater.inflate(R.layout.thank_payment_mode_item, null, false)
        paymentModeItemView.findViewById<TextView>(R.id.tvInvoicePaymentModeName).text = paymentModeMap.paymentModeStr
        paymentModeItemView.findViewById<TextView>(R.id.tvInvoicePaidWithModeValue)
                .text = if (paymentModeMap.gatewayCode == MODE_OVO_POINT)
            paymentModeMap.paidAmountStr
        else
            getString(R.string.thankyou_rp_without_space, paymentModeMap.paidAmountStr)
        return paymentModeItemView
    }

    companion object {
        private const val MODE_OVO_POINT = "OVOPOINTS"
        val LAYOUT_ID = R.layout.thank_widget_payment_info
    }
}
