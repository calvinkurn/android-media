package com.tokopedia.pdpsimulation.paylater.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.paylater.domain.model.BasePayLaterWidgetUiModel
import com.tokopedia.pdpsimulation.paylater.domain.model.SimulationUiModel
import com.tokopedia.pdpsimulation.paylater.presentation.detail.viewholder.PayLaterSimulationTenureViewHolder

class PayLaterSimulationTenureAdapter(
    private val showPayLaterOption: (ArrayList<BasePayLaterWidgetUiModel>) -> Unit) :
    RecyclerView.Adapter<PayLaterSimulationTenureViewHolder>() {

    private val tenureItemList = arrayListOf<SimulationUiModel>()
    private var lastSelectedPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PayLaterSimulationTenureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PayLaterSimulationTenureViewHolder.getViewHolder(inflater, parent) { pos ->
            if (pos != RecyclerView.NO_POSITION && pos != lastSelectedPosition &&
                !tenureItemList[pos].simulationList.isNullOrEmpty()) {
                showPayLaterOption(tenureItemList[pos].simulationList!!)
                tenureItemList[lastSelectedPosition].isSelected = !tenureItemList[lastSelectedPosition].isSelected
                notifyItemChanged(lastSelectedPosition)
                lastSelectedPosition = pos
                notifyItemChanged(pos)
            }
        }
    }

    override fun onBindViewHolder(holder: PayLaterSimulationTenureViewHolder, position: Int) {
        val descriptionData = tenureItemList[position]
        holder.bindData(descriptionData)
    }

    override fun getItemCount(): Int {
        return tenureItemList.size
    }

    fun setData(data: List<SimulationUiModel>) {
        tenureItemList.clear()
        tenureItemList.addAll(data)
        notifyDataSetChanged()
    }
}