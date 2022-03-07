package com.tokopedia.flight.booking.presentation.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.databinding.ItemFlightBookingV3PassengerBinding
import com.tokopedia.flight.detail.view.model.SimpleModel
import com.tokopedia.flight.passenger.view.model.FlightBookingPassengerModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.date.toString

/**
 * @author by jessica on 2019-11-04
 */

class FlightBookingPassengerAdapter : RecyclerView.Adapter<FlightBookingPassengerAdapter.ViewHolder>() {

    var list: List<FlightBookingPassengerModel> = listOf()
    lateinit var listener: PassengerViewHolderListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                ItemFlightBookingV3PassengerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], listener)
    }

    fun updateList(list: List<FlightBookingPassengerModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemFlightBookingV3PassengerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(passenger: FlightBookingPassengerModel, listener: PassengerViewHolderListener) {
            with(binding) {
                tvPassengerName.text = passenger.headerTitle
                if (passenger.passengerFirstName.isNotEmpty()) {
                    renderPassengerInfo(passenger)
                    tvEditPassengerInfo.text = itemView.context.getString(R.string.flight_booking_passenger_meal_change_label)
                } else {
                    rvPassengerInfo.hide()
                    tvEditPassengerInfo.text = itemView.context.getString(R.string.flight_booking_passenger_fill_data_label)
                }

                itemView.setOnClickListener { listener.onClickEditPassengerListener(passenger) }
            }
        }

        private fun renderPassengerInfo(passenger: FlightBookingPassengerModel) {
            with(binding) {
                rvPassengerInfo.show()
                tvPassengerName.text = String.format("%s %s %s", passenger.passengerTitle,
                        passenger.passengerFirstName, passenger.passengerLastName)

                //initiate passenger detail like passport num, birthdate, luggage and amenities, identification number
                val simpleViewModels = listOf<SimpleModel>().toMutableList()
                if (passenger.passengerBirthdate.isNotEmpty()) {
                    simpleViewModels.add(SimpleModel(itemView.context.getString(R.string.flight_booking_list_passenger_birthdate_label) + " | ",
                            passenger.passengerBirthdate.toDate(DateUtil.YYYY_MM_DD).toString(DateUtil.DEFAULT_VIEW_FORMAT)))
                }
                if(passenger.identificationNumber.isNotEmpty()){
                    simpleViewModels.add(
                        SimpleModel(
                            itemView.context.getString(R.string.flight_booking_passenger_identification_number_label) + " | ",
                            passenger.identificationNumber
                        )
                    )
                }
                passenger.passportNumber?.let {
                    if (it.isNotEmpty())
                        simpleViewModels.add(
                                SimpleModel(itemView.context.getString(R.string.flight_passenger_passport_number_hint) + " | ", it))
                }
                for (flightBookingLuggageRouteViewModel in passenger.flightBookingLuggageMetaViewModels) {
                    val selectedLuggages = arrayListOf<String>()
                    for (flightBookingLuggageViewModel in flightBookingLuggageRouteViewModel.amenities) {
                        selectedLuggages.add(flightBookingLuggageViewModel.title)
                    }
                    simpleViewModels.add(SimpleModel(itemView.context.getString(R.string.flight_booking_list_passenger_luggage_label) + flightBookingLuggageRouteViewModel.description + " | ",
                            TextUtils.join(" + ", selectedLuggages)))
                }
                for (flightBookingMealRouteViewModel in passenger.flightBookingMealMetaViewModels) {
                    simpleViewModels.add(SimpleModel(itemView.context.getString(R.string.flight_booking_list_passenger_meals_label) + flightBookingMealRouteViewModel.description + " | ",
                            TextUtils.join(" + ", flightBookingMealRouteViewModel.amenities)))
                }

                val adapter = FlightBookingPassengerInfoAdapter()
                rvPassengerInfo.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
                rvPassengerInfo.setHasFixedSize(true)
                rvPassengerInfo.adapter = adapter
                adapter.updateList(simpleViewModels.toList())
            }
        }

        companion object {
            val LAYOUT = R.layout.item_flight_booking_v3_passenger
        }
    }

    interface PassengerViewHolderListener {
        fun onClickEditPassengerListener(passenger: FlightBookingPassengerModel)
    }

}

