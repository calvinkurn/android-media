package com.tokopedia.flight.homepage.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.travel.constant.TravelType
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.common.travel.presentation.model.TravelVideoBannerModel
import com.tokopedia.common.travel.ticker.TravelTickerFlightPage
import com.tokopedia.common.travel.ticker.TravelTickerInstanceId
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.presentation.model.FlightAirportModel
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.homepage.data.cache.FlightDashboardCache
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.flight.homepage.presentation.model.FlightHomepageModel
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import com.tokopedia.flight.homepage.presentation.validator.FlightSelectPassengerValidator
import com.tokopedia.flight.search.domain.FlightSearchDeleteAllDataUseCase
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search_universal.presentation.viewmodel.FlightSearchUniversalViewModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
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
        private val dashboardCache: FlightDashboardCache,
        private val deleteAllFlightSearchDataUseCase: FlightSearchDeleteAllDataUseCase,
        private val passengerValidator: FlightSelectPassengerValidator,
        private val userSessionInterface: UserSessionInterface,
        private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {

    private val mutableBannerList = MutableLiveData<Result<TravelCollectiveBannerModel>>()
    val bannerList: LiveData<Result<TravelCollectiveBannerModel>>
        get() = mutableBannerList

    private val mutableVideoBanner = MutableLiveData<Result<TravelCollectiveBannerModel>>()
    val videoBanner: LiveData<Result<TravelCollectiveBannerModel>>
        get() = mutableVideoBanner

    private val mutableDashboardData = MutableLiveData<FlightHomepageModel>()
    val homepageData: LiveData<FlightHomepageModel>
        get() = mutableDashboardData

    private val mutableTickerData = MutableLiveData<Result<TravelTickerModel>>()
    val tickerData: LiveData<Result<TravelTickerModel>>
        get() = mutableTickerData

    private val mutableAutoSearch = MutableLiveData<Boolean>()
    val autoSearch: LiveData<Boolean>
        get() = mutableAutoSearch

    fun init() {
        mutableDashboardData.postValue(FlightHomepageModel())
        mutableAutoSearch.postValue (false)
    }

    fun fetchBannerData(isFromCloud: Boolean) {
        launch(dispatcherProvider.main) {
            val bannerList = getTravelCollectiveBannerUseCase.execute(TravelType.FLIGHT, isFromCloud)
            mutableBannerList.postValue(bannerList)
        }
    }

    fun fetchTickerData() {
        launch(dispatcherProvider.main) {
            val tickerData = travelTickerUseCase.execute(TravelTickerInstanceId.FLIGHT, TravelTickerFlightPage.HOME)
            mutableTickerData.postValue(tickerData)
        }
    }

    fun fetchVideoBannerData() {
        launch(dispatcherProvider.main) {
            val bannerList = getTravelCollectiveBannerUseCase.execute(TravelType.FLIGHT_VIDEO_BANNER, true)
            mutableVideoBanner.postValue(bannerList)
        }
    }

    /**
     * search applink format example :
     * tokopedia://pesawat/search?dest=CGK_Jakarta_DPS_Denpasar_2020-11-11,CGK_Jakarta_DPS_Denpasar_2020-12-11&a=3&c=2&i=1&s=1&auto_search=0
     */
    fun setupApplinkParams(extrasTrip: String, extrasAdult: String, extrasChild: String,
                           extrasInfant: String, extrasClass: String, extrasAutoSearch: String): Int {
        var errorStringResourceId = -1
        try {
            // transform trip extras
            val tempExtras = extrasTrip.split(",")
            val extrasTripDeparture = tempExtras[INDEX_DEPARTURE_TRIP].split("_")

            dashboardCache.putDepartureAirport(extrasTripDeparture[INDEX_ID_AIRPORT_DEPARTURE_TRIP])
            dashboardCache.putDepartureCityName(extrasTripDeparture[INDEX_NAME_CITY_DEPARTURE_TRIP].replace("%20", " "))
            dashboardCache.putDepartureCityCode("")
            dashboardCache.putArrivalAirport(extrasTripDeparture[INDEX_ID_AIRPORT_ARRIVAL_TRIP])
            dashboardCache.putArrivalCityName(extrasTripDeparture[INDEX_NAME_CITY_ARRIVAL_TRIP].replace("%20", " "))
            dashboardCache.putRoundTrip(false)
            if (extrasTripDeparture[INDEX_DATE_TRIP].isNotEmpty()) {
                dashboardCache.putDepartureDate(extrasTripDeparture[INDEX_DATE_TRIP])
            }
            dashboardCache.putReturnDate("")

            // if applink params is roundtrip
            if (tempExtras.size > 1) {
                val extrasTripReturn = tempExtras[INDEX_RETURN_TRIP].split("_")
                dashboardCache.putRoundTrip(true)
                if (extrasTripReturn[INDEX_DATE_TRIP].isNotEmpty()) {
                    dashboardCache.putReturnDate(extrasTripReturn[INDEX_DATE_TRIP])
                }
            }

            // transform passenger
            if (!passengerValidator.validateInfantNotGreaterThanAdult(extrasAdult.toInt(), extrasInfant.toInt())) {
                errorStringResourceId = R.string.select_passenger_infant_greater_than_adult_error_message
            } else if (!passengerValidator.validateTotalPassenger(extrasAdult.toInt(), extrasChild.toInt())) {
                errorStringResourceId = R.string.select_passenger_total_passenger_error_message
            } else {
                dashboardCache.putPassengerCount(extrasAdult.toInt(), extrasChild.toInt(), extrasInfant.toInt())
            }

            // transform class
            dashboardCache.putClassCache(extrasClass.toInt())

            if (extrasAutoSearch.toInt() == 1) {
                mutableAutoSearch.postValue(true)
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return errorStringResourceId
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
        flightAnalytics.eventPromotionClick(position + 1, banner,
                FlightAnalytics.Screen.HOMEPAGE,
                if (userSessionInterface.isLoggedIn) userSessionInterface.userId else "")
    }

    fun onDepartureAirportChanged(departureAirport: FlightAirportModel) {
        flightAnalytics.eventOriginClick(departureAirport.cityName, departureAirport.airportCode)
        homepageData.value?.let {
            val newDashboardData = cloneViewModel(it)
            newDashboardData.departureAirport = departureAirport
            mutableDashboardData.postValue(newDashboardData)
        }
    }

    fun onArrivalAirportChanged(arrivalAirport: FlightAirportModel) {
        flightAnalytics.eventDestinationClick(arrivalAirport.cityName, arrivalAirport.airportCode)
        homepageData.value?.let {
            val newDashboardData = cloneViewModel(it)
            newDashboardData.arrivalAirport = arrivalAirport
            mutableDashboardData.postValue(newDashboardData)
        }
    }

    fun onClassChanged(classModel: FlightClassModel) {
        flightAnalytics.eventClassClick(classModel.title)
        homepageData.value?.let {
            val newDashboardData = cloneViewModel(it)
            newDashboardData.flightClass = classModel
            mutableDashboardData.postValue(newDashboardData)
        }
    }

    fun onPassengerChanged(passengerModel: FlightPassengerModel) {
        flightAnalytics.eventPassengerClick(passengerModel.adult, passengerModel.children, passengerModel.infant)
        homepageData.value?.let {
            val newDashboardData = cloneViewModel(it)
            newDashboardData.flightPassengerViewModel = passengerModel
            mutableDashboardData.postValue(newDashboardData)
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
        val oneYears = FlightDateUtil.removeTime(FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, MAX_YEAR_FOR_FLIGHT),
                Calendar.DATE, MINUS_ONE_DAY))

        if (departureDate.after(oneYears)) {
            resultStringResourceId = R.string.flight_dashboard_departure_max_one_years_from_today_error
        } else if (departureDate.before(FlightDateUtil.removeTime(FlightDateUtil.getCurrentDate()))) {
            resultStringResourceId = R.string.flight_dashboard_departure_should_atleast_today_error
        }

        return resultStringResourceId
    }

    fun validateReturnDate(departureDate: Date, returnDate: Date): Int {
        var resultStringResourceId = -1

        val oneYears = FlightDateUtil.removeTime(FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, MAX_YEAR_FOR_FLIGHT),
                Calendar.DATE, MINUS_ONE_DAY))

        if (returnDate.after(oneYears)) {
            resultStringResourceId = R.string.flight_dashboard_return_max_one_years_from_today_error
        } else if (returnDate.before(FlightDateUtil.removeTime(departureDate))) {
            resultStringResourceId = R.string.flight_dashboard_return_should_greater_equal_error
        }

        return resultStringResourceId
    }

    fun onSearchTicket(flightSearchData: FlightSearchPassDataModel) {
        launch(dispatcherProvider.main) {
            flightAnalytics.eventSearchClick(mapSearchPassDataToDashboardModel(flightSearchData),
                    FlightAnalytics.Screen.HOMEPAGE,
                    if (userSessionInterface.isLoggedIn) userSessionInterface.userId else "")
            deleteAllFlightSearchDataUseCase.execute()
        }
    }

    fun sendTrackingOpenScreen(screenName: String) {
        flightAnalytics.eventOpenScreen(screenName)
    }

    fun sendTrackingRoundTripSwitchChanged(tripType: String) {
        flightAnalytics.eventTripTypeClick(tripType)
    }

    fun sendTrackingPromoScrolled(position: Int) {
        getBannerData(position)?.let {
            flightAnalytics.eventPromoImpression(position, it, FlightAnalytics.Screen.HOMEPAGE,
                    if (userSessionInterface.isLoggedIn) userSessionInterface.userId else "")
        }
    }

    private fun mapSearchPassDataToDashboardModel(flightSearchData: FlightSearchPassDataModel): FlightHomepageModel {
        val dashboardModel = FlightHomepageModel()

        dashboardModel.departureAirport = flightSearchData.departureAirport
        dashboardModel.arrivalAirport = flightSearchData.arrivalAirport
        dashboardModel.isOneWay = flightSearchData.isOneWay
        dashboardModel.flightPassengerViewModel = flightSearchData.flightPassengerModel
        dashboardModel.flightClass = flightSearchData.flightClass
        dashboardModel.departureDate = flightSearchData.departureDate
        dashboardModel.returnDate = flightSearchData.returnDate

        return dashboardModel
    }

    private fun cloneViewModel(currentHomepageData: FlightHomepageModel): FlightHomepageModel =
            currentHomepageData.clone() as FlightHomepageModel

    fun sendTrackingVideoBannerImpression(travelVideoBannerModel: TravelVideoBannerModel){
        flightAnalytics.eventVideoBannerImpression(travelVideoBannerModel, FlightAnalytics.Screen.HOMEPAGE,
                if (userSessionInterface.isLoggedIn) userSessionInterface.userId else "")
    }

    fun sendTrackingVideoBannerClick(travelVideoBannerModel: TravelVideoBannerModel){
        flightAnalytics.eventVideoBannerClick(travelVideoBannerModel, FlightAnalytics.Screen.HOMEPAGE,
                if (userSessionInterface.isLoggedIn) userSessionInterface.userId else "")
    }

    companion object {
        private const val MAX_YEAR_FOR_FLIGHT = 1
        private const val MINUS_ONE_DAY = -1

        // applink params index
        private const val INDEX_DEPARTURE_TRIP: Int = 0
        private const val INDEX_RETURN_TRIP = 1
        private const val INDEX_ID_AIRPORT_DEPARTURE_TRIP = 0
        private const val INDEX_NAME_CITY_DEPARTURE_TRIP = 1
        private const val INDEX_ID_AIRPORT_ARRIVAL_TRIP = 2
        private const val INDEX_NAME_CITY_ARRIVAL_TRIP = 3
        private const val INDEX_DATE_TRIP = 4
    }

}