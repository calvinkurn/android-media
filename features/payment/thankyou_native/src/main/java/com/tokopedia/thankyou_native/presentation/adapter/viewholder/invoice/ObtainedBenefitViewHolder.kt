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
import com.tokopedia.thankyou_native.presentation.adapter.model.CashBackMap
import com.tokopedia.thankyou_native.presentation.adapter.model.CashBackEarned

class ObtainedBenefitViewHolder(val view: View) : AbstractViewHolder<CashBackEarned>(view) {

    private lateinit var inflater: LayoutInflater

    private val container: LinearLayout = itemView.findViewById(R.id.llObtainedBenefitContainer)
    private val cashBackHeading: TextView = itemView.findViewById(R.id.tvObtainedAfterTransaction)

    override fun bind(element: CashBackEarned?) {
        element?.let {
            if (it.cashBackOVOPoint) {
                cashBackHeading.visible()
            } else {
                cashBackHeading.gone()
            }
            addBenefits(container, it)
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
        val paymentModeItemView = inflater.inflate(R.layout.thank_cashback_stack, null, false)
        val tvCashBackTitle = paymentModeItemView.findViewById<TextView>(R.id.tvCashBackTitle)
        val tvCashBackAmount = paymentModeItemView.findViewById<TextView>(R.id.tvCashBackAmount)
        val tvCashBackDescription = paymentModeItemView.findViewById<TextView>(R.id.tvCashBackEarnedPoint)
        tvCashBackTitle.text = if (cashBackMap.isBBICashBack) {
            getString(R.string.thank_potensi_cashback) + "\n" + cashBackMap.benefitName
        } else {
            cashBackMap.benefitName
        }
        if (cashBackMap.isStackedCashBack) {
            tvCashBackAmount.text = getString(R.string.thankyou_rp_without_space, cashBackMap.benefitAmount)
            tvCashBackDescription.text = cashBackMap.cashBackDescription ?: ""
            tvCashBackDescription.visible()
        } else {
            tvCashBackAmount.text = cashBackMap.benefitAmount
            tvCashBackDescription.gone()
        }
        return paymentModeItemView
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_widget_obtained_benifit
    }
}