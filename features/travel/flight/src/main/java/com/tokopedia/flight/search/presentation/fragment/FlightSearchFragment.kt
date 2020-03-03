package com.tokopedia.flight.search.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.common.travel.ticker.TravelTickerUtils
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.dashboard.view.widget.FlightCalendarOneWayWidget
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel
import com.tokopedia.flight.filter.presentation.bottomsheets.FlightFilterBottomSheet
import com.tokopedia.flight.search.di.DaggerFlightSearchComponent
import com.tokopedia.flight.search.di.FlightSearchComponent
import com.tokopedia.flight.search.presentation.activity.FlightSearchActivity
import com.tokopedia.flight.search.presentation.activity.FlightSearchFilterActivity
import com.tokopedia.flight.search.presentation.adapter.FlightSearchAdapterTypeFactory
import com.tokopedia.flight.search.presentation.adapter.viewholder.EmptyResultViewHolder
import com.tokopedia.flight.search.presentation.adapter.viewholder.EmptyResultViewHolder.Callback
import com.tokopedia.flight.search.presentation.contract.FlightSearchContract
import com.tokopedia.flight.search.presentation.model.*
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.presenter.FlightSearchPresenter
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_search_flight.*
import kotlinx.android.synthetic.main.include_filter_bottom_action_view.*
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 07/01/19
 */
open class FlightSearchFragment : BaseListFragment<FlightJourneyViewModel, FlightSearchAdapterTypeFactory>(),
        FlightSearchContract.View, FlightSearchAdapterTypeFactory.OnFlightSearchListener,
        ErrorNetworkModel.OnRetryListener, FlightFilterBottomSheet.FlightFilterBottomSheetListener {

    lateinit var flightSearchPresenter: FlightSearchPresenter
        @Inject set

    protected var flightSearchComponent: FlightSearchComponent? = null
    protected lateinit var flightSearchPassData: FlightSearchPassDataViewModel
    protected var onFlightSearchFragmentListener: OnFlightSearchFragmentListener? = null
    private lateinit var flightAirportCombineModelList: FlightAirportCombineModelList

    private var inFilterMode: Boolean = false
    private var progress = 0
    var selectedSortOption: Int = TravelSortOption.NO_PREFERENCE
    private var isTraceStop = false

    protected var isCombineDone: Boolean = false

    private lateinit var flightFilterModel: FlightFilterModel

    private lateinit var performanceMonitoringP1: PerformanceMonitoring
    private lateinit var performanceMonitoringP2: PerformanceMonitoring

    private val filterItems = arrayListOf<SortFilterItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flightSearchPassData = arguments!!.getParcelable(FlightSearchActivity.EXTRA_PASS_DATA)

        if (savedInstanceState == null) {
            flightFilterModel = buildFilterModel(FlightFilterModel())
            selectedSortOption = TravelSortOption.CHEAPEST
            setUpCombinationAirport()
            progress = 0
        } else {
            flightFilterModel = savedInstanceState.getParcelable(SAVED_FILTER_MODEL)
            selectedSortOption = savedInstanceState.getInt(SAVED_SORT_OPTION)
            flightAirportCombineModelList = savedInstanceState.getParcelable(SAVED_AIRPORT_COMBINE)
            progress = savedInstanceState.getInt(SAVED_PROGRESS, 0)
            isCombineDone = savedInstanceState.getBoolean(SAVED_IS_COMBINE_DONE, false)
        }

        performanceMonitoringP1 = PerformanceMonitoring.start(FLIGHT_SEARCH_P1_TRACE)
        performanceMonitoringP2 = PerformanceMonitoring.start(FLIGHT_SEARCH_P2_TRACE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(getLayout(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flightSearchPresenter.attachView(this)

        showLoading()
        setUpProgress()
        setUpBottomAction()
        setUpSwipeRefresh()

        flightSearchPresenter.initialize(true)

        searchFlightData()
        flightSearchPresenter.fetchTickerData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(SAVED_FILTER_MODEL, flightFilterModel)
        outState.putInt(SAVED_SORT_OPTION, selectedSortOption)
        outState.putParcelable(SAVED_AIRPORT_COMBINE, flightAirportCombineModelList)
        outState.putInt(SAVED_PROGRESS, progress)
        outState.putBoolean(SAVED_IS_COMBINE_DONE, isCombineDone)
    }

    override fun onResume() {
        super.onResume()
        flightSearchPresenter.fetchSortAndFilter(selectedSortOption, flightFilterModel, true)
    }

    override fun onPause() {
        super.onPause()
        flightSearchPresenter.unsubscribeAll()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_SEARCH_FILTER -> {
                    if (data != null && data.hasExtra(FlightSearchFilterActivity.EXTRA_FILTER_MODEL)) {
                        flightFilterModel = data.extras.get(FlightSearchFilterActivity.EXTRA_FILTER_MODEL) as FlightFilterModel
                        flightFilterModel = buildFilterModel(flightFilterModel)

                        flightSearchPresenter.fetchSortAndFilter(selectedSortOption, flightFilterModel, false)
                        setUIMarkFilter()
                    }
                }
                REQUEST_CODE_SEE_DETAIL_FLIGHT -> {
                    if (data != null && data.hasExtra(FlightDetailActivity.EXTRA_FLIGHT_SELECTED)) {
                        val selectedId: String = data.getStringExtra(FlightDetailActivity.EXTRA_FLIGHT_SELECTED)
                        val selectedTerm: String = data.getStringExtra(FlightDetailActivity.EXTRA_FLIGHT_SELECTED_TERM)
                        if (!selectedId.isEmpty()) {
                            onSelectedFromDetail(selectedId, selectedTerm)
                        }
                    }
                }
            }
        }
    }

    override fun initInjector() {
        if (flightSearchComponent == null) {
            flightSearchComponent = DaggerFlightSearchComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(activity!!.application))
                    .build()
        }
        flightSearchComponent?.inject(this)
    }

    override fun getRecyclerView(view: View?): RecyclerView = recycler_view

    override fun getAdapterTypeFactory(): FlightSearchAdapterTypeFactory =
            FlightSearchAdapterTypeFactory(this)

    override fun createAdapterInstance(): BaseListAdapter<FlightJourneyViewModel, FlightSearchAdapterTypeFactory> {
        val adapter: BaseListAdapter<FlightJourneyViewModel, FlightSearchAdapterTypeFactory> = super.createAdapterInstance()

        val errorNetworkModel: ErrorNetworkModel = adapter.errorNetworkModel
        errorNetworkModel.iconDrawableRes = R.drawable.ic_flight_empty_state
        errorNetworkModel.onRetryListener = this

        adapter.errorNetworkModel = errorNetworkModel

        return adapter
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        onFlightSearchFragmentListener = context as OnFlightSearchFragmentListener
    }

    override fun loadInitialData() {

    }

    override fun loadData(page: Int) {

    }

    override fun renderList(list: MutableList<FlightJourneyViewModel>) {
        hideLoading()

        // remove all unneeded element (empty/retry/loading/etc)
        if (isLoadingInitialData) {
            clearAllData()
        } else {
            adapter.clearAllNonDataElement()
        }

        adapter.addElement(list)
        // update the load more state (paging/can loadmore)
        updateScrollListenerState(false)

        if (isListEmpty && flightSearchPresenter.isDoneLoadData()) {
            adapter.addElement(emptyDataViewModel)
        } else {
            isLoadingInitialData = false
        }

        if (flightSearchPresenter.isDoneLoadData()) {
            performanceMonitoringP2.stopTrace()
        }

        setUpProgress()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean = false

    override fun isListEmpty(): Boolean = !adapter.isContainData

    open fun getLayout(): Int = R.layout.fragment_search_flight

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.swipe_refresh_layout
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun onDestroyView() {
        flightSearchPresenter.unsubscribeAll()
        stopTrace()
        performanceMonitoringP2.stopTrace()
        super.onDestroyView()
        this.clearFindViewByIdCache()
    }

    override fun getSearchPassData(): FlightSearchPassDataViewModel = flightSearchPassData

    override fun isReturning(): Boolean = false

    override fun isDoneLoadData(): Boolean = progress >= MAX_PROGRESS

    override fun getFilterModel(): FlightFilterModel = flightFilterModel

    override fun getAirportCombineModelList(): FlightAirportCombineModelList = flightAirportCombineModelList

    override fun isStatusCombineDone(): Boolean = isCombineDone

    override fun fetchFlightSearchData() {
        setUpProgress()
        if (adapter.itemCount == 0) {
            showLoading()
        }

        flightSearchPresenter.fetchSearchData(flightSearchPassData, flightAirportCombineModelList)
    }

    override fun fetchSortAndFilterData(fromCombo: Boolean) {
        setUpProgress()
        if (adapter.itemCount == 0) {
            showLoading()
        }

        flightSearchPresenter.fetchSortAndFilter(selectedSortOption, flightFilterModel, true, fromCombo)
    }

    override fun renderSearchList(list: List<FlightJourneyViewModel>, needRefresh: Boolean) {
        if (!flightSearchPassData.isOneWay && !adapter.isLoading
                && !adapter.isContainData) {
            adapter.addElement(FlightSearchTitleRouteViewModel(getSearchRouteTitle()))
        }

        if (!needRefresh || list.isNotEmpty()) {
            renderList(list.toMutableList())
        }

        if (list.isNotEmpty()) {
            showFilterAndSortView()
        }
    }

    override fun renderTickerView(travelTickerViewModel: TravelTickerViewModel) {
        TravelTickerUtils.buildTravelTicker(context, travelTickerViewModel, flight_ticker_view)
    }

    override fun addToolbarElevation() {
        (activity as AppCompatActivity).supportActionBar!!.elevation =
                resources.getDimension(com.tokopedia.design.R.dimen.dp_4)
    }

    override fun addProgress(numberToAdd: Int) {
        progress += numberToAdd
    }

    override fun addBottomPaddingForSortAndFilterActionButton() {
        val scale: Float = resources.displayMetrics.density
        getRecyclerView(view).setPadding(
                EMPTY_MARGIN,
                EMPTY_MARGIN,
                EMPTY_MARGIN,
                (scale * PADDING_SEARCH_LIST + DEFAULT_DIMENS_MULTIPLIER).toInt()
        )
    }

    override fun setCombineStatus(isCombineDone: Boolean) {
        this.isCombineDone = isCombineDone
        setUpProgress()
    }

    override fun setUIMarkFilter() {
        inFilterMode = if (flightFilterModel.hasFilter()) {
            bottom_action_filter_sort.setMarkLeft(true)
            true
        } else {
            bottom_action_filter_sort.setMarkLeft(false)
            false
        }
    }

    override fun setSearchPassData(passDataViewModel: FlightSearchPassDataViewModel) {
        flightSearchPassData = passDataViewModel
    }

    override fun setSelectedSortItem(sortItemId: Int) {
        selectedSortOption = sortItemId
        setUIMarkSort()
    }

    override fun showDepartureDateMaxTwoYears(resId: Int) {
        showMessageErrorInSnackbar(resId)
    }

    override fun showDepartureDateShouldAtLeastToday(resId: Int) {
        showMessageErrorInSnackbar(resId)
    }

    override fun showReturnDateShouldGreatedOrEqual(resId: Int) {
        showMessageErrorInSnackbar(resId)
    }

    override fun showFilterAndSortView() {
        bottom_action_filter_sort.visibility = View.VISIBLE
        buildQuickFilterView()
    }

    override fun showEmptyFlightStateView() {
        adapter.addElement(emptyDataViewModel)
    }

    override fun showNoRouteFlightEmptyState(message: String) {
        adapter.clearAllElements()
        adapter.addElement(getNoFlightRouteDataViewModel(message))
    }

    override fun showGetSearchListError(e: Throwable) {
        showGetListError(e)
    }

    override fun showGetListError(throwable: Throwable?) {
        super.showGetListError(throwable)
        horizontal_progress_bar.visibility = View.GONE
        removeBottomPaddingForSortAndFilterActionButton()
        hideLoading()
        // Note: add element should be the last in line
        if (adapter.isContainData) {
            onGetListErrorWithEmptyData(throwable)
        }
    }

    override fun hideHorizontalProgress() {
        horizontal_progress_bar.visibility = View.INVISIBLE
    }

    override fun hideFilterAndSortView() {
        bottom_action_filter_sort.visibility = View.GONE
    }

    override fun removeToolbarElevation() {
        (activity as AppCompatActivity).supportActionBar!!.elevation = 0.0F
    }

    override fun removeBottomPaddingForSortAndFilterActionButton() {
        getRecyclerView(view).setPadding(EMPTY_MARGIN, EMPTY_MARGIN, EMPTY_MARGIN, EMPTY_MARGIN)
    }

    override fun clearAdapterData() {
        adapter.clearAllElements()
    }

    override fun finishFragment() {
        activity!!.finish()
    }

    override fun navigateToTheNextPage(selectedId: String, selectedTerm: String, fareViewModel: FlightPriceViewModel, isBestPairing: Boolean) {
        if (onFlightSearchFragmentListener != null) {
            onFlightSearchFragmentListener!!.selectFlight(selectedId, selectedTerm, fareViewModel, isBestPairing, isCombineDone)
        }
    }

    override fun onGetSearchMeta(flightSearchMetaViewModel: FlightSearchMetaViewModel) {
        addToolbarElevation()

        val departureAirport = flightSearchMetaViewModel.departureAirport
        val arrivalAirport = flightSearchMetaViewModel.arrivalAirport
        val flightAirportCombineModel = flightAirportCombineModelList.getData(departureAirport, arrivalAirport)
        val localAirlines = flightAirportCombineModel.airlines
        localAirlines.addAll(flightSearchMetaViewModel.airlines)
        flightAirportCombineModel.airlines = localAirlines
        val size: Int = flightAirportCombineModelList.data.size
        val halfProgressAmount: Int = divideTo(divideTo(MAX_PROGRESS, size), 2)
        if (!flightAirportCombineModel.isHasLoad) {
            flightAirportCombineModel.isHasLoad = true
            progress += halfProgressAmount
        }

        if (flightAirportCombineModel.isNeedRefresh) {
            if (flightSearchMetaViewModel.isNeedRefresh) {
                var noRetry: Int = flightAirportCombineModel.noOfRetry
                noRetry++
                flightAirportCombineModel.noOfRetry = noRetry
                progress += divideTo(halfProgressAmount, flightSearchMetaViewModel.maxRetry)

                // already reach max retry limit, end retry
                if (noRetry > flightSearchMetaViewModel.maxRetry) {
                    flightAirportCombineModel.isNeedRefresh = false
                } else {
                    // retry load data
                    flightSearchPresenter.fetchSearchDataCloud(flightSearchPassData,
                            flightAirportCombineModel, flightSearchMetaViewModel.refreshTime)
                }
            } else {
                flightAirportCombineModel.isNeedRefresh = false
                progress += (flightSearchMetaViewModel.maxRetry - flightAirportCombineModel.noOfRetry) *
                        divideTo(halfProgressAmount, flightSearchMetaViewModel.maxRetry)
            }
        }

        setUpProgress()

        flightSearchPresenter.fetchSortAndFilter(selectedSortOption, flightFilterModel,
                flightAirportCombineModel.isNeedRefresh)
    }

    override fun onSuccessGetDetailFlightDeparture(flightJourneyViewModel: FlightJourneyViewModel) {
        // DO NOTHING
    }

    override fun onErrorDeleteFlightCache(e: Throwable) {
        resetDateAndReload()
    }

    override fun onSuccessDeleteFlightCache() {
        resetDateAndReload()
    }

    override fun stopTrace() {
        if (!isTraceStop) {
            performanceMonitoringP1.stopTrace()
            isTraceStop = true
        }
    }

    override fun onRetryClicked() {
        adapter.clearAllElements()
        flightSearchPresenter.resetCounterCall()
        fetchFlightSearchData()
    }

    override fun onDetailClicked(journeyViewModel: FlightJourneyViewModel?, adapterPosition: Int) {
        flightSearchPresenter.onSeeDetailItemClicked(journeyViewModel!!, adapterPosition)
        val flightDetailViewModel = FlightDetailViewModel()
        flightDetailViewModel.build(journeyViewModel)
        flightDetailViewModel.build(flightSearchPassData)

        if (journeyViewModel.fare.adultNumericCombo != 0) {
            flightDetailViewModel.total = journeyViewModel.comboPrice
            flightDetailViewModel.totalNumeric = journeyViewModel.comboPriceNumeric
            flightDetailViewModel.adultNumericPrice = journeyViewModel.fare.adultNumericCombo
            flightDetailViewModel.childNumericPrice = journeyViewModel.fare.childNumericCombo
            flightDetailViewModel.infantNumericPrice = journeyViewModel.fare.infantNumericCombo
        }

        startActivityForResult(FlightDetailActivity.createIntent(activity,
                flightDetailViewModel, true), REQUEST_CODE_SEE_DETAIL_FLIGHT)
    }

    override fun onItemClicked(journeyViewModel: FlightJourneyViewModel?, adapterPosition: Int) {
        flightSearchPresenter.onSearchItemClicked(journeyViewModel, adapterPosition)
    }

    override fun onShowAllClicked() {
        // need in return search
    }

    override fun onShowBestPairingClicked() {
        // need in return search
    }

    override fun onItemClicked(journeyViewModel: FlightJourneyViewModel?) {
        flightSearchPresenter.onSearchItemClicked(journeyViewModel = journeyViewModel)
    }

    override fun getScreenName(): String = ""

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyResultViewModel = EmptyResultViewModel()
        emptyResultViewModel.iconRes = R.drawable.ic_flight_empty_state
        if (inFilterMode) {
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

    override fun onSaveFilter(sortOption: Int, flightFilterModel: FlightFilterModel?) {
        this.selectedSortOption = sortOption
        if (flightFilterModel != null) {
            this.flightFilterModel = flightFilterModel
        }
        flightSearchPresenter.fetchSortAndFilter(selectedSortOption, this.flightFilterModel, false)
    }

    fun searchFlightData() {
        fetchFlightSearchData()
    }

    fun refreshData() {
        flightSearchPresenter.initialize(true)
        resetDateAndReload()
    }

    private fun getNoFlightRouteDataViewModel(message: String): Visitable<FlightSearchAdapterTypeFactory> {
        val emptyResultViewModel = EmptyResultViewModel()
        emptyResultViewModel.iconRes = R.drawable.ic_flight_empty_state
        emptyResultViewModel.title = message
        emptyResultViewModel.buttonTitleRes = R.string.flight_change_search_content_button
        emptyResultViewModel.callback = object : Callback {
            override fun onEmptyButtonClicked() {
                finishFragment()
            }

            override fun onEmptyContentItemTextClicked() {

            }
        }

        return emptyResultViewModel
    }

    open fun onSelectedFromDetail(selectedId: String, selectedTerm: String) {
        flightSearchPresenter.onSearchItemClicked(selectedId = selectedId)
    }

    open fun buildFilterModel(flightFilterModel: FlightFilterModel): FlightFilterModel =
            flightFilterModel

    private fun setUpSwipeRefresh() {
        swipe_refresh_layout.setSwipeDistance()
        swipe_refresh_layout.setOnRefreshListener {
            hideLoading()
            swipe_refresh_layout.isRefreshing = false
            resetDateAndReload()
        }
    }

    private fun setUpBottomAction() {
        bottom_action_filter_sort.setButton2OnClickListener {
            showFilterSortBottomSheet()
        }

        setUIMarkSort()
        setUIMarkFilter()

        bottom_action_filter_sort.setButton1OnClickListener {
            addToolbarElevation()
            showFilterSortBottomSheet()
        }
        bottom_action_filter_sort.visibility = View.GONE
    }

    open fun getDepartureAirport(): FlightAirportViewModel =
            flightSearchPassData.departureAirport

    open fun getArrivalAirport(): FlightAirportViewModel =
            flightSearchPassData.arrivalAirport

    private fun getSearchRouteTitle(): Int = if (isReturning()) {
        R.string.flight_search_choose_return_flight
    } else {
        R.string.flight_search_choose_departure_flight
    }

    private fun setUpCombinationAirport() {
        val departureAirportCode: String? = getDepartureAirport().airportCode
        val departureAirportList: List<String> = if (departureAirportCode == null || departureAirportCode == "") {
            arrayListOf(getDepartureAirport().cityCode)
        } else {
            arrayListOf(departureAirportCode)
        }

        val arrivalAirportCode: String? = getArrivalAirport().airportCode
        val arrivalAirportList: List<String> = if (arrivalAirportCode == null || arrivalAirportCode == "") {
            arrayListOf(getArrivalAirport().cityCode)
        } else {
            arrayListOf(arrivalAirportCode)
        }

        flightAirportCombineModelList = FlightAirportCombineModelList(departureAirportList, arrivalAirportList)
    }

    private fun setUIMarkSort() {
        if (selectedSortOption == TravelSortOption.NO_PREFERENCE) {
            bottom_action_filter_sort.setMarkRight(false)
        } else {
            bottom_action_filter_sort.setMarkRight(true)
        }
    }

    private fun setUpProgress() {
        if (horizontal_progress_bar.visibility == View.VISIBLE) {
            if (isDoneLoadData() || isCombineDone) {
                if (isDoneLoadData() && !isCombineDone) {
                    progress = MAX_PROGRESS - 10
                } else if (isDoneLoadData() && isCombineDone) {
                    progress = MAX_PROGRESS
                }
                horizontal_progress_bar.setProgress(progress)
                flightSearchPresenter.setDelayHorizontalProgress()
            } else {
                horizontal_progress_bar.setProgress(progress)
            }
        }
    }

    private fun setMinMaxDatePicker(datePicker: DatePicker) {
        var maxDate: Date = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 2)
        maxDate = FlightDateUtil.addTimeToSpesificDate(maxDate, Calendar.DATE, -1)
        maxDate = FlightDateUtil.trimDate(maxDate)

        if (isReturning()) {
            val dateDepStr = flightSearchPassData.getDate(false)
            val dateDep = FlightDateUtil.trimDate(FlightDateUtil.stringToDate(dateDepStr))
            datePicker.minDate = dateDep.time
            datePicker.maxDate = maxDate.time
        } else {
            val dateNow = FlightDateUtil.trimDate(FlightDateUtil.getCurrentDate())
            datePicker.minDate = dateNow.time

            if (!flightSearchPassData.isOneWay) {
                val dateReturnStr = flightSearchPassData.getDate(true)
                val dateReturn = FlightDateUtil.trimDate(FlightDateUtil.stringToDate(dateReturnStr))
                datePicker.maxDate = dateReturn.time
            } else {
                datePicker.maxDate = maxDate.time
            }
        }
    }

    private fun showMessageErrorInSnackbar(resId: Int) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, getString(resId))
    }

    private fun divideTo(number: Int, pieces: Int): Int =
            Math.ceil(number.toDouble() / pieces).toInt()

    private fun resetDateAndReload() {
        flightSearchPresenter.unsubscribeAll()

        onFlightSearchFragmentListener!!.changeDate(flightSearchPassData)

        setUpCombinationAirport()
        horizontal_progress_bar.visibility = View.VISIBLE
        progress = 0
        bottom_action_filter_sort.visibility = View.GONE

        flightSearchPresenter.attachView(this)
        clearAllData()
        showLoading()

        flightSearchPresenter.resetCounterCall()

        searchFlightData()
        if (!flightSearchPassData.isOneWay && !isCombineDone) {
            flightSearchPresenter.fetchCombineData(flightSearchPassData)
        }
    }

    private fun onResetFilterClicked() {
        flightFilterModel = buildFilterModel(FlightFilterModel())
        adapter.clearAllNonDataElement()
        showLoading()
        setUIMarkFilter()
        fetchSortAndFilterData()
    }

    private fun onChangeDateClicked() {
        if (!activity!!.isFinishing) {
            var maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, MAX_DATE_ADDITION_YEAR)
            maxDate = FlightDateUtil.addTimeToSpesificDate(maxDate, Calendar.DATE, -1)
            maxDate = FlightDateUtil.trimDate(maxDate)
            var title = getString(com.tokopedia.travelcalendar.R.string.travel_calendar_label_choose_departure_trip_date)
            var minDate: Date

            if (isReturning()) {
                val dateDepStr = flightSearchPassData.getDate(false)
                val dateDep = FlightDateUtil.stringToDate(dateDepStr)
                minDate = FlightDateUtil.trimDate(dateDep)
                title = getString(com.tokopedia.travelcalendar.R.string.travel_calendar_label_choose_return_trip_date)
            } else {
                minDate = FlightDateUtil.trimDate(FlightDateUtil.getCurrentDate())

                if (!flightSearchPassData.isOneWay) {
                    val dateReturnStr = flightSearchPassData.getDate(true)
                    val dateReturn = FlightDateUtil.stringToDate(dateReturnStr)
                    maxDate = FlightDateUtil.trimDate(dateReturn)
                }
            }

            val dateInput = flightSearchPassData.getDate(isReturning())
            val date = FlightDateUtil.stringToDate(dateInput)
            val flightCalendarDialog = FlightCalendarOneWayWidget.newInstance(
                    TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, minDate),
                    TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, maxDate),
                    TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, date),
                    flightSearchPassData.departureAirport.airportCode,
                    flightSearchPassData.arrivalAirport.airportCode,
                    flightSearchPassData.flightClass.id)
            flightCalendarDialog.setListener(object : FlightCalendarOneWayWidget.ActionListener {
                override fun onDateSelected(dateSelected: Date) {
                    val calendar = FlightDateUtil.getCurrentCalendar()
                    calendar.time = dateSelected
                    flightSearchPresenter.resetCounterCall()
                    flightSearchPresenter.onSuccessDateChanged(calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
                }
            })
            flightCalendarDialog.show(activity!!.supportFragmentManager, "travel calendar")
        }
    }

    private fun showFilterSortBottomSheet() {
        val flightFilterBottomSheet = FlightFilterBottomSheet.getInstance(selectedSortOption, flightFilterModel)
        flightFilterBottomSheet.listener = this
        flightFilterBottomSheet.setShowListener { flightFilterBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        flightFilterBottomSheet.show(requireFragmentManager(), FlightFilterBottomSheet.TAG_FILTER)
    }

    private fun buildQuickFilterView() {
        flight_sort_filter.filterType = SortFilter.TYPE_ADVANCED
        flight_sort_filter.sortFilterHorizontalScrollView.scrollX = 0
        flight_sort_filter.parentListener = {
            showFilterSortBottomSheet()
        }

        if (filterItems.size < FILTER_SORT_ITEM_SIZE) {
            val quickDirectFilter = SortFilterItem(getString(R.string.direct))
            quickDirectFilter.listener = {
                quickDirectFilter.toggle()
            }

            val quickBaggageFilter = SortFilterItem(getString(R.string.flight_search_filter_baggage_label))
            quickBaggageFilter.listener = {
                quickBaggageFilter.toggle()
            }

            val quickMealFilter = SortFilterItem(getString(R.string.flight_search_filter_meal_label))
            quickMealFilter.listener = {
                quickMealFilter.toggle()
            }

            val quickTransitFilter = SortFilterItem(getString(R.string.flight_search_filter_transit))
            quickTransitFilter.listener = {
                quickTransitFilter.toggle()
            }

            filterItems.add(quickDirectFilter)
            filterItems.add(quickBaggageFilter)
            filterItems.add(quickMealFilter)
            filterItems.add(quickTransitFilter)
        }

        flight_sort_filter.addItem(filterItems)

        for (item in filterItems) {
            item.refChipUnify.setChevronClickListener { }
        }
    }

    private fun SortFilterItem.toggle() {
        type = if (type == ChipsUnify.TYPE_NORMAL) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
    }

    interface OnFlightSearchFragmentListener {

        fun selectFlight(selectedFlightID: String, selectedTerm: String, flightPriceViewModel: FlightPriceViewModel,
                         isBestPairing: Boolean, isCombineDone: Boolean)

        fun changeDate(flightSearchPassDataViewModel: FlightSearchPassDataViewModel)
    }

    companion object {
        const val MAX_PROGRESS = 100
        private const val FILTER_SORT_ITEM_SIZE = 4
        private const val EMPTY_MARGIN = 0
        private const val REQUEST_CODE_SEARCH_FILTER = 1
        private const val REQUEST_CODE_SEE_DETAIL_FLIGHT = 2
        private const val SAVED_FILTER_MODEL = "svd_filter_model"
        private const val SAVED_SORT_OPTION = "svd_sort_option"
        private const val SAVED_AIRPORT_COMBINE = "svd_airport_combine"
        private const val SAVED_PROGRESS = "svd_progress"
        private const val SAVED_IS_COMBINE_DONE = "svd_is_combine_done"
        private const val DEFAULT_DIMENS_MULTIPLIER = 0.5f
        private const val PADDING_SEARCH_LIST = 60
        private const val FLIGHT_SEARCH_P1_TRACE = "tr_flight_search_p1"
        private const val FLIGHT_SEARCH_P2_TRACE = "tr_flight_search_p2"
        private const val MAX_DATE_ADDITION_YEAR = 1
        private val TAG_FLIGHT_SORT = "tag_flight_sort"

        fun newInstance(passDataViewModel: FlightSearchPassDataViewModel): FlightSearchFragment {
            val bundle = Bundle()
            bundle.putParcelable(FlightSearchActivity.EXTRA_PASS_DATA, passDataViewModel)

            val fragment = FlightSearchFragment()
            fragment.arguments = bundle

            return fragment
        }
    }
}