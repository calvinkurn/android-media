package com.tokopedia.flight.cancellationdetail.presentation.adapter.viewholder

import android.view.View
import android.view.animation.AnimationUtils
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationDetailRouteAdapterTypeFactory
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationDetailRouteTypeFactory
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel
import kotlinx.android.synthetic.main.item_flight_detail_order.view.*

/**
 * @author by furqan on 07/01/2021
 */
class FlightOrderCancellationDetailJourneyViewHolder(private val listener: Listener,
                                                     private val titleFontSize: Float,
                                                     view: View)
    : AbstractViewHolder<FlightOrderDetailJourneyModel>(view) {

    private var isFlightInfoShowed: Boolean = true

    override fun bind(element: FlightOrderDetailJourneyModel) {
        with(itemView) {
            container_flight_detail_order.setBackgroundColor(
                    MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))

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
            recycler_view_flight_detail_journey.adapter = detailRouteAdapter

            // expandable action
            layout_expendable_flight.setOnClickListener {
                image_expendable_journey.startAnimation(AnimationUtils
                        .loadAnimation(context, R.anim.flight_rotate_reverse))
                toggleFlightInfo()
            }

            // flight counter
            counter_flight.textSize = titleFontSize
            counter_flight.text = context.getString(R.string.flight_label_detail_counter, adapterPosition + 1)

            // journey title
            title_expendable_passenger.text = context.getString(R.string.flight_label_detail_format,
                    element.departureCityName, element.departureId, element.arrivalCityName, element.arrivalId)
        }
    }

    private fun toggleFlightInfo() {
        with(itemView) {
            if (isFlightInfoShowed) {
                isFlightInfoShowed = false
                recycler_view_flight_detail_journey.visibility = View.GONE
                text_view_flight_cek_syarat.visibility = View.GONE
                flight_detail_order_separator_line.visibility = View.GONE
                image_expendable_journey.rotation = 180f
            } else {
                isFlightInfoShowed = true
                recycler_view_flight_detail_journey.visibility = View.VISIBLE
                text_view_flight_cek_syarat.visibility = View.VISIBLE
                flight_detail_order_separator_line.visibility = View.VISIBLE
                image_expendable_journey.rotation = 0f
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