package com.tokopedia.hotel.search.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.common.util.TRACKING_HOTEL_SEARCH
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.hotel.search.data.model.*
import com.tokopedia.hotel.search.data.model.params.ParamFilter
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.data.util.CommonParam
import com.tokopedia.hotel.search.di.HotelSearchPropertyComponent
import com.tokopedia.hotel.search.presentation.activity.HotelSearchFilterActivity
import com.tokopedia.hotel.search.presentation.activity.HotelSearchResultActivity.Companion.SEARCH_SCREEN_NAME
import com.tokopedia.hotel.search.presentation.adapter.HotelOptionMenuAdapter
import com.tokopedia.hotel.search.presentation.adapter.HotelOptionMenuAdapter.Companion.MODE_CHECKED
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchResultAdapter
import com.tokopedia.hotel.search.presentation.adapter.PropertyAdapterTypeFactory
import com.tokopedia.hotel.search.presentation.adapter.viewholder.SpaceItemDecoration
import com.tokopedia.hotel.search.presentation.viewmodel.HotelSearchResultViewModel
import com.tokopedia.hotel.search.presentation.widget.HotelClosedSortBottomSheets
import com.tokopedia.hotel.search.presentation.widget.HotelFilterBottomSheets
import com.tokopedia.hotel.search.presentation.widget.SubmitFilterListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_search_result.*
import javax.inject.Inject

class HotelSearchResultFragment : BaseListFragment<Property, PropertyAdapterTypeFactory>(),
        BaseEmptyViewHolder.Callback, HotelSearchResultAdapter.OnClickListener, SubmitFilterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var searchResultviewModel: HotelSearchResultViewModel
    lateinit var sortMenu: HotelClosedSortBottomSheets
    lateinit var filterBottomSheet: HotelFilterBottomSheets

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil
    private var performanceMonitoring: PerformanceMonitoring? = null
    private var isTraceStop = false

    var searchDestinationName = ""
    var searchDestinationType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performanceMonitoring = PerformanceMonitoring.start(TRACKING_HOTEL_SEARCH)
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        searchResultviewModel = viewModelProvider.get(HotelSearchResultViewModel::class.java)
        arguments?.let {
            val hotelSearchModel = it.getParcelable(ARG_HOTEL_SEARCH_MODEL) ?: HotelSearchModel()
            searchResultviewModel.initSearchParam(hotelSearchModel)
            searchDestinationName = hotelSearchModel.name
            searchDestinationType = if (hotelSearchModel.searchType.isNotEmpty()) hotelSearchModel.searchType else hotelSearchModel.type
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchResultviewModel.liveSearchResult.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetResult(it.data)
                is Fail -> showGetListError(it.throwable)
            }
            stopTrace()
        })
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
        recyclerView.removeItemDecorationAt(0)
        context?.let {
            recyclerView.addItemDecoration(SpaceItemDecoration(it.resources.getDimensionPixelSize(R.dimen.hotel_12dp),
                    LinearLayoutManager.VERTICAL))
        }

        bottom_action_view.sortItem.title = getString(R.string.hotel_search_sort_label)
        bottom_action_view.sortItem.listener = {
            if (::sortMenu.isInitialized) {
                sortMenu.setTitle(getString(R.string.hotel_bottomsheet_sort_title))
                sortMenu.show(childFragmentManager, javaClass.simpleName)
            }
        }
    }

    private fun stopTrace() {
        if (!isTraceStop) {
            performanceMonitoring?.stopTrace()
            isTraceStop = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FILTER && data != null && data.hasExtra(CommonParam.ARG_CACHE_FILTER_ID)) {
                val cacheId = data.getStringExtra(CommonParam.ARG_CACHE_FILTER_ID)
                val cacheManager = context?.let { SaveInstanceCacheManager(it, cacheId) } ?: return
                val paramFilter = cacheManager.get(CommonParam.ARG_SELECTED_FILTER, ParamFilter::class.java)
                        ?: ParamFilter()

                trackingHotelUtil.hotelUserClickFilter(context, SEARCH_SCREEN_NAME)
                searchResultviewModel.addFilter(paramFilter)
                loadInitialData()
            }
        }
    }

    fun changeSearchParam() {
        loadInitialData()
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

        bottom_action_view.visibility = View.VISIBLE

        super.renderList(searchProperties, searchProperties.isNotEmpty())

        generateSortMenu(data.displayInfo.sort)

        if (isFirstInitializeFilter)  {
            initializeFilterV2BottomSheet(data.filters)
            initializeQuickFilter(data.quickFilter, data.filters)
            isFirstInitializeFilter = false
        }
    }

    private var isFirstInitializeFilter = true

    private fun initializeFilterV2BottomSheet(filterV2s: List<FilterV2>) {
        bottom_action_view.filterItem.listener = {
            filterBottomSheet = HotelFilterBottomSheets()
                    .setSubmitFilterListener(this)
                    .setSelected(searchResultviewModel.selectedFilterV2)
                    .setFilter(filterV2s)
            filterBottomSheet.show(childFragmentManager, javaClass.simpleName)
        }
    }

    private var quickFilters: List<QuickFilter> = listOf()

    private fun initializeQuickFilter(quickFilters: List<QuickFilter>, filters: List<FilterV2>) {
        this.quickFilters = quickFilters.map { quickFilter ->
            val item = filters.filter { it.name.equals(quickFilter.name, true) }
            if (item.isNotEmpty()) {
                quickFilter.type = (item.firstOrNull() ?: FilterV2()).type
            }
            quickFilter
        }

        quick_filter_sort_filter.dismissListener = {
            refreshSelectedFilter(quickFilters)
        }

        val sortFilterItem = quickFilters.map {
            val item =  SortFilterItem(title = it.displayName,
                type = if (it.selected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL)
            item.listener = {
                item.toggleSelected()
            }
            return@map item
        }
        quick_filter_sort_filter.addItem(ArrayList(sortFilterItem))

        for (item in quick_filter_sort_filter.chipItems)  {
            item.refChipUnify.setOnClickListener {
                item.toggleSelected()
                refreshSelectedFilter(quickFilters)
            }
        }

        refreshSelectedFilter(quickFilters, false)
        quick_filter_sort_filter.parentListener = { }

        quick_filter_sort_filter.show()
    }

    //to sync bottomsheet with quickfilter
    private fun refreshSelectedFilter(quickFilters: List<QuickFilter>, shouldCallApi: Boolean = true) {
        val selectedFilterV2 = searchResultviewModel.selectedFilterV2
        quickFilters.forEachIndexed { index,  quickFilter ->
            var isVisited = false
            val isQuickFilterSelected = quick_filter_sort_filter.chipItems[index].type == ChipsUnify.TYPE_SELECTED
            for ((filterIndex, selectedFilter) in searchResultviewModel.selectedFilterV2.withIndex()) {
                if (quickFilter.name.equals(selectedFilter.name, true))  {
                    for ((valueIndex, value) in selectedFilter.values.withIndex()) {
                        if (value.equals(quickFilter.value, true)) {
                            selectedFilterV2[filterIndex].values.removeAt(valueIndex)
                            break
                        }
                    }
                    if (isQuickFilterSelected) {
                        if (quickFilter.type.equals(FilterV2.FILTER_TYPE_SELECTION_RANGE, true) ||
                                quickFilter.type.equals(FilterV2.FILTER_TYPE_OPEN_RANGE, true) )  {
                            selectedFilterV2[filterIndex].values = mutableListOf(quickFilter.value)
                        } else selectedFilterV2[filterIndex].values.add(quickFilter.value)
                        isVisited = true
                        break
                    }
                }
            }
            if (isQuickFilterSelected && !isVisited) {
                //if it is the last item
                selectedFilterV2.add(ParamFilterV2(name = quickFilter.name,  values = mutableListOf(quickFilter.value) ))
            }
        }
        if (shouldCallApi) onQuickFilterChanged(selectedFilterV2)
    }

    private fun generateSortMenu(sort: List<Sort>) {
        sortMenu = HotelClosedSortBottomSheets()
                .setSheetTitle(getString(R.string.hotel_bottomsheet_sort_title))
                .setMode(MODE_CHECKED)
                .setMenu(sort)
                .setSelectedItem(searchResultviewModel.selectedSort)

        sortMenu.onMenuSelect = object : HotelOptionMenuAdapter.OnSortMenuSelected {
            override fun onSelect(sort: Sort) {
                trackingHotelUtil.hotelUserClickSort(context, sort.displayName, SEARCH_SCREEN_NAME)

                searchResultviewModel.addSort(sort)
                if (sortMenu.isAdded) {
                    sortMenu.dismiss()
                }
                loadInitialData()
            }
        }
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
        var emptyModel = EmptyModel()
        emptyModel.urlRes = getString(R.string.hotel_url_empty_search_result)
        emptyModel.title = getString(R.string.hotel_search_empty_title)

        if (!searchResultviewModel.isFilter) {
            emptyModel.content = getString(R.string.hotel_search_empty_subtitle)
            emptyModel.buttonTitle = getString(R.string.hotel_search_empty_button)
        } else {
            emptyModel.content = getString(R.string.hotel_search_filter_empty_subtitle)
            emptyModel.buttonTitle = getString(R.string.hotel_search_filter_empty_button)
        }

        bottom_action_view.visibility = View.GONE
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
            onSubmitFilter(listOf())
        }
    }

    override fun onSubmitFilter(selectedFilter: List<ParamFilterV2>) {
        //track
        searchResultviewModel.addFilter(selectedFilter)
        setUpQuickFilterBaseOnSelectedFilter(selectedFilter)
        loadInitialData()
    }

    private fun onQuickFilterChanged(selectedFilters: List<ParamFilterV2>)  {
        searchResultviewModel.addFilter(selectedFilters)
        loadInitialData()
    }

    private fun setUpQuickFilterBaseOnSelectedFilter(selectedFilters: List<ParamFilterV2>) {
        quick_filter_sort_filter.chipItems.forEach {
            it.type = ChipsUnify.TYPE_NORMAL
        }

        quickFilters.forEachIndexed { index, quickFilter ->
            for (selectedFilter in selectedFilters)  {
                if (selectedFilter.name.equals(quickFilter.name, true)) {
                    for (value in selectedFilter.values) {
                        if (value.equals(quickFilter.value, true)) {
                            quick_filter_sort_filter.chipItems[index].type = ChipsUnify.TYPE_SELECTED
                        }
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

    override fun loadData(page: Int) {
        val searchQuery = GraphqlHelper.loadRawString(resources, R.raw.gql_get_property_search)
        searchResultviewModel.searchParam
        searchResultviewModel.searchProperty(page, searchQuery)
    }

    override fun isAutoLoadEnabled(): Boolean = true

    companion object {
        private const val REQUEST_FILTER = 0x10
        private const val REQUEST_CODE_DETAIL_HOTEL = 101

        const val ARG_HOTEL_SEARCH_MODEL = "arg_hotel_search_model"

        fun createInstance(hotelSearchModel: HotelSearchModel): HotelSearchResultFragment {

            return HotelSearchResultFragment().also {
                it.arguments = Bundle().apply {
                    putParcelable(ARG_HOTEL_SEARCH_MODEL, hotelSearchModel)
                }
            }
        }
    }
}