package com.tokopedia.flight.search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.flight.R
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import kotlinx.android.synthetic.main.item_flight_search_new.view.*

/**
 * @author by furqan on 13/04/2020
 */
class FlightSearchViewHolder(itemView: View,
                             private val onFLightSearchListener: FlightSearchAdapterTypeFactory.OnFlightSearchListener)
    : AbstractViewHolder<FlightJourneyModel>(itemView) {

    override fun bind(element: FlightJourneyModel) {
        with(itemView) {
            tvDepartureTime.text = element.departureTime
            tvDepartureAirport.text = element.departureAirport
            tvArrivalTime.text = element.arrivalTime
            tvArrivalAirport.text = element.arrivalAirport
            tvFlightPrice.text = element.fare.adult
            btnFlightDetail.setOnClickListener {
                onFLightSearchListener.onDetailClicked(element, adapterPosition)
            }

            setDuration(element)
            setAirline(element)
            setSavingPrice(element)
            setBestPairingPrice(element)
            setSeatDistanceAndRapidTest(element)
            setOnClickListener {
                onFLightSearchListener.onItemClicked(element, adapterPosition)
            }
        }
    }

    private fun setDuration(element: FlightJourneyModel) {
        with(itemView) {
            var totalTransitAndStop = element.totalTransit
            for (route in element.routeList) {
                totalTransitAndStop += route.stops
            }

            if (totalTransitAndStop > 0) {
                tvFlightDuration.text = context.getString(R.string.flight_label_duration_transit,
                        element.duration, totalTransitAndStop.toString())
            } else {
                tvFlightDuration.text = context.getString(R.string.flight_label_duration_direct,
                        element.duration)
            }
        }
    }

    private fun setSavingPrice(element: FlightJourneyModel) {
        with(itemView) {
            if (element.beforeTotal != null && element.beforeTotal.isNotEmpty()) {
                tvFlightStrikePrice.visibility = View.VISIBLE
                tvFlightStrikePrice.text = TextHtmlUtils.getTextFromHtml(getString(R.string.flight_label_saving_price_html, element.beforeTotal))
            } else {
                tvFlightStrikePrice.visibility = View.GONE
            }
        }
    }

    private fun setAirline(element: FlightJourneyModel) {
        with(itemView) {
            if (element.airlineDataList != null && element.airlineDataList.size > 1) {
                val flightAirlineList = element.airlineDataList
                flightMultiAirline.setAirlineLogos(null)
                tvAirline.text = ""
                for ((index, item) in flightAirlineList.withIndex()) {
                    if (index < flightAirlineList.size - 1) {
                        tvAirline.append("${item.shortName} + ")
                    } else {
                        tvAirline.append(item.shortName)
                    }
                }
            } else if (element.airlineDataList != null && element.airlineDataList.size == 1) {
                flightMultiAirline.setAirlineLogo(element.airlineDataList[0].logo)
                tvAirline.text = element.airlineDataList[0].shortName
            }
        }
    }

    private fun setBestPairingPrice(element: FlightJourneyModel) {
        with(itemView) {
            when {
                element.isBestPairing -> {
                    tagBestPairing.visibility = View.VISIBLE
                    tvFlightPrice.text = element.fare.adultCombo
                }
                element.fare.adultNumericCombo != 0 -> {
                    tagBestPairing.visibility = View.GONE
                    tvFlightPrice.text = element.fare.adultCombo
                }
                else -> {
                    tagBestPairing.visibility = View.GONE
                }
            }
        }
    }

    private fun setAdditionalArrivalDay(element: FlightJourneyModel) {
        with(itemView) {
            if (element.addDayArrival > 0) {
                tvFlightAdditionalDay.visibility = View.VISIBLE
                tvFlightAdditionalDay.text = context.getString(R.string.flight_label_duration_add_day, element.addDayArrival)
            } else {
                tvFlightAdditionalDay.visibility = View.GONE
            }
        }
    }

    private fun setSeatDistanceAndRapidTest(element: FlightJourneyModel) {
        with(itemView) {
            if (element.isSeatDistancing) {
                labelSeatDistancing.visibility = View.VISIBLE
            } else {
                labelSeatDistancing.visibility = View.GONE
            }

            if (element.hasFreeRapidTest) {
                labelFreeRapidTest.visibility = View.VISIBLE
            } else {
                labelFreeRapidTest.visibility = View.GONE
            }

            if (element.isSeatDistancing && element.hasFreeRapidTest) {
                labelSeatDistancingBackground.visibility = View.VISIBLE
            } else {
                labelSeatDistancingBackground.visibility = View.GONE
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_search_new
    }

}