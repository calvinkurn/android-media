package com.tokopedia.flight.cancellationdetail.presentation.adapter.viewholder

import android.view.View
import android.view.animation.AnimationUtils
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationDetailRouteAdapterTypeFactory
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationDetailRouteTypeFactory
import com.tokopedia.flight.databinding.ItemFlightDetailOrderBinding
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel

/**
 * @author by furqan on 07/01/2021
 */
class FlightOrderCancellationDetailJourneyViewHolder(private val listener: Listener,
                                                     private val titleFontSize: Float,
                                                     val binding: ItemFlightDetailOrderBinding)
    : AbstractViewHolder<FlightOrderDetailJourneyModel>(binding.root) {

    private var isFlightInfoShowed: Boolean = true

    override fun bind(element: FlightOrderDetailJourneyModel) {
        with(binding) {
            containerFlightDetailOrder.setBackgroundColor(
                    MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN0))

            // journey routes
            val detailRouteTypeFactory: FlightOrderCancellationDetailRouteTypeFactory =
                    FlightOrderCancellationDetailRouteAdapterTypeFactory(
                            object : FlightOrderCancellationDetailRouteViewHolder.Listener {
                                override fun getItemCount(): Int = element.routes.size
                            },
                            true
                    )
            val detailRouteAdapter = BaseAdapter<FlightOrderCancellationDetailRouteTypeFactory>(
                    detailRouteTypeFactory,
                    element.routes)
            recyclerViewFlightDetailJourney.adapter = detailRouteAdapter

            // expandable action
            layoutExpendableFlight.setOnClickListener {
                imageExpendableJourney.startAnimation(AnimationUtils
                        .loadAnimation(itemView.context, R.anim.flight_rotate_reverse))
                toggleFlightInfo()
            }

            // flight counter
            counterFlight.textSize = titleFontSize
            counterFlight.text = itemView.context.getString(R.string.flight_label_detail_counter, adapterPosition + 1)

            // journey title
            titleExpendablePassenger.text = itemView.context.getString(R.string.flight_label_detail_format,
                    element.departureCityName, element.departureId, element.arrivalCityName, element.arrivalId)
        }
    }

    private fun toggleFlightInfo() {
        with(binding) {
            if (isFlightInfoShowed) {
                isFlightInfoShowed = false
                recyclerViewFlightDetailJourney.visibility = View.GONE
                textViewFlightCekSyarat.visibility = View.GONE
                flightDetailOrderSeparatorLine.visibility = View.GONE
                imageExpendableJourney.rotation = 180f
            } else {
                isFlightInfoShowed = true
                recyclerViewFlightDetailJourney.visibility = View.VISIBLE
                textViewFlightCekSyarat.visibility = View.VISIBLE
                flightDetailOrderSeparatorLine.visibility = View.VISIBLE
                imageExpendableJourney.rotation = 0f
                listener.onCloseExpand(adapterPosition)
            }
        }
    }

    interface Listener {
        fun onCloseExpand(position: Int)
    }

    companion object {
        val LAYOUT = R.layout.item_flight_detail_order
    }
}