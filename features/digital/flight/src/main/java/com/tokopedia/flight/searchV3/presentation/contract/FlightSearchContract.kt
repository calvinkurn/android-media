package com.tokopedia.flight.searchV3.presentation.contract

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.flight.search.constant.FlightSortOption
import com.tokopedia.flight.search.presentation.model.*
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel

/**
 * @author by furqan on 07/01/19
 */
interface FlightSearchContract {

    interface View: CustomerView {

//        fun getActivity(): Activity

        fun getSearchPassData(): FlightSearchPassDataViewModel

        fun getFilterModel(): FlightFilterModel

        fun isReturning(): Boolean

        fun isDoneLoadData(): Boolean

        fun renderSearchList(list: List<FlightJourneyViewModel>, needRefresh: Boolean)

        fun addToolbarElevation()

        fun addProgress(numberToAdd: Int)

        fun addBottomPaddingForSortAndFilterActionButton()

        fun setUIMarkFilter()

        fun setSearchPassData(passDataViewModel: FlightSearchPassDataViewModel)

        fun setSelectedSortItem(sortItemId: Int)

        fun showDepartureDateMaxTwoYears(resId: Int)

        fun showDepartureDateShouldAtLeastToday(resId: Int)

        fun showReturnDateShouldGreatedOrEqual(resId: Int)

        fun showFilterAndSortView()

        fun showEmptyFlightStateView()

        fun showNoRouteFlightEmptyState(message: String)

        fun showGetListError(e: Throwable)

        fun hideHorizontalProgress()

        fun hideFilterAndSortView()

        fun removeToolbarElevation()

        fun removeBottomPaddingForSortAndFilterActionButton()

        fun clearAdapterData()

        fun finishFragment()

        fun navigateToTheNextPage(selectedId: String, fareViewModel: FlightPriceViewModel, isBestPairing: Boolean)

        fun onGetSearchMeta(flightSearchMetaViewModel: FlightSearchMetaViewModel)

        fun onSuccessGetDetailFlightDeparture(flightJourneyViewModel: FlightJourneyViewModel)

        fun onErrorDeleteFlightCache(e: Throwable)

        fun onSuccessDeleteFlightCache()

    }

    interface Presenter {

        fun setDelayHorizontalProgress()

        fun resetCounterCall()

        fun isDoneLoadData(): Boolean

        fun onSeeDetailItemClicked(journeyViewModel: FlightJourneyViewModel, adapterPosition: Int)

        fun onSearchItemClicked(journeyViewModel: FlightJourneyViewModel?, adapterPosition: Int?, selectedId: String?)

        fun onSuccessDateChanged(year: Int, month: Int, dayOfMonth: Int)

        fun getDetailDepartureFlight(journeyId: String)

        fun fetchCombineData(passDataViewModel: FlightSearchPassDataViewModel)

        fun fetchSearchData(passDataViewModel: FlightSearchPassDataViewModel, airportCombineModel: FlightAirportCombineModel, delayInSecond: Int?)

        fun fetchSortAndFilter(@FlightSortOption flightSortOption: Int, flightFilterModel: FlightFilterModel, needRefresh: Boolean)

    }
}