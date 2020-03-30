package com.tokopedia.flight.dashboardV2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.constant.TravelType
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.dashboard.view.fragment.model.FlightClassModel
import com.tokopedia.flight.dashboard.view.fragment.model.FlightDashboardModel
import com.tokopedia.flight.dashboard.view.fragment.model.FlightPassengerModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by furqan on 27/03/2020
 */
class FlightDashboardViewModel @Inject constructor(
        private val flightAnalytics: FlightAnalytics,
        private val getTravelCollectiveBannerUseCase: GetTravelCollectiveBannerUseCase,
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    private val mutableBannerList = MutableLiveData<Result<TravelCollectiveBannerModel>>()
    val bannerList: LiveData<Result<TravelCollectiveBannerModel>>
        get() = mutableBannerList

    private val mutableDashboardData = MutableLiveData<FlightDashboardModel>()
    val dashboardData: LiveData<FlightDashboardModel>
        get() = mutableDashboardData

    init {
        mutableDashboardData.value = FlightDashboardModel()
    }

    fun fetchBannerData(query: String, isFromCloud: Boolean) {
        launch(dispatcherProvider.ui()) {
            val bannerList = getTravelCollectiveBannerUseCase.execute(query, TravelType.FLIGHT, isFromCloud)
            mutableBannerList.postValue(bannerList)
        }
    }

    fun getBannerData(position: Int): TravelCollectiveBannerModel.Banner? {
        return try {
            val data = mutableBannerList.value as Success<TravelCollectiveBannerModel>
            data.data.banners[position]
        } catch (t: Throwable) {
            null
        }
    }

    fun onBannerClicked(position: Int, banner: TravelCollectiveBannerModel.Banner) {
        //                flightAnalytics.eventPromotionClick(position + 1, banner)
    }

    fun onDepartureAirportChanged(departureAirport: FlightAirportModel) {
//        flightAnalytics.eventOriginClick(departureAirport.cityName, departureAirport.airportCode)
        dashboardData.value?.let {
            val newDashboardData = cloneViewModel(it)
            newDashboardData.departureAirport = departureAirport
            mutableDashboardData.value = newDashboardData
        }
    }

    fun onArrivalAirportChanged(arrivalAirport: FlightAirportModel) {
//        flightAnalytics.eventDestinationClick(arrivalAirport.cityName, arrivalAirport.airportCode)
        dashboardData.value?.let {
            val newDashboardData = cloneViewModel(it)
            newDashboardData.arrivalAirport = arrivalAirport
            mutableDashboardData.value = newDashboardData
        }
    }

    fun onClassChanged(classModel: FlightClassModel) {
//        flightAnalytics.eventClassClick(classModel.title)
        dashboardData.value?.let {
            val newDashboardData = cloneViewModel(it)
            newDashboardData.flightClass = classModel
            mutableDashboardData.value = newDashboardData
        }
    }

    fun onPassengerChanged(passengerModel: FlightPassengerModel) {
//        flightAnalytics.eventPassengerClick(passengerModel.adult, passengerModel.children, passengerModel.infant)
        dashboardData.value?.let {
            val newDashboardData = cloneViewModel(it)
            newDashboardData.flightPassengerViewModel = passengerModel
            mutableDashboardData.value = newDashboardData
        }
    }

    private fun cloneViewModel(currentDashboardData: FlightDashboardModel): FlightDashboardModel {
        val dashboardModel: FlightDashboardModel
        try {
            dashboardModel = currentDashboardData.clone() as FlightDashboardModel
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
            throw RuntimeException("Failed to Clone FlightDashboardModel")
        }
        return dashboardModel
    }

}