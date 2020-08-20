package com.tokopedia.thankyou_native.presentation.adapter.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.BenefitMap
import com.tokopedia.thankyou_native.presentation.adapter.model.ObtainedAfterTransaction

class ObtainedBenefitViewHolder(val view: View) : AbstractViewHolder<ObtainedAfterTransaction>(view) {

    private lateinit var inflater: LayoutInflater

    override fun bind(element: ObtainedAfterTransaction?) {
        element?.let {
            addBenefits(view.findViewById(R.id.llObtainedBenefitContainer), it)
        }
    }

    private fun addBenefits(container: LinearLayout, obtainedAfterTransaction: ObtainedAfterTransaction) {
        container.removeAllViews()
        obtainedAfterTransaction.benefitMapList.forEach {
            val view = createBenefitView(context = view.context, benefitMap = it)
            container.addView(view)
        }
    }

    private fun createBenefitView(context: Context, benefitMap: BenefitMap): View {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(context)
        val paymentModeItemView = inflater.inflate(R.layout.thank_payment_mode_item, null, false)
        paymentModeItemView.findViewById<TextView>(R.id.tvInvoicePaymentModeName)
                .text = benefitMap.benefitName
        paymentModeItemView.findViewById<TextView>(R.id.tvInvoicePaidWithModeValue)
                .text = benefitMap.benefitAmount
        val description = paymentModeItemView.findViewById<TextView>(R.id.tvInvoicePaymentModeDescription)
        if(benefitMap.isBBICashBack){
            description.visible()
            description.text = getString(R.string.thank_bbi)
        }else
            description.gone()
        return paymentModeItemView
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_widget_obtained_benifit
    }
}
