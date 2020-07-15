package com.tokopedia.flight.cancellationV2.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.cancellationV2.presentation.adapter.viewholder.FlightCancellationPassengerViewHolder
import com.tokopedia.flight.cancellationV2.presentation.adapter.viewholder.FlightCancellationViewHolder
import com.tokopedia.flight.cancellationV2.presentation.adapter.viewholder.FlightCancellationViewHolderListener
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationPassengerModel

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
        var allChecked = true
        if (passengerViewHolderList.size == 0) {
            allChecked = false
        } else {
            for (item in passengerViewHolderList) {
                if (!item.isPassengerChecked() &&
                        item.getPassengerModel().statusString != null &&
                        item.getPassengerModel().statusString?.isNotEmpty() == true) {
                    allChecked = false
                }
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
            if (item.getPassengerModel().statusString == null || item.getPassengerModel().statusString?.isEmpty() != false) {
                item.onCheck(true)
            }
        }
    }

    fun uncheckAllData() {
        for (item in passengerViewHolderList) {
            if (item.getPassengerModel().statusString == null || item.getPassengerModel().statusString?.isEmpty() != false) {
                item.onCheck(false)
            }
        }
    }

}