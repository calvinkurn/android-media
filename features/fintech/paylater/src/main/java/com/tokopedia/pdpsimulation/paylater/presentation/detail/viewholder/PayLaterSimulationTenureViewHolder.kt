package com.tokopedia.pdpsimulation.paylater.presentation.detail.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.SimulationUiModel
import com.tokopedia.unifycomponents.CardUnify
import kotlinx.android.synthetic.main.paylater_simulation_tenure_item.view.*

class PayLaterSimulationTenureViewHolder(val view: View, val onTenureSelected: (Int) -> Unit) : RecyclerView.ViewHolder(view) {

    fun bindData(simulationUiModel: SimulationUiModel) {
        setCardType(simulationUiModel)
        setTenureContents(simulationUiModel)
        view.setOnClickListener {
            simulationUiModel.isSelected = !simulationUiModel.isSelected
            onTenureSelected(adapterPosition)
        }
    }

    private fun setTenureContents(simulationUiModel: SimulationUiModel) {
        view.tvInstallmentHeader.text = simulationUiModel.text.orEmpty()
        view.tvMonthlyTenure.text = simulationUiModel.smallText.orEmpty()
    }

    private fun setCardType(simulationUiModel: SimulationUiModel) {
        view.payLaterSimulationCard.cardType = if (simulationUiModel.isSelected) CardUnify.TYPE_BORDER_ACTIVE else CardUnify.TYPE_BORDER
    }

    companion object {
        private val LAYOUT_ID = R.layout.paylater_simulation_tenure_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup, onTenureSelected: (Int) -> Unit) =
            PayLaterSimulationTenureViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
            , onTenureSelected)
    }
}