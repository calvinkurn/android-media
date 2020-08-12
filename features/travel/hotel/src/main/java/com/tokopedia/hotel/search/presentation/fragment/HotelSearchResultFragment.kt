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
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.hotel.search.data.model.*
import com.tokopedia.hotel.search.data.model.params.ParamFilter
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.data.util.CommonParam
import com.tokopedia.hotel.search.di.HotelSearchPropertyComponent
import com.tokopedia.hotel.search.presentation.activity.HotelSearchFilterActivity
import com.tokopedia.hotel.search.presentation.activity.HotelSearchResultActivity.Companion.CHANGE_SEARCH_REQ_CODE
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
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_hotel_search_result.*
import kotlinx.android.synthetic.main.item_property_search_result.*
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

        //dummy
        bottom_action_view.visibility = View.VISIBLE
        initializeFilterV2BottomSheet(getDummyFilter())
    }

    private fun getDummyFilter(): List<FilterV2>  {
        val filterType1 =  FilterV2(type = "Selection", name = "Campaign", displayName = "Promo",
                options = listOf("Gajian Seru", "Diskon Login", "Flash Sale"))
        val filterType2 = FilterV2(type= "open range", name = "price", displayName = "Harga per malam",
                options = listOf("0", "100000"))
        val filterType3 = FilterV2("selection range", "rating", "Rating",
                listOf("Semua", "6.0", "7.0", "8.0", "9.0"))
        val filterType4 = FilterV2("selection", "star", "Bintang",
                listOf("1", "2", "3"))
        return listOf(filterType1, filterType2, filterType3, filterType4)
    }

    private fun getDummySelectedFilter(): List<ParamFilterV2> {
        val filterType1 = ParamFilterV2(name = "Campaign", values = listOf("Diskon Login"))
        val filterType2 = ParamFilterV2(name = "price", values = listOf("10000", "50000"))
        val filterType3 = ParamFilterV2(name = "rating", values = listOf("Semua"))
        val filterType4 = ParamFilterV2(name = "star", values = listOf("3"))
        return listOf(filterType1, filterType2, filterType3, filterType4)
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
//        initializeFilterClick(data.displayInfo.filter)
        initializeFilterV2BottomSheet(data.filters)
        initializeQuickFilter(data.quickFilter)
    }

    private fun initializeFilterV2BottomSheet(filterV2s: List<FilterV2>) {
        filterBottomSheet = HotelFilterBottomSheets()
                .setSubmitFilterListener(this)
                .setSelected(getDummySelectedFilter())
                .setFilter(filterV2s)

        bottom_action_view.filterItem.listener = {
            filterBottomSheet.show(childFragmentManager, javaClass.simpleName)
        }
    }

    private fun initializeFilterClick(filter: Filter) {
        bottom_action_view.filterItem.listener = {
            searchResultviewModel.filter = filter
            context?.let {
                val cacheManager = SaveInstanceCacheManager(it, true).apply {
                    put(CommonParam.ARG_FILTER, filter)
                    put(CommonParam.ARG_SELECTED_FILTER, searchResultviewModel.selectedFilter)
                }
                startActivityForResult(HotelSearchFilterActivity.createIntent(it, cacheManager.id), REQUEST_FILTER)
            }
        }
    }

    private fun initializeQuickFilter(quickFilters: List<QuickFilter>) {

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
            context?.let {
                val cacheManager = SaveInstanceCacheManager(it, true).apply {
                    put(CommonParam.ARG_FILTER, searchResultviewModel.filter)
                    put(CommonParam.ARG_SELECTED_FILTER, searchResultviewModel.selectedFilter)
                }
                startActivityForResult(HotelSearchFilterActivity.createIntent(it, cacheManager.id), REQUEST_FILTER)
            }
        }
    }

    override fun onSubmitFilter(selectedFilter: List<ParamFilterV2>) {
        //track
        searchResultviewModel.addFilter(selectedFilter)
        loadInitialData()
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
//        searchResultviewModel.searchProperty(page, searchQuery)
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