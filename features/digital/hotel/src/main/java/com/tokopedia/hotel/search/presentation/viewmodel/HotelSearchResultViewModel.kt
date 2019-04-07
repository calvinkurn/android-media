package com.tokopedia.hotel.search.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.hotel.search.data.model.PropertySearch
import com.tokopedia.hotel.search.data.model.Sort
import com.tokopedia.hotel.search.data.model.params.SearchParam
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.delay
import javax.inject.Inject
import javax.inject.Named

class HotelSearchResultViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val userSessionInterface: UserSessionInterface,
        dispatcher: CoroutineDispatcher,
        @Named("dummy_search_result")
        private val dummySearchResult: String
): BaseViewModel(dispatcher){

    private val searchParam: SearchParam = SearchParam()

    val liveSearchResult = MutableLiveData<Result<PropertySearch>>()

    fun initSearchParam(destinationID: Int, type: String, latitude: Float, longitude: Float,
                        checkIn: String, checkOut: String, totalRoom: Int, totalAdult: Int) {
        searchParam.location.cityID = destinationID
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
            delay(3000)
            val gson = Gson()
            liveSearchResult.value = Success(gson.fromJson(dummySearchResult,
                    PropertySearch.Response::class.java).response)
        }){
            it.printStackTrace()
        }
    }

    fun addSort(sort: Sort) {
        with(searchParam.sort){
            popularity = sort.name.toLowerCase() == "popularity"
            price = sort.name.toLowerCase() == "price"
            ranking = sort.name.toLowerCase() == "ranking"
            star = sort.name.toLowerCase() == "star"
            reviewScore = sort.name.toLowerCase() == "reviewScore"
        }
    }
}