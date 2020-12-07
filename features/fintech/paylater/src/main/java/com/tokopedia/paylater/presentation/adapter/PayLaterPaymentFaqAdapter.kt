package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.domain.model.PayLaterPartnerFaq
import com.tokopedia.paylater.presentation.viewholder.*

class PayLaterPaymentFaqAdapter(private val faqList: ArrayList<PayLaterPartnerFaq>) : RecyclerView.Adapter<PayLaterPaymentFaqViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayLaterPaymentFaqViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PayLaterPaymentFaqViewHolder.getViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PayLaterPaymentFaqViewHolder, position: Int) {
        holder.bindData(faqList[position])
    }

    override fun getItemCount(): Int {
        return faqList.size
    }
}