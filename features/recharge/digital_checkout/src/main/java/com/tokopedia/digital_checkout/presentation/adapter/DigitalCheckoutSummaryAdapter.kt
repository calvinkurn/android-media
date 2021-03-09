package com.tokopedia.digital_checkout.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_checkout.R
import com.tokopedia.digital_checkout.data.PaymentSummary
import com.tokopedia.digital_checkout.data.PaymentSummary.Payment
import kotlinx.android.synthetic.main.item_digital_checkout_summary_detail.view.*

class DigitalCheckoutSummaryAdapter: RecyclerView.Adapter<DigitalCheckoutSummaryAdapter.DigitalCheckoutSummaryViewHolder>() {

    private var summaries: MutableList<Payment> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigitalCheckoutSummaryViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(DigitalCheckoutSummaryViewHolder.LAYOUT, parent, false)
        return DigitalCheckoutSummaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DigitalCheckoutSummaryViewHolder, position: Int) {
        holder.bind(summaries[position])
    }

    override fun getItemCount(): Int = summaries.size

    fun setSummaries(paymentSummary: PaymentSummary) {
        summaries = paymentSummary.summaries
        notifyDataSetChanged()
    }

    class DigitalCheckoutSummaryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        companion object {
            val LAYOUT = R.layout.item_digital_checkout_summary_detail
        }
        fun bind(item: Payment) {
            with(itemView) {
                tvCheckoutSummaryDetailLabel.text = item.title
                tvCheckoutSummaryDetailValue.text = item.priceAmount
            }
        }
    }
}