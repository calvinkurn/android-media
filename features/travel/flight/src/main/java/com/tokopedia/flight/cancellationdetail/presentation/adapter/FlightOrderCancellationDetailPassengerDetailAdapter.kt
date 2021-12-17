package com.tokopedia.flight.cancellationdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.databinding.ItemFlightDetailPassengerInfoBinding
import com.tokopedia.flight.detail.view.model.SimpleModel

/**
 * @author by furqan on 08/01/2021
 */
class FlightOrderCancellationDetailPassengerDetailAdapter(private val infoList: List<SimpleModel>)
    : RecyclerView.Adapter<FlightOrderCancellationDetailPassengerDetailAdapter.FlightOrderCancellationDetailPassengerDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightOrderCancellationDetailPassengerDetailViewHolder =
            FlightOrderCancellationDetailPassengerDetailViewHolder(
                ItemFlightDetailPassengerInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

    override fun getItemCount(): Int = infoList.size

    override fun onBindViewHolder(holder: FlightOrderCancellationDetailPassengerDetailViewHolder, position: Int) {
        holder.bindDate(infoList[position])
    }

    class FlightOrderCancellationDetailPassengerDetailViewHolder(val binding: ItemFlightDetailPassengerInfoBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bindDate(info: SimpleModel) {
            with(binding) {
                titleInfo.text = info.description
                descInfo.text = info.label.trim()
            }
        }

        companion object {
            val LAYOUT = R.layout.item_flight_detail_passenger_info
        }

    }

}