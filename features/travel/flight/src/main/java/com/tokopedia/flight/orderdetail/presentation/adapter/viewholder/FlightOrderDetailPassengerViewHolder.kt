package com.tokopedia.flight.orderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.orderdetail.presentation.adapter.FlightOrderDetailPassengerCancelStatusAdapter
import com.tokopedia.flight.orderdetail.presentation.adapter.FlightOrderDetailSimpleAdapter
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailAmenityEnum
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailPassengerCancelStatusModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailPassengerModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailSimpleModel
import kotlinx.android.synthetic.main.item_flight_order_detail_passenger_detail.view.*

/**
 * @author by furqan on 30/10/2020
 */
class FlightOrderDetailPassengerViewHolder(view: View)
    : RecyclerView.ViewHolder(view) {

    fun bind(passengerModel: FlightOrderDetailPassengerModel) {
        with(itemView) {
            tgFlightOrderPassengerNumber.text = context.getString(
                    R.string.flight_order_detail_passenger_detail_number,
                    passengerModel.passengerNo)

            tgFlightOrderPassengerName.text = String.format("%s %s %s (%s)",
                    passengerModel.titleString,
                    passengerModel.firstName,
                    passengerModel.lastName,
                    passengerModel.typeString)

            if (passengerModel.cancelStatus.isNotEmpty()) {
                setupPassengerCancelStatus(passengerModel.cancelStatus)
            }

            renderAmenities(passengerModel)
        }
    }

    private fun setupPassengerCancelStatus(cancelStatusList: List<FlightOrderDetailPassengerCancelStatusModel>) {
        with(itemView) {
            val adapter = FlightOrderDetailPassengerCancelStatusAdapter(cancelStatusList)
            rvFlightOrderPassengerCancelStatus.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            rvFlightOrderPassengerCancelStatus.setHasFixedSize(true)
            rvFlightOrderPassengerCancelStatus.adapter = adapter
        }
    }

    private fun renderAmenities(passengerModel: FlightOrderDetailPassengerModel) {
        with(itemView) {
            val luggageAmenities = ArrayList<FlightOrderDetailSimpleModel>()
            val mealAmenities = ArrayList<FlightOrderDetailSimpleModel>()
            passengerModel.amenities.map {
                when (it.type) {
                    FlightOrderDetailAmenityEnum.LUGGAGE.type -> {
                        luggageAmenities.add(
                                FlightOrderDetailSimpleModel(
                                        context.getString(
                                                R.string.flight_order_detail_passenger_amenities_label,
                                                FlightOrderDetailAmenityEnum.LUGGAGE.text,
                                                it.departureId,
                                                it.arrivalId
                                        ),
                                        it.detail,
                                        false,
                                        true
                                )
                        )
                    }
                    FlightOrderDetailAmenityEnum.MEAL.type -> {
                        mealAmenities.add(
                                FlightOrderDetailSimpleModel(
                                        context.getString(
                                                R.string.flight_order_detail_passenger_amenities_label,
                                                FlightOrderDetailAmenityEnum.MEAL.text,
                                                it.departureId,
                                                it.arrivalId
                                        ),
                                        it.detail,
                                        false,
                                        true
                                )
                        )
                    }
                    else -> {
                    }
                }
            }

            val amenityItems = ArrayList<FlightOrderDetailSimpleModel>()
            amenityItems.addAll(luggageAmenities)
            amenityItems.addAll(mealAmenities)

            val adapter = FlightOrderDetailSimpleAdapter(amenityItems)
            rvFlightOrderPassengerAmenity.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            rvFlightOrderPassengerAmenity.setHasFixedSize(true)
            rvFlightOrderPassengerAmenity.adapter = adapter
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_order_detail_passenger_detail
    }

}