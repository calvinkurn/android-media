package com.tokopedia.pdpsimulation.creditcard.presentation.simulation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardInstallmentItem
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.credit_card_simulation_info_item.view.*

class CreditCardSimulationViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(simulationData: CreditCardInstallmentItem) {
        view.apply {
            tvInstallmentHeader.text = simulationData.tenureDescription
            val amount = String.format("%.3f", simulationData.installmentAmount ?: 0.0f).toDouble()
            tvInstallmentPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(amount, false)
            if (simulationData.isPopular == true) ivPopular.visible() else ivPopular.gone()
            setBackGround(simulationData)
        }
    }

    fun setBackGround(simulationData: CreditCardInstallmentItem) {
        view.apply {
            when {
                simulationData.isSelected -> {
                    ccSimulationCard.cardType = CardUnify.TYPE_BORDER_ACTIVE
                    tvInstallmentHeader.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                    tvInstallmentPrice.visible()
                    tvMonthlyTenure.text = "/bulan"
                }
                simulationData.isDisabled == true -> {
                    tvInstallmentHeader.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
                    ccSimulationCard.cardType = CardUnify.TYPE_BORDER_DISABLED
                    tvInstallmentPrice.gone()
                    val amount = simulationData.minAmount?.div(MILLION)
                    tvMonthlyTenure.text = "Untuk harga min. Rp${amount} Juta"
                }
                else -> {
                    ccSimulationCard.cardType = CardUnify.TYPE_BORDER
                    tvInstallmentHeader.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                    tvInstallmentPrice.visible()
                    tvMonthlyTenure.text = "/bulan"
                }
            }
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_simulation_info_item
        private const val MILLION = 1000000
        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardSimulationViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}