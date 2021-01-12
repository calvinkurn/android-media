package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.SimulationTableResponse
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.credit_card_simulation_info_item.view.*

class CreditCardSimulationViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(simulationData: SimulationTableResponse) {
        view.apply {
            tvInstallmentHeader.text = simulationData.tenureDesc
            tvInstallmentPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(simulationData.amount, false)
            if (simulationData.isPopular) ivPopular.visible() else ivPopular.gone()
            setBackGround(simulationData)
        }
    }

    fun setBackGround(simulationData: SimulationTableResponse) {
        view.apply {
            if (simulationData.isSelected)
                clSimulationCard.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G100))
            else
                clSimulationCard.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }

    }

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_simulation_info_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardSimulationViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}