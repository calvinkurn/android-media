package com.tokopedia.flight.bookingV3.presentation.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.flight.bookingV2.presentation.viewmodel.FlightBookingPassengerViewModel
import com.tokopedia.flight.bookingV2.presentation.viewmodel.SimpleViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_flight_booking_v3_passenger.view.*

/**
 * @author by jessica on 2019-11-04
 */

class FlightBookingPassengerAdapter: RecyclerView.Adapter<FlightBookingPassengerAdapter.ViewHolder>() {

    var list: List<FlightBookingPassengerViewModel> = listOf()
    lateinit var listener: PassengerViewHolderListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], listener)
    }

    fun updateList(list: List<FlightBookingPassengerViewModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(passenger: FlightBookingPassengerViewModel, listener: PassengerViewHolderListener) {
            with(view) {
                tv_passenger_name.text = passenger.headerTitle
                if (passenger.passengerFirstName != null) {
                    renderPassengerInfo(passenger)
                    tv_edit_passenger_info.text = "Ubah"
                } else {
                    rv_passenger_info.hide()
                    tv_edit_passenger_info.text = "Isi Data"
                }

                layout_edit_passenger.setOnClickListener { listener?.onClickEditPassengerListener(passenger) }
            }
        }

        fun renderPassengerInfo(passenger: FlightBookingPassengerViewModel) {
            with(view) {
                rv_passenger_info.show()
                tv_passenger_name.text = String.format("%s %s %s", passenger.passengerTitle ?: "", passenger.passengerFirstName, passenger.passengerLastName ?: "")

                //initiate passenger detail like passport num, birthdate, luggage and amenities
                var simpleViewModels = listOf<SimpleViewModel>().toMutableList()
                if (!passenger.passengerBirthdate.isNullOrEmpty()) simpleViewModels.add(SimpleViewModel("Tanggal Lahir" + " | ",
                        TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, passenger.passengerBirthdate))))
                if (!passenger.passportNumber.isNullOrEmpty()) simpleViewModels.add(SimpleViewModel("Nomor Paspor" + " | ", passenger.passportNumber))
                if (passenger.flightBookingLuggageMetaViewModels != null) {
                    for (flightBookingLuggageRouteViewModel in passenger.flightBookingLuggageMetaViewModels) {
                        val selectedLuggages = arrayListOf<String>()
                        for (flightBookingLuggageViewModel in flightBookingLuggageRouteViewModel.amenities) {
                            selectedLuggages.add(flightBookingLuggageViewModel.title)
                        }
                        simpleViewModels.add(SimpleViewModel("Bagasi" + " " + flightBookingLuggageRouteViewModel.description + " | ",
                                TextUtils.join(" + ", selectedLuggages)))
                    }
                }
                if (passenger.flightBookingMealMetaViewModels != null) {
                    for (flightBookingMealRouteViewModel in passenger.flightBookingMealMetaViewModels) {
                        simpleViewModels.add(SimpleViewModel("Makanan" + " " + flightBookingMealRouteViewModel.description + " | ",
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
            val LAYOUT = com.tokopedia.flight.R.layout.item_flight_booking_v3_passenger
        }
    }

    interface PassengerViewHolderListener{
        fun onClickEditPassengerListener(passenger: FlightBookingPassengerViewModel)
    }

}

