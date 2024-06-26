package com.tokopedia.hotel.destination.view.viewmodel

import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.destination.data.model.HotelSuggestion
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.hotel.destination.data.model.RecentSearch
import com.tokopedia.hotel.destination.data.model.SearchDestination
import com.tokopedia.hotel.destination.usecase.GetHotelRecentSearchUseCase
import com.tokopedia.hotel.destination.usecase.GetPropertyPopularUseCase
import com.tokopedia.hotel.destination.view.fragment.HotelRecommendationFragment
import com.tokopedia.hotel.destination.view.mapper.HotelDestinationMapper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.locationmanager.DeviceLocation
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
    private val hotelDestinationMapper: HotelDestinationMapper,
    val graphqlRepository: GraphqlRepository,
    val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

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
                val params = mapOf(PARAM_USER_ID to userSessionInterface.userId.toLongOrZero())
                getHotelRecentSearchUseCase.params = params
                val recentSearchData = getHotelRecentSearchUseCase.executeOnBackground()
                if (recentSearchData.isNotEmpty()) recentSearch.postValue(Success(recentSearchData))
            }
        }) {
            popularSearch.postValue(Fail(it))
            recentSearch.postValue(Fail(it))
        }
    }

    fun getHotelSearchDestination(rawQuery: GqlQueryInterface, keyword: String) {
        val params = mapOf(PARAM_SEARCH_KEY to keyword)
        val dataParams = mapOf(PARAM_DATA to params)
        launchCatchError(block = {
            searchDestination.postValue(Shimmering)
            val data = withContext(dispatcher.main) {
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_SEARCH_RESPONSE, dataParams)
                graphqlRepository.response(listOf(graphqlRequest))
            }.getSuccessData<HotelSuggestion.Response>()
            searchDestination.postValue(Loaded(Success(hotelDestinationMapper.mapSource(data).toMutableList())))
        }) {
            searchDestination.postValue(Loaded(Fail(it)))
        }
    }

    fun deleteRecentSearch(query: GqlQueryInterface, uuid: String) {
        val params = mapOf(PARAM_USER_ID to userSessionInterface.userId.toLong(), PARAM_DELETE_RECENT_UUID to uuid)
        launchCatchError(block = {
            val data = withContext(dispatcher.main) {
                val graphqlRequest = GraphqlRequest(query, RecentSearch.DeleteResponse::class.java, params)
                graphqlRepository.response(listOf(graphqlRequest))
            }.getSuccessData<RecentSearch.DeleteResponse>()
            deleteSuccess.postValue(data.travelRecentSearchDelete.result)
        }) {
            deleteSuccess.postValue(false)
        }
    }

    fun getLocationFromUpdates(fusedLocationProviderClient: FusedLocationProviderClient,looper: Looper? = Looper.myLooper() ) {
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = LOCATION_REQUEST_INTERVAL

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach {
                    if (it != null) {
                        validateLocation(it.latitude, it.longitude)
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

        looper?.let {
            try {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback,
                    looper
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }


    }

    fun validateLocation(latitude: Double, longitude: Double) {
        if (latitude == DEFAULT_LATITUDE && longitude == DEFAULT_LONGITUDE) {
            longLat.postValue(Fail(Throwable()))
        } else {
            longLat.postValue(Success(Pair(longitude, latitude)))
        }
    }

    fun onGetLocation(): Function1<DeviceLocation, Unit> {
        return { (latitude, longitude) ->
            if (latitude == DEFAULT_LATITUDE && longitude == DEFAULT_LONGITUDE) {
                longLat.postValue(Fail(Throwable()))
            } else {
                longLat.postValue(Success(Pair(longitude, latitude)))
            }
        }
    }

    companion object {
        private const val LOCATION_REQUEST_INTERVAL: Long = 10 * 1000
        private val TYPE_SEARCH_RESPONSE = HotelSuggestion.Response::class.java

        const val PARAM_SEARCH_KEY = "searchKey"
        const val PARAM_DATA = "data"
        const val PARAM_USER_ID = "id"
        const val PARAM_DELETE_RECENT_UUID = "uuid"
        const val DEFAULT_LATITUDE = 0.0
        const val DEFAULT_LONGITUDE = 0.0
    }
}
