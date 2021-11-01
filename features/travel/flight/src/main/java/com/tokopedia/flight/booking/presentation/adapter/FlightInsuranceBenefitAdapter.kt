package com.tokopedia.flight.booking.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.booking.data.FlightCart
import com.tokopedia.flight.databinding.ItemFlightBookingInsuranceDetailBinding
import com.tokopedia.kotlin.extensions.view.loadImage

/**
 * @author by jessica on 2019-11-04
 */

class FlightInsuranceBenefitAdapter(val benefits: List<FlightCart.Benefit>): RecyclerView.Adapter<FlightInsuranceBenefitAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFlightBookingInsuranceDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = benefits.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(benefits[position])

    class ViewHolder(val binding: ItemFlightBookingInsuranceDetailBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(benefit: FlightCart.Benefit) {
            with(binding) {
                ivInsuranceDetail.loadImage(benefit.icon)
                tvInsuranceDetailTitle.text = benefit.title
                tvInsuranceDetailSubtitle.text = benefit.description
            }
        }
    }
}