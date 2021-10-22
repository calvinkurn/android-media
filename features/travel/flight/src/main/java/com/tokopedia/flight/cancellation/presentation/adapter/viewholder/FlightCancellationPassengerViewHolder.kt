package com.tokopedia.flight.cancellation.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.databinding.ItemFlightCancellationPassengerBinding
import com.tokopedia.flight.passenger.constant.FlightBookingPassenger

/**
 * @author by furqan on 14/07/2020
 */
class FlightCancellationPassengerViewHolder(val binding: ItemFlightCancellationPassengerBinding,
                                            private val cancellationListener: FlightCancellationViewHolder.FlightCancellationListener,
                                            private val passengerListener: FlightPassengerAdapterListener)
    : RecyclerView.ViewHolder(binding.root) {

    private lateinit var passengerModel: FlightCancellationPassengerModel
    private var isPassengerChecked: Boolean = false
    private var passengerAdapterPosition: Int = 0

    init {
        binding.checkbox.setOnClickListener {
            onCheck(!cancellationListener.isChecked(passengerModel))
        }

        binding.root.setOnClickListener {
            onCheck(!isPassengerChecked)
        }
    }

    fun bindData(passengerModel: FlightCancellationPassengerModel, adapterPosition: Int) {
            this.passengerModel = passengerModel
            this.passengerAdapterPosition = adapterPosition

        with(binding){
            if (passengerModel.statusString.isNotEmpty()) {
                tgPassengerStatus.text = passengerModel.statusString
                tgPassengerStatus.visibility = View.VISIBLE
                checkbox.isEnabled = false
                itemView.isEnabled = false
            } else {
                tgPassengerStatus.text = ""
                tgPassengerStatus.visibility = View.GONE
                checkbox.isEnabled = true
                itemView.isEnabled = true
            }

            tvPassengerName.text = String.format(
                "%s %s %s", passengerModel.titleString,
                passengerModel.firstName, passengerModel.lastName
            )

            when (passengerModel.type) {
                FlightBookingPassenger.ADULT.value -> {
                    tvPassengerType.setText(R.string.flightbooking_price_adult_label)
                }
                FlightBookingPassenger.CHILDREN.value -> {
                    tvPassengerType.setText(R.string.flightbooking_price_child_label)
                }
                FlightBookingPassenger.INFANT.value -> {
                    tvPassengerType.setText(R.string.flightbooking_price_infant_label)
                }
                else -> {
                    tvPassengerType.setText(R.string.flightbooking_price_adult_label)
                }
            }

            updateCheckedButton(cancellationListener.isChecked(passengerModel))
            passengerListener.checkIfAllPassengerIsChecked()
        }
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
        binding.checkbox.isChecked = checkedStatus
    }

    interface FlightPassengerAdapterListener {
        fun checkIfAllPassengerIsChecked()
    }

    companion object {
        val LAYOUT = R.layout.item_flight_cancellation_passenger
    }
}