package com.tokopedia.hotel.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.ticker.TravelTickerHotelPage
import com.tokopedia.common.travel.ticker.TravelTickerInstanceId
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.search.data.model.*
import com.tokopedia.hotel.search.data.model.params.*
import com.tokopedia.hotel.search.presentation.adapter.viewholder.FilterSelectionViewHolder
import com.tokopedia.hotel.search.usecase.SearchPropertyUseCase
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

class HotelSearchResultViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val searchPropertyUseCase: SearchPropertyUseCase,
        private val travelTickerUseCase: TravelTickerCoroutineUseCase)
    : BaseViewModel(dispatcher.io) {

    val searchParam: SearchParam = SearchParam()

    var selectedSort: Sort = Sort()

    var defaultSort = ""

    var filter: Filter = Filter()

    val liveSearchResult = MutableLiveData<Result<PropertySearch>>()
    val liveSelectedFilter = MutableLiveData<Pair<List<ParamFilterV2>, Boolean>>()

    private val mutableTickerData = MutableLiveData<Result<TravelTickerModel>>()
    val tickerData: LiveData<Result<TravelTickerModel>>
        get() = mutableTickerData

    var isFilter = false

    fun initSearchParam(hotelSearchModel: HotelSearchModel) {

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

    fun getSelectedFilter(): List<ParamFilterV2> = liveSelectedFilter.value?.first?.toMutableList() ?: mutableListOf()

    fun searchProperty(page: Int, searchQuery: String) {
        searchParam.page = page
        searchParam.filters = getSelectedFilter().toMutableList()
        isFilter = searchParam.filters.isNotEmpty()

        launch {
            liveSearchResult.postValue(searchPropertyUseCase.execute(searchQuery, searchParam))
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
        launch(dispatcher.main) {
            val tickerData = travelTickerUseCase.execute(TravelTickerInstanceId.HOTEL, TravelTickerHotelPage.SEARCH_LIST)
            mutableTickerData.postValue(tickerData)
        }
    }

    companion object {
        const val PARAM_SEARCH_PROPERTY = "data"
        private const val DEFAULT_SORT = "popularity"
    }
}