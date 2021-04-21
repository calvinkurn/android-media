package com.tokopedia.flight.homepage.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.common.travel.presentation.model.TravelVideoBannerModel
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.dummy.BANNER_DATA
import com.tokopedia.flight.dummy.TICKER_DATA
import com.tokopedia.flight.homepage.data.cache.FlightDashboardCache
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import com.tokopedia.flight.homepage.presentation.validator.FlightSelectPassengerValidator
import com.tokopedia.flight.searchV4.domain.FlightSearchDeleteAllDataUseCase
import com.tokopedia.flight.searchV4.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search_universal.presentation.viewmodel.FlightSearchUniversalViewModel
import com.tokopedia.flight.shouldBe
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

/**
 * @author by furqan on 08/05/2020
 */
class FlightHomepageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcherProvider = CoroutineTestDispatchersProvider

    private val travelTickerUseCase = mockk<TravelTickerCoroutineUseCase>()
    private val travelCollectiveBannerUseCase = mockk<GetTravelCollectiveBannerUseCase>()
    private val userSessionInterface = mockk<UserSession>()

    @RelaxedMockK
    private lateinit var dashboardCache: FlightDashboardCache

    @RelaxedMockK
    private lateinit var flightAnalytics: FlightAnalytics

    @RelaxedMockK
    private lateinit var deleteAllFlightSearch: FlightSearchDeleteAllDataUseCase

    private val passengerValidator = FlightSelectPassengerValidator()
    private lateinit var flightHomepageViewModel: FlightHomepageViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        flightHomepageViewModel = FlightHomepageViewModel(flightAnalytics,
                travelTickerUseCase, travelCollectiveBannerUseCase, dashboardCache,
                deleteAllFlightSearch, passengerValidator, userSessionInterface,
                testDispatcherProvider)
        flightHomepageViewModel.init()
    }

    @Test
    fun fetchVideoBannerData_returnEmptyData_videoBannerSizeShouldBeEmpty() {
        // given
        coEvery {
            travelCollectiveBannerUseCase.execute(any(), any())
        } returns Success(TravelCollectiveBannerModel())

        // when
        flightHomepageViewModel.fetchVideoBannerData()

        // then
        assert(flightHomepageViewModel.videoBanner.value is Success<TravelCollectiveBannerModel>)
        val bannerData = (flightHomepageViewModel.videoBanner.value as Success<TravelCollectiveBannerModel>).data

        bannerData.banners.size shouldBe 0
    }

    @Test
    fun fetchVideoBannerData_returnListBanner_bannerSizeShouldBeSameAsData() {
        // given
        coEvery {
            travelCollectiveBannerUseCase.execute(any(), any())
        } returns Success(BANNER_DATA)

        // when
        flightHomepageViewModel.fetchVideoBannerData()

        // then
        assert(flightHomepageViewModel.videoBanner.value is Success<TravelCollectiveBannerModel>)
        val bannerData = (flightHomepageViewModel.videoBanner.value as Success<TravelCollectiveBannerModel>).data

        bannerData.banners.size shouldBe BANNER_DATA.banners.size
        for ((index, banner) in bannerData.banners.withIndex()) {
            banner.id shouldBe BANNER_DATA.banners[index].id
            banner.product shouldBe BANNER_DATA.banners[index].product
            banner.attribute.appUrl shouldBe BANNER_DATA.banners[index].attribute.appUrl
            banner.attribute.imageUrl shouldBe BANNER_DATA.banners[index].attribute.imageUrl
            banner.attribute.description shouldBe BANNER_DATA.banners[index].attribute.description
            banner.attribute.promoCode shouldBe BANNER_DATA.banners[index].attribute.promoCode
            banner.attribute.webUrl shouldBe BANNER_DATA.banners[index].attribute.webUrl
        }
    }

    @Test
    fun fetchVideoBannerData_returnFail_bannerValueShouldBeFailed() {
        // given
        coEvery {
            travelCollectiveBannerUseCase.execute(any(), any())
        } returns Fail(Throwable())

        // when
        flightHomepageViewModel.fetchVideoBannerData()

        // then
        assert(flightHomepageViewModel.videoBanner.value is Fail)
    }

    @Test
    fun fetchBannerData_returnEmptyData_bannerSizeShouldBeEmpty() {
        // given
        coEvery {
            travelCollectiveBannerUseCase.execute(any(), any())
        } returns Success(TravelCollectiveBannerModel())

        // when
        flightHomepageViewModel.fetchBannerData(true)

        // then
        assert(flightHomepageViewModel.bannerList.value is Success<TravelCollectiveBannerModel>)
        val bannerData = (flightHomepageViewModel.bannerList.value as Success<TravelCollectiveBannerModel>).data

        bannerData.banners.size shouldBe 0
    }

    @Test
    fun fetchBannerData_returnListBanner_bannerSizeShouldBeSameAsData() {
        // given
        coEvery {
            travelCollectiveBannerUseCase.execute(any(), any())
        } returns Success(BANNER_DATA)

        // when
        flightHomepageViewModel.fetchBannerData(true)

        // then
        assert(flightHomepageViewModel.bannerList.value is Success<TravelCollectiveBannerModel>)
        val bannerData = (flightHomepageViewModel.bannerList.value as Success<TravelCollectiveBannerModel>).data

        bannerData.banners.size shouldBe BANNER_DATA.banners.size
        for ((index, banner) in bannerData.banners.withIndex()) {
            banner.id shouldBe BANNER_DATA.banners[index].id
            banner.product shouldBe BANNER_DATA.banners[index].product
            banner.attribute.appUrl shouldBe BANNER_DATA.banners[index].attribute.appUrl
            banner.attribute.imageUrl shouldBe BANNER_DATA.banners[index].attribute.imageUrl
            banner.attribute.description shouldBe BANNER_DATA.banners[index].attribute.description
            banner.attribute.promoCode shouldBe BANNER_DATA.banners[index].attribute.promoCode
            banner.attribute.webUrl shouldBe BANNER_DATA.banners[index].attribute.webUrl
        }
    }

    @Test
    fun fetchBannerData_returnFail_bannerValueShouldBeFailed() {
        // given
        coEvery {
            travelCollectiveBannerUseCase.execute(any(), any())
        } returns Fail(Throwable())

        // when
        flightHomepageViewModel.fetchBannerData(true)

        // then
        assert(flightHomepageViewModel.bannerList.value is Fail)
    }

    @Test
    fun fetchTickerData_successFetchData_tickerDataShouldBeSameAsData() {
        // given
        coEvery {
            travelTickerUseCase.execute(any(), any())
        } returns Success(TICKER_DATA)

        // when
        flightHomepageViewModel.fetchTickerData()

        // then
        assert(flightHomepageViewModel.tickerData.value is Success<TravelTickerModel>)
        val tickerData = (flightHomepageViewModel.tickerData.value as Success<TravelTickerModel>).data

        tickerData.title shouldBe TICKER_DATA.title
        tickerData.message shouldBe TICKER_DATA.message
        tickerData.status shouldBe TICKER_DATA.status
        tickerData.startTime shouldBe TICKER_DATA.startTime
        tickerData.endTime shouldBe TICKER_DATA.endTime
        tickerData.page shouldBe TICKER_DATA.page
        tickerData.isPeriod shouldBe TICKER_DATA.isPeriod
        tickerData.instances shouldBe TICKER_DATA.instances
        tickerData.type shouldBe TICKER_DATA.type
        tickerData.url shouldBe TICKER_DATA.url
    }

    @Test
    fun fetchTickerData_returnFail_tickerValueShouldBeFailed() {
        // given
        coEvery {
            travelTickerUseCase.execute(any(), any())
        } returns Fail(Throwable())

        // when
        flightHomepageViewModel.fetchTickerData()

        // then
        assert(flightHomepageViewModel.tickerData.value is Fail)
    }

    @Test
    fun setupApplinkParams_oneWayTrip_successValidationAndAutoSearch() {
        // given
        val extrasTrip = "CGK_Jakarta_DPS_Denpasar_2020-11-11"
        val extrasAdult = "3"
        val extrasChild = "2"
        val extrasInfant = "1"
        val extrasClass = "1"
        val extrasAutoSearch = "1"

        // when
        val errorStringResourceId = flightHomepageViewModel.setupApplinkParams(
                extrasTrip, extrasAdult, extrasChild,
                extrasInfant, extrasClass, extrasAutoSearch)

        // then
        verifySequence {
            dashboardCache.putDepartureAirport("CGK")
            dashboardCache.putDepartureCityName("Jakarta")
            dashboardCache.putDepartureCityCode("")
            dashboardCache.putArrivalAirport("DPS")
            dashboardCache.putArrivalCityName("Denpasar")
            dashboardCache.putRoundTrip(false)
            dashboardCache.putDepartureDate("2020-11-11")
            dashboardCache.putReturnDate("")
            dashboardCache.putPassengerCount(3, 2, 1)
            dashboardCache.putClassCache(1)
        }

        flightHomepageViewModel.autoSearch.value shouldBe true
        errorStringResourceId shouldBe -1
    }

    @Test
    fun setupApplinkParams_oneWayTrip_successValidationAndNotAutoSearch() {
        // given
        val extrasTrip = "CGK_Jakarta_DPS_Denpasar_2020-11-11"
        val extrasAdult = "3"
        val extrasChild = "2"
        val extrasInfant = "1"
        val extrasClass = "1"
        val extrasAutoSearch = "0"

        // when
        val errorStringResourceId = flightHomepageViewModel.setupApplinkParams(
                extrasTrip, extrasAdult, extrasChild,
                extrasInfant, extrasClass, extrasAutoSearch)

        // then
        verifySequence {
            dashboardCache.putDepartureAirport("CGK")
            dashboardCache.putDepartureCityName("Jakarta")
            dashboardCache.putDepartureCityCode("")
            dashboardCache.putArrivalAirport("DPS")
            dashboardCache.putArrivalCityName("Denpasar")
            dashboardCache.putRoundTrip(false)
            dashboardCache.putDepartureDate("2020-11-11")
            dashboardCache.putReturnDate("")
            dashboardCache.putPassengerCount(3, 2, 1)
            dashboardCache.putClassCache(1)
        }

        flightHomepageViewModel.autoSearch.value shouldBe false
        errorStringResourceId shouldBe -1
    }

    @Test
    fun setupApplinkParams_roundTrip_successValidationAndAutoSearch() {
        // given
        val extrasTrip = "CGK_Jakarta_DPS_Denpasar_2020-11-11,CGK_Jakarta_DPS_Denpasar_2020-12-11"
        val extrasAdult = "3"
        val extrasChild = "2"
        val extrasInfant = "1"
        val extrasClass = "1"
        val extrasAutoSearch = "1"

        // when
        val errorStringResourceId = flightHomepageViewModel.setupApplinkParams(
                extrasTrip, extrasAdult, extrasChild,
                extrasInfant, extrasClass, extrasAutoSearch)

        // then
        verifySequence {
            dashboardCache.putDepartureAirport("CGK")
            dashboardCache.putDepartureCityName("Jakarta")
            dashboardCache.putDepartureCityCode("")
            dashboardCache.putArrivalAirport("DPS")
            dashboardCache.putArrivalCityName("Denpasar")
            dashboardCache.putRoundTrip(false)
            dashboardCache.putDepartureDate("2020-11-11")
            dashboardCache.putReturnDate("")
            dashboardCache.putRoundTrip(true)
            dashboardCache.putReturnDate("2020-12-11")
            dashboardCache.putPassengerCount(3, 2, 1)
            dashboardCache.putClassCache(1)
        }

        flightHomepageViewModel.autoSearch.value shouldBe true
        errorStringResourceId shouldBe -1
    }

    @Test
    fun setupApplinkParams_roundTrip_successValidationAndNotAutoSearch() {
        // given
        val extrasTrip = "CGK_Jakarta_DPS_Denpasar_2020-11-11,CGK_Jakarta_DPS_Denpasar_2020-12-11"
        val extrasAdult = "3"
        val extrasChild = "2"
        val extrasInfant = "1"
        val extrasClass = "1"
        val extrasAutoSearch = "0"

        // when
        val errorStringResourceId = flightHomepageViewModel.setupApplinkParams(
                extrasTrip, extrasAdult, extrasChild,
                extrasInfant, extrasClass, extrasAutoSearch)

        // then
        verifySequence {
            dashboardCache.putDepartureAirport("CGK")
            dashboardCache.putDepartureCityName("Jakarta")
            dashboardCache.putDepartureCityCode("")
            dashboardCache.putArrivalAirport("DPS")
            dashboardCache.putArrivalCityName("Denpasar")
            dashboardCache.putRoundTrip(false)
            dashboardCache.putDepartureDate("2020-11-11")
            dashboardCache.putReturnDate("")
            dashboardCache.putRoundTrip(true)
            dashboardCache.putReturnDate("2020-12-11")
            dashboardCache.putPassengerCount(3, 2, 1)
            dashboardCache.putClassCache(1)
        }

        flightHomepageViewModel.autoSearch.value shouldBe false
        errorStringResourceId shouldBe -1
    }

    @Test
    fun setupApplinkParams_roundTrip_successValidationWithoutFlightDate() {
        // given
        val extrasTrip = "CGK_Jakarta_DPS_Denpasar_,CGK_Jakarta_DPS_Denpasar_"
        val extrasAdult = "3"
        val extrasChild = "2"
        val extrasInfant = "1"
        val extrasClass = "1"
        val extrasAutoSearch = "0"

        // when
        val errorStringResourceId = flightHomepageViewModel.setupApplinkParams(
                extrasTrip, extrasAdult, extrasChild,
                extrasInfant, extrasClass, extrasAutoSearch)

        // then
        verifySequence {
            dashboardCache.putDepartureAirport("CGK")
            dashboardCache.putDepartureCityName("Jakarta")
            dashboardCache.putDepartureCityCode("")
            dashboardCache.putArrivalAirport("DPS")
            dashboardCache.putArrivalCityName("Denpasar")
            dashboardCache.putRoundTrip(false)
            dashboardCache.putReturnDate("")
            dashboardCache.putRoundTrip(true)
            dashboardCache.putPassengerCount(3, 2, 1)
            dashboardCache.putClassCache(1)
        }

        flightHomepageViewModel.autoSearch.value shouldBe false
        errorStringResourceId shouldBe -1
    }

    @Test
    fun setupApplinkParams_roundTrip_failedValidationInfantMoreThanAdultAndAutoSearch() {
        // given
        val extrasTrip = "CGK_Jakarta_DPS_Denpasar_2020-11-11,CGK_Jakarta_DPS_Denpasar_2020-12-11"
        val extrasAdult = "1"
        val extrasChild = "2"
        val extrasInfant = "3"
        val extrasClass = "1"
        val extrasAutoSearch = "1"

        // when
        val errorStringResourceId = flightHomepageViewModel.setupApplinkParams(
                extrasTrip, extrasAdult, extrasChild,
                extrasInfant, extrasClass, extrasAutoSearch)

        // then
        verifySequence {
            dashboardCache.putDepartureAirport("CGK")
            dashboardCache.putDepartureCityName("Jakarta")
            dashboardCache.putDepartureCityCode("")
            dashboardCache.putArrivalAirport("DPS")
            dashboardCache.putArrivalCityName("Denpasar")
            dashboardCache.putRoundTrip(false)
            dashboardCache.putDepartureDate("2020-11-11")
            dashboardCache.putReturnDate("")
            dashboardCache.putRoundTrip(true)
            dashboardCache.putReturnDate("2020-12-11")
            dashboardCache.putClassCache(1)
        }

        flightHomepageViewModel.autoSearch.value shouldBe true
        errorStringResourceId shouldBe R.string.select_passenger_infant_greater_than_adult_error_message
    }

    @Test
    fun setupApplinkParams_roundTrip_failedValidationTotalPassengerAndAutoSearch() {
        // given
        val extrasTrip = "CGK_Jakarta_DPS_Denpasar_2020-11-11,CGK_Jakarta_DPS_Denpasar_2020-12-11"
        val extrasAdult = "4"
        val extrasChild = "4"
        val extrasInfant = "1"
        val extrasClass = "1"
        val extrasAutoSearch = "1"

        // when
        val errorStringResourceId = flightHomepageViewModel.setupApplinkParams(
                extrasTrip, extrasAdult, extrasChild,
                extrasInfant, extrasClass, extrasAutoSearch)

        // then
        verifySequence {
            dashboardCache.putDepartureAirport("CGK")
            dashboardCache.putDepartureCityName("Jakarta")
            dashboardCache.putDepartureCityCode("")
            dashboardCache.putArrivalAirport("DPS")
            dashboardCache.putArrivalCityName("Denpasar")
            dashboardCache.putRoundTrip(false)
            dashboardCache.putDepartureDate("2020-11-11")
            dashboardCache.putReturnDate("")
            dashboardCache.putRoundTrip(true)
            dashboardCache.putReturnDate("2020-12-11")
            dashboardCache.putClassCache(1)
        }

        flightHomepageViewModel.autoSearch.value shouldBe true
        errorStringResourceId shouldBe R.string.select_passenger_total_passenger_error_message
    }

    @Test
    fun setupApplinkParams_exceptionIndexOutOfBound() {
        // given
        val extrasTrip = "DPS_Denpasar_2020-11-11"
        val extrasAdult = "4"
        val extrasChild = "4"
        val extrasInfant = "1"
        val extrasClass = "1"
        val extrasAutoSearch = "1"

        // when
        try {
            flightHomepageViewModel.setupApplinkParams(
                    extrasTrip, extrasAdult, extrasChild,
                    extrasInfant, extrasClass, extrasAutoSearch)
        } catch (t: Throwable) {

        }
    }

    @Test
    fun getBannerData_successGetBannerData() {
        // given
        coEvery {
            travelCollectiveBannerUseCase.execute(any(), any())
        } returns Success(BANNER_DATA)
        val selectedBannerData = 0

        // when
        flightHomepageViewModel.fetchBannerData(false)
        val bannerData = flightHomepageViewModel.getBannerData(selectedBannerData)

        // then
        bannerData?.id shouldBe BANNER_DATA.banners[selectedBannerData].id
        bannerData?.product shouldBe BANNER_DATA.banners[selectedBannerData].product
        bannerData?.attribute?.webUrl shouldBe BANNER_DATA.banners[selectedBannerData].attribute.webUrl
        bannerData?.attribute?.promoCode shouldBe BANNER_DATA.banners[selectedBannerData].attribute.promoCode
        bannerData?.attribute?.description shouldBe BANNER_DATA.banners[selectedBannerData].attribute.description
        bannerData?.attribute?.imageUrl shouldBe BANNER_DATA.banners[selectedBannerData].attribute.imageUrl
        bannerData?.attribute?.appUrl shouldBe BANNER_DATA.banners[selectedBannerData].attribute.appUrl
    }

    @Test
    fun getBannerData_failedGetBannerData() {
        // given
        coEvery {
            travelCollectiveBannerUseCase.execute(any(), any())
        } returns Success(BANNER_DATA)
        val selectedBannerData = 5

        // when
        flightHomepageViewModel.fetchBannerData(false)
        val bannerData = flightHomepageViewModel.getBannerData(selectedBannerData)

        // then
        bannerData shouldBe null
    }

    @Test
    fun onBannerClick_whenLoggedIn_shouldSendAnalytics() {
        // given
        val position = 0
        val bannerData = BANNER_DATA.banners[position]
        coEvery { userSessionInterface.isLoggedIn } returns true
        coEvery { userSessionInterface.userId } returns "dummy user id"

        // when
        flightHomepageViewModel.onBannerClicked(position, bannerData)

        // then
        verify {
            flightAnalytics.eventPromotionClick(position + 1,
                    bannerData,
                    FlightAnalytics.Screen.HOMEPAGE,
                    any())
        }
    }

    @Test
    fun onBannerClick_whenNotLoggedIn_shouldSendAnalytics() {
        // given
        val position = 0
        val bannerData = BANNER_DATA.banners[position]
        coEvery { userSessionInterface.isLoggedIn } returns false

        // when
        flightHomepageViewModel.onBannerClicked(position, bannerData)

        // then
        verify {
            flightAnalytics.eventPromotionClick(position + 1,
                    bannerData,
                    FlightAnalytics.Screen.HOMEPAGE,
                    any())
        }
    }

    @Test
    fun onDepartureAirportChanged_withNullDashboardData_shouldDoNothing() {
        // given
        val departureAirport = FlightAirportModel()
        departureAirport.cityName = "Banda Aceh"
        departureAirport.airportCode = "BTJ"
        departureAirport.airportName = "Bandara International Sultan Iskandar Muda"
        departureAirport.cityCode = ""
        departureAirport.cityId = ""
        departureAirport.cityAirports = arrayListOf()
        val newViewModel = FlightHomepageViewModel(flightAnalytics, travelTickerUseCase, travelCollectiveBannerUseCase,
                dashboardCache, deleteAllFlightSearch, passengerValidator, userSessionInterface,
                testDispatcherProvider)

        // when
        newViewModel.onDepartureAirportChanged(departureAirport)

        // then
        newViewModel.homepageData.value shouldBe null
    }

    @Test
    fun onDepartureAirportChanged_shouldSendAnalytics() {
        // given
        val departureAirport = FlightAirportModel()
        departureAirport.cityName = "Banda Aceh"
        departureAirport.airportCode = "BTJ"
        departureAirport.airportName = "Bandara International Sultan Iskandar Muda"
        departureAirport.cityCode = ""
        departureAirport.cityId = ""
        departureAirport.cityAirports = arrayListOf()

        // when
        flightHomepageViewModel.onDepartureAirportChanged(departureAirport)

        // then
        verify {
            flightAnalytics.eventOriginClick(departureAirport.cityName, departureAirport.airportCode)
        }
    }

    @Test
    fun onDepartureAirportChanged_changeDashboardData() {
        // given
        val departureAirport = FlightAirportModel()
        departureAirport.cityName = "Banda Aceh"
        departureAirport.airportCode = "BTJ"
        departureAirport.airportName = "Bandara International Sultan Iskandar Muda"
        departureAirport.cityCode = ""
        departureAirport.cityId = ""
        departureAirport.cityAirports = arrayListOf()

        // when
        flightHomepageViewModel.onDepartureAirportChanged(departureAirport)

        // then
        flightHomepageViewModel.homepageData.value?.departureAirport?.cityName shouldBe departureAirport.cityName
        flightHomepageViewModel.homepageData.value?.departureAirport?.airportCode shouldBe departureAirport.airportCode
        flightHomepageViewModel.homepageData.value?.departureAirport?.airportName shouldBe departureAirport.airportName
        flightHomepageViewModel.homepageData.value?.departureAirport?.cityCode shouldBe departureAirport.cityCode
        flightHomepageViewModel.homepageData.value?.departureAirport?.cityId shouldBe departureAirport.cityId
        flightHomepageViewModel.homepageData.value?.departureAirport?.cityAirports shouldBe departureAirport.cityAirports
    }

    @Test
    fun onArrivalAirportChanged_withNullDashboardData_shouldDoNothing() {
        // given
        val arrivalAirport = FlightAirportModel()
        arrivalAirport.cityName = "Banda Aceh"
        arrivalAirport.airportCode = "BTJ"
        arrivalAirport.airportName = "Bandara International Sultan Iskandar Muda"
        arrivalAirport.cityCode = ""
        arrivalAirport.cityId = ""
        arrivalAirport.cityAirports = arrayListOf()
        val newViewModel = FlightHomepageViewModel(flightAnalytics, travelTickerUseCase, travelCollectiveBannerUseCase,
                dashboardCache, deleteAllFlightSearch, passengerValidator, userSessionInterface,
                testDispatcherProvider)

        // when
        newViewModel.onArrivalAirportChanged(arrivalAirport)

        // then
        newViewModel.homepageData.value shouldBe null
    }

    @Test
    fun onArrivalAirportChanged_shouldSendAnalytics() {
        // given
        val arrivalAirport = FlightAirportModel()
        arrivalAirport.cityName = "Banda Aceh"
        arrivalAirport.airportCode = "BTJ"
        arrivalAirport.airportName = "Bandara International Sultan Iskandar Muda"
        arrivalAirport.cityCode = ""
        arrivalAirport.cityId = ""
        arrivalAirport.cityAirports = arrayListOf()

        // when
        flightHomepageViewModel.onArrivalAirportChanged(arrivalAirport)

        // then
        verify {
            flightAnalytics.eventDestinationClick(arrivalAirport.cityName, arrivalAirport.airportCode)
        }
    }

    @Test
    fun onArrivalAirportChanged_changeDashboardData() {
        // given
        val arrivalAirport = FlightAirportModel()
        arrivalAirport.cityName = "Banda Aceh"
        arrivalAirport.airportCode = "BTJ"
        arrivalAirport.airportName = "Bandara International Sultan Iskandar Muda"
        arrivalAirport.cityCode = ""
        arrivalAirport.cityId = ""
        arrivalAirport.cityAirports = arrayListOf()

        // when
        flightHomepageViewModel.onArrivalAirportChanged(arrivalAirport)

        // then
        flightHomepageViewModel.homepageData.value?.arrivalAirport?.cityName shouldBe arrivalAirport.cityName
        flightHomepageViewModel.homepageData.value?.arrivalAirport?.airportCode shouldBe arrivalAirport.airportCode
        flightHomepageViewModel.homepageData.value?.arrivalAirport?.airportName shouldBe arrivalAirport.airportName
        flightHomepageViewModel.homepageData.value?.arrivalAirport?.cityCode shouldBe arrivalAirport.cityCode
        flightHomepageViewModel.homepageData.value?.arrivalAirport?.cityId shouldBe arrivalAirport.cityId
        flightHomepageViewModel.homepageData.value?.arrivalAirport?.cityAirports shouldBe arrivalAirport.cityAirports
    }

    @Test
    fun onClassChanged_withNullDashboardData_shouldDoNothing() {
        // given
        val flightClassModel = FlightClassModel(1, "Ekonomi")
        val newViewModel = FlightHomepageViewModel(flightAnalytics, travelTickerUseCase, travelCollectiveBannerUseCase,
                dashboardCache, deleteAllFlightSearch, passengerValidator, userSessionInterface,
                testDispatcherProvider)

        // when
        newViewModel.onClassChanged(flightClassModel)

        // then
        newViewModel.homepageData.value shouldBe null
    }

    @Test
    fun onClassChanged_shouldSendAnalytics() {
        // given
        val flightClassModel = FlightClassModel(1, "Ekonomi")

        // when
        flightHomepageViewModel.onClassChanged(flightClassModel)

        // then
        verify {
            flightAnalytics.eventClassClick(flightClassModel.title)
        }
    }

    @Test
    fun onClassChanged_changeDashboardData() {
        // given
        val flightClassModel = FlightClassModel(1, "Ekonomi")

        // when
        flightHomepageViewModel.onClassChanged(flightClassModel)

        // then
        flightHomepageViewModel.homepageData.value?.flightClass?.id shouldBe flightClassModel.id
        flightHomepageViewModel.homepageData.value?.flightClass?.title shouldBe flightClassModel.title
    }

    @Test
    fun onPassengerChanged_withNullDashboardData_shouldDoNothing() {
        // given
        val passengerModel = FlightPassengerModel()
        passengerModel.adult = 3
        passengerModel.children = 2
        passengerModel.infant = 1
        val newViewModel = FlightHomepageViewModel(flightAnalytics, travelTickerUseCase, travelCollectiveBannerUseCase,
                dashboardCache, deleteAllFlightSearch, passengerValidator, userSessionInterface,
                testDispatcherProvider)

        // when
        newViewModel.onPassengerChanged(passengerModel)

        // then
        newViewModel.homepageData.value shouldBe null
    }

    @Test
    fun onPassengerChanged_shouldSendAnalytics() {
        // given
        val passengerModel = FlightPassengerModel()
        passengerModel.adult = 3
        passengerModel.children = 2
        passengerModel.infant = 1

        // when
        flightHomepageViewModel.onPassengerChanged(passengerModel)

        // then
        verify {
            flightAnalytics.eventPassengerClick(passengerModel.adult,
                    passengerModel.children,
                    passengerModel.infant)
        }
    }

    @Test
    fun onPassengerChanged_changeDashboardData() {
        // given
        val passengerModel = FlightPassengerModel()
        passengerModel.adult = 3
        passengerModel.children = 2
        passengerModel.infant = 1

        // when
        flightHomepageViewModel.onPassengerChanged(passengerModel)

        // then
        flightHomepageViewModel.homepageData.value?.flightPassengerViewModel?.adult shouldBe passengerModel.adult
        flightHomepageViewModel.homepageData.value?.flightPassengerViewModel?.children shouldBe passengerModel.children
        flightHomepageViewModel.homepageData.value?.flightPassengerViewModel?.infant shouldBe passengerModel.infant
    }

    @Test
    fun generatePairOfMinAndMaxDateForDeparture_shouldReturnPairDate() {
        // given
        val maxDateCalendar = FlightDateUtil.getCurrentCalendar()
        maxDateCalendar.time = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, FlightSearchUniversalViewModel.MAX_YEAR_FOR_FLIGHT),
                Calendar.DATE,
                FlightSearchUniversalViewModel.MINUS_ONE_DAY)
        maxDateCalendar.set(Calendar.HOUR_OF_DAY, FlightSearchUniversalViewModel.DEFAULT_LAST_HOUR_IN_DAY)
        maxDateCalendar.set(Calendar.MINUTE, FlightSearchUniversalViewModel.DEFAULT_LAST_MIN)
        maxDateCalendar.set(Calendar.SECOND, FlightSearchUniversalViewModel.DEFAULT_LAST_SEC)

        // when
        val pair = flightHomepageViewModel.generatePairOfMinAndMaxDateForDeparture()

        // then
        assert(pair.second >= maxDateCalendar.time)
    }

    @Test
    fun generatePairOfMinAndMaxDateForReturn_shouldReturnPairDate() {
        // given
        val departureDate = FlightDateUtil.getCurrentDate()

        // when
        val pair = flightHomepageViewModel.generatePairOfMinAndMaxDateForReturn(departureDate)

        // then
        pair.first.compareTo(departureDate) shouldBe 0
    }

    @Test
    fun validateDepartureDate_validDate() {
        // given
        val departureDate = FlightDateUtil.addTimeToCurrentDate(Calendar.MONTH, 1)

        // when
        val result = flightHomepageViewModel.validateDepartureDate(departureDate)

        // then
        result shouldBe -1
    }

    @Test
    fun validateDepartureDate_dateMoreThanOneYear() {
        // given
        val departureDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 2)

        // when
        val result = flightHomepageViewModel.validateDepartureDate(departureDate)

        // then
        result shouldBe R.string.flight_dashboard_departure_max_one_years_from_today_error
    }

    @Test
    fun validateDepartureDate_dateBeforeToday() {
        // given
        val departureDate = FlightDateUtil.addTimeToCurrentDate(Calendar.MONTH, -1)

        // when
        val result = flightHomepageViewModel.validateDepartureDate(departureDate)

        // then
        result shouldBe R.string.flight_dashboard_departure_should_atleast_today_error
    }

    @Test
    fun validateReturnDate_validDate() {
        // given
        val departureDate = FlightDateUtil.addTimeToCurrentDate(Calendar.MONTH, 1)
        val returnDate = FlightDateUtil.addTimeToCurrentDate(Calendar.MONTH, 2)

        // when
        val result = flightHomepageViewModel.validateReturnDate(departureDate, returnDate)

        // then
        result shouldBe -1
    }

    @Test
    fun validateReturnDate_returnDateMoreThanOneYear() {
        // given
        val departureDate = FlightDateUtil.addTimeToCurrentDate(Calendar.MONTH, 1)
        val returnDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 2)

        // when
        val result = flightHomepageViewModel.validateReturnDate(departureDate, returnDate)

        // then
        result shouldBe R.string.flight_dashboard_return_max_one_years_from_today_error
    }

    @Test
    fun validateDepartureDate_returnDateBeforeDepartureDate() {
        // given
        val departureDate = FlightDateUtil.addTimeToCurrentDate(Calendar.MONTH, 3)
        val returnDate = FlightDateUtil.addTimeToCurrentDate(Calendar.MONTH, 2)

        // when
        val result = flightHomepageViewModel.validateReturnDate(departureDate, returnDate)

        // then
        result shouldBe R.string.flight_dashboard_return_should_greater_equal_error
    }

    @Test
    fun onSearchTicket_whenLoggedIn_shouldSendAnalyticsAndDeleteSearchData() {
        // given
        val flightSearchData = FlightSearchPassDataModel(
                "2020-02-01", "2020-03-01", false,
                FlightPassengerModel(3, 2, 1),
                FlightAirportModel().apply {
                    cityCode = ""
                    cityName = "Banda Aceh"
                    cityId = ""
                    cityAirports = arrayListOf()
                    airportCode = "BTJ"
                    airportName = "Bandara International Sultan Iskandar Muda"
                },
                FlightAirportModel().apply {
                    cityCode = "JKTA"
                    cityName = "Jakarta"
                    cityId = ""
                    cityAirports = arrayListOf("CGK", "HLP")
                    airportCode = ""
                    airportName = ""
                },
                FlightClassModel(1, "Ekonomi"),
                "", ""
        )
        coEvery { userSessionInterface.isLoggedIn } returns true
        coEvery { userSessionInterface.userId } returns "dummy user id"

        // when
        flightHomepageViewModel.onSearchTicket(flightSearchData)

        // then
        coVerifySequence {
            flightAnalytics.eventSearchClick(any(), FlightAnalytics.Screen.HOMEPAGE, any())
            deleteAllFlightSearch.execute()
        }
    }

    @Test
    fun onSearchTicket_whenNotLoggedIn_shouldSendAnalyticsAndDeleteSearchData() {
        // given
        val flightSearchData = FlightSearchPassDataModel(
                "2020-02-01", "2020-03-01", false,
                FlightPassengerModel(3, 2, 1),
                FlightAirportModel().apply {
                    cityCode = ""
                    cityName = "Banda Aceh"
                    cityId = ""
                    cityAirports = arrayListOf()
                    airportCode = "BTJ"
                    airportName = "Bandara International Sultan Iskandar Muda"
                },
                FlightAirportModel().apply {
                    cityCode = "JKTA"
                    cityName = "Jakarta"
                    cityId = ""
                    cityAirports = arrayListOf("CGK", "HLP")
                    airportCode = ""
                    airportName = ""
                },
                FlightClassModel(1, "Ekonomi"),
                "", ""
        )
        coEvery { userSessionInterface.isLoggedIn } returns false

        // when
        flightHomepageViewModel.onSearchTicket(flightSearchData)

        // then
        coVerifySequence {
            flightAnalytics.eventSearchClick(any(), FlightAnalytics.Screen.HOMEPAGE, any())
            deleteAllFlightSearch.execute()
        }
    }

    @Test
    fun validateSendTrackingOpenScreen() {
        // given
        val screenName = "Dummy Screen"
        every { userSessionInterface.isLoggedIn } returns false

        // when
        flightHomepageViewModel.sendTrackingOpenScreen(screenName)

        // then
        verify {
            flightAnalytics.eventOpenScreen(screenName)
        }
    }

    @Test
    fun validateSendTrackingRoundTripSwitchChanged() {
        // given
        val tripType = "One Way"

        // when
        flightHomepageViewModel.sendTrackingRoundTripSwitchChanged(tripType)

        // then
        verify {
            flightAnalytics.eventTripTypeClick(tripType)
        }
    }

    @Test
    fun validateSendTrackingPromoScrolledWhenLoggedIn() {
        // given
        coEvery {
            travelCollectiveBannerUseCase.execute(any(), any())
        } returns Success(BANNER_DATA)
        val selectedBannerData = 0
        coEvery { userSessionInterface.isLoggedIn } returns true
        coEvery { userSessionInterface.userId } returns "dummy user id"

        // when
        flightHomepageViewModel.fetchBannerData(false)
        flightHomepageViewModel.sendTrackingPromoScrolled(selectedBannerData)

        // then
        verify {
            flightAnalytics.eventPromoImpression(selectedBannerData,
                    BANNER_DATA.banners[selectedBannerData],
                    FlightAnalytics.Screen.HOMEPAGE,
                    any())
        }
    }

    @Test
    fun validateSendTrackingPromoScrolledNotLoggedIn() {
        // given
        coEvery {
            travelCollectiveBannerUseCase.execute(any(), any())
        } returns Success(BANNER_DATA)
        val selectedBannerData = 0
        coEvery { userSessionInterface.isLoggedIn } returns false

        // when
        flightHomepageViewModel.fetchBannerData(false)
        flightHomepageViewModel.sendTrackingPromoScrolled(selectedBannerData)

        // then
        verify {
            flightAnalytics.eventPromoImpression(selectedBannerData,
                    BANNER_DATA.banners[selectedBannerData],
                    FlightAnalytics.Screen.HOMEPAGE,
                    any())
        }
    }

    @Test
    fun validateFailedSendTrackingPromoScrolled() {
        // given
        coEvery {
            travelCollectiveBannerUseCase.execute(any(), any())
        } returns Success(BANNER_DATA)
        val selectedBannerData = 5

        // when
        flightHomepageViewModel.fetchBannerData(false)
        flightHomepageViewModel.sendTrackingPromoScrolled(selectedBannerData)

        // then
    }

    @Test
    fun validateSendTrackingVideoBannerImpression_LoggedIn(){
        val travelVideoBannerModel = TravelVideoBannerModel(title = "Travelling aman", id = "634")
        coEvery { userSessionInterface.isLoggedIn } returns true
        coEvery { userSessionInterface.userId } returns "dummy user id"

        flightHomepageViewModel.sendTrackingVideoBannerImpression(travelVideoBannerModel)

        verify {
            flightAnalytics.eventVideoBannerImpression(travelVideoBannerModel,
                    FlightAnalytics.Screen.HOMEPAGE,
                    any())
        }
    }

    @Test
    fun validateSendTrackingVideoBannerImpression_NotLoggedIn(){
        val travelVideoBannerModel = TravelVideoBannerModel(title = "Travelling aman", id = "634")
        coEvery { userSessionInterface.isLoggedIn } returns false

        flightHomepageViewModel.sendTrackingVideoBannerImpression(travelVideoBannerModel)

        verify {
            flightAnalytics.eventVideoBannerImpression(travelVideoBannerModel,
                    FlightAnalytics.Screen.HOMEPAGE,
                    any())
        }
    }
}