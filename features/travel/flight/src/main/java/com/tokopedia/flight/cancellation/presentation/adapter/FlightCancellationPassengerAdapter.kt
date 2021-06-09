package com.tokopedia.flight.cancellation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.cancellation.presentation.adapter.viewholder.FlightCancellationPassengerViewHolder
import com.tokopedia.flight.cancellation.presentation.adapter.viewholder.FlightCancellationViewHolder
import com.tokopedia.flight.cancellation.presentation.adapter.viewholder.FlightCancellationViewHolderListener
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationPassengerModel

/**
 * @author by furqan on 14/07/2020
 */
class FlightCancellationPassengerAdapter(private val cancellationListener: FlightCancellationViewHolder.FlightCancellationListener,
                                         private val cancellationJourneyListener: FlightCancellationViewHolderListener,
                                         private val journeyAdapterPosition: Int)
    : RecyclerView.Adapter<FlightCancellationPassengerViewHolder>(), FlightCancellationPassengerViewHolder.FlightPassengerAdapterListener {

    private var passengerModelList: MutableList<FlightCancellationPassengerModel> = arrayListOf()
    private var passengerViewHolderList: MutableList<FlightCancellationPassengerViewHolder> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightCancellationPassengerViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(FlightCancellationPassengerViewHolder.LAYOUT, parent, false)
        return FlightCancellationPassengerViewHolder(view, cancellationListener, this)
    }

    override fun getItemCount(): Int = passengerModelList.size

    override fun onBindViewHolder(holder: FlightCancellationPassengerViewHolder, position: Int) {
        holder.bindData(passengerModelList[position], journeyAdapterPosition)
        passengerViewHolderList.add(holder)
    }

    override fun checkIfAllPassengerIsChecked() {
        var allChecked = passengerViewHolderList.size > 0

        for (item in passengerViewHolderList) {
            if (!item.isPassengerChecked() && item.getPassengerModel().statusString.isEmpty()) {
                allChecked = false
            }
        }

        cancellationJourneyListener.setJourneyCheck(allChecked)
        if (allChecked) {
            cancellationJourneyListener.toggleCheckJourney(true)
        } else {
            cancellationJourneyListener.toggleCheckJourney(false)
        }
    }

    fun addData(passengerModelList: List<FlightCancellationPassengerModel>) {
        this.passengerModelList.clear()
        this.passengerModelList.addAll(passengerModelList)
        notifyDataSetChanged()
    }

    fun checkAllData() {
        for (item in passengerViewHolderList) {
            if (item.getPassengerModel().statusString.isEmpty()) {
                item.onCheck(true)
            }
        }
    }

    fun uncheckAllData() {
        for (item in passengerViewHolderList) {
            if (item.getPassengerModel().statusString.isEmpty()) {
                item.onCheck(false)
            }
        }
    }

}