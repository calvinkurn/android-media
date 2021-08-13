package com.tokopedia.flight.cancellation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.presentation.adapter.viewholder.FlightCancellationReviewPassengerViewHolder
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationPassengerModel

/**
 * @author by furqan on 21/07/2020
 */
class FlightCancellationReviewPassengerAdapter : RecyclerView.Adapter<FlightCancellationReviewPassengerViewHolder>() {

    private val passengerModelList: MutableList<FlightCancellationPassengerModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightCancellationReviewPassengerViewHolder =
            FlightCancellationReviewPassengerViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_flight_review_cancellation_passenger, parent, false))

    override fun getItemCount(): Int = passengerModelList.size

    override fun onBindViewHolder(holder: FlightCancellationReviewPassengerViewHolder, position: Int) {
        holder.bind(passengerModelList[position])
    }

    fun addData(passengerList: List<FlightCancellationPassengerModel>) {
        passengerModelList.clear()
        passengerModelList.addAll(passengerList)
        notifyDataSetChanged()
    }

}