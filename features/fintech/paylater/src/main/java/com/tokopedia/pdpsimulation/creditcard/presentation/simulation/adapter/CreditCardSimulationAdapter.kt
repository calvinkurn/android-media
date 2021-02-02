package com.tokopedia.pdpsimulation.creditcard.presentation.simulation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardInstallmentItem
import com.tokopedia.pdpsimulation.creditcard.domain.model.SimulationBank
import com.tokopedia.pdpsimulation.creditcard.presentation.simulation.viewholder.CreditCardSimulationViewHolder

class CreditCardSimulationAdapter(
        private val simulationList: ArrayList<CreditCardInstallmentItem>,
        private val clickListener: (ArrayList<SimulationBank>?) -> Unit,
) : RecyclerView.Adapter<CreditCardSimulationViewHolder>() {
    private var oldPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardSimulationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = CreditCardSimulationViewHolder.getViewHolder(inflater, parent)
        viewHolder.itemView.setOnClickListener {
            val newPosition = viewHolder.adapterPosition
            if (newPosition != RecyclerView.NO_POSITION && oldPosition != newPosition) {
                val model = simulationList[newPosition]
                if (model.isDisabled == false) {
                    model.isSelected = true
                    viewHolder.setBackGround(model)
                    clickListener(model.simulationBankList)
                    simulationList[oldPosition].isSelected = false
                    notifyItemChanged(oldPosition)
                    oldPosition = viewHolder.adapterPosition

                }
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CreditCardSimulationViewHolder, position: Int) {
        holder.bindData(simulationList[position])
    }

    override fun getItemCount(): Int {
        return simulationList.size
    }
}