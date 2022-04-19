package com.tokopedia.flight.cancellation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.cancellation.presentation.adapter.viewholder.FlightCancellationReviewPassengerViewHolder
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.databinding.ItemFlightReviewCancellationPassengerBinding

/**
 * @author by furqan on 21/07/2020
 */
class FlightCancellationReviewPassengerAdapter : RecyclerView.Adapter<FlightCancellationReviewPassengerViewHolder>() {

    private val passengerModelList: MutableList<FlightCancellationPassengerModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightCancellationReviewPassengerViewHolder =
            FlightCancellationReviewPassengerViewHolder(
                ItemFlightReviewCancellationPassengerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

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