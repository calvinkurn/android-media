package com.tokopedia.flight.searchV3.presentation.contract

import android.app.Activity
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchMetaViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel

/**
 * @author by furqan on 07/01/19
 */
interface FlightSearchContract {

    interface View: CustomerView {

        fun getActivity(): Activity

        fun getFlightSearchPassData(): FlightSearchPassDataViewModel

        fun getFilterModel(): FlightFilterModel

        fun isReturning(): Boolean

        fun isDoneLoadData(): Boolean

        fun renderSearchList(list: List<FlightJourneyViewModel>, needRefresh: Boolean)

        fun addToolbarElevation()

        fun addProgress(numberToAdd: Int)

        fun addBottomPaddingForSortAndFilterActionButton()

        fun setUIMarkFilter()

        fun setFlightSearchPassData(passDataViewModel: FlightSearchPassDataViewModel)

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

    }
}