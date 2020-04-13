package com.tokopedia.flight.searchV4.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.search.presentation.adapter.FlightSearchAdapterTypeFactory
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.flight.search.util.DurationUtil
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
            setOnClickListener {
                onFLightSearchListener.onItemClicked(element, adapterPosition)
            }
        }
    }

    private fun setDuration(element: FlightJourneyModel) {
        with(itemView) {
            val duration = DurationUtil.convertFormMinute(element.durationMinute)
            val durationString = DurationUtil.getReadableString(context, duration)
            if (element.totalTransit > 0) {
                tvFlightDuration.text = context.getString(R.string.flight_label_duration_transit,
                        durationString, element.totalTransit.toString())
            } else {
                tvFlightDuration.text = context.getString(R.string.flight_label_duration_direct,
                        durationString)
            }
        }
    }

    private fun setSavingPrice(element: FlightJourneyModel) {
        with(itemView) {
            if (element.beforeTotal != null && element.beforeTotal.isNotEmpty()) {
                tvFlightStrikePrice.visibility = View.VISIBLE
                tvFlightStrikePrice.text = element.beforeTotal
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

    companion object {
        val LAYOUT = R.layout.item_flight_search_new
    }

}