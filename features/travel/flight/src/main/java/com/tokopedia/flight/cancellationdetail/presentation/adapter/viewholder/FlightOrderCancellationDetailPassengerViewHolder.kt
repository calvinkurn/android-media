package com.tokopedia.flight.cancellationdetail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationDetailPassengerDetailAdapter
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationDetailPassengerModel
import com.tokopedia.flight.databinding.ItemFlightReviewPassengerBinding
import com.tokopedia.flight.detail.view.model.SimpleModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailAmenityEnum

/**
 * @author by furqan on 08/01/2021
 */
class FlightOrderCancellationDetailPassengerViewHolder(val binding: ItemFlightReviewPassengerBinding)
    : AbstractViewHolder<FlightOrderCancellationDetailPassengerModel>(binding.root) {

    override fun bind(element: FlightOrderCancellationDetailPassengerModel) {
        with(binding) {
            passengerNumber.text = String.format("%d.", adapterPosition + 1)
            passengerName.text = String.format("%s %s", element.firstName, element.lastName)
            passengerCategory.text = String.format("(%s)", element.typeString)

            recyclerViewPassengerDetail.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
            if (element.amenities.isNotEmpty()) {
                recyclerViewPassengerDetail.visibility = View.VISIBLE
                val simpleModel: ArrayList<SimpleModel> = arrayListOf()
                for (item in element.amenities) {
                    if (item.departureId == element.departureAirportId &&
                            item.arrivalId == element.arrivalAirportId) {
                        simpleModel.add(SimpleModel().apply {
                            description = generateLabelPassenger(item.type,
                                    item.departureId,
                                    item.arrivalId)
                            label = item.detail
                        })
                    }
                }
                FlightOrderCancellationDetailPassengerDetailAdapter(simpleModel)
            } else {
                recyclerViewPassengerDetail.visibility = View.GONE
            }
        }
    }

    private fun generateLabelPassenger(type: Int, departureId: String, arrivalId: String): String {
        return when (type) {
            FlightOrderDetailAmenityEnum.LUGGAGE.type -> String.format(getString(R.string.flight_luggage_detail_order), departureId, arrivalId)
            FlightOrderDetailAmenityEnum.MEAL.type -> String.format(getString(R.string.flight_meal_detail_order), departureId, arrivalId)
            else -> ""
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_review_passenger
    }
}