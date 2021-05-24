package com.tokopedia.flight.booking.presentation.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.flight.R
import com.tokopedia.flight.detail.view.model.SimpleModel
import com.tokopedia.flight.passenger.view.model.FlightBookingPassengerModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_flight_booking_v3_passenger.view.*

/**
 * @author by jessica on 2019-11-04
 */

class FlightBookingPassengerAdapter: RecyclerView.Adapter<FlightBookingPassengerAdapter.ViewHolder>() {

    var list: List<FlightBookingPassengerModel> = listOf()
    lateinit var listener: PassengerViewHolderListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], listener)
    }

    fun updateList(list: List<FlightBookingPassengerModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(passenger: FlightBookingPassengerModel, listener: PassengerViewHolderListener) {
            with(view) {
                tv_passenger_name.text = passenger.headerTitle
                if (passenger.passengerFirstName != null) {
                    renderPassengerInfo(passenger)
                    tv_edit_passenger_info.text = context.getString(R.string.flight_booking_passenger_meal_change_label)
                } else {
                    rv_passenger_info.hide()
                    tv_edit_passenger_info.text = context.getString(R.string.flight_booking_passenger_fill_data_label)
                }

                itemView.setOnClickListener { listener.onClickEditPassengerListener(passenger) }
            }
        }

        fun renderPassengerInfo(passenger: FlightBookingPassengerModel) {
            with(view) {
                rv_passenger_info.show()
                tv_passenger_name.text = String.format("%s %s %s", passenger.passengerTitle ?: "", passenger.passengerFirstName, passenger.passengerLastName ?: "")

                //initiate passenger detail like passport num, birthdate, luggage and amenities
                var simpleViewModels = listOf<SimpleModel>().toMutableList()
                if (!passenger.passengerBirthdate.isNullOrEmpty()) simpleViewModels.add(SimpleModel(context.getString(R.string.flight_booking_list_passenger_birthdate_label) + " | ",
                        TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, passenger.passengerBirthdate))))
                if (!passenger.passportNumber.isNullOrEmpty()) simpleViewModels.add(SimpleModel(context.getString(R.string.flight_passenger_passport_number_hint) + " | ", passenger.passportNumber))
                if (passenger.flightBookingLuggageMetaViewModels != null) {
                    for (flightBookingLuggageRouteViewModel in passenger.flightBookingLuggageMetaViewModels) {
                        val selectedLuggages = arrayListOf<String>()
                        for (flightBookingLuggageViewModel in flightBookingLuggageRouteViewModel.amenities) {
                            selectedLuggages.add(flightBookingLuggageViewModel.title)
                        }
                        simpleViewModels.add(SimpleModel(context.getString(R.string.flight_booking_list_passenger_luggage_label) + flightBookingLuggageRouteViewModel.description + " | ",
                                TextUtils.join(" + ", selectedLuggages)))
                    }
                }
                if (passenger.flightBookingMealMetaViewModels != null) {
                    for (flightBookingMealRouteViewModel in passenger.flightBookingMealMetaViewModels) {
                        simpleViewModels.add(SimpleModel(context.getString(R.string.flight_booking_list_passenger_meals_label) + flightBookingMealRouteViewModel.description + " | ",
                                TextUtils.join(" + ", flightBookingMealRouteViewModel.amenities)))
                    }
                }

                val adapter = FlightBookingPassengerInfoAdapter()
                rv_passenger_info.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                rv_passenger_info.setHasFixedSize(true)
                rv_passenger_info.adapter = adapter
                adapter.updateList(simpleViewModels.toList())
            }
        }

        companion object {
            val LAYOUT = R.layout.item_flight_booking_v3_passenger
        }
    }

    interface PassengerViewHolderListener{
        fun onClickEditPassengerListener(passenger: FlightBookingPassengerModel)
    }

}

