package com.tokopedia.flight.searchV3.presentation.contract

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.flight.search.presentation.model.*
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel

/**
 * @author by furqan on 07/01/19
 */
interface FlightSearchContract {

    interface View: CustomerView {

        fun getSearchPassData(): FlightSearchPassDataViewModel

        fun getFilterModel(): FlightFilterModel

        fun getAirportCombineModelList(): FlightAirportCombineModelList

        fun isStatusCombineDone(): Boolean

        fun isReturning(): Boolean

        fun isDoneLoadData(): Boolean

        fun fetchFlightSearchData()

        fun fetchSortAndFilterData()

        fun renderSearchList(list: List<FlightJourneyViewModel>, needRefresh: Boolean)

        fun addToolbarElevation()

        fun addProgress(numberToAdd: Int)

        fun addBottomPaddingForSortAndFilterActionButton()

        fun setCombineStatus(isCombineDone: Boolean)

        fun setUIMarkFilter()

        fun setSearchPassData(passDataViewModel: FlightSearchPassDataViewModel)

        fun setSelectedSortItem(sortItemId: Int)

        fun showDepartureDateMaxTwoYears(resId: Int)

        fun showDepartureDateShouldAtLeastToday(resId: Int)

        fun showReturnDateShouldGreatedOrEqual(resId: Int)

        fun showFilterAndSortView()

        fun showEmptyFlightStateView()

        fun showNoRouteFlightEmptyState(message: String)

        fun showGetSearchListError(e: Throwable)

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

        fun stopTrace()
    }

    interface Presenter {

        fun initialize(needDeleteData: Boolean = false)

        fun setDelayHorizontalProgress()

        fun resetCounterCall()

        fun isDoneLoadData(): Boolean

        fun onSeeDetailItemClicked(journeyViewModel: FlightJourneyViewModel, adapterPosition: Int)

        fun onSearchItemClicked(journeyViewModel: FlightJourneyViewModel? = null, adapterPosition: Int = -1, selectedId: String = "")

        fun onSuccessDateChanged(year: Int, month: Int, dayOfMonth: Int)

        fun getDetailDepartureFlight(journeyId: String)

        fun fetchCombineData(passDataViewModel: FlightSearchPassDataViewModel)

        fun fetchSearchData(passDataViewModel: FlightSearchPassDataViewModel, airportCombineModelList: FlightAirportCombineModelList)

        fun fetchSearchDataCloud(passDataViewModel: FlightSearchPassDataViewModel, airportCombineModel: FlightAirportCombineModel, delayInSecond: Int = -1)

        fun fetchSortAndFilter(@TravelSortOption flightSortOption: Int, flightFilterModel: FlightFilterModel, needRefresh: Boolean)

        fun fireAndForgetReturnFlight(passDataViewModel: FlightSearchPassDataViewModel, airportCombineModel: FlightAirportCombineModel)

        fun unsubscribeAll()

    }
}