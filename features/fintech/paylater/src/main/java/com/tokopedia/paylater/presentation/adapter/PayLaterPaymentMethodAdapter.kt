package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.domain.model.OfferDescriptionItem
import com.tokopedia.paylater.presentation.viewholder.*

class PayLaterPaymentMethodAdapter(private val clickListener: () -> Unit) : RecyclerView.Adapter<PayLaterPaymentMethodViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayLaterPaymentMethodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PayLaterPaymentMethodViewHolder.getViewHolder(inflater, parent, clickListener)
    }

    override fun onBindViewHolder(holder:PayLaterPaymentMethodViewHolder, position: Int) {
        holder.bindData()
    }

    override fun getItemCount(): Int {
        return 4
    }

    companion object {

    }
}