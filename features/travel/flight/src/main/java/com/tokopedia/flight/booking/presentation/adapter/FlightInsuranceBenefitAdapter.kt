package com.tokopedia.flight.booking.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.booking.data.FlightCart
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_flight_booking_insurance_detail.view.*

/**
 * @author by jessica on 2019-11-04
 */

class FlightInsuranceBenefitAdapter(val benefits: List<FlightCart.Benefit>): RecyclerView.Adapter<FlightInsuranceBenefitAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.tokopedia.flight.R.layout.item_flight_booking_insurance_detail, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = benefits.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(benefits[position])

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(benefit: FlightCart.Benefit) {
            with(view) {
                iv_insurance_detail.loadImage(benefit.icon)
                tv_insurance_detail_title.text = benefit.title
                tv_insurance_detail_subtitle.text = benefit.description
            }
        }
    }
}