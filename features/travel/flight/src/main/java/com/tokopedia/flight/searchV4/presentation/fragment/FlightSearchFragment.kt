package com.tokopedia.flight.searchV4.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.dashboard.view.widget.FlightCalendarOneWayWidget
import com.tokopedia.flight.filter.presentation.FlightFilterFacilityEnum
import com.tokopedia.flight.filter.presentation.bottomsheets.FlightFilterBottomSheet
import com.tokopedia.flight.search.presentation.adapter.viewholder.EmptyResultViewHolder
import com.tokopedia.flight.search.presentation.model.EmptyResultModel
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.model.filter.TransitEnum
import com.tokopedia.flight.search.util.FlightSearchCache
import com.tokopedia.flight.search.util.select
import com.tokopedia.flight.search.util.unselect
import com.tokopedia.flight.searchV4.di.DaggerFlightSearchComponent
import com.tokopedia.flight.searchV4.di.FlightSearchComponent
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchActivity
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_IS_COMBINE_DONE
import com.tokopedia.flight.searchV4.presentation.adapter.viewholder.FlightSearchAdapterTypeFactory
import com.tokopedia.flight.searchV4.presentation.viewmodel.FlightSearchViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_search_flight.*
import kotlinx.android.synthetic.main.include_flight_quick_filter.*
import kotlinx.android.synthetic.main.include_flight_search_title_route.*
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 06/04/2020
 */
open class FlightSearchFragment : BaseListFragment<FlightJourneyModel, FlightSearchAdapterTypeFactory>(),
        FlightSearchAdapterTypeFactory.OnFlightSearchListener,
        FlightFilterBottomSheet.FlightFilterBottomSheetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    protected lateinit var flightSearchViewModel: FlightSearchViewModel
    protected var onFlightSearchFragmentListener: OnFlightSearchFragmentListener? = null
    private lateinit var flightSearchComponent: FlightSearchComponent

    private lateinit var coachMarkCache: FlightSearchCache

    private val filterItems = arrayListOf<SortFilterItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightSearchViewModel = viewModelProvider.get(FlightSearchViewModel::class.java)

            arguments?.let { args ->
                args.getParcelable<FlightSearchPassDataModel>(FlightSearchActivity.EXTRA_PASS_DATA)?.let {
                    flightSearchViewModel.flightSearchPassData = it
                }
                flightSearchViewModel.isCombineDone = args.getBoolean(EXTRA_IS_COMBINE_DONE, false)
            }

            if (savedInstanceState == null) {
                flightSearchViewModel.filterModel = buildFilterModel(FlightFilterModel())
                flightSearchViewModel.flightAirportCombine = flightSearchViewModel.buildAirportCombineModel(
                        getDepartureAirport(), getArrivalAirport())
            }

            flightSearchViewModel.initialize(true, isReturnTrip())
            flightSearchViewModel.fetchSearchDataCloud(isReturnTrip())
        }

        coachMarkCache = FlightSearchCache(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(getLayout(), container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flightSearchViewModel.journeyList.observe(viewLifecycleOwner, Observer {
            renderSearchList(it)
        })

        flightSearchViewModel.progress.observe(viewLifecycleOwner, Observer {
            setUpProgress(it)
        })

        flightSearchViewModel.selectedJourney.observe(viewLifecycleOwner, Observer {
            it?.let { flightSearchSelectedModel ->
                navigateToTheNextPage(flightSearchSelectedModel.journeyModel.id,
                        flightSearchSelectedModel.journeyModel.term,
                        flightSearchSelectedModel.priceModel,
                        flightSearchSelectedModel.journeyModel.isBestPairing)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSwipeRefresh()
        setupQuickFilter()
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        onFlightSearchFragmentListener = context as OnFlightSearchFragmentListener
    }

    override fun getAdapterTypeFactory(): FlightSearchAdapterTypeFactory =
            FlightSearchAdapterTypeFactory(this)

    override fun getScreenName(): String = FlightAnalytics.Screen.SEARCH

    override fun initInjector() {
        if (!::flightSearchComponent.isInitialized) {
            flightSearchComponent = DaggerFlightSearchComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(requireActivity().application))
                    .build()
        }
        flightSearchComponent.inject(this)
    }

    override fun renderList(list: List<FlightJourneyModel>) {
        hideLoading()

        // remove all unneeded element (empty/retry/loading/etc)
        if (isLoadingInitialData) {
            clearAllData()
        } else {
            adapter.clearAllNonDataElement()
        }

        adapter.addElement(list)
        updateScrollListenerState(false)

        if (isListEmpty && flightSearchViewModel.isDoneLoadData()) {

        }

        if (flightSearchViewModel.isDoneLoadData()) {
            // stop trace search
            if (!coachMarkCache.isSearchCoachMarkShowed()) {
                (activity as FlightSearchActivity).setupAndShowCoachMark()
            }
        }
    }

    override fun isLoadMoreEnabledByDefault(): Boolean = false

    override fun isListEmpty(): Boolean = !adapter.isContainData

    override fun onDestroyView() {
        super.onDestroyView()
        this.clearFindViewByIdCache()
    }

    override fun onItemClicked(journeyModel: FlightJourneyModel?) {
        flightSearchViewModel.onSearchItemClicked(journeyModel = journeyModel)
    }

    override fun loadData(page: Int) {}

    override fun onDetailClicked(journeyViewModel: FlightJourneyModel?, adapterPosition: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClicked(journeyViewModel: FlightJourneyModel?, adapterPosition: Int) {
        flightSearchViewModel.onSearchItemClicked(journeyViewModel, adapterPosition)
    }

    override fun onShowAllClicked() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShowBestPairingClicked() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSaveFilter(sortOption: Int, flightFilterModel: FlightFilterModel?, statisticPricePair: Pair<Int, Int>) {
        flightSearchViewModel.selectedSortOption = sortOption
        flightSearchViewModel.priceFilterStatistic = statisticPricePair
        if (flightFilterModel != null) {
            flightSearchViewModel.filterModel = flightFilterModel
        }
        fetchSortAndFilterData()
    }

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    open fun getLayout(): Int = R.layout.fragment_search_flight

    open fun isReturnTrip(): Boolean = false

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyResultViewModel = EmptyResultModel()
        emptyResultViewModel.iconRes = R.drawable.ic_flight_empty_state
        if (flightSearchViewModel.isInFilterMode) {
            emptyResultViewModel.contentRes = R.string.flight_there_is_zero_flight_for_the_filter
            emptyResultViewModel.buttonTitleRes = R.string.reset_filter
            emptyResultViewModel.callback = object : EmptyResultViewHolder.Callback {
                override fun onEmptyContentItemTextClicked() {

                }

                override fun onEmptyButtonClicked() {
                    onResetFilterClicked()
                }
            }
        } else {
            emptyResultViewModel.contentRes = R.string.flight_there_is_no_flight_available
            emptyResultViewModel.buttonTitleRes = R.string.change_date
            emptyResultViewModel.callback = object : EmptyResultViewHolder.Callback {
                override fun onEmptyContentItemTextClicked() {

                }

                override fun onEmptyButtonClicked() {
                    onChangeDateClicked()
                }
            }
        }

        return emptyResultViewModel
    }


    open fun buildFilterModel(filterModel: FlightFilterModel): FlightFilterModel =
            filterModel

    open fun getDepartureAirport(): FlightAirportModel = flightSearchViewModel.flightSearchPassData.departureAirport

    open fun getArrivalAirport(): FlightAirportModel = flightSearchViewModel.flightSearchPassData.arrivalAirport

    open fun onSelectedFromDetail(selectedId: String, selectedTerm: String) {
        flightSearchViewModel.onSearchItemClicked(selectedId = selectedId)
    }

    private fun renderSearchList(list: List<FlightJourneyModel>) {
        if (!flightSearchViewModel.isOneWay() && !adapter.isContainData) {
            showSearchRouteTitle()
        }

        if (list.isNotEmpty()) {
            renderList(list)
        }

        if (list.isNotEmpty()) {
            setupQuickFilter()
            showQuickFilter()
        } else if (!adapter.isContainData) {
            hideQuickFilter()
        }
    }

    private fun fetchSortAndFilterData() {
        if (adapter.itemCount == 0) {
            showLoading()
        }

        flightSearchViewModel.fetchSortAndFilter()
    }

    private fun setUpProgress(progress: Int) {
        if (horizontal_progress_bar.visibility == View.VISIBLE) {
            horizontal_progress_bar.setProgress(progress)
            if (flightSearchViewModel.isDoneLoadData()) {
                Handler().postDelayed({
                    try {
                        horizontal_progress_bar.hide()
                    } catch (t: Throwable) {
                        t.printStackTrace()
                    }
                }, HIDE_HORIZONTAL_PROGRESS_DELAY)
            }
        }
    }

    private fun onResetFilterClicked() {
        flightSearchViewModel.filterModel = buildFilterModel(FlightFilterModel())
        adapter.clearAllNonDataElement()
        showLoading()
        setupQuickFilter()
        fetchSortAndFilterData()
    }

    private fun onChangeDateClicked() {
        if (!activity!!.isFinishing) {
            var maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, MAX_DATE_ADDITION_YEAR)
            maxDate = FlightDateUtil.addTimeToSpesificDate(maxDate, Calendar.DATE, -1)
            maxDate = FlightDateUtil.trimDate(maxDate)
            var minDate: Date

            if (isReturnTrip()) {
                val dateDepStr = flightSearchViewModel.flightSearchPassData.getDate(false)
                val dateDep = FlightDateUtil.stringToDate(dateDepStr)
                minDate = FlightDateUtil.trimDate(dateDep)
            } else {
                minDate = FlightDateUtil.trimDate(FlightDateUtil.getCurrentDate())

                if (!flightSearchViewModel.flightSearchPassData.isOneWay) {
                    val dateReturnStr = flightSearchViewModel.flightSearchPassData.getDate(true)
                    val dateReturn = FlightDateUtil.stringToDate(dateReturnStr)
                    maxDate = FlightDateUtil.trimDate(dateReturn)
                }
            }

            val dateInput = flightSearchViewModel.flightSearchPassData.getDate(isReturnTrip())
            val date = FlightDateUtil.stringToDate(dateInput)
            val flightCalendarDialog = FlightCalendarOneWayWidget.newInstance(
                    TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, minDate),
                    TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, maxDate),
                    TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, date),
                    flightSearchViewModel.flightSearchPassData.departureAirport.airportCode,
                    flightSearchViewModel.flightSearchPassData.arrivalAirport.airportCode,
                    flightSearchViewModel.flightSearchPassData.flightClass.id)
            flightCalendarDialog.setListener(object : FlightCalendarOneWayWidget.ActionListener {
                override fun onDateSelected(dateSelected: Date) {
                    val calendar = FlightDateUtil.getCurrentCalendar()
                    calendar.time = dateSelected
                    /*flightSearchViewModel.onSuccessDateChanged(calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))*/
                }
            })
            flightCalendarDialog.show(activity!!.supportFragmentManager, TAG_TRAVEL_CALENDAR)
        }
    }

    private fun setupSwipeRefresh() {
        val swipeRefreshLayout = requireView().findViewById<SwipeToRefresh>(swipeRefreshLayoutResourceId)
        swipeRefreshLayout.setSwipeDistance()
        swipeRefreshLayout.setOnRefreshListener {
            hideLoading()
            swipeRefreshLayout.isRefreshing = false
            resetDateAndReload()
        }
    }

    private fun setupQuickFilter() {
        buildQuickFilterView()
    }

    private fun showQuickFilter() {
        flight_sort_filter.visibility = View.VISIBLE
    }

    private fun hideQuickFilter() {
        flight_sort_filter.visibility = View.GONE
    }

    private fun showFilterSortBottomSheet() {
        val flightFilterBottomSheet = FlightFilterBottomSheet.getInstance(flightSearchViewModel.selectedSortOption, flightSearchViewModel.filterModel)
        flightFilterBottomSheet.listener = this
        flightFilterBottomSheet.setShowListener { flightFilterBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        flightFilterBottomSheet.show(requireFragmentManager(), FlightFilterBottomSheet.TAG_FILTER)
    }

    private fun buildQuickFilterView() {
        flight_sort_filter.filterType = SortFilter.TYPE_ADVANCED
        flight_sort_filter.parentListener = {
            flightSearchViewModel.sendQuickFilterTrack(FLIGHT_QUICK_FILTER)
            showFilterSortBottomSheet()
        }
        flight_sort_filter.sortFilterHorizontalScrollView.scrollX = 0

        // setup items
        if (filterItems.size < FILTER_SORT_ITEM_SIZE) {
            val quickDirectFilter = SortFilterItem(getString(R.string.direct))
            quickDirectFilter.listener = {
                if (flightSearchViewModel.isFilterModelInitialized() &&
                        flightSearchViewModel.filterModel.transitTypeList.contains(TransitEnum.DIRECT)) {
                    flightSearchViewModel.filterModel.transitTypeList.remove(TransitEnum.DIRECT)
                    quickDirectFilter.unselect()
                } else if (flightSearchViewModel.isFilterModelInitialized()) {
                    flightSearchViewModel.filterModel.transitTypeList.add(TransitEnum.DIRECT)
                    quickDirectFilter.select()
                }

                flightSearchViewModel.sendQuickFilterTrack(FLIGHT_QUICK_FILTER_DIRECT)
                //need refresh false
                fetchSortAndFilterData()
            }

            val quickBaggageFilter = SortFilterItem(getString(R.string.flight_search_filter_baggage_label))
            quickBaggageFilter.listener = {
                if (flightSearchViewModel.isFilterModelInitialized() &&
                        flightSearchViewModel.filterModel.facilityList.contains(FlightFilterFacilityEnum.BAGGAGE)) {
                    flightSearchViewModel.filterModel.facilityList.remove(FlightFilterFacilityEnum.BAGGAGE)
                    quickBaggageFilter.unselect()
                } else if (flightSearchViewModel.isFilterModelInitialized()) {
                    flightSearchViewModel.filterModel.facilityList.add(FlightFilterFacilityEnum.BAGGAGE)
                    quickBaggageFilter.select()
                }
                flightSearchViewModel.sendQuickFilterTrack(FLIGHT_QUICK_FILTER_BAGGAGE)
                //need refresh false
                fetchSortAndFilterData()
            }

            val quickMealFilter = SortFilterItem(getString(R.string.flight_search_filter_meal_label))
            quickMealFilter.listener = {
                if (flightSearchViewModel.isFilterModelInitialized() &&
                        flightSearchViewModel.filterModel.facilityList.contains(FlightFilterFacilityEnum.MEAL)) {
                    flightSearchViewModel.filterModel.facilityList.remove(FlightFilterFacilityEnum.MEAL)
                    quickMealFilter.unselect()
                } else if (flightSearchViewModel.isFilterModelInitialized()) {
                    flightSearchViewModel.filterModel.facilityList.add(FlightFilterFacilityEnum.MEAL)
                    quickMealFilter.select()
                }
                flightSearchViewModel.sendQuickFilterTrack(FLIGHT_QUICK_FILTER_MEAL)
                fetchSortAndFilterData()
            }

            val quickTransitFilter = SortFilterItem(getString(R.string.flight_search_filter_transit))
            quickTransitFilter.listener = {
                if (flightSearchViewModel.isFilterModelInitialized() &&
                        flightSearchViewModel.filterModel.transitTypeList.contains(TransitEnum.ONE)) {
                    flightSearchViewModel.filterModel.transitTypeList.remove(TransitEnum.ONE)
                    quickTransitFilter.unselect()
                } else if (flightSearchViewModel.isFilterModelInitialized()) {
                    flightSearchViewModel.filterModel.transitTypeList.add(TransitEnum.ONE)
                    quickTransitFilter.select()
                }
                flightSearchViewModel.sendQuickFilterTrack(FLIGHT_QUICK_FILTER_TRANSIT)
                fetchSortAndFilterData()
            }

            filterItems.add(quickDirectFilter)
            filterItems.add(quickBaggageFilter)
            filterItems.add(quickMealFilter)
            filterItems.add(quickTransitFilter)

            flight_sort_filter.addItem(filterItems)
        }

        // setup state
        if (flightSearchViewModel.isFilterModelInitialized()) {
            flightSearchViewModel.filterModel.let {
                if (it.transitTypeList.contains(TransitEnum.DIRECT)) {
                    flight_sort_filter.chipItems[QUICK_FILTER_DIRECT_ORDER].select()
                    flight_sort_filter.chipItems[QUICK_FILTER_DIRECT_ORDER].refChipUnify.select()
                } else {
                    flight_sort_filter.chipItems[QUICK_FILTER_DIRECT_ORDER].unselect()
                    flight_sort_filter.chipItems[QUICK_FILTER_DIRECT_ORDER].refChipUnify.unselect()
                }
                if (it.facilityList.contains(FlightFilterFacilityEnum.BAGGAGE)) {
                    flight_sort_filter.chipItems[QUICK_FILTER_BAGGAGE_ORDER].select()
                    flight_sort_filter.chipItems[QUICK_FILTER_BAGGAGE_ORDER].refChipUnify.select()
                } else {
                    flight_sort_filter.chipItems[QUICK_FILTER_BAGGAGE_ORDER].unselect()
                    flight_sort_filter.chipItems[QUICK_FILTER_BAGGAGE_ORDER].refChipUnify.unselect()
                }
                if (it.facilityList.contains(FlightFilterFacilityEnum.MEAL)) {
                    flight_sort_filter.chipItems[QUICK_FILTER_MEAL_ORDER].select()
                    flight_sort_filter.chipItems[QUICK_FILTER_MEAL_ORDER].refChipUnify.select()
                } else {
                    flight_sort_filter.chipItems[QUICK_FILTER_MEAL_ORDER].unselect()
                    flight_sort_filter.chipItems[QUICK_FILTER_MEAL_ORDER].refChipUnify.unselect()
                }
                if (it.transitTypeList.contains(TransitEnum.ONE)) {
                    flight_sort_filter.chipItems[QUICK_FILTER_TRANSIT_ORDER].select()
                    flight_sort_filter.chipItems[QUICK_FILTER_TRANSIT_ORDER].refChipUnify.select()
                } else {
                    flight_sort_filter.chipItems[QUICK_FILTER_TRANSIT_ORDER].unselect()
                    flight_sort_filter.chipItems[QUICK_FILTER_TRANSIT_ORDER].refChipUnify.unselect()
                }
            }
        }

        flight_sort_filter.indicatorCounter = flightSearchViewModel.recountFilterCounter()
    }

    private fun showSearchRouteTitle() {
        tv_flight_search_title_route.text = getSearchRouteTitle()
        tv_flight_search_title_route.show()
    }

    private fun getSearchRouteTitle(): String = if (isReturnTrip()) {
        getString(R.string.flight_search_choose_return_flight)
    } else {
        getString(R.string.flight_search_choose_departure_flight)
    }

    private fun navigateToTheNextPage(selectedId: String, selectedTerm: String,
                                      fareModel: FlightPriceModel, isBestPairing: Boolean) {
        onFlightSearchFragmentListener?.let {
            it.selectFlight(selectedId, selectedTerm, fareModel,
                    isBestPairing, flightSearchViewModel.isCombineDone,
                    flightSearchViewModel.flightSearchPassData.searchRequestId)
        }
    }

    private fun resetDateAndReload() {
        flightSearchViewModel.flush()
        onFlightSearchFragmentListener?.changeDate(flightSearchViewModel.flightSearchPassData)

        horizontal_progress_bar.visibility = View.VISIBLE
        flightSearchViewModel.setProgress(0)
        flight_sort_filter.visibility = View.GONE

        clearAllData()
        showLoading()

        flightSearchViewModel.initialize(true, isReturnTrip())
        flightSearchViewModel.fetchSearchDataCloud(isReturnTrip())
    }

    interface OnFlightSearchFragmentListener {
        fun selectFlight(selectedFlightID: String, selectedTerm: String, flightPriceModel: FlightPriceModel,
                         isBestPairing: Boolean, isCombineDone: Boolean, requestId: String)

        fun changeDate(flightSearchPassDataModel: FlightSearchPassDataModel)
    }

    companion object {
        private const val TAG_TRAVEL_CALENDAR = "travel calendar"

        private const val HIDE_HORIZONTAL_PROGRESS_DELAY: Long = 500
        private const val FILTER_SORT_ITEM_SIZE = 4

        private const val QUICK_FILTER_DIRECT_ORDER = 0
        private const val QUICK_FILTER_BAGGAGE_ORDER = 1
        private const val QUICK_FILTER_MEAL_ORDER = 2
        private const val QUICK_FILTER_TRANSIT_ORDER = 3

        private const val FLIGHT_QUICK_FILTER = "Filter"
        private const val FLIGHT_QUICK_FILTER_DIRECT = "Langsung"
        private const val FLIGHT_QUICK_FILTER_BAGGAGE = "Gratis Bagasi"
        private const val FLIGHT_QUICK_FILTER_MEAL = "In-flight Meal"
        private const val FLIGHT_QUICK_FILTER_TRANSIT = "Transit"

        private const val MAX_DATE_ADDITION_YEAR = 1

        fun newInstance(flightSearchPassDataModel: FlightSearchPassDataModel): FlightSearchFragment =
                FlightSearchFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(FlightSearchActivity.EXTRA_PASS_DATA, flightSearchPassDataModel)
                    }
                }
    }
}