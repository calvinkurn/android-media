package com.tokopedia.hotel.search.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.search.data.model.*
import com.tokopedia.hotel.search.data.model.params.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HotelSearchResultViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: TravelDispatcherProvider)
    : BaseViewModel(dispatcher.io()) {

    val searchParam: SearchParam = SearchParam()

    var selectedSort: Sort = Sort()
    val selectedFilter: ParamFilter
        get() = searchParam.filter
    val selectedFilterV2: MutableList<ParamFilterV2>
        get() = searchParam.filters

    var filter: Filter = Filter()

    val liveSearchResult = MutableLiveData<Result<PropertySearch>>()

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

    fun searchProperty(page: Int, searchQuery: String) {
        searchParam.page = page
        launchCatchError(block = {
            val params = mapOf(PARAM_SEARCH_PROPERTY to searchParam)
            val graphqlRequest = GraphqlRequest(searchQuery, PropertySearch.Response::class.java, params)

            val response = withContext(dispatcher.ui()) { graphqlRepository.getReseponse(listOf(graphqlRequest)) }
            liveSearchResult.value = Success(response.getSuccessData<PropertySearch.Response>().response)
        }) {
            liveSearchResult.value = Fail(it)
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

    fun addFilter(filter: ParamFilter) {
        searchParam.filter = filter
        isFilter = true
    }

    fun addFilter(filterV2: List<ParamFilterV2>) {
        searchParam.filters = filterV2.filter { it.values.isNotEmpty() }.toMutableList()
        isFilter = selectedFilterV2.isNotEmpty()
    }

    companion object {
        private const val PARAM_SEARCH_PROPERTY = "data"

        private const val DEFAULT_SORT = "popularity"
    }
}