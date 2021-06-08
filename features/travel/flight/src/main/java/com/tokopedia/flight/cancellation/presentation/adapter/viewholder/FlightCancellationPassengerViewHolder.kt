package com.tokopedia.flight.cancellation.presentation.adapter.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.passenger.constant.FlightBookingPassenger
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by furqan on 14/07/2020
 */
class FlightCancellationPassengerViewHolder(itemView: View,
                                            private val cancellationListener: FlightCancellationViewHolder.FlightCancellationListener,
                                            private val passengerListener: FlightPassengerAdapterListener)
    : RecyclerView.ViewHolder(itemView) {

    private var tvPassengerName: TextView = itemView.findViewById(R.id.tv_passenger_name)
    private var tvPassengerType: TextView = itemView.findViewById(R.id.tv_passenger_type)
    private var checkBoxPassenger: CheckBox = itemView.findViewById(R.id.checkbox)
    private var tvPassengerStatus: Typography = itemView.findViewById(R.id.tg_passenger_status)

    private lateinit var passengerModel: FlightCancellationPassengerModel
    private var isPassengerChecked: Boolean = false
    private var passengerAdapterPosition: Int = 0

    init {
        checkBoxPassenger.setOnClickListener {
            onCheck(!cancellationListener.isChecked(passengerModel))
        }

        itemView.setOnClickListener {
            onCheck(!isPassengerChecked)
        }
    }

    fun bindData(passengerModel: FlightCancellationPassengerModel, adapterPosition: Int) {
        this.passengerModel = passengerModel
        this.passengerAdapterPosition = adapterPosition

        if (passengerModel.statusString.isNotEmpty()) {
            tvPassengerStatus.text = passengerModel.statusString
            tvPassengerStatus.visibility = View.VISIBLE
            checkBoxPassenger.isEnabled = false
            itemView.isEnabled = false
        } else {
            tvPassengerStatus.text = ""
            tvPassengerStatus.visibility = View.GONE
            checkBoxPassenger.isEnabled = true
            itemView.isEnabled = true
        }

        tvPassengerName.text = String.format("%s %s %s", passengerModel.titleString,
                passengerModel.firstName, passengerModel.lastName)

        when (passengerModel.type) {
            FlightBookingPassenger.ADULT -> {
                tvPassengerType.setText(R.string.flightbooking_price_adult_label)
            }
            FlightBookingPassenger.CHILDREN -> {
                tvPassengerType.setText(R.string.flightbooking_price_child_label)
            }
            FlightBookingPassenger.INFANT -> {
                tvPassengerType.setText(R.string.flightbooking_price_infant_label)
            }
            else -> {
                tvPassengerType.setText(R.string.flightbooking_price_adult_label)
            }
        }

        updateCheckedButton(cancellationListener.isChecked(passengerModel))
        passengerListener.checkIfAllPassengerIsChecked()
    }

    fun isPassengerChecked(): Boolean = isPassengerChecked

    fun getPassengerModel(): FlightCancellationPassengerModel = passengerModel

    fun onCheck(checkStatus: Boolean) {
        isPassengerChecked = checkStatus

        if (isPassengerChecked) {
            cancellationListener.onPassengerChecked(passengerModel, passengerAdapterPosition)
        } else {
            cancellationListener.onPassengerUnchecked(passengerModel, passengerAdapterPosition)
        }

        updateCheckedButton(isPassengerChecked)
        passengerListener.checkIfAllPassengerIsChecked()
    }

    private fun updateCheckedButton(checkedStatus: Boolean) {
        checkBoxPassenger.isChecked = checkedStatus
    }

    interface FlightPassengerAdapterListener {
        fun checkIfAllPassengerIsChecked()
    }

    companion object {
        val LAYOUT = R.layout.item_flight_cancellation_passenger
    }
}