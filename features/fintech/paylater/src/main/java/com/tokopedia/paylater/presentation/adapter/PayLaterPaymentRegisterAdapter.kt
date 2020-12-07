package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.presentation.viewholder.PayLaterPaymentRegisterViewHolder


class PayLaterPaymentRegisterAdapter(private val partnerSteps: ArrayList<String>) : RecyclerView.Adapter<PayLaterPaymentRegisterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayLaterPaymentRegisterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PayLaterPaymentRegisterViewHolder.getViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PayLaterPaymentRegisterViewHolder, position: Int) {
        holder.bindData(partnerSteps[position], position == partnerSteps.size-1, position+1)
    }

    override fun getItemCount(): Int {
        return partnerSteps.size
    }
}