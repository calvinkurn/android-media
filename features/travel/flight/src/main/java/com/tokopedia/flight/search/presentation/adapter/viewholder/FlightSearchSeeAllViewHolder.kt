package com.tokopedia.flight.search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.flight.R
import com.tokopedia.flight.search.presentation.model.FlightSearchSeeAllResultModel
import kotlinx.android.synthetic.main.item_flight_search_other_journeys.view.*

/**
 * @author by furqan on 20/04/2020
 */
class FlightSearchSeeAllViewHolder(itemView: View,
                                   private val onFLightSearchListener: FlightSearchAdapterTypeFactory.OnFlightSearchListener)
    : AbstractViewHolder<FlightSearchSeeAllResultModel>(itemView) {
    override fun bind(element: FlightSearchSeeAllResultModel) {
        with(itemView) {
            if (element.isOnlyBestPairing) {
                tvFlightSearchSeeAllJourneyTitle.setText(R.string.flight_search_see_only_best_pairing_journey_title)
                tvFlightSearchSeeAllJourneyDescription.text = TextHtmlUtils.getTextFromHtml(
                        getString(R.string.flight_search_see_only_best_pairing_journey_description, element.newPrice))
                btnFlightSearchSeeAllJourney.setText(R.string.flight_search_see_only_best_pairing_journey_button)
                btnFlightSearchSeeAllJourney.setOnClickListener {
                    onFLightSearchListener.onShowBestPairingClicked()
                }
            } else {
                tvFlightSearchSeeAllJourneyTitle.setText(R.string.flight_search_see_other_journey_title)
                tvFlightSearchSeeAllJourneyDescription.text = TextHtmlUtils.getTextFromHtml(
                        getString(R.string.flight_search_see_other_journey_description, element.newPrice))
                btnFlightSearchSeeAllJourney.setText(R.string.flight_search_see_other_journey_button)
                btnFlightSearchSeeAllJourney.setOnClickListener {
                    onFLightSearchListener.onShowAllClicked()
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_search_other_journeys
    }
}