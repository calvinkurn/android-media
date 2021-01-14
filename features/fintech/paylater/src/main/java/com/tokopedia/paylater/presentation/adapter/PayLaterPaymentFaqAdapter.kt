package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.domain.model.PayLaterPartnerFaq
import com.tokopedia.paylater.presentation.viewholder.PayLaterPaymentFaqViewHolder

class PayLaterPaymentFaqAdapter(private val faqList: ArrayList<PayLaterPartnerFaq>) : RecyclerView.Adapter<PayLaterPaymentFaqViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayLaterPaymentFaqViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = PayLaterPaymentFaqViewHolder.getViewHolder(inflater, parent)
        viewHolder.view.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val faqData = faqList[position]
                faqData.expandLayout = !faqData.expandLayout
                viewHolder.setLayout(faqData)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PayLaterPaymentFaqViewHolder, position: Int) {
        holder.bindData(faqList[position])
    }

    override fun getItemCount(): Int {
        return faqList.size
    }
}