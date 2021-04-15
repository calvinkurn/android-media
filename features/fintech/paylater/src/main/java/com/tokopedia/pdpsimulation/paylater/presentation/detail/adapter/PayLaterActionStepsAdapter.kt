package com.tokopedia.pdpsimulation.paylater.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.paylater.presentation.detail.viewholder.PayLaterActionStepViewHolder


class PayLaterActionStepsAdapter(private val partnerSteps: ArrayList<String>) : RecyclerView.Adapter<PayLaterActionStepViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayLaterActionStepViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PayLaterActionStepViewHolder.getViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PayLaterActionStepViewHolder, position: Int) {
        holder.bindData(partnerSteps[position], position == partnerSteps.size - 1, position + 1)
    }

    override fun getItemCount(): Int {
        return partnerSteps.size
    }
}