package com.tokopedia.flight.searchV3.presentation.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.search.constant.FlightSortOption
import com.tokopedia.flight.search.di.DaggerFlightSearchComponent
import com.tokopedia.flight.search.di.FlightSearchComponent
import com.tokopedia.flight.search.presentation.activity.FlightSearchFilterActivity
import com.tokopedia.flight.search.presentation.adapter.FlightSearchAdapterTypeFactory
import com.tokopedia.flight.search.presentation.model.*
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.searchV3.presentation.activity.FlightSearchActivity
import com.tokopedia.flight.searchV3.presentation.contract.FlightSearchContract
import com.tokopedia.flight.searchV3.presentation.presenter.FlightSearchPresenter
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
        ErrorNetworkModel.OnRetryListener {

    lateinit var flightSearchPresenter: FlightSearchPresenter
        @Inject set

    protected lateinit var flightSearchComponent: FlightSearchComponent
    protected lateinit var flightSearchPassData: FlightSearchPassDataViewModel
    protected lateinit var onFlightSearchFragmentListener: OnFlightSearchFragmentListener
    protected lateinit var flightAirportCombineModelList: FlightAirportCombineModelList

    private var inFilterMode: Boolean = false
    private var progress = 0
    var selectedSortOption: Int = FlightSortOption.NO_PREFERENCE

    private lateinit var flightFilterModel: FlightFilterModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flightSearchPassData = arguments!!.getParcelable(FlightSearchActivity.EXTRA_PASS_DATA)

        if (savedInstanceState == null) {
            flightFilterModel = buildFilterModel(FlightFilterModel())
            selectedSortOption = FlightSortOption.CHEAPEST
            setUpCombinationAirport()
            progress = 0
        } else {
            flightFilterModel = savedInstanceState.getParcelable(SAVED_FILTER_MODEL)
            selectedSortOption = savedInstanceState.getInt(SAVED_SORT_OPTION)
            flightAirportCombineModelList = savedInstanceState.getParcelable(SAVED_AIRPORT_COMBINE)
            progress = savedInstanceState.getInt(SAVED_PROGRESS, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(getLayout(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flightSearchPresenter.attachView(this)

        setUpProgress()
        setUpBottomAction()
        setUpSwipeRefresh()

        searchFlightData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(SAVED_FILTER_MODEL, flightFilterModel)
        outState.putInt(SAVED_SORT_OPTION, selectedSortOption)
        outState.putParcelable(SAVED_AIRPORT_COMBINE, flightAirportCombineModelList)
        outState.putInt(SAVED_PROGRESS, progress)
    }

    override fun onResume() {
        super.onResume()
        flightSearchPresenter.attachView(this)

        flightSearchPresenter.fetchSortAndFilter(selectedSortOption, flightFilterModel, true)
    }

    override fun onPause() {
        super.onPause()
        flightSearchPresenter.detachView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun initInjector() {
        flightSearchComponent = DaggerFlightSearchComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(activity!!.application))
                .build()

        flightSearchComponent.inject(this)
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

    override fun callInitialLoadAutomatically(): Boolean = true

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

        /*if (isListEmpty() && flightSearchPresenter.isDoneLoadData()) {
            adapter.addElement(emptyDataViewModel)
        } else {
            isLoadingInitialData = false
        }*/
    }

    override fun isListEmpty(): Boolean = !adapter.isContainData()

    protected fun getLayout(): Int = R.layout.fragment_search_flight

    override fun onDestroyView() {
        super.onDestroyView()
        this.clearFindViewByIdCache()
    }

    override fun getSearchPassData(): FlightSearchPassDataViewModel = flightSearchPassData

    override fun isReturning(): Boolean = false

    override fun isDoneLoadData(): Boolean = progress >= MAX_PROGRESS

    override fun getFilterModel(): FlightFilterModel = flightFilterModel

    override fun renderSearchList(list: List<FlightJourneyViewModel>, needRefresh: Boolean) {
        if (!needRefresh || list.size > 0) {
            renderList(list.toMutableList())
        }

        if (list.isNotEmpty()) {
            showFilterAndSortView()
        }
    }

    override fun addToolbarElevation() {
        (activity as AppCompatActivity).supportActionBar!!.elevation =
                resources.getDimension(R.dimen.dp_4)
    }

    override fun addProgress(numberToAdd: Int) {
        progress += numberToAdd
    }

    override fun addBottomPaddingForSortAndFilterActionButton() {
        val scale: Float = resources.displayMetrics.density;
        getRecyclerView(view).setPadding(
                EMPTY_MARGIN,
                EMPTY_MARGIN,
                EMPTY_MARGIN,
                (scale * PADDING_SEARCH_LIST + DEFAULT_DIMENS_MULTIPLIER).toInt()
        )
    }

    override fun setUIMarkFilter() {
        if (flightFilterModel.hasFilter()) {
            bottom_action_filter_sort.setMarkLeft(true)
            inFilterMode = true
        } else {
            bottom_action_filter_sort.setMarkLeft(false)
            inFilterMode = false
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
    }

    override fun showEmptyFlightStateView() {
        adapter.addElement(emptyDataViewModel)
    }

    override fun showNoRouteFlightEmptyState(message: String) {
        adapter.clearAllElements()
        adapter.addElement(getNoFlightRouteDataViewModel(message))
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
        adapter.setElement(arrayListOf())
    }

    override fun finishFragment() {
        activity!!.finish()
    }

    override fun navigateToTheNextPage(selectedId: String, fareViewModel: FlightPriceViewModel, isBestPairing: Boolean) {
        if (onFlightSearchFragmentListener != null) {
            onFlightSearchFragmentListener.selectFlight(selectedId, fareViewModel, isBestPairing)
        }
    }

    fun searchFlightData() {
        if (isReturning()) {
            flightSearchPresenter.fetchCombineData(flightSearchPassData)
        } else {
            fetchFlightSearchData()
        }
    }

    protected fun getNoFlightRouteDataViewModel(message: String): Visitable<FlightSearchAdapterTypeFactory> {
        val emptyResultViewModel: EmptyResultViewModel = EmptyResultViewModel()
        emptyResultViewModel.iconRes = R.drawable.ic_flight_empty_state
        emptyResultViewModel.title = message
        emptyResultViewModel.buttonTitleRes = R.string.flight_change_search_content_button
        /*emptyResultViewModel.callback = Callback {
            fun onEmptyContentItemTextClicked() {

            }

            fun onEmptyButtonClicked() {
                finishFragment()
            }
        }*/

        return emptyResultViewModel
    }

    protected fun buildFilterModel(flightFilterModel: FlightFilterModel): FlightFilterModel =
            flightFilterModel

    protected fun setUpSwipeRefresh() {
        swipe_refresh_layout.setSwipeDistance()
        swipe_refresh_layout.setOnRefreshListener {
            hideLoading()
            swipe_refresh_layout.isEnabled = false
            resetDateAndReload()
        }
    }

    protected fun setUpBottomAction() {
        bottom_action_filter_sort.setButton2OnClickListener {
            val bottomSheetBuilder: BottomSheetBuilder = CheckedBottomSheetBuilder(activity)
                    .setMode(BottomSheetBuilder.MODE_LIST)
                    .addTitleItem(getString(R.string.flight_search_sort_title))

            (bottomSheetBuilder as CheckedBottomSheetBuilder).addItem(FlightSortOption.CHEAPEST, getString(R.string.flight_search_sort_item_cheapest_price), null, selectedSortOption == FlightSortOption.CHEAPEST)
            bottomSheetBuilder.addItem(FlightSortOption.MOST_EXPENSIVE, getString(R.string.flight_search_sort_item_most_expensive_price), null, selectedSortOption == FlightSortOption.MOST_EXPENSIVE)
            bottomSheetBuilder.addItem(FlightSortOption.EARLIEST_DEPARTURE, getString(R.string.flight_search_sort_item_earliest_departure), null, selectedSortOption == FlightSortOption.EARLIEST_DEPARTURE)
            bottomSheetBuilder.addItem(FlightSortOption.LATEST_DEPARTURE, getString(R.string.flight_search_sort_item_latest_departure), null, selectedSortOption == FlightSortOption.LATEST_DEPARTURE)
            bottomSheetBuilder.addItem(FlightSortOption.SHORTEST_DURATION, getString(R.string.flight_search_sort_item_shortest_duration), null, selectedSortOption == FlightSortOption.SHORTEST_DURATION)
            bottomSheetBuilder.addItem(FlightSortOption.LONGEST_DURATION, getString(R.string.flight_search_sort_item_longest_duration), null, selectedSortOption == FlightSortOption.LONGEST_DURATION)
            bottomSheetBuilder.addItem(FlightSortOption.EARLIEST_ARRIVAL, getString(R.string.flight_search_sort_item_earliest_arrival), null, selectedSortOption == FlightSortOption.EARLIEST_ARRIVAL)
            bottomSheetBuilder.addItem(FlightSortOption.LATEST_ARRIVAL, getString(R.string.flight_search_sort_item_latest_arrival), null, selectedSortOption == FlightSortOption.LATEST_ARRIVAL)

            val bottomSheetDialog: BottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                    .setItemClickListener {
                        if (adapter.data != null) {
                            selectedSortOption = it.itemId
                            flightSearchPresenter.fetchSortAndFilter(selectedSortOption, flightFilterModel, false)
                        }
                    }
                    .createDialog()
            bottomSheetDialog.show()
        }

        setUIMarkSort()
        setUIMarkFilter()

        bottom_action_filter_sort.setButton1OnClickListener {
            addToolbarElevation()
            startActivityForResult(FlightSearchFilterActivity.createInstance(activity, isReturning(), flightFilterModel),
                    REQUEST_CODE_SEARCH_FILTER)
        }
        bottom_action_filter_sort.visibility = View.GONE
    }

    private fun setUIMarkSort() {
        if (selectedSortOption == FlightSortOption.NO_PREFERENCE) {
            bottom_action_filter_sort.setMarkRight(false)
        } else {
            bottom_action_filter_sort.setMarkRight(true)
        }
    }

    private fun setUpProgress() {
        if (horizontal_progress_bar.visibility == View.VISIBLE) {
            if (isDoneLoadData()) {
                progress = MAX_PROGRESS
                horizontal_progress_bar.setProgress(MAX_PROGRESS)
//                flightSearchPresenter.setDelayHorizontalProgress()
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
        flightSearchPresenter.detachView()

        onFlightSearchFragmentListener.changeDate(flightSearchPassData)

        setUpCombinationAirport()
        horizontal_progress_bar.visibility = View.VISIBLE
        progress = 0
        bottom_action_filter_sort.visibility = View.GONE

        flightSearchPresenter.attachView(this)
        clearAllData()
        showLoading()

        flightSearchPresenter.resetCounterCall()

        searchFlightData()
    }

    interface OnFlightSearchFragmentListener {

        fun selectFlight(selectedFlightID: String, flightPriceViewModel: FlightPriceViewModel, isBestPairing: Boolean)

        fun changeDate(flightSearchPassDataViewModel: FlightSearchPassDataViewModel)
    }

    companion object {
        val MAX_PROGRESS = 100
        private val EMPTY_MARGIN = 0
        private val REQUEST_CODE_SEARCH_FILTER = 1
        private val REQUEST_CODE_SEE_DETAIL_FLIGHT = 2
        private val REQUEST_CODE_CHANGE_DATE = 3
        private val SAVED_FILTER_MODEL = "svd_filter_model"
        private val SAVED_SORT_OPTION = "svd_sort_option"
        private val SAVED_STAT_MODEL = "svd_stat_model"
        private val SAVED_AIRPORT_COMBINE = "svd_airport_combine"
        private val SAVED_PROGRESS = "svd_progress"
        private val DEFAULT_DIMENS_MULTIPLIER = 0.5f
        private val PADDING_SEARCH_LIST = 60

        fun newInstance(passDataViewModel: FlightSearchPassDataViewModel): FlightSearchFragment {
            val bundle = Bundle()
            bundle.putParcelable(FlightSearchActivity.EXTRA_PASS_DATA, passDataViewModel)

            val fragment = FlightSearchFragment()
            fragment.arguments = bundle

            return fragment
        }
    }
}