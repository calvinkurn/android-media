package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.domain.model.OfferDescriptionItem
import com.tokopedia.paylater.presentation.viewholder.*

class PayLaterPaymentFaqAdapter() : RecyclerView.Adapter<PayLaterPaymentFaqViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayLaterPaymentFaqViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PayLaterPaymentFaqViewHolder.getViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PayLaterPaymentFaqViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }

    companion object {

    }
}