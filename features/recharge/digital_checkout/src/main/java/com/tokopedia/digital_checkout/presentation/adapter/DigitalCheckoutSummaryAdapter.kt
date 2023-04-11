package com.tokopedia.digital_checkout.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_checkout.data.PaymentSummary
import com.tokopedia.digital_checkout.data.PaymentSummary.Payment
import com.tokopedia.digital_checkout.databinding.ItemDigitalCheckoutSummaryDetailBinding

class DigitalCheckoutSummaryAdapter: RecyclerView.Adapter<DigitalCheckoutSummaryAdapter.DigitalCheckoutSummaryViewHolder>() {

    private var summaries: MutableList<Payment> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigitalCheckoutSummaryViewHolder {
        val binding = ItemDigitalCheckoutSummaryDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DigitalCheckoutSummaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DigitalCheckoutSummaryViewHolder, position: Int) {
        holder.bind(summaries[position])
    }

    override fun getItemCount(): Int = summaries.size

    fun setSummaries(paymentSummary: PaymentSummary) {
        summaries = paymentSummary.summaries
        notifyDataSetChanged()
    }

    inner class DigitalCheckoutSummaryViewHolder(private val binding: ItemDigitalCheckoutSummaryDetailBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Payment) {
            with(binding) {
                tvCheckoutSummaryDetailLabel.text = item.title
                tvCheckoutSummaryDetailValue.text = item.priceAmount
            }
        }
    }
}