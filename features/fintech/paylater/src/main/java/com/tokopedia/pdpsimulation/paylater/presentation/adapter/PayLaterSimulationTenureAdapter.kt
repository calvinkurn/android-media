package com.tokopedia.pdpsimulation.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.paylater.domain.model.BasePayLaterWidgetUiModel
import com.tokopedia.pdpsimulation.paylater.domain.model.SimulationUiModel
import com.tokopedia.pdpsimulation.paylater.presentation.viewholder.PayLaterSimulationShimmerViewHolder
import com.tokopedia.pdpsimulation.paylater.presentation.viewholder.PayLaterSimulationTenureViewHolder

class PayLaterSimulationTenureAdapter(
    private val showPayLaterOption: (ArrayList<BasePayLaterWidgetUiModel>) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val tenureItemList = arrayListOf<SimulationUiModel>()
    var lastSelectedPosition = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            PayLaterSimulationShimmerViewHolder.LAYOUT_ID -> PayLaterSimulationShimmerViewHolder.getViewHolder(inflater, parent)
            else ->
            PayLaterSimulationTenureViewHolder.getViewHolder(inflater, parent) { pos ->
                if (isTenureSelectionChanged(pos)) {
                    showPayLaterOption(tenureItemList[pos].simulationList!!)
                    changeAndUpdateSelection(pos)
                }
            }
        }
    }

    private fun isTenureSelectionChanged(pos: Int) = pos != RecyclerView.NO_POSITION && pos != lastSelectedPosition &&
            !tenureItemList[pos].simulationList.isNullOrEmpty()

    private fun changeAndUpdateSelection(pos: Int) {
        tenureItemList[lastSelectedPosition].isSelected = !tenureItemList[lastSelectedPosition].isSelected
        notifyItemChanged(lastSelectedPosition)
        lastSelectedPosition = pos
        notifyItemChanged(pos)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PayLaterSimulationTenureViewHolder) {
            val descriptionData = tenureItemList[position]
            holder.bindData(descriptionData)
        }
    }

    override fun getItemCount(): Int {
        return if (tenureItemList.isEmpty()) 3 else tenureItemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(tenureItemList.isEmpty()) PayLaterSimulationShimmerViewHolder.LAYOUT_ID
            else PayLaterSimulationTenureViewHolder.LAYOUT_ID
    }

    fun setData(data: List<SimulationUiModel>) {
        tenureItemList.clear()
        tenureItemList.addAll(data)
        notifyDataSetChanged()
    }
}