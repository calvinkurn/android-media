package com.tokopedia.flight.searchV4.presentation.fragment

import android.os.Bundle
import com.tokopedia.flight.R
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchActivity.Companion.EXTRA_PASS_DATA
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_DEPARTURE_ID
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_IS_BEST_PAIRING
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_IS_COMBINE_DONE
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_PRICE_MODEL

/**
 * @author by furqan on 15/04/2020
 */
class FlightSearchReturnFragment : FlightSearchFragment() {

    override fun getLayout(): Int = R.layout.fragment_search_return

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    override fun isReturnTrip(): Boolean = true

    override fun onItemClicked(journeyModel: FlightJourneyModel?) {
        super.onItemClicked(journeyModel)
    }

    override fun onItemClicked(journeyViewModel: FlightJourneyModel?, adapterPosition: Int) {
        super.onItemClicked(journeyViewModel, adapterPosition)
    }


    companion object {
        fun newInstance(passDataModel: FlightSearchPassDataModel,
                        selectedDepartureId: String,
                        isBestPairing: Boolean,
                        priceModel: FlightPriceModel,
                        isCombineDone: Boolean): FlightSearchReturnFragment =
                FlightSearchReturnFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(EXTRA_PASS_DATA, passDataModel)
                        putString(EXTRA_DEPARTURE_ID, selectedDepartureId)
                        putBoolean(EXTRA_IS_BEST_PAIRING, isBestPairing)
                        putParcelable(EXTRA_PRICE_MODEL, priceModel)
                        putBoolean(EXTRA_IS_COMBINE_DONE, isCombineDone)
                    }
                }
    }
}