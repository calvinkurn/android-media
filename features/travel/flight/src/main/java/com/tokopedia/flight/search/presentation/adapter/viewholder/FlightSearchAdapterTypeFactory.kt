package com.tokopedia.flight.search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder
import com.tokopedia.flight.common.view.adapter.FlightAdapterTypeFactory
import com.tokopedia.flight.databinding.ItemFlightSearchNewBinding
import com.tokopedia.flight.databinding.ItemFlightSearchOtherJourneysBinding
import com.tokopedia.flight.databinding.ItemFlightSearchShimmeringBinding
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.flight.search.presentation.model.FlightSearchSeeAllResultModel

/**
 * @author by furqan on 13/04/2020
 */
class FlightSearchAdapterTypeFactory(private val onFlightSearchListener: OnFlightSearchListener)
    : FlightAdapterTypeFactory(), ErrorNetworkViewHolder.OnRetryListener {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            FlightSearchViewHolder.LAYOUT -> {
                val binding = ItemFlightSearchNewBinding.bind(parent)
                FlightSearchViewHolder(binding, onFlightSearchListener)
            }
            ErrorNetworkViewHolder.LAYOUT -> ErrorNetworkViewHolder(parent)
            FlightSearchShimmeringViewHolder.LAYOUT -> {
                val binding = ItemFlightSearchShimmeringBinding.bind(parent)
                FlightSearchShimmeringViewHolder(binding)
            }
            FlightSearchSeeAllViewHolder.LAYOUT -> {
                val binding = ItemFlightSearchOtherJourneysBinding.bind(parent)
                FlightSearchSeeAllViewHolder(binding, onFlightSearchListener)
            }
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(loadingModel: LoadingModel): Int = FlightSearchShimmeringViewHolder.LAYOUT

    fun type(seeAllResultModel: FlightSearchSeeAllResultModel): Int = FlightSearchSeeAllViewHolder.LAYOUT

    fun type(journeyModel: FlightJourneyModel): Int = FlightSearchViewHolder.LAYOUT

    override fun onRetryClicked() {
        onFlightSearchListener.onRetryClicked()
    }

    interface OnFlightSearchListener {
        fun onRetryClicked()
        fun onDetailClicked(journeyModel: FlightJourneyModel?, adapterPosition: Int)
        fun onItemClicked(journeyModel: FlightJourneyModel?, adapterPosition: Int)
        fun onShowAllClicked()
        fun onShowBestPairingClicked()
    }
}