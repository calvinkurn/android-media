package com.tokopedia.hotel.destination.view.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.HotelDestinationQueries
import com.tokopedia.hotel.destination.data.model.HotelSuggestion
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.hotel.destination.data.model.RecentSearch
import com.tokopedia.hotel.destination.data.model.SearchDestination
import com.tokopedia.hotel.destination.usecase.GetHotelRecentSearchUseCase
import com.tokopedia.hotel.destination.usecase.GetPropertyPopularUseCase
import com.tokopedia.hotel.destination.view.fragment.HotelRecommendationFragment
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by jessica on 27/03/19
 */

class HotelDestinationViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val getPropertyPopularUseCase: GetPropertyPopularUseCase,
        private val getHotelRecentSearchUseCase: GetHotelRecentSearchUseCase,
        val graphqlRepository: GraphqlRepository,
        val dispatcher: CoroutineDispatchers) : BaseViewModel(dispatcher.io) {

    private lateinit var permissionCheckerHelper: PermissionCheckerHelper
    val popularSearch = MutableLiveData<Result<List<PopularSearch>>>()
    val recentSearch = MutableLiveData<Result<List<RecentSearch>>>()
    val searchDestination = MutableLiveData<RecentSearchState<MutableList<SearchDestination>>>()
    val longLat = MutableLiveData<Result<Pair<Double, Double>>>()
    val deleteSuccess = MutableLiveData<Boolean>()

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    fun getHotelRecommendation() {
        launchCatchError(block = {
            val popularPropertyData = getPropertyPopularUseCase.executeOnBackground()
            if (popularPropertyData.isNotEmpty()) popularSearch.postValue(Success(popularPropertyData))

            if (userSessionInterface.isLoggedIn) {
                val params = mapOf(PARAM_USER_ID to userSessionInterface.userId.toInt())
                getHotelRecentSearchUseCase.params = params
                val recentSearchData = getHotelRecentSearchUseCase.executeOnBackground()
                if (recentSearchData.isNotEmpty()) recentSearch.postValue(Success(recentSearchData))
            }
        }) {
            popularSearch.postValue(Fail(it))
            recentSearch.postValue(Fail(it))
        }
    }

    fun getHotelSearchDestination(rawQuery: String, keyword: String) {
        val params = mapOf(PARAM_SEARCH_KEY to keyword)
        val dataParams = mapOf(PARAM_DATA to params)
        launchCatchError(block = {
            searchDestination.postValue(Shimmering)
            val data = withContext(dispatcher.main) {
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_SEARCH_RESPONSE, dataParams)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelSuggestion.Response>()
            searchDestination.postValue(Loaded(Success(data.propertySearchSuggestion.searchDestinationList.toMutableList())))
        }) {
            searchDestination.postValue(Loaded(Fail(it)))
        }
    }

    fun deleteRecentSearch(query: String, uuid: String) {
        val params = mapOf(PARAM_USER_ID to userSessionInterface.userId.toInt(), PARAM_DELETE_RECENT_UUID to uuid)
        launchCatchError(block = {
            val data = withContext(dispatcher.main) {
                val graphqlRequest = GraphqlRequest(query, RecentSearch.DeleteResponse::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<RecentSearch.DeleteResponse>()
            deleteSuccess.postValue(data.travelRecentSearchDelete.result)
        }) {
            deleteSuccess.postValue(false)
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

    fun getLocationFromUpdates(fusedLocationProviderClient: FusedLocationProviderClient) {
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = LOCATION_REQUEST_INTERVAL

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) return
                locationResult.locations.forEach {
                    if (it != null) {
                        if (it.latitude == 0.0 && it.longitude == 0.0) longLat.postValue(Fail(Throwable()))
                        else longLat.postValue(Success(Pair(it.longitude, it.latitude)))
                        try {
                            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                        } catch (e: Throwable) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                if (!locationAvailability.isLocationAvailable) longLat.postValue(Fail(Throwable(HotelRecommendationFragment.GPS_FAILED_SHOW_ERROR)))
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun onGetLocation(): Function1<DeviceLocation, Unit> {
        return { (latitude, longitude) ->
            if (latitude == 0.0 && longitude == 0.0) longLat.postValue(Fail(Throwable()))
            else longLat.postValue(Success(Pair(longitude, latitude)))
        }
    }

    companion object {
        private const val LOCATION_REQUEST_INTERVAL: Long = 10 * 1000
        private val TYPE_SEARCH_RESPONSE = HotelSuggestion.Response::class.java

        const val PARAM_SEARCH_KEY = "searchKey"
        const val PARAM_DATA = "data"
        const val PARAM_USER_ID = "id"
        const val PARAM_DELETE_RECENT_UUID = "uuid"
    }
}