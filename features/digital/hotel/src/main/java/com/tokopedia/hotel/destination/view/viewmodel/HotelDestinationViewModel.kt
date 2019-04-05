package com.tokopedia.hotel.destination.view.viewmodel

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.data.model.HotelRecommendation
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.hotel.destination.data.model.RecentSearch
import com.tokopedia.hotel.destination.data.model.SearchDestination
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.experimental.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by jessica on 27/03/19
 */

class HotelDestinationViewModel @Inject constructor(val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher){

    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    val recentSearch = MutableLiveData<Result<MutableList<RecentSearch>>>()
    val popularSearch = MutableLiveData<Result<MutableList<PopularSearch>>>()
    val searchDestination = MutableLiveData<Result<MutableList<SearchDestination>>>()
    val longLat = MutableLiveData<Result<Pair<Double, Double>>>()

    init {
        var list: MutableList<SearchDestination> = arrayListOf()
        list.add(SearchDestination(0,"city","Kota", "", "Jakarta", "Indonesia", 2000))
        list.add(SearchDestination(0,"city","Region", "", "Jakarta Barat", "Jakarta, Indonesia", 2000))
        list.add(SearchDestination(0,"city","Hotel", "", "Jakarta Indah Samudra", "Jakarta, Indonesia", 0))
        list.add(SearchDestination(0,"city","Area", "", "Jl. Jaka Sembung 55", "Jakarta, Indonesia", 2000))
        searchDestination.value = Success(list)

        //dummy data
        var listagain: MutableList<RecentSearch> = arrayListOf()
        listagain.add(RecentSearch(0,"", "Jakarta", ""))
        listagain.add(RecentSearch(0,"", "Jalan Sudirman", ""))
        listagain.add(RecentSearch(0,"", "Central Park Mall", ""))
        listagain.add(RecentSearch(0,"", "Bandung", ""))
        listagain.add(RecentSearch(0,"", "Kyoto Japan", ""))
        recentSearch.value = Success(listagain)

        var list3: MutableList<PopularSearch> = arrayListOf()
        list3.add(PopularSearch(0, "city","","Jakarta", "Indonesia",2000))
        list3.add(PopularSearch(0, "city","","Bandung", "Indonesia",1200))
        list3.add(PopularSearch(0, "city","","Yogyakarta", "Indonesia",1300))
        list3.add(PopularSearch(0, "city","","Kuta", "Bali, Indonesia",500))
        popularSearch.value = Success(list3)
    }

    fun getHotelRecommendation() {

    }

    fun getHotelSearchDestination(keyword: String) {
        var list: MutableList<SearchDestination> = arrayListOf()
        list.add(SearchDestination(0,"city","Kota", "", "Jakarta", "Indonesia", 2000))
        list.add(SearchDestination(0,"city","Region", "", "Jakarta Barat", "Jakarta, Indonesia", 2000))
        list.add(SearchDestination(0,"city","Hotel", "", "Jakarta Indah Samudra", "Jakarta, Indonesia", 0))
        list.add(SearchDestination(0,"city","Area", "", "Jl. Jaka Sembung 55", "Jakarta, Indonesia", 2000))
        searchDestination.value = Success(list)
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
}