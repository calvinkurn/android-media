package com.tokopedia.hotel.destination.view.viewmodel

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.data.model.HotelSuggestion
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.hotel.destination.data.model.RecentSearch
import com.tokopedia.hotel.destination.data.model.SearchDestination
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by jessica on 27/03/19
 */

class HotelDestinationViewModel @Inject constructor(
        val userSessionInterface: UserSessionInterface,
        val graphqlRepository: GraphqlRepository,
        val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    lateinit var permissionCheckerHelper: PermissionCheckerHelper
    val popularSearch = MutableLiveData<Result<List<PopularSearch>>>()
    val recentSearch = MutableLiveData<Result<List<RecentSearch>>>()
    val searchDestination = MutableLiveData<RecentSearchState<MutableList<SearchDestination>>>()
    val longLat = MutableLiveData<Result<Pair<Double, Double>>>()
    val deleteSuccess = MutableLiveData<Boolean>()

    fun getHotelRecommendation(popularRawQuery: String, recentSearchRawQuery: String) {

        launchCatchError(block = {
            lateinit var gqlResponse: GraphqlResponse

            val popularSearchRequest = GraphqlRequest(popularRawQuery, TYPE_POPULAR_RESPONSE, false)

            if (userSessionInterface.isLoggedIn) {
                val params = mapOf(PARAM_USER_ID to userSessionInterface.userId.toInt())
                val recentSearchRequest = GraphqlRequest(recentSearchRawQuery, RecentSearch.Response::class.java, params, false)
                gqlResponse = graphqlRepository.getReseponse(listOf(popularSearchRequest, recentSearchRequest))

                if (gqlResponse.getError(RecentSearch.Response::class.java)?.isNotEmpty() != true) {
                    val result = (gqlResponse.getData(RecentSearch.Response::class.java) as RecentSearch.Response).recentSearch
                    if (result.isNotEmpty()) recentSearch.value = Success(result)
                }
            } else {
                gqlResponse = graphqlRepository.getReseponse(listOf(popularSearchRequest))
            }

            if (gqlResponse.getError(PopularSearch.Response::class.java)?.isNotEmpty() != true) {
                val result = (gqlResponse.getData(PopularSearch.Response::class.java) as PopularSearch.Response).popularSearchList
                if (result.isNotEmpty()) popularSearch.value = Success(result)
            }

        }) {
            popularSearch.value = Fail(it)
            recentSearch.value = Fail(it)
        }
    }

    fun getHotelSearchDestination(rawQuery: String, keyword: String) {
        val params = mapOf(PARAM_SEARCH_KEY to keyword)
        val dataParams = mapOf(PARAM_DATA to params)
        launchCatchError(block = {
            searchDestination.value = Shimmering
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_SEARCH_RESPONSE, dataParams)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelSuggestion.Response>()
            searchDestination.value = Loaded(Success(data.propertySearchSuggestion.searchDestinationList.toMutableList()))
        }) {
            searchDestination.value = Loaded(Fail(it))
        }
    }

    fun deleteRecentSearch(query: String, uuid: String) {
        val params = mapOf(PARAM_USER_ID to userSessionInterface.userId.toInt(), PARAM_DELETE_RECENT_UUID to uuid)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(query, RecentSearch.DeleteResponse::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<RecentSearch.DeleteResponse>()
            deleteSuccess.value = data.travelRecentSearchDelete.result
        }) {
            deleteSuccess.value = false
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
                activity.getString(R.string.hotel_destination_need_permission))
    }

    private fun onGetLocation(): Function1<DeviceLocation, Unit> {
        return { (latitude, longitude) ->
            if (latitude == 0.0 && longitude == 0.0) longLat.value = Fail(Throwable())
            else longLat.value = Success(Pair(longitude, latitude))
        }
    }

    companion object {
        private val TYPE_POPULAR_RESPONSE = PopularSearch.Response::class.java
        private val TYPE_SEARCH_RESPONSE = HotelSuggestion.Response::class.java

        const val PARAM_SEARCH_KEY = "searchKey"
        const val PARAM_DATA = "data"
        const val PARAM_USER_ID = "id"
        const val PARAM_DELETE_RECENT_UUID = "uuid"
    }
}