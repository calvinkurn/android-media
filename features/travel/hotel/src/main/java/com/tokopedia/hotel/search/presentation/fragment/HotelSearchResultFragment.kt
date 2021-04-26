package com.tokopedia.hotel.search.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.common.util.HotelGqlQuery
import com.tokopedia.hotel.common.util.TRACKING_HOTEL_SEARCH
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.hotel.search.data.model.*
import com.tokopedia.hotel.search.data.model.FilterV2.Companion.FILTER_TYPE_SORT
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.di.HotelSearchPropertyComponent
import com.tokopedia.hotel.search.presentation.activity.HotelSearchResultActivity.Companion.SEARCH_SCREEN_NAME
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchResultAdapter
import com.tokopedia.hotel.search.presentation.adapter.PropertyAdapterTypeFactory
import com.tokopedia.hotel.search.presentation.adapter.viewholder.SpaceItemDecoration
import com.tokopedia.hotel.search.presentation.viewmodel.HotelSearchResultViewModel
import com.tokopedia.hotel.search.presentation.widget.HotelFilterBottomSheets
import com.tokopedia.hotel.search.presentation.widget.SubmitFilterListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_search_result.*
import javax.inject.Inject

class HotelSearchResultFragment : BaseListFragment<Property, PropertyAdapterTypeFactory>(),
        BaseEmptyViewHolder.Callback, HotelSearchResultAdapter.OnClickListener, SubmitFilterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var searchResultviewModel: HotelSearchResultViewModel
    lateinit var filterBottomSheet: HotelFilterBottomSheets

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil
    private var performanceMonitoring: PerformanceMonitoring? = null
    private var isTraceStop = false

    private lateinit var localCacheHandler: LocalCacheHandler

    var searchDestinationName = ""
    var searchDestinationType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        localCacheHandler = LocalCacheHandler(context, PREFERENCES_NAME)

        performanceMonitoring = PerformanceMonitoring.start(TRACKING_HOTEL_SEARCH)
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        searchResultviewModel = viewModelProvider.get(HotelSearchResultViewModel::class.java)
        arguments?.let {
            val hotelSearchModel = it.getParcelable(ARG_HOTEL_SEARCH_MODEL) ?: HotelSearchModel()

            val selectedParam = it.getParcelable(ARG_FILTER_PARAM) ?: ParamFilterV2()
            if (selectedParam.name.isNotEmpty()) {
                searchResultviewModel.addFilter(listOf(selectedParam), false)
            }

            searchResultviewModel.initSearchParam(hotelSearchModel)
            searchDestinationName = hotelSearchModel.name
            searchDestinationType = if (hotelSearchModel.searchType.isNotEmpty()) hotelSearchModel.searchType else hotelSearchModel.type
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchResultviewModel.liveSearchResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetResult(it.data)
                is Fail -> showGetListError(it.throwable)
            }
            stopTrace()
        })

        searchResultviewModel.liveSelectedFilter.observe(viewLifecycleOwner, Observer { (data, notifyUi) ->
            if (notifyUi) {
                showQuickFilterShimmering(true)
                setUpQuickFilterBaseOnSelectedFilter(data)
                loadInitialData()
            }
        })

        searchResultviewModel.tickerData.observe(viewLifecycleOwner, Observer {
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
    }

    private fun hideTickerView() {
        hotelSearchResultTicker.hide()
    }

    private fun renderTickerView(travelTickerModel: TravelTickerModel) {
        if (travelTickerModel.title.isNotEmpty()) hotelSearchResultTicker.tickerTitle = travelTickerModel.title
        var message = travelTickerModel.message
        if (travelTickerModel.url.isNotEmpty()) message += getString(R.string.hotel_ticker_desc, travelTickerModel.url)
        hotelSearchResultTicker.setHtmlDescription(message)
        hotelSearchResultTicker.tickerType = Ticker.TYPE_WARNING
        hotelSearchResultTicker.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                if (linkUrl.isNotEmpty()) {
                    RouteManager.route(context, linkUrl.toString())
                }
            }

            override fun onDismiss() {}

        })
        if (travelTickerModel.url.isNotEmpty()) {
            hotelSearchResultTicker.setOnClickListener {
                RouteManager.route(requireContext(), travelTickerModel.url)
            }
        }

        hotelSearchResultTicker.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hotel_search_result, container, false)
    }

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = getRecyclerView(view)
        recyclerView?.removeItemDecorationAt(0)
        searchResultviewModel.fetchTickerData()
    }

    private fun stopTrace() {
        if (!isTraceStop) {
            performanceMonitoring?.stopTrace()
            isTraceStop = true
        }
    }

    fun changeSearchParam() {
        searchResultviewModel.addFilter(listOf())
    }

    override fun createAdapterInstance(): BaseListAdapter<Property, PropertyAdapterTypeFactory> {
        return HotelSearchResultAdapter(this, adapterTypeFactory)
    }

    private fun onSuccessGetResult(data: PropertySearch) {
        val searchParam = searchResultviewModel.searchParam
        trackingHotelUtil.hotelViewHotelListImpression(context,
                searchDestinationName,
                searchDestinationType,
                searchParam,
                data.properties,
                adapter.dataSize, SEARCH_SCREEN_NAME)

        val searchProperties = data.properties

        showQuickFilterShimmering(false)

        super.renderList(searchProperties, searchProperties.isNotEmpty())

        if (isFirstInitializeFilter) {
            isFirstInitializeFilter = false
            initializeQuickFilter(data.quickFilter, data.filters, data.displayInfo.sort)

            quick_filter_sort_filter.chipItems?.filter { it.type == ChipsUnify.TYPE_SELECTED }?.forEach { _ ->
                quick_filter_sort_filter.indicatorCounter -= 1
            }
        }
    }

    private var isFirstInitializeFilter = true

    private var quickFilters: List<QuickFilter> = listOf()

    private fun initializeQuickFilter(quickFilters: List<QuickFilter>, filters: List<FilterV2>, sort: List<Sort>) {
        this.quickFilters = quickFilters.map { quickFilter ->
            val item = filters.filter { it.name.equals(quickFilter.name, true) }
            if (item.isNotEmpty()) {
                quickFilter.type = (item.firstOrNull() ?: FilterV2()).type
            }
            quickFilter
        }

        quick_filter_sort_filter.chipItems?.let {
            quick_filter_sort_filter.dismissListener = {
                searchResultviewModel.addFilter(quickFilters, it)
            }
        }

        val sortFilterItem = quickFilters.map {
            val item = SortFilterItem(title = it.displayName,
                    type = if (it.selected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL)
            item.listener = {
                item.toggleSelected()
            }
            return@map item
        }
        quick_filter_sort_filter.addItem(ArrayList(sortFilterItem))

        quick_filter_sort_filter.chipItems?.let { sortFilterItemList ->
            for ((index, item) in sortFilterItemList.withIndex()) {
                item.refChipUnify.setOnClickListener {
                    item.toggleSelected()
                    trackingHotelUtil.clickOnQuickFilter(context, SEARCH_SCREEN_NAME, item.title.toString(), index)
                    searchResultviewModel.addFilter(quickFilters, sortFilterItemList)
                }
            }
        }

        quick_filter_sort_filter.filterType = SortFilter.TYPE_ADVANCED
        quick_filter_sort_filter.parentListener = {
            trackingHotelUtil.clickOnAdvancedFilter(context, SEARCH_SCREEN_NAME)
            initiateAdvancedFilter(filters.toMutableList(), sort)
        }

        quick_filter_sort_filter.show()
        checkShouldShowCoachMark()
    }

    private fun checkShouldShowCoachMark() {
        val shouldShowCoachMark = localCacheHandler.getBoolean(SHOW_COACH_MARK_KEY, true)
        if (shouldShowCoachMark) {
            val coachMark = CoachMarkBuilder().build().apply {
                enableSkip = true
                onFinishListener = {
                    localCacheHandler.apply {
                        putBoolean(SHOW_COACH_MARK_KEY, false)
                        applyEditor()
                    }
                }
            }

            val quickFilterCoachMark = CoachMarkItem(
                    quick_filter_sort_filter.sortFilterPrefix,
                    getString(R.string.hotel_search_advance_filter_coachmark_title),
                    getString(R.string.hotel_search_advance_filter_coachmark_desc)
            )

            coachMark.enableSkip = false
            coachMark.setHighlightMargin(4)

            coachMark.show(
                    activity,
                    HotelSearchResultFragment::class.java.simpleName,
                    arrayListOf(quickFilterCoachMark)
            )
        }
    }

    private fun initiateAdvancedFilter(filterV2s: MutableList<FilterV2>, sort: List<Sort>) {
        val sortInFilterBottomSheet = FilterV2(type = FILTER_TYPE_SORT, name = FILTER_TYPE_SORT, displayName = getString(R.string.hotel_bottomsheet_sort_title))
        if (searchResultviewModel.selectedSort.displayName.isEmpty()) {
            val sortDisplayName = sort.filter { it.name == searchResultviewModel.selectedSort.name }.firstOrNull()
                    ?: Sort()
            searchResultviewModel.selectedSort.displayName = sortDisplayName.displayName
            searchResultviewModel.defaultSort = sortDisplayName.displayName
        }
        sortInFilterBottomSheet.options = sort.map { it.displayName }
        sortInFilterBottomSheet.optionSelected = listOf(searchResultviewModel.selectedSort.displayName)
        sortInFilterBottomSheet.defaultOption = searchResultviewModel.defaultSort
        filterV2s.add(0, sortInFilterBottomSheet)


        val selectedFilter = searchResultviewModel.getSelectedFilter().toMutableList()
        selectedFilter.add(ParamFilterV2(FILTER_TYPE_SORT, mutableListOf(searchResultviewModel.selectedSort.displayName)))

        filterBottomSheet = HotelFilterBottomSheets()
                .setSubmitFilterListener(this)
                .setSelected(selectedFilter)
                .setFilter(filterV2s)
        filterBottomSheet.show(childFragmentManager, javaClass.simpleName)
    }

    override fun getAdapterTypeFactory(): PropertyAdapterTypeFactory = PropertyAdapterTypeFactory(this)

    override fun onItemClicked(t: Property) {
        // no op
    }

    override fun onItemClicked(property: Property, position: Int) {
        with(searchResultviewModel.searchParam) {
            trackingHotelUtil.chooseHotel(
                    context,
                    searchDestinationName,
                    searchDestinationType,
                    this,
                    property,
                    position,
                    SEARCH_SCREEN_NAME)

            context?.run {
                startActivityForResult(HotelDetailActivity.getCallingIntent(this,
                        checkIn, checkOut, property.id, room, guest.adult,
                        searchDestinationType, searchDestinationName, property.isDirectPayment),
                        REQUEST_CODE_DETAIL_HOTEL)
            }
        }
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.urlRes = getString(R.string.hotel_url_empty_search_result)
        emptyModel.title = getString(R.string.hotel_search_empty_title)

        if (!searchResultviewModel.isFilter) {
            emptyModel.content = getString(R.string.hotel_search_empty_subtitle)
            emptyModel.buttonTitle = getString(R.string.hotel_search_empty_button)
        } else {
            emptyModel.content = getString(R.string.hotel_search_filter_empty_subtitle)
            emptyModel.buttonTitle = getString(R.string.hotel_search_filter_empty_button)
        }
        return emptyModel
    }

    override fun onEmptyContentItemTextClicked() {

    }

    fun onClickChangeSearch(hotelSearchModel: HotelSearchModel, screenName: String) {
        context?.let {
            val type = if (hotelSearchModel.searchType.isNotEmpty()) hotelSearchModel.searchType else hotelSearchModel.type
            trackingHotelUtil.hotelClickChangeSearch(context, type,
                    hotelSearchModel.name, hotelSearchModel.room, hotelSearchModel.adult,
                    hotelSearchModel.checkIn, hotelSearchModel.checkOut, screenName)
        }
    }

    override fun onEmptyButtonClicked() {
        if (!searchResultviewModel.isFilter) activity?.onBackPressed()
        else {
            val searchResultValue = searchResultviewModel.liveSearchResult.value as Success
            initiateAdvancedFilter(searchResultValue.data.filters.toMutableList(), searchResultValue.data.displayInfo.sort)
        }
    }

    override fun onSubmitFilter(selectedFilter: MutableList<ParamFilterV2>) {
        trackingHotelUtil.clickSubmitFilterOnBottomSheet(context, SEARCH_SCREEN_NAME, selectedFilter)

        var sortIndex: Int? = null
        selectedFilter.forEachIndexed { index, it ->
            if (it.name == FILTER_TYPE_SORT) {
                sortIndex = index
            }
        }

        sortIndex?.let { index ->
            val sort = findSortValue(selectedFilter[index])
            sort?.let { searchResultviewModel.addSort(it) }
            selectedFilter.removeAt(index)
        }

        searchResultviewModel.addFilter(selectedFilter)
    }

    private fun findSortValue(filter: ParamFilterV2): Sort? {
        return if (searchResultviewModel.liveSearchResult.value != null
                && searchResultviewModel.liveSearchResult.value is Success) {
            var sortOption = (searchResultviewModel.liveSearchResult.value as Success).data.displayInfo.sort
            sortOption = sortOption.filter { it.displayName == filter.values.firstOrNull() }
            sortOption.firstOrNull()
        } else null
    }

    //for setup quick filter after click submit in bottom sheet
    private fun setUpQuickFilterBaseOnSelectedFilter(selectedFilters: List<ParamFilterV2>) {
        quick_filter_sort_filter.chipItems?.forEach { it.type = ChipsUnify.TYPE_NORMAL }
        val selectedFiltersMap = selectedFilters.associateBy({ it.name }, { it })
        quickFilters.forEachIndexed { index, quickFilter ->
            if (selectedFiltersMap.containsKey(quickFilter.name)) {
                val selectedFilter = selectedFiltersMap[quickFilter.name]
                selectedFilter?.let { selectedFilterMap ->
                    var contains = true
                    for (quickFilterValue in quickFilter.values) {
                        for ((i, selectedFilterValue) in selectedFilterMap.values.withIndex()) {
                            if (quickFilterValue == selectedFilterValue) break
                            else if (i == selectedFilterMap.values.lastIndex) contains = false
                        }
                        if (!contains) break
                    }
                    quick_filter_sort_filter.chipItems?.let {
                        if (contains) it[index].type = ChipsUnify.TYPE_SELECTED
                    }
                }
            }
        }
    }

    override fun onGetListErrorWithEmptyData(throwable: Throwable?) {
        adapter.errorNetworkModel.iconDrawableRes = ErrorHandlerHotel.getErrorImage(throwable)
        adapter.errorNetworkModel.errorMessage = ErrorHandlerHotel.getErrorTitle(context, throwable)
        adapter.errorNetworkModel.subErrorMessage = ErrorHandlerHotel.getErrorMessage(context, throwable)
        adapter.errorNetworkModel.onRetryListener = this
        adapter.showErrorNetwork()
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(HotelSearchPropertyComponent::class.java).inject(this)
    }

    private fun showQuickFilterShimmering(isShimmering: Boolean) {
        if (isShimmering) {
            shimmer_quick_filter_sort_filter.show()
            quick_filter_sort_filter.hide()
        } else {
            shimmer_quick_filter_sort_filter.hide()
            quick_filter_sort_filter.show()
        }
        quick_filter_sort_filter.indicatorCounter = searchResultviewModel.getFilterCount()
    }

    override fun loadData(page: Int) {
        val searchQuery = HotelGqlQuery.PROPERTY_SEARCH
        searchResultviewModel.searchProperty(page, searchQuery)
    }

    override fun isAutoLoadEnabled(): Boolean = true
    override fun getMinimumScrollableNumOfItems(): Int = 5

    companion object {
        private const val REQUEST_CODE_DETAIL_HOTEL = 101

        const val ARG_HOTEL_SEARCH_MODEL = "arg_hotel_search_model"
        const val ARG_FILTER_PARAM = "arg_hotel_filter_param"

        const val SHOW_COACH_MARK_KEY = "hotel_quick_filter_show_coach_mark"
        const val PREFERENCES_NAME = "hotel_quick_filter_preferences"

        fun createInstance(hotelSearchModel: HotelSearchModel, selectedParam: ParamFilterV2): HotelSearchResultFragment {

            return HotelSearchResultFragment().also {
                it.arguments = Bundle().apply {
                    putParcelable(ARG_HOTEL_SEARCH_MODEL, hotelSearchModel)
                    putParcelable(ARG_FILTER_PARAM, selectedParam)
                }
            }
        }
    }
}