package com.tokopedia.flight.search.presentation.contract

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.flight.search.presentation.model.*
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel

/**
 * @author by furqan on 07/01/19
 */
interface FlightSearchContract {

    interface View: CustomerView {

        fun getSearchPassData(): FlightSearchPassDataModel

        fun getFilterModel(): FlightFilterModel

        fun getPriceStatisticPair(): Pair<Int, Int>

        fun getAirportCombineModelList(): FlightAirportCombineModelList

        fun isStatusCombineDone(): Boolean

        fun isReturning(): Boolean

        fun isDoneLoadData(): Boolean

        fun fetchFlightSearchData()

        fun fetchSortAndFilterData(fromCombo: Boolean = false)

        fun renderSearchList(list: List<FlightJourneyModel>, needRefresh: Boolean)

        fun renderTickerView(travelTickerModel: TravelTickerModel)

        fun addToolbarElevation()

        fun addProgress(numberToAdd: Int)

        fun addBottomPaddingForSortAndFilterActionButton()

        fun setCombineStatus(isCombineDone: Boolean)

        fun setSearchPassData(passDataModel: FlightSearchPassDataModel)

        fun showDepartureDateMaxTwoYears(resId: Int)

        fun showDepartureDateShouldAtLeastToday(resId: Int)

        fun showReturnDateShouldGreatedOrEqual(resId: Int)

        fun showEmptyFlightStateView()

        fun showNoRouteFlightEmptyState(message: String)

        fun showGetSearchListError(e: Throwable)

        fun hideHorizontalProgress()

        fun removeToolbarElevation()

        fun removeBottomPaddingForSortAndFilterActionButton()

        fun clearAdapterData()

        fun finishFragment()

        fun navigateToTheNextPage(selectedId: String, searchTerm: String, fareModel: FlightPriceModel, isBestPairing: Boolean)

        fun onGetSearchMeta(flightSearchMetaModel: FlightSearchMetaModel)

        fun onSuccessGetDetailFlightDeparture(flightJourneyModel: FlightJourneyModel)

        fun onErrorDeleteFlightCache(e: Throwable)

        fun onSuccessDeleteFlightCache()

        fun stopTrace()
    }

    interface Presenter {

        fun initialize(needDeleteData: Boolean = false)

        fun setDelayHorizontalProgress()

        fun resetCounterCall()

        fun isDoneLoadData(): Boolean

        fun onSeeDetailItemClicked(journeyModel: FlightJourneyModel, adapterPosition: Int)

        fun onSearchItemClicked(journeyModel: FlightJourneyModel? = null, adapterPosition: Int = -1, selectedId: String = "")

        fun onSuccessDateChanged(year: Int, month: Int, dayOfMonth: Int)

        fun getDetailDepartureFlight(journeyId: String)

        fun fetchCombineData(passDataModel: FlightSearchPassDataModel)

        fun fetchSearchData(passDataModel: FlightSearchPassDataModel, airportCombineModelList: FlightAirportCombineModelList)

        fun fetchSearchDataCloud(passDataModel: FlightSearchPassDataModel, airportCombineModel: FlightAirportCombineModel, delayInSecond: Int = -1)

        fun fetchSortAndFilter(@TravelSortOption flightSortOption: Int, flightFilterModel: FlightFilterModel, needRefresh: Boolean, fromCombo: Boolean = false)

        fun fetchTickerData()

        fun fireAndForgetReturnFlight(passDataModel: FlightSearchPassDataModel, airportCombineModel: FlightAirportCombineModel)

        fun unsubscribeAll()

        fun recountFilterCounter(): Int

        fun sendQuickFilterTrack(filterName: String)

    }
}