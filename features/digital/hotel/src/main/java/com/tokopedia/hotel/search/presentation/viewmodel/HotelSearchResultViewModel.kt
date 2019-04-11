package com.tokopedia.hotel.search.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.common.getSuccessData
import com.tokopedia.hotel.search.data.model.PropertySearch
import com.tokopedia.hotel.search.data.model.Sort
import com.tokopedia.hotel.search.data.model.params.ParamFilter
import com.tokopedia.hotel.search.data.model.params.SearchParam
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import javax.inject.Named

class HotelSearchResultViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatcher,
        @Named("search_query")
        private val searchQuery: String,
        @Named("dummy_search_result")
        private val dummySearchResult: String
): BaseViewModel(dispatcher){

    private val searchParam: SearchParam = SearchParam()
    var selectedSort: Sort = Sort()
    val selectedFilter: ParamFilter
        get() = searchParam.filter

    val liveSearchResult = MutableLiveData<Result<PropertySearch>>()

    fun initSearchParam(destinationID: Int, type: String, latitude: Float, longitude: Float,
                        checkIn: String, checkOut: String, totalRoom: Int, totalAdult: Int) {
        if (type == TYPE_CITY)
            searchParam.location.cityID = destinationID
        else if (type == TYPE_DISTRICT){
            searchParam.location.districtID = destinationID
        } else {
            searchParam.location.regionID = destinationID
        }
        searchParam.location.latitude = latitude
        searchParam.location.longitude = longitude
        searchParam.checkIn = checkIn
        searchParam.checkOut = checkOut
        searchParam.room = totalRoom
        searchParam.guest.adult = totalAdult
    }

    fun searchProperty(page: Int){
        searchParam.page = page
        launchCatchError(block = {
            val params = mapOf(PARAM_SEARCH_PROPERTY to searchParam)
            val graphqlRequest = GraphqlRequest(searchQuery, PropertySearch.Response::class.java, params)

            val response = withContext(Dispatchers.IO){ graphqlRepository.getReseponse(listOf(graphqlRequest)) }
            liveSearchResult.value = Success(response.getSuccessData<PropertySearch.Response>().response)
        }){
            it.printStackTrace()
            val gson = Gson()
            liveSearchResult.value = Success(gson.fromJson(dummySearchResult,
                    PropertySearch.Response::class.java).response)
        }
    }

    fun addSort(sort: Sort) {
        selectedSort = sort
        with(searchParam.sort){
            popularity = sort.name.toLowerCase() == "popularity"
            price = sort.name.toLowerCase() == "price"
            ranking = sort.name.toLowerCase() == "ranking"
            star = sort.name.toLowerCase() == "star"
            reviewScore = sort.name.toLowerCase() == "reviewScore"
        }
    }

    fun addFilter(filter: ParamFilter){
        searchParam.filter = filter
    }

    companion object {
        private const val PARAM_SEARCH_PROPERTY = "data"
        private const val TYPE_REGION = "region"
        private const val TYPE_DISTRICT = "district"
        private const val TYPE_CITY = "city"
    }
}