package com.tokopedia.pdpsimulation.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.SimulationUiModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.NotificationUnify
import kotlinx.android.synthetic.main.paylater_simulation_tenure_item.view.*

class PayLaterSimulationTenureViewHolder(val view: View, val onTenureSelected: (Int) -> Unit) :
    RecyclerView.ViewHolder(view) {

    fun bindData(simulationUiModel: SimulationUiModel) {
        setCardType(simulationUiModel)
        setTenureContents(simulationUiModel)
        setLabel(simulationUiModel)
        view.setOnClickListener {
            onTenureSelected(adapterPosition)
        }
    }

    private fun setTenureContents(simulationUiModel: SimulationUiModel) {
        view.tvInstallmentHeader.text = simulationUiModel.text.orEmpty()
        view.tvMonthlyTenure.text = simulationUiModel.smallText.orEmpty()
    }

    private fun setCardType(simulationUiModel: SimulationUiModel) {
        view.payLaterSimulationCard.changeTypeWithTransition(
            if (simulationUiModel.isSelected) CardUnify.TYPE_BORDER_ACTIVE
            else CardUnify.TYPE_BORDER
        )
        // need custom selected color
        if (simulationUiModel.isSelected) view.clSimulationCard.setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN50
            )
        )
        else view.clSimulationCard.setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN0
            )
        )
    }

    private fun setLabel(simulationUiModel: SimulationUiModel) {
        val label = simulationUiModel.label

        view.labelInstallment.shouldShowWithAction(label?.text?.isNotEmpty() ?: false) {
            label?.let {
                view.labelInstallment.setNotification(
                    it.text,
                    NotificationUnify.TEXT_TYPE,
                    getLabelColor(label),
                )

                setLabelTextColor(label)
            }
        }
    }

    private fun setLabelTextColor(label: SimulationUiModel.LabelUiModel) {
        when (label.textColor) {
            COLOR_WHITE_STRING -> {
                view.labelInstallment.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN0,
                    )
                )
            }
            else -> {
                view.labelInstallment.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN0,
                    )
                )
            }
        }
    }

    private fun getLabelColor(label: SimulationUiModel.LabelUiModel): Int {
        return when (label.bgColor) {
            COLOR_RED_STRING -> com.tokopedia.unifyprinciples.R.color.Unify_RN500
            else -> com.tokopedia.unifyprinciples.R.color.Unify_RN500
        }
    }

    companion object {
        private const val COLOR_RED_STRING = "red"
        private const val COLOR_WHITE_STRING = "white"
        val LAYOUT_ID = R.layout.paylater_simulation_tenure_item

        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            onTenureSelected: (Int) -> Unit
        ) =
            PayLaterSimulationTenureViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false), onTenureSelected
            )
    }
}
