package com.tokopedia.flight.search.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.flight.R
import com.tokopedia.flight.databinding.ItemFlightSearchNewBinding
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author by furqan on 13/04/2020
 */
class FlightSearchViewHolder(val binding: ItemFlightSearchNewBinding,
                             private val onFLightSearchListener: FlightSearchAdapterTypeFactory.OnFlightSearchListener)
    : AbstractViewHolder<FlightJourneyModel>(binding.root) {

    override fun bind(element: FlightJourneyModel) {
        with(binding) {
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
            root.setOnClickListener {
                onFLightSearchListener.onItemClicked(element, adapterPosition)
            }
        }
    }

    private fun setDuration(element: FlightJourneyModel) {
        with(binding) {
            var totalTransitAndStop = element.totalTransit
            for (route in element.routeList) {
                totalTransitAndStop += route.stops
            }

            if (totalTransitAndStop > 0) {
                tvFlightDuration.text = itemView.context.getString(R.string.flight_label_duration_transit,
                        element.duration, totalTransitAndStop.toString())
            } else {
                tvFlightDuration.text = itemView.context.getString(R.string.flight_label_duration_direct,
                        element.duration)
            }
        }
    }

    private fun setSavingPrice(element: FlightJourneyModel) {
        with(binding) {
            if (element.beforeTotal != null && element.beforeTotal.isNotEmpty()) {
                tvFlightStrikePrice.visibility = View.VISIBLE
                tvFlightStrikePrice.text = TextHtmlUtils.getTextFromHtml(getString(R.string.flight_label_saving_price_html, element.beforeTotal))
            } else {
                tvFlightStrikePrice.visibility = View.GONE
            }
        }
    }

    private fun setAirline(element: FlightJourneyModel) {
        with(binding) {
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
        with(binding) {
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
        with(binding) {
            if (element.addDayArrival > 0) {
                tvFlightAdditionalDay.visibility = View.VISIBLE
                tvFlightAdditionalDay.text = itemView.context.getString(R.string.flight_label_duration_add_day, element.addDayArrival)
            } else {
                tvFlightAdditionalDay.visibility = View.GONE
            }
        }
    }

    private fun setSeatDistanceAndRapidTest(element: FlightJourneyModel) {
        with(binding) {
            val isFreeRapidTest = element.freeRapidTestLabel.isNotEmpty()
            val isSeatDistancing = element.seatDistancingLabel.isNotEmpty()

            if (isSeatDistancing){
                labelSeatDistancing.text = element.seatDistancingLabel
                labelSeatDistancing.show()
            }else{
                labelSeatDistancing.hide()
            }

            if (isFreeRapidTest){
                labelFreeRapidTest.text = element.freeRapidTestLabel
                labelFreeRapidTest.show()
            }else{
                labelFreeRapidTest.hide()
            }

            if (isSeatDistancing && isFreeRapidTest) {
                labelSeatDistancingBackground.visibility = View.VISIBLE
            } else {
                labelSeatDistancingBackground.visibility = View.GONE
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_flight_search_new
    }

}