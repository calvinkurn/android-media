package com.tokopedia.flight.search.presentation.fragment

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
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.ticker.TravelTickerUtils
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.presentation.model.FlightAirportModel
import com.tokopedia.flight.common.constant_kotlin.FlightErrorConstant
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.view.HorizontalProgressBar
import com.tokopedia.flight.common.view.adapter.FlightAdapterTypeFactory
import com.tokopedia.flight.common.view.model.EmptyResultModel
import com.tokopedia.flight.detail.view.model.FlightDetailModel
import com.tokopedia.flight.detail.view.widget.FlightDetailBottomSheet
import com.tokopedia.flight.filter.presentation.FlightFilterFacilityEnum
import com.tokopedia.flight.filter.presentation.bottomsheets.FlightFilterBottomSheet
import com.tokopedia.flight.promo_chips.data.model.AirlinePrice
import com.tokopedia.flight.promo_chips.presentation.widget.FlightPromoChips
import com.tokopedia.flight.search.data.FlightSearchThrowable
import com.tokopedia.flight.search.di.DaggerFlightSearchComponent
import com.tokopedia.flight.search.di.FlightSearchComponent
import com.tokopedia.flight.search.presentation.activity.FlightSearchActivity
import com.tokopedia.flight.search.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_IS_COMBINE_DONE
import com.tokopedia.flight.search.presentation.adapter.viewholder.EmptyResultViewHolder
import com.tokopedia.flight.search.presentation.adapter.viewholder.FlightSearchAdapterTypeFactory
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.model.filter.TransitEnum
import com.tokopedia.flight.search.presentation.util.FlightSearchCache
import com.tokopedia.flight.search.presentation.util.select
import com.tokopedia.flight.search.presentation.util.unselect
import com.tokopedia.flight.search.presentation.viewmodel.FlightSearchViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_flight_search.*
import kotlinx.android.synthetic.main.include_flight_quick_filter.*
import javax.inject.Inject

/**
 * @author by furqan on 06/04/2020
 */
open class FlightSearchFragment : BaseListFragment<FlightJourneyModel, FlightSearchAdapterTypeFactory>(),
        FlightSearchAdapterTypeFactory.OnFlightSearchListener,
        FlightFilterBottomSheet.FlightFilterBottomSheetListener,
        FlightDetailBottomSheet.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    protected lateinit var flightSearchViewModel: FlightSearchViewModel
    protected var onFlightSearchFragmentListener: OnFlightSearchFragmentListener? = null
    protected lateinit var remoteConfig: RemoteConfig
    private lateinit var flightSearchComponent: FlightSearchComponent

    private lateinit var flightSearchCache: FlightSearchCache
    private lateinit var performanceMonitoringP1: PerformanceMonitoring
    private lateinit var performanceMonitoringP2: PerformanceMonitoring
    private var isTraceStop = false

    private lateinit var promoChipsWidget: FlightPromoChips

    private val filterItems = arrayListOf<SortFilterItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            remoteConfig = FirebaseRemoteConfigImpl(it)
        }
        initViewModels()
        flightSearchCache = FlightSearchCache(requireContext())
        performanceMonitoringP1 = PerformanceMonitoring.start(FLIGHT_SEARCH_P1_TRACE)
        performanceMonitoringP2 = PerformanceMonitoring.start(FLIGHT_SEARCH_P2_TRACE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewRoot = inflater.inflate(getLayout(), container, false)
        viewRoot.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_N0)
        promoChipsWidget = viewRoot.findViewById(R.id.flight_promo_chips_view)
        return viewRoot
    }

    override fun onResume() {
        super.onResume()
        if (::flightSearchCache.isInitialized && flightSearchCache.isBackgroundCacheExpired()) {
            resetDateAndReload(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flightSearchViewModel.journeyList.observe(viewLifecycleOwner, Observer {
            stopTrace()
            when (it) {
                is Success -> {
                    clearAllData()
                    renderSearchList(it.data)
                }
                is Fail -> {
                    if (it.throwable is FlightSearchThrowable) {
                        val errors = (it.throwable as FlightSearchThrowable).errorList
                        for (error in errors) {
                            if (error.id.toInt() == FlightErrorConstant.FLIGHT_ROUTE_NOT_FOUND.value) {
                                showNoRouteFlightEmptyState(error.title)
                                flightSearchViewModel.sendProductNotFoundTrack()
                                break
                            }
                        }
                    }
                }
            }
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

        flightSearchViewModel.tickerData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.message.isNotEmpty()) {
                        renderTickerView(it.data)
                    } else {
                        hideTickerView()
                    }
                }
                is Fail -> {
                    hideTickerView()
                }
            }
        })

        initPromoChips()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSwipeRefresh()
        setupQuickFilter()
        showLoading()
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        onFlightSearchFragmentListener = context as OnFlightSearchFragmentListener
    }

    override fun getAdapterTypeFactory(): FlightSearchAdapterTypeFactory =
            FlightSearchAdapterTypeFactory(this)

    override fun createAdapterInstance(): BaseListAdapter<FlightJourneyModel, FlightSearchAdapterTypeFactory> {
        val adapter: BaseListAdapter<FlightJourneyModel, FlightSearchAdapterTypeFactory> = super.createAdapterInstance()

        val errorNetworkModel: ErrorNetworkModel = adapter.errorNetworkModel
        errorNetworkModel.iconDrawableRes = R.drawable.ic_flight_empty_state
        errorNetworkModel.onRetryListener = this

        adapter.errorNetworkModel = errorNetworkModel

        return adapter
    }

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
        showPromoChips()

        // remove all unneeded element (empty/retry/loading/etc)
        if (isLoadingInitialData) {
            clearAllData()
        } else {
            adapter.clearAllNonDataElement()
        }

        adapter.addElement(list)
        updateScrollListenerState(false)

        if (isListEmpty && flightSearchViewModel.isDoneLoadData()) {
            hideLoading()
            adapter.addElement(emptyDataViewModel)
            if (!flightSearchViewModel.isInFilterMode)
                flightSearchViewModel.sendProductNotFoundTrack()
        } else {
            isLoadingInitialData = false
            if (isListEmpty) showLoading()
        }

        if (flightSearchViewModel.isDoneLoadData()) {
            performanceMonitoringP2.stopTrace()
            if (!flightSearchCache.isSearchCoachMarkShowed()) {
                (activity as FlightSearchActivity).setupAndShowCoachMark()
            } else if ((activity as FlightSearchActivity).isSearchFromWidget) {
                (activity as FlightSearchActivity).setupAndShowCoachMarkSearchFromWidget()
            }
        }
    }

    override fun isLoadMoreEnabledByDefault(): Boolean = false

    override fun isListEmpty(): Boolean = !adapter.isContainData

    override fun onDestroyView() {
        stopTrace()
        performanceMonitoringP2.stopTrace()
        super.onDestroyView()
        this.clearFindViewByIdCache()
    }

    override fun onItemClicked(journeyModel: FlightJourneyModel?) {
        flightSearchViewModel.onSearchItemClicked(journeyModel = journeyModel)
    }

    override fun loadData(page: Int) {}

    override fun onRetryClicked() {
        adapter.clearAllElements()
        resetDateAndReload()
    }

    override fun onDetailClicked(journeyModel: FlightJourneyModel?, adapterPosition: Int) {
        journeyModel?.let {
            flightSearchViewModel.sendDetailClickTrack(it, adapterPosition)
            val flightDetailModel = FlightDetailModel()
            flightDetailModel.build(it)
            flightDetailModel.build(flightSearchViewModel.flightSearchPassData)

            if (it.fare.adultNumericCombo != 0) {
                flightDetailModel.total = journeyModel.comboPrice
                flightDetailModel.totalNumeric = journeyModel.comboPriceNumeric
                flightDetailModel.adultNumericPrice = journeyModel.fare.adultNumericCombo
                flightDetailModel.childNumericPrice = journeyModel.fare.childNumericCombo
                flightDetailModel.infantNumericPrice = journeyModel.fare.infantNumericCombo
            }

            val flightDetailBottomSheet = FlightDetailBottomSheet.getInstance()
            flightDetailBottomSheet.setDetailModel(flightDetailModel)
            flightDetailBottomSheet.setShowSubmitButton(true)
            flightDetailBottomSheet.listener = this
            flightDetailBottomSheet.setShowListener { flightDetailBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
            flightDetailBottomSheet.show(requireFragmentManager(), FlightDetailBottomSheet.TAG_FLIGHT_DETAIL_BOTTOM_SHEET)
        }
    }

    override fun onItemClicked(journeyModel: FlightJourneyModel?, adapterPosition: Int) {
        flightSearchViewModel.onSearchItemClicked(journeyModel, adapterPosition)
    }

    override fun onShowAllClicked() {
        // need for return page
    }

    override fun onShowBestPairingClicked() {
        // need for return page
    }

    override fun onSaveFilter(sortOption: Int, flightFilterModel: FlightFilterModel?, statisticPricePair: Pair<Int, Int>) {
        flightSearchViewModel.selectedSortOption = sortOption
        flightSearchViewModel.priceFilterStatistic = statisticPricePair
        flightFilterModel?.let {
            flightSearchViewModel.filterModel = it
        }
        if (flightFilterModel?.isHasFilter == false) {
            promoChipsWidget.resetState()
        }
        clearAllData()
        flight_sort_filter.indicatorCounter = flightSearchViewModel.recountFilterCounter()
        fetchSortAndFilterData()
    }

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    open fun getLayout(): Int = R.layout.fragment_flight_search

    open fun isReturnTrip(): Boolean = false

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyResultViewModel = EmptyResultModel()
        emptyResultViewModel.iconRes = com.tokopedia.globalerror.R.drawable.unify_globalerrors_404
        if (flightSearchViewModel.isInFilterMode) {
            emptyResultViewModel.title = getString(R.string.flight_there_is_zero_flight_for_the_filter_title)
            emptyResultViewModel.contentRes = R.string.flight_there_is_zero_flight_for_the_filter_description
            emptyResultViewModel.buttonTitleRes = R.string.reset_filter
            emptyResultViewModel.callback = object : EmptyResultViewHolder.Callback {
                override fun onEmptyButtonClicked() {
                    onResetFilterClicked()
                }
            }
        } else if (flightSearchViewModel.filterModel.departureArrivalTime.isNotEmpty()) {
            emptyResultViewModel.title = getString(R.string.flight_there_is_no_flight_available_for_return_title)
            emptyResultViewModel.contentRes = R.string.flight_there_is_no_flight_available_for_return_description
            emptyResultViewModel.buttonTitleRes = R.string.flight_search_there_is_no_flight_available_for_return_button_label
            emptyResultViewModel.callback = object : EmptyResultViewHolder.Callback {
                override fun onEmptyButtonClicked() {
                    activity?.finish()
                }
            }
        } else {
            emptyResultViewModel.title = getString(R.string.flight_there_is_no_flight_available_title)
            emptyResultViewModel.contentRes = R.string.flight_there_is_no_flight_available_description
            emptyResultViewModel.buttonTitleRes = R.string.flight_search_change_search_empty_button_label
            emptyResultViewModel.callback = object : EmptyResultViewHolder.Callback {
                override fun onEmptyButtonClicked() {
                    (activity as FlightSearchActivity).showChangeSearchBottomSheet()
                }
            }
        }

        return emptyResultViewModel
    }

    override fun onSelectedFromDetail(detailBottomSheet: FlightDetailBottomSheet, selectedId: String) {
        flightSearchViewModel.onSearchItemClicked(selectedId = selectedId)
        if (detailBottomSheet.isAdded && detailBottomSheet.isVisible) detailBottomSheet.dismiss()
    }

    fun setSearchPassData(flightSearchPassDataModel: FlightSearchPassDataModel) {
        flightSearchViewModel.flightSearchPassData = flightSearchPassDataModel
    }

    fun resetDateAndReload(shouldResetCombine: Boolean = false) {
        flightSearchViewModel.flush()
        onFlightSearchFragmentListener?.changeDate(flightSearchViewModel.flightSearchPassData)

        getSearchHorizontalProgress().visibility = View.VISIBLE
        flightSearchViewModel.setProgress(0)
        flight_sort_filter.visibility = View.GONE

        clearAllData()
        showLoading()

        if (shouldResetCombine) {
            flightSearchViewModel.isCombineDone = false
        }

        flightSearchViewModel.flightAirportCombine = flightSearchViewModel.buildAirportCombineModel(
                getDepartureAirport(), getArrivalAirport())
        flightSearchViewModel.initialize(true, isReturnTrip())
        flightSearchViewModel.fetchSearchDataCloud(isReturnTrip())
    }

    open fun initViewModels() {
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightSearchViewModel = viewModelProvider.get(FlightSearchViewModel::class.java)

            arguments?.let { args ->
                args.getParcelable<FlightSearchPassDataModel>(FlightSearchActivity.EXTRA_PASS_DATA)?.let {
                    flightSearchViewModel.flightSearchPassData = it
                }
                flightSearchViewModel.isCombineDone = args.getBoolean(EXTRA_IS_COMBINE_DONE, false)
            }

            flightSearchViewModel.filterModel = buildFilterModel(FlightFilterModel())
            flightSearchViewModel.flightAirportCombine = flightSearchViewModel.buildAirportCombineModel(
                    getDepartureAirport(), getArrivalAirport())
            flightSearchViewModel.generateSearchStatistics()
            flightSearchViewModel.initialize(true, isReturnTrip())
            flightSearchViewModel.fetchSearchDataCloud(isReturnTrip())
            flightSearchViewModel.fetchPromoList(isReturnTrip())
        }
    }

    open fun buildFilterModel(filterModel: FlightFilterModel): FlightFilterModel =
            filterModel.also {
                it.canFilterFreeRapidTest = remoteConfig.getBoolean(RemoteConfigKey.ANDROID_CUSTOMER_FLIGHT_SHOW_FREE_RAPID_TEST, false)
                it.canFilterSeatDistancing = remoteConfig.getBoolean(RemoteConfigKey.ANDROID_CUSTOMER_FLIGHT_SHOW_SEAT_DISTANCING, false)
                it.departureArrivalTime = ""
            }

    open fun getDepartureAirport(): FlightAirportModel = flightSearchViewModel.flightSearchPassData.departureAirport

    open fun getArrivalAirport(): FlightAirportModel = flightSearchViewModel.flightSearchPassData.arrivalAirport

    open fun getFlightSearchTicker(): Ticker = flight_search_ticker

    open fun getSearchHorizontalProgress(): HorizontalProgressBar = horizontal_progress_bar

    open fun renderSearchList(list: List<FlightJourneyModel>) {

        renderList(list)

        if (list.isNotEmpty()) {
            setupQuickFilter()
            showQuickFilter()
        } else if (!adapter.isContainData) {
            hideQuickFilter()
        }
    }

    protected fun fetchSortAndFilterData() {
        if (adapter.itemCount == 0) {
            showLoading()
        }

        flightSearchViewModel.fetchSortAndFilter()
    }

    private fun renderTickerView(travelTickerModel: TravelTickerModel) {
        TravelTickerUtils.buildUnifyTravelTicker(travelTickerModel, getFlightSearchTicker())
        if (travelTickerModel.url.isNotEmpty()) {
            getFlightSearchTicker().setOnClickListener {
                RouteManager.route(requireContext(), travelTickerModel.url)
            }
        }

        showTickerView()
    }

    private fun showTickerView() {
        flight_search_ticker.visibility = View.VISIBLE
    }

    private fun hideTickerView() {
        flight_search_ticker.visibility = View.GONE
    }

    private fun setUpProgress(progress: Int) {
        if (getSearchHorizontalProgress().visibility == View.VISIBLE) {
            getSearchHorizontalProgress().setProgress(progress)
            if (flightSearchViewModel.isDoneLoadData()) {
                Handler().postDelayed({
                    try {
                        getSearchHorizontalProgress().visibility = View.GONE
                    } catch (t: Throwable) {
                        t.printStackTrace()
                    }
                }, HIDE_HORIZONTAL_PROGRESS_DELAY)
            }
        }
    }

    private fun onResetFilterClicked() {
        flightSearchViewModel.filterModel = buildFilterModel(FlightFilterModel())
        clearAllData()
        showLoading()
        setupQuickFilter()
        fetchSortAndFilterData()
        promoChipsWidget.resetState()
    }

    private fun setupSwipeRefresh() {
        val swipeRefreshLayout = requireView().findViewById<SwipeToRefresh>(swipeRefreshLayoutResourceId)
        swipeRefreshLayout.setSwipeDistance()
        swipeRefreshLayout.setOnRefreshListener {
            hideLoading()
            swipeRefreshLayout.isRefreshing = false
            adapter.clearAllElements()
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
        if (filterItems.size < flightSearchViewModel.getQuickFilterItemSize()) {

            if (flightSearchViewModel.isFilterModelInitialized() &&
                    flightSearchViewModel.filterModel.canFilterSeatDistancing) {
                val quickSeatDistancingFilter = SortFilterItem(getString(R.string.flight_search_has_seat_distancing_label))
                quickSeatDistancingFilter.listener = {
                    if (flightSearchViewModel.isFilterModelInitialized() &&
                            flightSearchViewModel.filterModel.canFilterSeatDistancing &&
                            flightSearchViewModel.filterModel.isSeatDistancing) {
                        flightSearchViewModel.filterModel.isSeatDistancing = false
                        quickSeatDistancingFilter.unselect()
                    } else if (flightSearchViewModel.isFilterModelInitialized() &&
                            flightSearchViewModel.filterModel.canFilterSeatDistancing) {
                        flightSearchViewModel.filterModel.isSeatDistancing = true
                        quickSeatDistancingFilter.select()
                    }
                    flightSearchViewModel.sendQuickFilterTrack(FLIGHT_QUICK_SEAT_DISTANCING)
                    flightSearchViewModel.changeHasFilterValue()
                    clearAllData()
                    fetchSortAndFilterData()
                }
                filterItems.add(quickSeatDistancingFilter)
            }

            if (flightSearchViewModel.isFilterModelInitialized() &&
                    flightSearchViewModel.filterModel.canFilterFreeRapidTest) {
                val quickFreeRapidTestFilter = SortFilterItem(getString(R.string.flight_search_free_rapid_test_label))
                quickFreeRapidTestFilter.listener = {
                    if (flightSearchViewModel.isFilterModelInitialized() &&
                            flightSearchViewModel.filterModel.canFilterFreeRapidTest &&
                            flightSearchViewModel.filterModel.isFreeRapidTest) {
                        flightSearchViewModel.filterModel.isFreeRapidTest = false
                        quickFreeRapidTestFilter.unselect()
                    } else if (flightSearchViewModel.isFilterModelInitialized() &&
                            flightSearchViewModel.filterModel.canFilterFreeRapidTest) {
                        flightSearchViewModel.filterModel.isFreeRapidTest = true
                        quickFreeRapidTestFilter.select()
                    }
                    flightSearchViewModel.sendQuickFilterTrack(FLIGHT_QUICK_FREE_RAPID_TEST)
                    flightSearchViewModel.changeHasFilterValue()
                    clearAllData()
                    fetchSortAndFilterData()
                }
                filterItems.add(quickFreeRapidTestFilter)
            }

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
                flightSearchViewModel.changeHasFilterValue()
                clearAllData()
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
                flightSearchViewModel.changeHasFilterValue()
                clearAllData()
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
                flightSearchViewModel.changeHasFilterValue()
                clearAllData()
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
                flightSearchViewModel.changeHasFilterValue()
                clearAllData()
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
                val quickFilterAdditionalOrder =
                        if (it.canFilterFreeRapidTest && it.canFilterSeatDistancing) {
                            if (it.isSeatDistancing) {
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_FIRST_ADDITIONAL_ORDER)?.select()
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_FIRST_ADDITIONAL_ORDER)?.refChipUnify?.select()
                            } else {
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_FIRST_ADDITIONAL_ORDER)?.unselect()
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_FIRST_ADDITIONAL_ORDER)?.refChipUnify?.unselect()
                            }

                            if (it.isFreeRapidTest) {
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_SECOND_ADDITIONAL_ORDER)?.select()
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_SECOND_ADDITIONAL_ORDER)?.refChipUnify?.select()
                            } else {
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_SECOND_ADDITIONAL_ORDER)?.unselect()
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_SECOND_ADDITIONAL_ORDER)?.refChipUnify?.unselect()
                            }

                            QUICK_FILTER_ADDITIONAL_TWO_ORDER
                        } else if (it.canFilterFreeRapidTest || it.canFilterSeatDistancing) {
                            if (it.canFilterSeatDistancing && it.isSeatDistancing) {
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_FIRST_ADDITIONAL_ORDER)?.select()
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_FIRST_ADDITIONAL_ORDER)?.refChipUnify?.select()
                            } else {
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_FIRST_ADDITIONAL_ORDER)?.unselect()
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_FIRST_ADDITIONAL_ORDER)?.refChipUnify?.unselect()
                            }

                            if (it.canFilterFreeRapidTest && it.isFreeRapidTest) {
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_SECOND_ADDITIONAL_ORDER)?.select()
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_SECOND_ADDITIONAL_ORDER)?.refChipUnify?.select()
                            } else {
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_SECOND_ADDITIONAL_ORDER)?.unselect()
                                flight_sort_filter.chipItems?.get(QUICK_FILTER_SECOND_ADDITIONAL_ORDER)?.refChipUnify?.unselect()
                            }

                            QUICK_FILTER_ADDITIONAL_ONE_ORDER
                        } else {
                            QUICK_FILTER_NO_ADDITIONAL
                        }

                if (it.transitTypeList.contains(TransitEnum.DIRECT)) {
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_DIRECT_ORDER + quickFilterAdditionalOrder)?.select()
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_DIRECT_ORDER + quickFilterAdditionalOrder)?.refChipUnify?.select()
                } else {
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_DIRECT_ORDER + quickFilterAdditionalOrder)?.unselect()
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_DIRECT_ORDER + quickFilterAdditionalOrder)?.refChipUnify?.unselect()
                }
                if (it.facilityList.contains(FlightFilterFacilityEnum.BAGGAGE)) {
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_BAGGAGE_ORDER + quickFilterAdditionalOrder)?.select()
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_BAGGAGE_ORDER + quickFilterAdditionalOrder)?.refChipUnify?.select()
                } else {
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_BAGGAGE_ORDER + quickFilterAdditionalOrder)?.unselect()
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_BAGGAGE_ORDER + quickFilterAdditionalOrder)?.refChipUnify?.unselect()
                }
                if (it.facilityList.contains(FlightFilterFacilityEnum.MEAL)) {
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_MEAL_ORDER + quickFilterAdditionalOrder)?.select()
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_MEAL_ORDER + quickFilterAdditionalOrder)?.refChipUnify?.select()
                } else {
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_MEAL_ORDER + quickFilterAdditionalOrder)?.unselect()
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_MEAL_ORDER + quickFilterAdditionalOrder)?.refChipUnify?.unselect()
                }
                if (it.transitTypeList.contains(TransitEnum.ONE)) {
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_TRANSIT_ORDER + quickFilterAdditionalOrder)?.select()
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_TRANSIT_ORDER + quickFilterAdditionalOrder)?.refChipUnify?.select()
                } else {
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_TRANSIT_ORDER + quickFilterAdditionalOrder)?.unselect()
                    flight_sort_filter.chipItems?.get(QUICK_FILTER_TRANSIT_ORDER + quickFilterAdditionalOrder)?.refChipUnify?.unselect()
                }
            }
        }

        Handler().postDelayed({ flight_sort_filter.indicatorCounter = flightSearchViewModel.recountFilterCounter() },
                QUICK_FILTER_INDICATOR_DELAY)
    }

    private fun navigateToTheNextPage(selectedId: String, selectedTerm: String,
                                      fareModel: FlightPriceModel, isBestPairing: Boolean) {
        onFlightSearchFragmentListener?.let {
            it.selectFlight(selectedId, selectedTerm, fareModel,
                    isBestPairing, flightSearchViewModel.isCombineDone,
                    flightSearchViewModel.flightSearchPassData.searchRequestId)
        }
    }

    private fun stopTrace() {
        if (!isTraceStop) {
            performanceMonitoringP1.stopTrace()
            isTraceStop = true
        }
    }

    private fun showNoRouteFlightEmptyState(message: String) {
        adapter.clearAllElements()
        adapter.addElement(getNoFlightRouteDataModel(message))
    }

    private fun getNoFlightRouteDataModel(message: String): Visitable<FlightAdapterTypeFactory> {
        val emptyResultViewModel = EmptyResultModel()
        emptyResultViewModel.iconRes = com.tokopedia.globalerror.R.drawable.unify_globalerrors_404
        emptyResultViewModel.title = getString(R.string.flight_there_is_no_flight_available_title)
        emptyResultViewModel.contentRes = R.string.flight_there_is_no_flight_available_description
        emptyResultViewModel.buttonTitleRes = R.string.flight_search_change_search_empty_button_label
        emptyResultViewModel.callback = object : EmptyResultViewHolder.Callback {
            override fun onEmptyButtonClicked() {
                (activity as FlightSearchActivity).showChangeSearchBottomSheet()
            }
        }
        return emptyResultViewModel
    }

    fun hidePromoChips() {
        promoChipsWidget.hide()
    }

    fun showPromoChips() {
        promoChipsWidget.show()
    }

    private fun initPromoChips() {
        flightSearchViewModel.promoData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (!it.data.dataPromoChips.isNullOrEmpty()) {
                        showPromoChips()
                        promoChipsWidget.renderPromoList(it.data.dataPromoChips[FLIGHT_PROMO_CHIPS_START_DATE].airlinePrices)
                    } else {
                        hidePromoChips()
                    }
                }
                is Fail -> {
                    hidePromoChips()
                }
            }
        })
        promoChipsWidget.setListener(promoChipsListener)
    }

    private val promoChipsListener = object : FlightPromoChips.PromoChipsListener {
        override fun onClickPromoChips(airlinePrice: AirlinePrice, position: Int) {
            flightSearchViewModel.onPromotionChipsClicked(position, airlinePrice, isReturnTrip())
            flightSearchViewModel.filterModel.airlineList = mutableListOf(airlinePrice.airlineID)
            clearAllData()
            fetchSortAndFilterData()
        }

        override fun onUnselectChips() {
            flightSearchViewModel.filterModel.airlineList = mutableListOf()
            clearAllData()
            fetchSortAndFilterData()
        }
    }

    interface OnFlightSearchFragmentListener {
        fun selectFlight(selectedFlightID: String, selectedTerm: String, flightPriceModel: FlightPriceModel,
                         isBestPairing: Boolean, isCombineDone: Boolean, requestId: String)

        fun changeDate(flightSearchPassDataModel: FlightSearchPassDataModel)
    }

    companion object {
        private const val FLIGHT_SEARCH_P1_TRACE = "tr_flight_search_p1"
        private const val FLIGHT_SEARCH_P2_TRACE = "tr_flight_search_p2"

        private const val HIDE_HORIZONTAL_PROGRESS_DELAY: Long = 500
        private const val QUICK_FILTER_INDICATOR_DELAY: Long = 50

        private const val QUICK_FILTER_DIRECT_ORDER = 0
        private const val QUICK_FILTER_BAGGAGE_ORDER = 1
        private const val QUICK_FILTER_MEAL_ORDER = 2
        private const val QUICK_FILTER_TRANSIT_ORDER = 3

        private const val QUICK_FILTER_FIRST_ADDITIONAL_ORDER = 0
        private const val QUICK_FILTER_SECOND_ADDITIONAL_ORDER = 1

        private const val QUICK_FILTER_ADDITIONAL_TWO_ORDER = 2
        private const val QUICK_FILTER_ADDITIONAL_ONE_ORDER = 1
        private const val QUICK_FILTER_NO_ADDITIONAL = 0

        private const val FLIGHT_PROMO_CHIPS_START_DATE = 0

        private const val FLIGHT_QUICK_FILTER = "Filter"
        private const val FLIGHT_QUICK_FILTER_DIRECT = "Langsung"
        private const val FLIGHT_QUICK_FILTER_BAGGAGE = "Gratis Bagasi"
        private const val FLIGHT_QUICK_FILTER_MEAL = "In-flight Meal"
        private const val FLIGHT_QUICK_FILTER_TRANSIT = "Transit"
        private const val FLIGHT_QUICK_FREE_RAPID_TEST = "Free Rapid Test"
        private const val FLIGHT_QUICK_SEAT_DISTANCING = "Seat Distancing"

        fun newInstance(flightSearchPassDataModel: FlightSearchPassDataModel): FlightSearchFragment =
                FlightSearchFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(FlightSearchActivity.EXTRA_PASS_DATA, flightSearchPassDataModel)
                    }
                }
    }
}