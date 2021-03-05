package com.tokopedia.hotel.search_map.presentation.viewmodel

import android.app.Activity
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.VisibleRegion
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.ticker.TravelTickerHotelPage
import com.tokopedia.common.travel.ticker.TravelTickerInstanceId
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.search.data.model.*
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.data.model.params.ParamLocation
import com.tokopedia.hotel.search.data.model.params.ParamSort
import com.tokopedia.hotel.search.data.model.params.SearchParam
import com.tokopedia.hotel.search.presentation.adapter.viewholder.FilterSelectionViewHolder
import com.tokopedia.hotel.search.usecase.SearchPropertyUseCase
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by furqan on 01/03/2021
 */
class HotelSearchMapViewModel @Inject constructor(
        private val dispatcher: TravelDispatcherProvider,
        private val searchPropertyUseCase: SearchPropertyUseCase,
        private val travelTickerUseCase: TravelTickerCoroutineUseCase)
    : BaseViewModel(dispatcher.io()) {

    lateinit var hotelSearchModel: HotelSearchModel
    val searchParam: SearchParam = SearchParam()

    var selectedSort: Sort = Sort()

    var defaultSort = ""

    var filter: Filter = Filter()

    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    val liveSearchResult = MutableLiveData<Result<PropertySearch>>()
    val liveSelectedFilter = MutableLiveData<Pair<List<ParamFilterV2>, Boolean>>()

    private val mutableLatLong = MutableLiveData<Result<Pair<Double, Double>>>()
    val latLong: LiveData<Result<Pair<Double, Double>>>
        get() = mutableLatLong

    private val mutableRadius = MutableLiveData<Result<Double>>()
    val radius: LiveData<Result<Double>>
        get() = mutableRadius

    private val mutableTickerData = MutableLiveData<Result<TravelTickerModel>>()
    val tickerData: LiveData<Result<TravelTickerModel>>
        get() = mutableTickerData

    var isFilter = false

    fun initSearchParam(hotelSearchModel: HotelSearchModel) {
        this.hotelSearchModel = hotelSearchModel
        with(searchParam) {
            location = ParamLocation()

            when (hotelSearchModel.type) {
                // temp: to support the popular search and recent search in suggestion page
                HotelTypeEnum.CITY.value -> {
                    location.cityID = hotelSearchModel.id
                }
                HotelTypeEnum.DISTRICT.value -> {
                    location.districtID = hotelSearchModel.id
                }
                HotelTypeEnum.REGION.value -> {
                    location.regionID = hotelSearchModel.id
                }
            }

            // when user search by coordinate
            if (hotelSearchModel.searchType == HotelTypeEnum.COORDINATE.value) {
                location.latitude = hotelSearchModel.lat
                location.longitude = hotelSearchModel.long
            }

            checkIn = hotelSearchModel.checkIn
            checkOut = hotelSearchModel.checkOut
            room = hotelSearchModel.room
            guest.adult = hotelSearchModel.adult
            location.searchType = hotelSearchModel.searchType
            location.searchId = hotelSearchModel.searchId

            //Default param
            sort.popularity = true
            addSort(Sort(DEFAULT_SORT))
        }
    }

    fun getSelectedFilter(): List<ParamFilterV2> = liveSelectedFilter.value?.first?.toMutableList()
            ?: mutableListOf()

    fun searchProperty(page: Int, searchQuery: String) {
        searchParam.page = page
        searchParam.filters = getSelectedFilter().toMutableList()
        isFilter = searchParam.filters.isNotEmpty()

        launch {
            liveSearchResult.value = searchPropertyUseCase.execute(searchQuery, searchParam)
        }
    }

    fun addSort(sort: Sort) {
        selectedSort = sort

        searchParam.sort = when (sort.name.toLowerCase()) {
            HotelSortEnum.POPULARITY.value -> ParamSort(popularity = true, sortDir = HotelSortEnum.POPULARITY.order)
            HotelSortEnum.PRICE.value -> ParamSort(price = true, sortDir = HotelSortEnum.PRICE.order)
            HotelSortEnum.RANKING.value -> ParamSort(ranking = true, sortDir = HotelSortEnum.RANKING.order)
            HotelSortEnum.STAR.value -> ParamSort(star = true, sortDir = HotelSortEnum.STAR.order)
            HotelSortEnum.REVIEWSCORE.value -> ParamSort(reviewScore = true, sortDir = HotelSortEnum.REVIEWSCORE.order)
            else -> ParamSort()
        }
    }

    fun addFilter(filterV2: List<ParamFilterV2>, notifyUi: Boolean = true) {
        liveSelectedFilter.value = Pair(filterV2.filter { it.values.isNotEmpty() }.toMutableList(), notifyUi)
    }

    fun addFilter(quickFilters: List<QuickFilter>, sortFilterItems: ArrayList<SortFilterItem>) {
        val selectedFilters = getSelectedFilter().associateBy({ it.name }, { it }).toMutableMap()
        quickFilters.forEachIndexed { index, quickFilter ->
            val isQuickFilterSelected = sortFilterItems[index].type == ChipsUnify.TYPE_SELECTED
            if (isQuickFilterSelected) {
                if (selectedFilters.containsKey(quickFilter.name)) {
                    val selectedFilter = selectedFilters[quickFilter.name] ?: ParamFilterV2()
                    val filterValue = selectedFilter.values.toHashSet()
                    if (quickFilter.type == FilterV2.FILTER_TYPE_OPEN_RANGE
                            || quickFilter.type == FilterV2.FILTER_TYPE_SELECTION_RANGE ||
                            quickFilter.name == FilterSelectionViewHolder.SELECTION_STAR_TYPE) filterValue.clear()
                    filterValue.addAll(quickFilter.values)
                    selectedFilters[quickFilter.name] = ParamFilterV2(quickFilter.name, filterValue.toMutableList())
                } else {
                    selectedFilters[quickFilter.name] = ParamFilterV2(quickFilter.name, quickFilter.values.toMutableList())
                }
            } else {
                if (selectedFilters.containsKey(quickFilter.name)) {
                    var isContainsAllValue = true
                    val selectedFilter = selectedFilters[quickFilter.name] ?: ParamFilterV2()
                    val filterValue = selectedFilter.values.toHashSet()
                    for (value in quickFilter.values) {
                        if (!filterValue.contains(value)) isContainsAllValue = false
                    }
                    if (isContainsAllValue) {
                        for (value in quickFilter.values) {
                            filterValue.remove(value)
                        }
                    }
                    selectedFilters[quickFilter.name] = ParamFilterV2(quickFilter.name, filterValue.toMutableList())
                }
            }
        }
        addFilter(selectedFilters.values.toMutableList())
    }

    fun getFilterCount(): Int {
        var count = 0
        getSelectedFilter().forEach {
            count += if (it.name == "price") 1 else it.values.size
        }
        if (selectedSort.displayName != defaultSort) count += 1
        return count
    }

    fun fetchTickerData() {
        launch(dispatcher.ui()) {
            val tickerData = travelTickerUseCase.execute(TravelTickerInstanceId.HOTEL, TravelTickerHotelPage.SEARCH_LIST)
            mutableTickerData.postValue(tickerData)
        }
    }

    fun setPermissionHelper(permissionCheckerHelper: PermissionCheckerHelper) {
        this.permissionCheckerHelper = permissionCheckerHelper
    }

    fun getCurrentLocation(fusedLocationProviderClient: FusedLocationProviderClient, activity: Activity) {
        val locationDetectorHelper = LocationDetectorHelper(
                permissionCheckerHelper,
                fusedLocationProviderClient,
                activity.applicationContext)
        locationDetectorHelper.getLocation(onGetLocation(), activity,
                LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                activity.getString(R.string.hotel_destination_need_permission))
    }

    private fun onGetLocation(): Function1<DeviceLocation, Unit> {
        return { (latitude, longitude) ->
            if (latitude == 0.0 && longitude == 0.0) mutableLatLong.postValue(Fail(Throwable()))
            else mutableLatLong.postValue(Success(Pair(longitude, latitude)))
        }
    }

    /**Temp variable mutableLatLong will change it to midPoint afterwards*/
    fun getMidPoint(latLong: LatLng){
        if (latLong.latitude == 0.0 && latLong.longitude == 0.0) mutableLatLong.postValue(Fail(Throwable()))
        else mutableLatLong.postValue(Success(Pair(latLong.longitude, latLong.longitude)))
    }

    fun getVisibleRadius(googleMap: GoogleMap){
        try {
            val visibleRegion: VisibleRegion = googleMap.projection.visibleRegion
            val diagonalDistance = FloatArray(1)

            val farLeft = visibleRegion.farLeft
            val nearRight = visibleRegion.nearRight

            Location.distanceBetween(
                    farLeft.latitude,
                    farLeft.longitude,
                    nearRight.latitude,
                    nearRight.longitude,
                    diagonalDistance
            )
            mutableRadius.postValue(Success((diagonalDistance[0] / 2).toDouble()))
        }catch (error: Throwable){
            mutableRadius.postValue(Fail(error))
        }
    }

    companion object {
        const val PARAM_SEARCH_PROPERTY = "data"
        private const val DEFAULT_SORT = "popularity"
    }
}