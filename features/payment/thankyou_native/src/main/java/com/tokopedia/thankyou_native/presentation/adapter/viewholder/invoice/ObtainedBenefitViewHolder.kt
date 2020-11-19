package com.tokopedia.thankyou_native.presentation.adapter.viewholder.invoice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.CashBackMap
import com.tokopedia.thankyou_native.presentation.adapter.model.CashBackEarned

class ObtainedBenefitViewHolder(val view: View) : AbstractViewHolder<CashBackEarned>(view) {

    private lateinit var inflater: LayoutInflater

    override fun bind(element: CashBackEarned?) {
        element?.let {
            addBenefits(view.findViewById(R.id.llObtainedBenefitContainer), it)
        }
    }

    private fun addBenefits(container: LinearLayout, cashBackEarned: CashBackEarned) {
        container.removeAllViews()
        cashBackEarned.benefitMapList?.forEach {
            val view = createBenefitView(context = view.context, cashBackMap = it)
            container.addView(view)
        }
    }

    private fun createBenefitView(context: Context, cashBackMap: CashBackMap): View {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(context)
        val paymentModeItemView = inflater.inflate(R.layout.thank_payment_mode_item, null, false)
        val modeName = paymentModeItemView.findViewById<TextView>(R.id.tvInvoicePaymentModeName)
        modeName.text = if (cashBackMap.isBBICashBack) {
            getString(R.string.thank_potensi_cashback) + "\n" + cashBackMap.benefitName
        } else {
            cashBackMap.benefitName
        }

        paymentModeItemView.findViewById<TextView>(R.id.tvInvoicePaidWithModeValue)
                .text = cashBackMap.benefitAmount
        return paymentModeItemView
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_widget_obtained_benifit
    }
}