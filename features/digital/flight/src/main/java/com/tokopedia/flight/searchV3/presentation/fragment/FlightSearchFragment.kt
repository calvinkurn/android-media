package com.tokopedia.flight.searchV3.presentation.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.search.constant.FlightSortOption
import com.tokopedia.flight.search.di.FlightSearchComponent
import com.tokopedia.flight.search.presentation.adapter.FlightSearchAdapterTypeFactory
import com.tokopedia.flight.search.presentation.adapter.viewholder.EmptyResultViewHolder
import com.tokopedia.flight.search.presentation.model.*
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.searchV3.presentation.activity.FlightSearchActivity
import com.tokopedia.flight.searchV3.presentation.contract.FlightSearchContract
import com.tokopedia.flight.searchV3.presentation.presenter.FlightSearchPresenter
import kotlinx.android.synthetic.main.fragment_search_flight.*
import kotlinx.android.synthetic.main.include_filter_bottom_action_view.*
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 07/01/19
 */
class FlightSearchFragment : BaseListFragment<FlightJourneyViewModel, FlightSearchAdapterTypeFactory>(),
        FlightSearchContract.View, FlightSearchAdapterTypeFactory.OnFlightSearchListener,
        ErrorNetworkModel.OnRetryListener {

    @Inject
    val flightSearchPresenter: FlightSearchPresenter

    protected lateinit var flightSearchComponent: FlightSearchComponent
    protected lateinit var flightSearchPassData: FlightSearchPassDataViewModel
    protected lateinit var onFlightSearchFragmentListener: OnFlightSearchFragmentListener
    protected lateinit var flightAirportCombineModelList: FlightAirportCombineModelList

    private var inFilterMode: Boolean = false
    private var progress = 0
    lateinit var selectedSortOption: Int

    private lateinit var flightFilterModel: FlightFilterModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flightSearchPassData = arguments.getParcelable(FlightSearchActivity.EXTRA_PASS_DATA)

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayout(), container, false)

        setUpProgress()
        setUpBottomAction()
        setUpSwipeRefresh()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flightSearchPresenter.attachView(this)
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
    }

    override fun onPause() {
        super.onPause()
        flightSearchPresenter.detachView()
    }

    override fun initInjector() {
        flightSearchComponent = DaggerFlightSearchComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(activity.application))
                .build()

        flightSearchComponent.inject(this)
    }

    override fun getRecyclerView(view: View?): RecyclerView =
            return recycler_view

    override fun getAdapterTypeFactory(): FlightSearchAdapterTypeFactory =
            return FlightSearchAdapterTypeFactory(this)

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
        onFlightSearchFragmentListener = (OnFlightSearchFragmentListener) context
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

        if (isListEmpty() && flightSearchPresenter.isDoneLoadData()) {
            adapter.addElement(emptyDataViewModel)
        } else {
            isLoadingInitialData = false
        }
    }

    override fun isListEmpty(): Boolean = !adapter.isContainData()

    protected fun getLayout(): Int = R.layout.fragment_search_flight

    override fun onDestroyView() {
        super.onDestroyView()
        this.`_$_clearFindViewByIdCache`()
    }

    override fun getActivity(): Activity = activity

    override fun getFlightSearchPassData(): FlightSearchPassDataViewModel = flightSearchPassData

    override fun isReturning(): Boolean = false

    override fun isDoneLoadData(): Boolean = progress >= MAX_PROGRESS

    override fun getFilterModel(): FlightFilterModel = flightFilterModel

    override fun renderSearchList(list: List<FlightJourneyViewModel>, needRefresh: Boolean) {
        if (!needRefresh || list.size > 0) {
            renderList(list)
        }

        if (list.size > 0) {
            showFilterAndSortView()
        }
    }

    override fun addToolbarElevation() {
        ((AppCompatActivity) activity).supportActionBar.elevation =
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

    override fun setFlightSearchPassData(passDataViewModel: FlightSearchPassDataViewModel) {
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
        ((AppCompatActivity) activity).supportActionBar.elevation = 0
    }

    override fun removeBottomPaddingForSortAndFilterActionButton() {
        getRecyclerView(view).setPadding(EMPTY_MARGIN, EMPTY_MARGIN, EMPTY_MARGIN, EMPTY_MARGIN)
    }

    override fun clearAdapterData() {
        adapter.setElement(arrayListOf())
    }

    override fun finishFragment() {
        activity.finish()
    }

    override fun navigateToTheNextPage(selectedId: String, fareViewModel: FlightPriceViewModel, isBestPairing: Boolean) {
        if (onFlightSearchFragmentListener != null) {
            onFlightSearchFragmentListener.selectFlight(selectedId, fareViewModel, isBestPairing)
        }
    }

    protected fun getNoFlightRouteDataViewModel(message: String): Visitable {
        val emptyResultViewModel: EmptyResultViewModel = EmptyResultViewModel()
        emptyResultViewModel.iconRes = R.drawable.ic_flight_empty_state
        emptyResultViewModel.title = message
        emptyResultViewModel.buttonTitleRes = R.string.flight_change_search_content_button
        emptyResultViewModel.callback = EmptyResultViewHolder.Callback() {
            override fun onEmptyContentItemTextClicked() {

            }

            override fun onEmptyButtonClicked() {
                finishFragment()
            }
        }

        return emptyResultViewModel
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