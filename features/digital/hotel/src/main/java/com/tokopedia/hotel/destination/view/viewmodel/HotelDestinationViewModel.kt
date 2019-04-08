package com.tokopedia.hotel.destination.view.viewmodel

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.data.model.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

/**
 * @author by jessica on 27/03/19
 */

class HotelDestinationViewModel @Inject constructor(
        val graphqlRepository: GraphqlRepository,
        val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher){

    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    val recentSearch = MutableLiveData<Result<MutableList<RecentSearch>>>()
    val popularSearch = MutableLiveData<Result<MutableList<PopularSearch>>>()
    val searchDestination = MutableLiveData<RecentSearchState<MutableList<SearchDestination>>>()
    val longLat = MutableLiveData<Result<Pair<Double, Double>>>()

    init {

        //dummy data
        var listagain: MutableList<RecentSearch> = arrayListOf()
        listagain.add(RecentSearch(0,"", "Jakarta", ""))
        listagain.add(RecentSearch(0,"", "Jalan Sudirman", ""))
        listagain.add(RecentSearch(0,"", "Central Park Mall", ""))
        listagain.add(RecentSearch(0,"", "Bandung", ""))
        listagain.add(RecentSearch(0,"", "Kyoto Japan", ""))
        recentSearch.value = Success(listagain)

    }

    fun getHotelRecommendation(rawQuery: String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_POPULAR_RESPONSE, false)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelRecommendation>()

            popularSearch.value = Success(data.popularSearchList.toMutableList())
        }) {

        }
    }

    fun getHotelSearchDestination(rawQuery: String, keyword: String) {
        val params = mapOf(PARAM_SEARCH_KEY to keyword)
        val dataParams = mapOf(PARAM_DATA to params)
        launchCatchError(block = {
            searchDestination.value = Shimmering
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_SEARCH_RESPONSE, dataParams, false)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelSuggestion.Response>()
            searchDestination.value = Loaded(Success(data.propertySearchSuggestion.searchDestinationList.toMutableList()))
        }){
            searchDestination.value = Loaded(Fail(it))
        }
    }

    fun setPermissionChecker(permissionCheckerHelper: PermissionCheckerHelper) {
        this.permissionCheckerHelper = permissionCheckerHelper
    }

    fun getCurrentLocation(activity: Activity, fusedLocationProviderClient: FusedLocationProviderClient) {
        val locationDetectorHelper = LocationDetectorHelper(
                permissionCheckerHelper,
                fusedLocationProviderClient,
                activity.applicationContext)
        locationDetectorHelper.getLocation(onGetLocation(), activity,
                LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                activity.getString(R.string.hotel_search_need_permission))
    }

    private fun onGetLocation(): Function1<DeviceLocation, Unit> {
        return { (latitude, longitude) ->
            Log.d("OUTPUTTT", latitude.toString() + " " + longitude.toString())
            longLat.value = Success(Pair(longitude, latitude))
            null
        }
    }

    companion object {
        private val TYPE_POPULAR_RESPONSE = HotelRecommendation::class.java
        private val TYPE_SEARCH_RESPONSE = HotelSuggestion.Response::class.java

        const val PARAM_SEARCH_KEY = "searchKey"
        const val PARAM_DATA = "data"
    }
}