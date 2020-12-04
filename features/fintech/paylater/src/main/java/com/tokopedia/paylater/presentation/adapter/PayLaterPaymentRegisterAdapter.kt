package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.presentation.viewholder.PayLaterPaymentRegisterViewHolder


class PayLaterPaymentRegisterAdapter() : RecyclerView.Adapter<PayLaterPaymentRegisterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayLaterPaymentRegisterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PayLaterPaymentRegisterViewHolder.getViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PayLaterPaymentRegisterViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return 5
    }

    companion object {

    }
}