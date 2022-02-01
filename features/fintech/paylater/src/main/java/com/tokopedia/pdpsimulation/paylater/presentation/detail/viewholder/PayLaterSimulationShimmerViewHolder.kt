package com.tokopedia.pdpsimulation.paylater.presentation.detail.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.SimulationUiModel
import com.tokopedia.unifycomponents.CardUnify
import kotlinx.android.synthetic.main.paylater_simulation_tenure_item.view.*

class PayLaterSimulationShimmerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData() {}

    companion object {
        val LAYOUT_ID = R.layout.paylater_simulation_shimmer_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
            PayLaterSimulationShimmerViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false))
    }
}