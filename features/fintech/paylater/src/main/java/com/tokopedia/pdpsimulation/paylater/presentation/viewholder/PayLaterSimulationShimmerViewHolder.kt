package com.tokopedia.pdpsimulation.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.R

class PayLaterSimulationShimmerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData() {}

    companion object {
        val LAYOUT_ID = R.layout.paylater_simulation_shimmer_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
                PayLaterSimulationShimmerViewHolder(
                        inflater.inflate(LAYOUT_ID, parent, false))
    }
}