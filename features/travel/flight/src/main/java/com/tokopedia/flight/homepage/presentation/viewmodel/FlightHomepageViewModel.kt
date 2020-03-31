package com.tokopedia.flight.homepage.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.constant.TravelType
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.common.travel.ticker.TravelTickerFlightPage
import com.tokopedia.common.travel.ticker.TravelTickerInstanceId
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.dashboard.view.fragment.model.FlightClassModel
import com.tokopedia.flight.dashboard.view.fragment.model.FlightDashboardModel
import com.tokopedia.flight.dashboard.view.fragment.model.FlightPassengerModel
import com.tokopedia.flight.search_universal.presentation.viewmodel.FlightSearchUniversalViewModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 27/03/2020
 */
class FlightHomepageViewModel @Inject constructor(
        private val flightAnalytics: FlightAnalytics,
        private val travelTickerUseCase: TravelTickerCoroutineUseCase,
        private val getTravelCollectiveBannerUseCase: GetTravelCollectiveBannerUseCase,
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    private val mutableBannerList = MutableLiveData<Result<TravelCollectiveBannerModel>>()
    val bannerList: LiveData<Result<TravelCollectiveBannerModel>>
        get() = mutableBannerList

    private val mutableDashboardData = MutableLiveData<FlightDashboardModel>()
    val dashboardData: LiveData<FlightDashboardModel>
        get() = mutableDashboardData

    private val mutableTickerData = MutableLiveData<Result<TravelTickerModel>>()
    val tickerData: LiveData<Result<TravelTickerModel>>
        get() = mutableTickerData

    init {
        mutableDashboardData.value = FlightDashboardModel()
    }

    fun fetchBannerData(query: String, isFromCloud: Boolean) {
        launch(dispatcherProvider.ui()) {
            val bannerList = getTravelCollectiveBannerUseCase.execute(query, TravelType.FLIGHT, isFromCloud)
            mutableBannerList.postValue(bannerList)
        }
    }

    fun fetchTickerData() {
        launch(dispatcherProvider.ui()) {
            val tickerData = travelTickerUseCase.execute(TravelTickerInstanceId.FLIGHT, TravelTickerFlightPage.HOME)
            mutableTickerData.postValue(tickerData)
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

    fun generatePairOfMinAndMaxDateForDeparture(): Pair<Date, Date> {
        val minDate = FlightDateUtil.getCurrentDate()
        val maxDate = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, FlightSearchUniversalViewModel.MAX_YEAR_FOR_FLIGHT),
                Calendar.DATE,
                FlightSearchUniversalViewModel.MINUS_ONE_DAY)
        val maxDateCalendar = FlightDateUtil.getCurrentCalendar()
        maxDateCalendar.time = maxDate
        maxDateCalendar.set(Calendar.HOUR_OF_DAY, FlightSearchUniversalViewModel.DEFAULT_LAST_HOUR_IN_DAY)
        maxDateCalendar.set(Calendar.MINUTE, FlightSearchUniversalViewModel.DEFAULT_LAST_MIN)
        maxDateCalendar.set(Calendar.SECOND, FlightSearchUniversalViewModel.DEFAULT_LAST_SEC)
        return Pair(minDate, maxDateCalendar.time)
    }

    fun generatePairOfMinAndMaxDateForReturn(departureDate: Date): Pair<Date, Date> {
        val minDate = departureDate
        val maxDate = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, FlightSearchUniversalViewModel.MAX_YEAR_FOR_FLIGHT),
                Calendar.DATE,
                FlightSearchUniversalViewModel.MINUS_ONE_DAY)
        val maxDateCalendar = FlightDateUtil.getCurrentCalendar()
        maxDateCalendar.time = maxDate
        maxDateCalendar.set(Calendar.HOUR_OF_DAY, FlightSearchUniversalViewModel.DEFAULT_LAST_HOUR_IN_DAY)
        maxDateCalendar.set(Calendar.MINUTE, FlightSearchUniversalViewModel.DEFAULT_LAST_MIN)
        maxDateCalendar.set(Calendar.SECOND, FlightSearchUniversalViewModel.DEFAULT_LAST_SEC)
        return Pair(minDate, maxDateCalendar.time)
    }

    fun validateDepartureDate(departureDate: Date): Int {
        var resultStringResourceId = -1
        val oneYears = FlightDateUtil.addTimeToSpesificDate(FlightDateUtil.addTimeToCurrentDate(
                Calendar.YEAR, MAX_YEAR_FOR_FLIGHT), Calendar.DATE, -1)

        if (departureDate.after(oneYears)) {
            resultStringResourceId = R.string.flight_dashboard_departure_max_one_years_from_today_error
        } else if (departureDate.before(FlightDateUtil.getCurrentDate())) {
            resultStringResourceId = R.string.flight_dashboard_departure_should_atleast_today_error
        }

        return resultStringResourceId
    }

    fun validateReturnDate(departureDate: Date, returnDate: Date): Int {
        var resultStringResourceId = -1

        var oneYears = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, FlightSearchUniversalViewModel.MAX_YEAR_FOR_FLIGHT),
                Calendar.DATE, -1)

        if (returnDate.after(oneYears)) {
            resultStringResourceId = R.string.flight_dashboard_return_max_one_years_from_today_error
        } else if (returnDate.before(departureDate)) {
            resultStringResourceId = R.string.flight_dashboard_return_should_greater_equal_error
        }

        return resultStringResourceId
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

    companion object {
        const val MAX_YEAR_FOR_FLIGHT = 1
        const val MINUS_ONE_DAY = -1
        const val DEFAULT_LAST_HOUR_IN_DAY = 23
        const val DEFAULT_LAST_MIN = 59
        const val DEFAULT_LAST_SEC = 59
    }

}