package com.tokopedia.hotel.search.presentation.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.list.decoration.SpaceItemDecoration
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.hotel.search.data.model.Filter
import com.tokopedia.hotel.search.data.model.Property
import com.tokopedia.hotel.search.data.model.PropertySearch
import com.tokopedia.hotel.search.data.model.Sort
import com.tokopedia.hotel.search.data.model.params.ParamFilter
import com.tokopedia.hotel.search.data.util.CommonParam
import com.tokopedia.hotel.search.di.HotelSearchPropertyComponent
import com.tokopedia.hotel.search.presentation.activity.HotelSearchFilterActivity
import com.tokopedia.hotel.search.presentation.adapter.HotelOptionMenuAdapter
import com.tokopedia.hotel.search.presentation.adapter.HotelOptionMenuAdapter.Companion.MODE_CHECKED
import com.tokopedia.hotel.search.presentation.adapter.PropertyAdapterTypeFactory
import com.tokopedia.hotel.search.presentation.viewmodel.HotelSearchResultViewModel
import com.tokopedia.hotel.search.presentation.widget.HotelClosedSortBottomSheets
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_search_result.*
import javax.inject.Inject

class HotelSearchResultFragment : BaseListFragment<Property, PropertyAdapterTypeFactory>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var searchResultviewModel: HotelSearchResultViewModel
    lateinit var sortMenu: HotelClosedSortBottomSheets
    private var onFilterClick: View.OnClickListener? = null
    lateinit var trackingHotelUtils: TrackingHotelUtil

    var searchDestinationName = ""

    companion object {
        private const val REQUEST_FILTER = 0x10

        const val ARG_DESTINATION_ID = "arg_destination"
        const val ARG_TYPE = "arg_type"
        const val ARG_LAT = "arg_lat"
        const val ARG_LONG = "arg_long"
        const val ARG_CHECK_IN = "arg_check_in"
        const val ARG_CHECK_OUT = "arg_check_out"
        const val ARG_TOTAL_ROOM = "arg_total_room"
        const val ARG_TOTAL_ADULT = "arg_total_adult"
        const val ARG_TOTAL_CHILDREN = "arg_total_children"
        const val ARG_DESTINATION_NAME = "arg_destination_name"

        val REQUEST_CODE_DETAIL_HOTEL = 101

        fun createInstance(destinationName: String = "", destinationID: Int = 0, type: String = "",
                           latitude: Float = 0f, longitude: Float = 0f, checkIn: String = "",
                           checkOut: String = "", totalRoom: Int = 1, totalAdult: Int = 0,
                           totalChildren: Int = 0): HotelSearchResultFragment {

            return HotelSearchResultFragment().also {
                it.arguments = Bundle().apply {
                    putString(ARG_DESTINATION_NAME, destinationName)
                    putInt(ARG_DESTINATION_ID, destinationID)
                    putString(ARG_TYPE, type)
                    putFloat(ARG_LAT, latitude)
                    putFloat(ARG_LONG, longitude)
                    putString(ARG_CHECK_IN, checkIn)
                    putString(ARG_CHECK_OUT, checkOut)
                    putInt(ARG_TOTAL_ROOM, totalRoom)
                    putInt(ARG_TOTAL_ADULT, totalAdult)
                    putInt(ARG_TOTAL_CHILDREN, totalChildren)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        searchResultviewModel = viewModelProvider.get(HotelSearchResultViewModel::class.java)
        arguments?.let {
            searchResultviewModel.initSearchParam(it.getInt(ARG_DESTINATION_ID),
                    it.getString(ARG_TYPE, ""),
                    it.getFloat(ARG_LAT, 0f),
                    it.getFloat(ARG_LONG, 0f),
                    it.getString(ARG_CHECK_IN, ""),
                    it.getString(ARG_CHECK_OUT, ""),
                    it.getInt(ARG_TOTAL_ROOM, 1),
                    it.getInt(ARG_TOTAL_ADULT, 0))
            searchDestinationName = it.getString(ARG_DESTINATION_NAME, "")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchResultviewModel.liveSearchResult.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetResult(it.data)
                is Fail -> showGetListError(it.throwable)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hotel_search_result, container, false)
    }

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = getRecyclerView(view)
        recyclerView.removeItemDecorationAt(0)
        context?.let {
            recyclerView.addItemDecoration(SpaceItemDecoration(it.resources.getDimensionPixelSize(R.dimen.dp_12),
                    LinearLayoutManager.VERTICAL))
        }
        bottom_action_view.setButton1OnClickListener {
            if (::sortMenu.isInitialized)
                sortMenu.show(childFragmentManager, javaClass.simpleName)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FILTER && data != null && data.hasExtra(CommonParam.ARG_CACHE_FILTER_ID)) {
                val cacheId = data.getStringExtra(CommonParam.ARG_CACHE_FILTER_ID)
                val cacheManager = context?.let { SaveInstanceCacheManager(it, cacheId) } ?: return
                searchResultviewModel.addFilter(cacheManager.get(CommonParam.ARG_SELECTED_FILTER, ParamFilter::class.java)
                        ?: ParamFilter())
                loadInitialData()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onSuccessGetResult(data: PropertySearch) {
        var searchParam = searchResultviewModel.searchParam
        trackingHotelUtils.hotelViewHotelListImpression(
                searchDestinationName,
                searchParam.room,
                searchParam.guest.adult,
                HotelUtils.countCurrentDayDifference(searchParam.checkIn).toInt(),
                HotelUtils.countDayDifference(searchParam.checkIn, searchParam.checkOut).toInt(),
                mapPropertySearch(data)
        )

        bottom_action_view.visible()
        super.renderList(data.properties, data.properties.size > 0)
        generateSortMenu(data.displayInfo.sort)
        initializeFilterClick(data.displayInfo.filter)
    }

    private fun mapPropertySearch(data: PropertySearch): List<TrackingHotelUtil.HotelImpressionProduct> {
        return data.properties.map {
            TrackingHotelUtil.HotelImpressionProduct(it.name, "", it.id,
                it.roomPrice.minBy { it.totalPriceAmount }?.totalPriceAmount?.toInt() ?: 0)
        }
    }

    private fun initializeFilterClick(filter: Filter) {
        onFilterClick = View.OnClickListener {
            context?.let {
                val cacheManager = SaveInstanceCacheManager(it, true).apply {
                    put(CommonParam.ARG_FILTER, filter)
                    put(CommonParam.ARG_SELECTED_FILTER, searchResultviewModel.selectedFilter)
                }
                startActivityForResult(HotelSearchFilterActivity.createIntent(it, cacheManager.id), REQUEST_FILTER)
            }
        }
        bottom_action_view.setButton2OnClickListener(onFilterClick)
    }

    private fun generateSortMenu(sort: List<Sort>) {
        sortMenu = HotelClosedSortBottomSheets()
                .setTitle(getString(R.string.hotel_bottomsheet_sort_title))
                .setMode(MODE_CHECKED)
                .setMenu(sort)
                .setSelecetedItem(searchResultviewModel.selectedSort)

        sortMenu.onMenuSelect = object : HotelOptionMenuAdapter.OnSortMenuSelected {
            override fun onSelect(sort: Sort) {
                searchResultviewModel.addSort(sort)
                sortMenu.dismiss()
                loadInitialData()
            }
        }
    }

    override fun getAdapterTypeFactory(): PropertyAdapterTypeFactory = PropertyAdapterTypeFactory()

    override fun onItemClicked(t: Property) {
        with(searchResultviewModel.searchParam) {
            startActivityForResult(HotelDetailActivity.getCallingIntent(context!!,
                    checkIn, checkOut, t.id, room, guest.adult),
                    REQUEST_CODE_DETAIL_HOTEL)
        }
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(HotelSearchPropertyComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        searchResultviewModel.searchProperty(page)
    }
}