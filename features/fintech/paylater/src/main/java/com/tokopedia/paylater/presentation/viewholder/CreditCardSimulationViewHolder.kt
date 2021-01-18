package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.CreditCardInstallmentItem
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.credit_card_simulation_info_item.view.*

class CreditCardSimulationViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(simulationData: CreditCardInstallmentItem) {
        view.apply {
            tvInstallmentHeader.text = simulationData.tenureDescription
            tvInstallmentPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(simulationData.installmentAmount?.toDouble()
                    ?: 0.0, false)
            if (simulationData.isPopular == true) ivPopular.visible() else ivPopular.gone()
            setBackGround(simulationData)
        }
    }

    fun setBackGround(simulationData: CreditCardInstallmentItem) {
        view.apply {
            if (simulationData.isSelected) {
                ccSimulationCard.cardType = CardUnify.TYPE_BORDER_ACTIVE
                tvInstallmentHeader.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                clSimulationCard.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G100))
                tvInstallmentPrice.visible()
                val amount = String.format("%.3f", simulationData.installmentAmount).toDouble()
                tvMonthlyTenure.text = "${amount}/bulan"
            } else if (simulationData.isDisabled == true) {
                tvInstallmentHeader.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
                ccSimulationCard.cardType = CardUnify.TYPE_BORDER_DISABLED
                tvInstallmentPrice.gone()
                val amount = String.format("%.3f", simulationData.minAmount?.div(1000000)).toDouble()
                tvMonthlyTenure.text = "Untuk harga min. Rp${amount} Juta"
                clSimulationCard.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            } else {
                ccSimulationCard.cardType = CardUnify.TYPE_BORDER
                tvInstallmentHeader.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                clSimulationCard.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                tvInstallmentPrice.visible()
                val amount = String.format("%.3f", simulationData.installmentAmount).toDouble()
                tvMonthlyTenure.text = "${amount}/bulan"
            }
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_simulation_info_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardSimulationViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}