package com.tokopedia.flight.cancellationdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.detail.view.model.SimpleModel
import kotlinx.android.synthetic.main.item_flight_detail_passenger_info.view.*

/**
 * @author by furqan on 08/01/2021
 */
class FlightOrderCancellationDetailPassengerDetailAdapter(private val infoList: List<SimpleModel>)
    : RecyclerView.Adapter<FlightOrderCancellationDetailPassengerDetailAdapter.FlightOrderCancellationDetailPassengerDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightOrderCancellationDetailPassengerDetailViewHolder =
            FlightOrderCancellationDetailPassengerDetailViewHolder(LayoutInflater.from(parent.context).inflate(
                    FlightOrderCancellationDetailPassengerDetailViewHolder.LAYOUT,
                    parent, false))

    override fun getItemCount(): Int = infoList.size

    override fun onBindViewHolder(holder: FlightOrderCancellationDetailPassengerDetailViewHolder, position: Int) {
        holder.bindDate(infoList[position])
    }

    class FlightOrderCancellationDetailPassengerDetailViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        fun bindDate(info: SimpleModel) {
            with(itemView) {
                title_info.text = info.description
                desc_info.text = info.label.trim()
            }
        }

        companion object {
            val LAYOUT = R.layout.item_flight_detail_passenger_info
        }

    }

}