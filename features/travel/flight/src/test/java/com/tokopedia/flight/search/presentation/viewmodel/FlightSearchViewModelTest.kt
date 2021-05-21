package com.tokopedia.flight.search.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.flight.airportv2.presentation.model.FlightAirportModel
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.dummy.*
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import com.tokopedia.flight.promo_chips.data.model.AirlinePrice
import com.tokopedia.flight.promo_chips.data.model.FlightLowestPrice
import com.tokopedia.flight.promo_chips.domain.FlightLowestPriceUseCase
import com.tokopedia.flight.search.data.FlightSearchThrowable
import com.tokopedia.flight.search.data.cloud.single.FlightSearchErrorEntity
import com.tokopedia.flight.search.domain.*
import com.tokopedia.flight.search.presentation.model.*
import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.util.FlightSearchCache
import com.tokopedia.flight.shouldBe
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 13/05/2020
 */
class FlightSearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcherProvider = CoroutineTestDispatchersProvider

    @RelaxedMockK
    private lateinit var flightAnalytics: FlightAnalytics
    @RelaxedMockK
    private lateinit var flightSearchDeleteAllDataUseCase: FlightSearchDeleteAllDataUseCase
    @RelaxedMockK
    private lateinit var flightSearchCombineUseCase: FlightSearchCombineUseCase
    @RelaxedMockK
    private lateinit var flightSortAndFilterUseCase: FlightSortAndFilterUseCase
    @RelaxedMockK
    private lateinit var flightSearchUseCase: FlightSearchUseCase
    @RelaxedMockK
    private lateinit var flightSearchDeleteReturnDataUseCase: FlightSearchDeleteReturnDataUseCase
    @RelaxedMockK
    private lateinit var flightSearchStatisticUseCase: FlightSearchStatisticsUseCase
    @RelaxedMockK
    private lateinit var flightLowestPriceUseCase: FlightLowestPriceUseCase
    @RelaxedMockK
    private lateinit var flightSearchCache: FlightSearchCache
    private val userSession = mockk<UserSessionInterface>()

    private val flightSearchJourneyByIdUseCase = mockk<FlightSearchJouneyByIdUseCase>()
    private val travelTickerUseCase = mockk<TravelTickerCoroutineUseCase>()

    private lateinit var flightSearchViewModel: FlightSearchViewModel

    private val defaultSearchData = FlightSearchPassDataModel(
            "2020-11-11", "", true, FlightPassengerModel(3, 2, 1),
            FlightAirportModel().apply {
                cityName = "Jakarta"
                cityId = ""
                cityCode = "JKTA"
                cityAirports = arrayListOf("CGK", "HLP")
                airportName = ""
                airportCode = ""
            },
            FlightAirportModel().apply {
                cityName = "Banda Aceh"
                cityId = ""
                cityCode = ""
                cityAirports = arrayListOf()
                airportName = "Bandara International Sultan Iskandar Muda"
                airportCode = "BTJ"
            },
            FlightClassModel(1, "Ekonomi"),
            "", "")
    private val defaultFilterModel = FlightFilterModel()
    private val defaultAirportCombineModel = FlightAirportCombineModel("JKTA", "BTJ")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { travelTickerUseCase.execute(any(), any()) } returns Success(SEARCH_TICKER_DATA)

        flightSearchViewModel = FlightSearchViewModel(
                flightSearchUseCase,
                flightSortAndFilterUseCase,
                flightSearchDeleteAllDataUseCase,
                flightSearchDeleteReturnDataUseCase,
                flightSearchJourneyByIdUseCase,
                flightSearchCombineUseCase,
                travelTickerUseCase,
                flightSearchStatisticUseCase,
                flightLowestPriceUseCase,
                flightAnalytics,
                flightSearchCache,
                userSession,
                testDispatcherProvider)
        flightSearchViewModel.flightSearchPassData = defaultSearchData
        flightSearchViewModel.filterModel = defaultFilterModel
        flightSearchViewModel.flightAirportCombine = defaultAirportCombineModel
    }

    @Test
    fun fetchTickerDataOnInitViewModel_tickerDataShouldBeSameAsResponse() {
        // given
        coEvery { travelTickerUseCase.execute(any(), any()) } returns Success(SEARCH_TICKER_DATA)

        // when
        val newFlightSearchViewModel = FlightSearchViewModel(
                flightSearchUseCase,
                flightSortAndFilterUseCase,
                flightSearchDeleteAllDataUseCase,
                flightSearchDeleteReturnDataUseCase,
                flightSearchJourneyByIdUseCase,
                flightSearchCombineUseCase,
                travelTickerUseCase,
                flightSearchStatisticUseCase,
                flightLowestPriceUseCase,
                flightAnalytics,
                flightSearchCache,
                mockk(),
                testDispatcherProvider)
        newFlightSearchViewModel.selectedSortOption = TravelSortOption.CHEAPEST

        // then
        assert(newFlightSearchViewModel.tickerData.value is Success<TravelTickerModel>)
        val tickerData = (newFlightSearchViewModel.tickerData.value as Success<TravelTickerModel>).data

        tickerData.type shouldBe SEARCH_TICKER_DATA.type
        tickerData.instances shouldBe SEARCH_TICKER_DATA.instances
        tickerData.url shouldBe SEARCH_TICKER_DATA.url
        tickerData.title shouldBe SEARCH_TICKER_DATA.title
        tickerData.message shouldBe SEARCH_TICKER_DATA.message
        tickerData.status shouldBe SEARCH_TICKER_DATA.status
        tickerData.startTime shouldBe SEARCH_TICKER_DATA.startTime
        tickerData.endTime shouldBe SEARCH_TICKER_DATA.endTime
        tickerData.page shouldBe SEARCH_TICKER_DATA.page
        tickerData.isPeriod shouldBe SEARCH_TICKER_DATA.isPeriod
    }

    @Test
    fun initializeWithNeedDeleteDataOnOneWayAndNotReturnTrip_shouldDeleteAllSearchData() {
        // given
        val needDeleteData = true
        val isReturnTrip = false

        // when
        flightSearchViewModel.initialize(needDeleteData, isReturnTrip)

        // then
        coVerify {
            flightSearchDeleteAllDataUseCase.execute()
        }
    }

    @Test
    fun initializeWithNeedDeleteDataOnRoundTripAndNotReturnTrip_shouldDeleteSearchData() {
        // given
        val needDeleteData = true
        val isReturnTrip = false
        flightSearchViewModel.flightSearchPassData.isOneWay = false

        coEvery { flightSearchCombineUseCase.execute(any()) } returns true
        coEvery { flightSortAndFilterUseCase.execute(any(), any()) } returns arrayListOf()
        coEvery { flightSearchUseCase.execute(any(), any(), any(), any()) } returns
                FlightSearchMetaModel("JKTA", "BTJ", "2020-11-11",
                        false, 2, 11, arrayListOf(),
                        "", "", 1800)

        // when
        flightSearchViewModel.initialize(needDeleteData, isReturnTrip)

        // then
        coVerifySequence {
            flightSearchDeleteAllDataUseCase.execute()
            flightSearchCombineUseCase.execute(any())
            flightSortAndFilterUseCase.execute(any(), any())
            flightSearchUseCase.execute(any(), any(), any(), any())
        }
        flightSearchViewModel.isCombineDone shouldBe true
        flightSearchViewModel.priceFilterStatistic.first shouldBe 0
        flightSearchViewModel.priceFilterStatistic.second shouldBe Int.MAX_VALUE
    }

    @Test
    fun initializeWithNeedDeleteDataOnRoundTripAndNotReturnTripAirportToCity_shouldDeleteSearchData() {
        // given
        val needDeleteData = true
        val isReturnTrip = false
        flightSearchViewModel.flightSearchPassData = FlightSearchPassDataModel(
                "2020-11-11", "2020-12-11", false,
                FlightPassengerModel(3, 2, 1),
                FlightAirportModel().apply {
                    cityName = "Banda Aceh"
                    cityId = ""
                    cityCode = ""
                    cityAirports = arrayListOf()
                    airportName = "Bandara International Sultan Iskandar Muda"
                    airportCode = "BTJ"
                },
                FlightAirportModel().apply {
                    cityName = "Jakarta"
                    cityId = ""
                    cityCode = "JKTA"
                    cityAirports = arrayListOf("CGK", "HLP")
                    airportName = ""
                    airportCode = ""
                },
                FlightClassModel(1, "Ekonomi"),
                "tokopedia-android-internal://dummy", "")

        coEvery { flightSearchCombineUseCase.execute(any()) } returns true
        coEvery { flightSortAndFilterUseCase.execute(any(), any()) } returns arrayListOf()
        coEvery { flightSearchUseCase.execute(any(), any(), any(), any()) } returns
                FlightSearchMetaModel("JKTA", "BTJ", "2020-11-11",
                        false, 2, 11, arrayListOf(),
                        "", "", 1800)

        // when
        flightSearchViewModel.initialize(needDeleteData, isReturnTrip)

        // then
        coVerifySequence {
            flightSearchDeleteAllDataUseCase.execute()
            flightSearchCombineUseCase.execute(any())
            flightSortAndFilterUseCase.execute(any(), any())
            flightSearchUseCase.execute(any(), any(), any(), any())
        }

    }

    @Test
    fun initializeWithNeedDeleteDataOnRoundTripAndReturnTrip_shouldDeleteReturnSearchData() {
        // given
        val needDeleteData = true
        val isReturnTrip = true
        flightSearchViewModel.flightSearchPassData.isOneWay = false
        flightSearchViewModel.isCombineDone = true

        // when
        flightSearchViewModel.initialize(needDeleteData, isReturnTrip)

        // then
        coVerifySequence {
            flightSearchDeleteReturnDataUseCase.execute()
        }
    }

    @Test
    fun initializeWithoutNeedDeleteDataOnRoundTripAndReturnTrip_shouldDeleteReturnSearchData() {
        // given
        val needDeleteData = false
        val isReturnTrip = true
        flightSearchViewModel.flightSearchPassData.isOneWay = false
        flightSearchViewModel.flightSearchPassData.searchRequestId

        coEvery { flightSearchCombineUseCase.execute(any()) } returns true
        coEvery { flightSortAndFilterUseCase.execute(any(), any()) } returns arrayListOf()
        coEvery { flightSearchUseCase.execute(any(), any(), any(), any()) } returns
                FlightSearchMetaModel("JKTA", "BTJ", "2020-11-11",
                        false, 2, 11, arrayListOf(),
                        "", "", 1800)

        // when
        flightSearchViewModel.initialize(needDeleteData, isReturnTrip)

        // then
        coVerifySequence {
            flightSearchCombineUseCase.execute(any())
            flightSortAndFilterUseCase.execute(any(), any())
        }
    }

    @Test
    fun initializeWithoutNeedDeleteDataOnRoundTripWithNullSearchRequestIdAndReturnTrip_shouldDeleteReturnSearchData() {
        // given
        val needDeleteData = false
        val isReturnTrip = true
        flightSearchViewModel.flightSearchPassData.isOneWay = false
        flightSearchViewModel.flightSearchPassData.searchRequestId = ""

        coEvery { flightSearchCombineUseCase.execute(any()) } returns true
        coEvery { flightSortAndFilterUseCase.execute(any(), any()) } returns arrayListOf()
        coEvery { flightSearchUseCase.execute(any(), any(), any(), any()) } returns
                FlightSearchMetaModel("JKTA", "BTJ", "2020-11-11",
                        false, 2, 11, arrayListOf(),
                        "", "", 1800)

        // when
        flightSearchViewModel.initialize(needDeleteData, isReturnTrip)

        // then
        coVerifySequence {
            flightSearchCombineUseCase.execute(any())
            flightSortAndFilterUseCase.execute(any(), any())
        }
    }

    @Test
    fun fetchSearchDataCloud_MultipleTimes() {
        // given
        coEvery { flightSearchUseCase.execute(any(), any(), any(), any()) } returnsMany listOf(META_MODEL_NEED_REFRESH, META_MODEL_NOT_NEED_REFRESH)
        coEvery { flightSortAndFilterUseCase.execute(any(), any()) } returns JOURNEY_LIST_DATA.toList()

        // when
        flightSearchViewModel.fetchSearchDataCloud(false)

        // then
        flightSearchViewModel.flightAirportCombine.airlines.size shouldBe 3
        flightSearchViewModel.flightSearchPassData.searchRequestId shouldBe "asdasd"
        flightSearchViewModel.flightAirportCombine.isNeedRefresh shouldBe false
        flightSearchViewModel.progress.value shouldBe 100
    }

    @Test
    fun fetchSearchDataCloud_NeedRefreshButMaxRetry() {
        // given
        coEvery { flightSearchUseCase.execute(any(), any(), any(), any()) } returns META_MODEL_NEED_REFRESH_MAX_RETRY
        coEvery { flightSortAndFilterUseCase.execute(any(), any()) } returns JOURNEY_LIST_DATA.toList()
        flightSearchViewModel.flightSearchPassData.searchRequestId = ""

        // when
        flightSearchViewModel.fetchSearchDataCloud(false)

        // then
        flightSearchViewModel.flightAirportCombine.airlines.size shouldBe 2
        flightSearchViewModel.flightSearchPassData.searchRequestId shouldBe "asdasd"
        flightSearchViewModel.flightAirportCombine.isNeedRefresh shouldBe false
        flightSearchViewModel.progress.value shouldBe 100
    }

    @Test
    fun generateSearchStatistics_successGenerated_searchStatisticModelShouldBeSameWithData() {
        // given
        coEvery { flightSearchStatisticUseCase.execute(any()) } returns SEARCH_STATISTICS_DATA

        // when
        flightSearchViewModel.generateSearchStatistics()

        // then
        coVerify {
            flightSearchStatisticUseCase.execute(flightSearchViewModel.filterModel)
        }
    }

    @Test
    fun generateSearchStatistics_failedGenerated_shouldThrowErrors() {
        // given
        coEvery { flightSearchStatisticUseCase.execute(any()) } coAnswers { throw Throwable("Errors") }

        // when
        try {
            flightSearchViewModel.generateSearchStatistics()
        } catch (t: Throwable) {
        }

        // then
        coVerify {
            flightSearchStatisticUseCase.execute(any())?.wasNot(Called)
        }
    }

    @Test
    fun changeHasFilterValue_noFilter_shouldBeFalse() {
        // given

        // when
        flightSearchViewModel.changeHasFilterValue()

        // then
        flightSearchViewModel.filterModel.isHasFilter shouldBe false
        flightSearchViewModel.isInFilterMode shouldBe false
    }

    @Test
    fun changeHasFilterValue_hasFilter_shouldBeTrue() {
        // given
        coEvery { flightSearchStatisticUseCase.execute(any()) } returns SEARCH_STATISTICS_DATA
        flightSearchViewModel.filterModel.departureTimeList = arrayListOf(DepartureTimeEnum._06)

        // when
        flightSearchViewModel.changeHasFilterValue()

        // then
        flightSearchViewModel.filterModel.isHasFilter shouldBe true
        flightSearchViewModel.isInFilterMode shouldBe true
    }

    @Test
    fun setProgress_shouldChangeTheProgressValue() {
        // given
        val newProgress = 50

        // when
        flightSearchViewModel.setProgress(newProgress)

        // then
        flightSearchViewModel.progress.value shouldBe newProgress
    }

    @Test
    fun buildAirportCombineModel_cityToCity_shouldReturnCorrectAirportCombineModel() {
        // given
        val departureAirport = FlightAirportModel().apply {
            cityCode = "JKTA"
            cityName = "Jakarta"
            cityAirports = arrayListOf("CGK", "HLP")
            airportCode = ""
            airportName = ""
            cityId = ""
        }
        val arrivalAirport = FlightAirportModel().apply {
            cityCode = "TKYA"
            cityName = "Tokyo"
            cityAirports = arrayListOf("HND", "NRT")
            airportCode = ""
            airportName = ""
            cityId = ""
        }

        // when
        val airportCombineModel = flightSearchViewModel.buildAirportCombineModel(departureAirport, arrivalAirport)

        // then
        airportCombineModel.departureAirport shouldBe "JKTA"
        airportCombineModel.arrivalAirport shouldBe "TKYA"
    }

    @Test
    fun buildAirportCombineModel_cityToCityWithNullAirport_shouldReturnCorrectAirportCombineModel() {
        // given
        val departureAirport = FlightAirportModel().apply {
            cityCode = "JKTA"
            cityName = "Jakarta"
            cityAirports = arrayListOf("CGK", "HLP")
            airportCode = ""
            airportName = ""
            cityId = ""
        }
        val arrivalAirport = FlightAirportModel().apply {
            cityCode = "TKYA"
            cityName = "Tokyo"
            cityAirports = arrayListOf("HND", "NRT")
            airportCode = ""
            airportName = ""
            cityId = ""
        }

        // when
        val airportCombineModel = flightSearchViewModel.buildAirportCombineModel(departureAirport, arrivalAirport)

        // then
        airportCombineModel.departureAirport shouldBe "JKTA"
        airportCombineModel.arrivalAirport shouldBe "TKYA"
    }

    @Test
    fun buildAirportCombineModel_airportToCity_shouldReturnCorrectAirportCombineModel() {
        // given
        val departureAirport = FlightAirportModel().apply {
            cityCode = ""
            cityName = "Banda Aceh"
            cityAirports = arrayListOf()
            airportCode = "BTJ"
            airportName = "Bandara International Sultan Iskandar Muda"
            cityId = ""
        }
        val arrivalAirport = FlightAirportModel().apply {
            cityCode = "TKYA"
            cityName = "Tokyo"
            cityAirports = arrayListOf("HND", "NRT")
            airportCode = ""
            airportName = ""
            cityId = ""
        }

        // when
        val airportCombineModel = flightSearchViewModel.buildAirportCombineModel(departureAirport, arrivalAirport)

        // then
        airportCombineModel.departureAirport shouldBe "BTJ"
        airportCombineModel.arrivalAirport shouldBe "TKYA"
    }

    @Test
    fun buildAirportCombineModel_cityToAirport_shouldReturnCorrectAirportCombineModel() {
        // given
        val departureAirport = FlightAirportModel().apply {
            cityCode = "JKTA"
            cityName = "Jakarta"
            cityAirports = arrayListOf("CGK", "HLP")
            airportCode = ""
            airportName = ""
            cityId = ""
        }
        val arrivalAirport = FlightAirportModel().apply {
            cityCode = ""
            cityName = "Banda Aceh"
            cityAirports = arrayListOf()
            airportCode = "BTJ"
            airportName = "Bandara International Sultan Iskandar Muda"
            cityId = ""
        }

        // when
        val airportCombineModel = flightSearchViewModel.buildAirportCombineModel(departureAirport, arrivalAirport)

        // then
        airportCombineModel.departureAirport shouldBe "JKTA"
        airportCombineModel.arrivalAirport shouldBe "BTJ"
    }

    @Test
    fun buildAirportCombineModel_airportToAirport_shouldReturnCorrectAirportCombineModel() {
        // given
        val departureAirport = FlightAirportModel().apply {
            cityCode = ""
            cityName = "Jakarta"
            cityAirports = arrayListOf()
            airportCode = "CGK"
            airportName = "Bandara International Soekarno Hatta"
            cityId = ""
        }
        val arrivalAirport = FlightAirportModel().apply {
            cityCode = ""
            cityName = "Banda Aceh"
            cityAirports = arrayListOf()
            airportCode = "BTJ"
            airportName = "Bandara International Sultan Iskandar Muda"
            cityId = ""
        }

        // when
        val airportCombineModel = flightSearchViewModel.buildAirportCombineModel(departureAirport, arrivalAirport)

        // then
        airportCombineModel.departureAirport shouldBe "CGK"
        airportCombineModel.arrivalAirport shouldBe "BTJ"
    }

    @Test
    fun fetchSortAndFilter_successFetchData_shouldBeTheSameAsData() {
        // given
        coEvery { flightSortAndFilterUseCase.execute(any(), any()) } returns JOURNEY_LIST_DATA.toList()

        // when
        flightSearchViewModel.fetchSortAndFilter()

        // then
        flightSearchViewModel.journeyList.value is Success<List<FlightJourneyModel>>
        val journeyList = (flightSearchViewModel.journeyList.value as Success<List<FlightJourneyModel>>).data

        for ((index, journey) in journeyList.withIndex()) {
            journey.duration shouldBe JOURNEY_LIST_DATA[index].duration
            journey.totalTransit shouldBe JOURNEY_LIST_DATA[index].totalTransit
            journey.totalNumeric shouldBe JOURNEY_LIST_DATA[index].totalNumeric
            journey.term shouldBe JOURNEY_LIST_DATA[index].term
            journey.specialTagText shouldBe JOURNEY_LIST_DATA[index].specialTagText
            journey.routeList.size shouldBe JOURNEY_LIST_DATA[index].routeList.size
            journey.showSpecialPriceTag shouldBe JOURNEY_LIST_DATA[index].showSpecialPriceTag
            journey.isReturning shouldBe JOURNEY_LIST_DATA[index].isReturning
            journey.isRefundable shouldBe JOURNEY_LIST_DATA[index].isRefundable
            journey.isBestPairing shouldBe JOURNEY_LIST_DATA[index].isBestPairing
            journey.id shouldBe JOURNEY_LIST_DATA[index].id
            journey.fare.adult shouldBe JOURNEY_LIST_DATA[index].fare.adult
            journey.fare.adultCombo shouldBe JOURNEY_LIST_DATA[index].fare.adultCombo
            journey.fare.adultNumeric shouldBe JOURNEY_LIST_DATA[index].fare.adultNumeric
            journey.fare.adultNumericCombo shouldBe JOURNEY_LIST_DATA[index].fare.adultNumericCombo
            journey.fare.child shouldBe JOURNEY_LIST_DATA[index].fare.child
            journey.fare.childCombo shouldBe JOURNEY_LIST_DATA[index].fare.childCombo
            journey.fare.childNumeric shouldBe JOURNEY_LIST_DATA[index].fare.childNumeric
            journey.fare.childNumericCombo shouldBe JOURNEY_LIST_DATA[index].fare.childNumericCombo
            journey.fare.infant shouldBe JOURNEY_LIST_DATA[index].fare.infant
            journey.fare.infantCombo shouldBe JOURNEY_LIST_DATA[index].fare.infantCombo
            journey.fare.infantNumeric shouldBe JOURNEY_LIST_DATA[index].fare.infantNumeric
            journey.fare.infantNumericCombo shouldBe JOURNEY_LIST_DATA[index].fare.infantNumericCombo
            journey.durationMinute shouldBe JOURNEY_LIST_DATA[index].durationMinute
            journey.departureTimeInt shouldBe JOURNEY_LIST_DATA[index].departureTimeInt
            journey.departureAirportName shouldBe JOURNEY_LIST_DATA[index].departureAirportName
            journey.departureAirportCity shouldBe JOURNEY_LIST_DATA[index].departureAirportCity
            journey.comboPriceNumeric shouldBe JOURNEY_LIST_DATA[index].comboPriceNumeric
            journey.comboId shouldBe JOURNEY_LIST_DATA[index].comboId
            journey.beforeTotal shouldBe JOURNEY_LIST_DATA[index].beforeTotal
            journey.arrivalTimeInt shouldBe JOURNEY_LIST_DATA[index].arrivalTimeInt
            journey.arrivalAirportName shouldBe JOURNEY_LIST_DATA[index].arrivalAirportName
            journey.arrivalAirportCity shouldBe JOURNEY_LIST_DATA[index].arrivalAirportCity
            journey.airlineDataList.size shouldBe JOURNEY_LIST_DATA[index].airlineDataList.size
            journey.addDayArrival shouldBe JOURNEY_LIST_DATA[index].addDayArrival
        }
    }

    @Test
    fun fetchSortAndFilter_successFetchDataWithError_shouldBeThrow() {
        // given
        coEvery { flightSortAndFilterUseCase.execute(any(), any()) } coAnswers {
            throw FlightSearchThrowable().apply {
                errorList = arrayListOf(FlightSearchErrorEntity("1", "Error", "Error Dummy"))
            }
        }

        // when
        try {
            flightSearchViewModel.fetchSortAndFilter()
        } catch (t: Throwable) {
        }

        // then
        flightSearchViewModel.journeyList.value is Fail
        val errorList = ((flightSearchViewModel.journeyList.value as Fail).throwable as FlightSearchThrowable).errorList

        errorList.size shouldBe 1
        errorList[0].id shouldBe "1"
        errorList[0].status shouldBe "Error"
        errorList[0].title shouldBe "Error Dummy"
    }

    @Test
    fun fetchSortAndFilter_failedFetchData_shouldBeThrow() {
        // given
        coEvery { flightSortAndFilterUseCase.execute(any(), any()) } coAnswers { throw Throwable("Errors") }

        // when
        try {
            flightSearchViewModel.fetchSortAndFilter()
        } catch (t: Throwable) {
        }

        // then
    }

    @Test
    fun onSearchItemClickedByNullJourney_whenLoggedIn_shouldDeleteFlightReturn() {
        // given
        val journeyModel: FlightJourneyModel? = null
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "dummy user id"

        // when
        flightSearchViewModel.onSearchItemClicked(journeyModel)

        // then
        coVerifySequence {
            flightAnalytics.eventSearchProductClickV2FromList(flightSearchViewModel.flightSearchPassData, journeyModel,
                    FlightAnalytics.Screen.SEARCH, any())
        }
    }

    @Test
    fun onSearchItemClickedByNullJourney_whenNotLoggedIn_shouldDeleteFlightReturn() {
        // given
        val journeyModel: FlightJourneyModel? = null
        coEvery { userSession.isLoggedIn } returns false

        // when
        flightSearchViewModel.onSearchItemClicked(journeyModel)

        // then
        coVerifySequence {
            flightAnalytics.eventSearchProductClickV2FromList(flightSearchViewModel.flightSearchPassData, journeyModel,
                    FlightAnalytics.Screen.SEARCH, any())
        }
    }

    @Test
    fun onSearchItemClickedByJourneyWithoutAdapterPosition_whenLoggedIn_shouldDeleteFlightReturn() {
        // given
        val journeyModel = JOURNEY_LIST_DATA[0]
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "dummy user id"

        // when
        flightSearchViewModel.onSearchItemClicked(journeyModel)

        // then
        coVerifySequence {
            flightAnalytics.eventSearchProductClickV2FromList(flightSearchViewModel.flightSearchPassData, journeyModel,
                    FlightAnalytics.Screen.SEARCH, any())
            flightSearchDeleteReturnDataUseCase.execute()
        }
        val selectedJourney = flightSearchViewModel.selectedJourney.value as FlightSearchSelectedModel
        selectedJourney.journeyModel shouldBe journeyModel
        selectedJourney.priceModel.departurePrice!!.adult shouldBe journeyModel.fare.adult
        selectedJourney.priceModel.departurePrice!!.adultNumeric shouldBe journeyModel.fare.adultNumeric
        selectedJourney.priceModel.departurePrice!!.child shouldBe journeyModel.fare.child
        selectedJourney.priceModel.departurePrice!!.childNumeric shouldBe journeyModel.fare.childNumeric
        selectedJourney.priceModel.departurePrice!!.infant shouldBe journeyModel.fare.infant
        selectedJourney.priceModel.departurePrice!!.adultCombo shouldBe ""
        selectedJourney.priceModel.departurePrice!!.adultNumericCombo shouldBe 0
        selectedJourney.priceModel.departurePrice!!.childCombo shouldBe ""
        selectedJourney.priceModel.departurePrice!!.childNumericCombo shouldBe 0
        selectedJourney.priceModel.departurePrice!!.infantCombo shouldBe ""
        selectedJourney.priceModel.departurePrice!!.infantNumericCombo shouldBe 0
    }

    @Test
    fun onSearchItemClickedByJourneyWithoutAdapterPosition_whenNotLoggedIn_shouldDeleteFlightReturn() {
        // given
        val journeyModel = JOURNEY_LIST_DATA[0]
        coEvery { userSession.isLoggedIn } returns false

        // when
        flightSearchViewModel.onSearchItemClicked(journeyModel)

        // then
        coVerifySequence {
            flightAnalytics.eventSearchProductClickV2FromList(flightSearchViewModel.flightSearchPassData, journeyModel,
                    FlightAnalytics.Screen.SEARCH, any())
            flightSearchDeleteReturnDataUseCase.execute()
        }
        val selectedJourney = flightSearchViewModel.selectedJourney.value as FlightSearchSelectedModel
        selectedJourney.journeyModel shouldBe journeyModel
        selectedJourney.priceModel.departurePrice!!.adult shouldBe journeyModel.fare.adult
        selectedJourney.priceModel.departurePrice!!.adultNumeric shouldBe journeyModel.fare.adultNumeric
        selectedJourney.priceModel.departurePrice!!.child shouldBe journeyModel.fare.child
        selectedJourney.priceModel.departurePrice!!.childNumeric shouldBe journeyModel.fare.childNumeric
        selectedJourney.priceModel.departurePrice!!.infant shouldBe journeyModel.fare.infant
        selectedJourney.priceModel.departurePrice!!.adultCombo shouldBe ""
        selectedJourney.priceModel.departurePrice!!.adultNumericCombo shouldBe 0
        selectedJourney.priceModel.departurePrice!!.childCombo shouldBe ""
        selectedJourney.priceModel.departurePrice!!.childNumericCombo shouldBe 0
        selectedJourney.priceModel.departurePrice!!.infantCombo shouldBe ""
        selectedJourney.priceModel.departurePrice!!.infantNumericCombo shouldBe 0
    }

    @Test
    fun onSearchItemClickedByJourneyRoundTrip_whenLoggedIn_shouldDeleteFlightReturn() {
        // given
        val journeyModel = JOURNEY_LIST_DATA[0]
        flightSearchViewModel.flightSearchPassData.isOneWay = false
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "dummy user id"

        // when
        flightSearchViewModel.onSearchItemClicked(journeyModel)

        // then
        coVerifySequence {
            flightAnalytics.eventSearchProductClickV2FromList(flightSearchViewModel.flightSearchPassData,
                    journeyModel, FlightAnalytics.Screen.SEARCH, any())
            flightSearchDeleteReturnDataUseCase.execute()
        }
        val selectedJourney = flightSearchViewModel.selectedJourney.value as FlightSearchSelectedModel
        selectedJourney.journeyModel shouldBe journeyModel
        selectedJourney.priceModel.departurePrice!!.adult shouldBe journeyModel.fare.adult
        selectedJourney.priceModel.departurePrice!!.adultNumeric shouldBe journeyModel.fare.adultNumeric
        selectedJourney.priceModel.departurePrice!!.child shouldBe journeyModel.fare.child
        selectedJourney.priceModel.departurePrice!!.childNumeric shouldBe journeyModel.fare.childNumeric
        selectedJourney.priceModel.departurePrice!!.infant shouldBe journeyModel.fare.infant
        selectedJourney.priceModel.departurePrice!!.adultCombo shouldBe journeyModel.fare.adultCombo
        selectedJourney.priceModel.departurePrice!!.adultNumericCombo shouldBe journeyModel.fare.adultNumericCombo
        selectedJourney.priceModel.departurePrice!!.childCombo shouldBe journeyModel.fare.childCombo
        selectedJourney.priceModel.departurePrice!!.childNumericCombo shouldBe journeyModel.fare.childNumericCombo
        selectedJourney.priceModel.departurePrice!!.infantCombo shouldBe journeyModel.fare.infantCombo
        selectedJourney.priceModel.departurePrice!!.infantNumericCombo shouldBe journeyModel.fare.infantNumericCombo
    }

    @Test
    fun onSearchItemClickedByJourneyRoundTrip_whenNotLoggedIn_shouldDeleteFlightReturn() {
        // given
        val journeyModel = JOURNEY_LIST_DATA[0]
        flightSearchViewModel.flightSearchPassData.isOneWay = false
        coEvery { userSession.isLoggedIn } returns false

        // when
        flightSearchViewModel.onSearchItemClicked(journeyModel)

        // then
        coVerifySequence {
            flightAnalytics.eventSearchProductClickV2FromList(flightSearchViewModel.flightSearchPassData,
                    journeyModel, FlightAnalytics.Screen.SEARCH, any())
            flightSearchDeleteReturnDataUseCase.execute()
        }
        val selectedJourney = flightSearchViewModel.selectedJourney.value as FlightSearchSelectedModel
        selectedJourney.journeyModel shouldBe journeyModel
        selectedJourney.priceModel.departurePrice!!.adult shouldBe journeyModel.fare.adult
        selectedJourney.priceModel.departurePrice!!.adultNumeric shouldBe journeyModel.fare.adultNumeric
        selectedJourney.priceModel.departurePrice!!.child shouldBe journeyModel.fare.child
        selectedJourney.priceModel.departurePrice!!.childNumeric shouldBe journeyModel.fare.childNumeric
        selectedJourney.priceModel.departurePrice!!.infant shouldBe journeyModel.fare.infant
        selectedJourney.priceModel.departurePrice!!.adultCombo shouldBe journeyModel.fare.adultCombo
        selectedJourney.priceModel.departurePrice!!.adultNumericCombo shouldBe journeyModel.fare.adultNumericCombo
        selectedJourney.priceModel.departurePrice!!.childCombo shouldBe journeyModel.fare.childCombo
        selectedJourney.priceModel.departurePrice!!.childNumericCombo shouldBe journeyModel.fare.childNumericCombo
        selectedJourney.priceModel.departurePrice!!.infantCombo shouldBe journeyModel.fare.infantCombo
        selectedJourney.priceModel.departurePrice!!.infantNumericCombo shouldBe journeyModel.fare.infantNumericCombo
    }

    @Test
    fun onSearchItemClickedByJourneyWithAdapterPosition_whenLoggedIn_shouldDeleteFlightReturn() {
        // given
        val adapterPosition = 0
        val journeyModel = JOURNEY_LIST_DATA[0]
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "dummy user id"

        // when
        flightSearchViewModel.onSearchItemClicked(journeyModel, adapterPosition)

        // then
        coVerifySequence {
            flightAnalytics.eventSearchProductClickV2FromList(flightSearchViewModel.flightSearchPassData, journeyModel,
                    adapterPosition, FlightAnalytics.Screen.SEARCH, any())
            flightSearchDeleteReturnDataUseCase.execute()
        }
        val selectedJourney = flightSearchViewModel.selectedJourney.value as FlightSearchSelectedModel
        selectedJourney.journeyModel shouldBe journeyModel
        selectedJourney.priceModel.departurePrice!!.adult shouldBe journeyModel.fare.adult
        selectedJourney.priceModel.departurePrice!!.adultNumeric shouldBe journeyModel.fare.adultNumeric
        selectedJourney.priceModel.departurePrice!!.child shouldBe journeyModel.fare.child
        selectedJourney.priceModel.departurePrice!!.childNumeric shouldBe journeyModel.fare.childNumeric
        selectedJourney.priceModel.departurePrice!!.infant shouldBe journeyModel.fare.infant
        selectedJourney.priceModel.departurePrice!!.adultCombo shouldBe ""
        selectedJourney.priceModel.departurePrice!!.adultNumericCombo shouldBe 0
        selectedJourney.priceModel.departurePrice!!.childCombo shouldBe ""
        selectedJourney.priceModel.departurePrice!!.childNumericCombo shouldBe 0
        selectedJourney.priceModel.departurePrice!!.infantCombo shouldBe ""
        selectedJourney.priceModel.departurePrice!!.infantNumericCombo shouldBe 0
    }

    @Test
    fun onSearchItemClickedByJourneyWithAdapterPosition_whenNotLoggedIn_shouldDeleteFlightReturn() {
        // given
        val adapterPosition = 0
        val journeyModel = JOURNEY_LIST_DATA[0]
        coEvery { userSession.isLoggedIn } returns false

        // when
        flightSearchViewModel.onSearchItemClicked(journeyModel, adapterPosition)

        // then
        coVerifySequence {
            flightAnalytics.eventSearchProductClickV2FromList(flightSearchViewModel.flightSearchPassData, journeyModel,
                    adapterPosition, FlightAnalytics.Screen.SEARCH, any())
            flightSearchDeleteReturnDataUseCase.execute()
        }
        val selectedJourney = flightSearchViewModel.selectedJourney.value as FlightSearchSelectedModel
        selectedJourney.journeyModel shouldBe journeyModel
        selectedJourney.priceModel.departurePrice!!.adult shouldBe journeyModel.fare.adult
        selectedJourney.priceModel.departurePrice!!.adultNumeric shouldBe journeyModel.fare.adultNumeric
        selectedJourney.priceModel.departurePrice!!.child shouldBe journeyModel.fare.child
        selectedJourney.priceModel.departurePrice!!.childNumeric shouldBe journeyModel.fare.childNumeric
        selectedJourney.priceModel.departurePrice!!.infant shouldBe journeyModel.fare.infant
        selectedJourney.priceModel.departurePrice!!.adultCombo shouldBe ""
        selectedJourney.priceModel.departurePrice!!.adultNumericCombo shouldBe 0
        selectedJourney.priceModel.departurePrice!!.childCombo shouldBe ""
        selectedJourney.priceModel.departurePrice!!.childNumericCombo shouldBe 0
        selectedJourney.priceModel.departurePrice!!.infantCombo shouldBe ""
        selectedJourney.priceModel.departurePrice!!.infantNumericCombo shouldBe 0
    }

    @Test
    fun onSearchItemClickedByJourneyId_shouldDeleteFlightReturn() {
        // given
        val selectedId = "dummy Id"
        val journeyModel = JOURNEY_LIST_DATA[0]
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } returns journeyModel

        // when
        flightSearchViewModel.onSearchItemClicked(selectedId = selectedId)

        // then
        val selectedJourney = flightSearchViewModel.selectedJourney.value as FlightSearchSelectedModel
        selectedJourney.journeyModel shouldBe journeyModel
        selectedJourney.priceModel.departurePrice!!.adult shouldBe journeyModel.fare.adult
        selectedJourney.priceModel.departurePrice!!.adultNumeric shouldBe journeyModel.fare.adultNumeric
        selectedJourney.priceModel.departurePrice!!.child shouldBe journeyModel.fare.child
        selectedJourney.priceModel.departurePrice!!.childNumeric shouldBe journeyModel.fare.childNumeric
        selectedJourney.priceModel.departurePrice!!.infant shouldBe journeyModel.fare.infant
        selectedJourney.priceModel.departurePrice!!.adultCombo shouldBe ""
        selectedJourney.priceModel.departurePrice!!.adultNumericCombo shouldBe 0
        selectedJourney.priceModel.departurePrice!!.childCombo shouldBe ""
        selectedJourney.priceModel.departurePrice!!.childNumericCombo shouldBe 0
        selectedJourney.priceModel.departurePrice!!.infantCombo shouldBe ""
        selectedJourney.priceModel.departurePrice!!.infantNumericCombo shouldBe 0
    }

    @Test
    fun onSearchItemClickedByJourneyId_journeyIdNotFound_shouldThrowError() {
        // given
        val selectedId = "dummy Id"
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } coAnswers { throw Throwable("Not Found") }

        // when
        try {
            flightSearchViewModel.onSearchItemClicked(selectedId = selectedId)
        } catch (t: Throwable) {
            // then
        }
    }

    @Test
    fun isDoneLoadData_withMaxProgress_shouldReturnTrue() {
        // given
        flightSearchViewModel.progress.value = 100

        // when
        val doneLoadData = flightSearchViewModel.isDoneLoadData()

        // then
        doneLoadData shouldBe true
    }

    @Test
    fun isDoneLoadData_withNotMaxProgress_shouldReturnFalse() {
        // given
        flightSearchViewModel.progress.value = 50

        // when
        val doneLoadData = flightSearchViewModel.isDoneLoadData()

        // then
        doneLoadData shouldBe false
    }

    @Test
    fun isDoneLoadData_withNullProgress_shouldReturnFalse() {
        // given
        flightSearchViewModel.progress.value = null

        // when
        val doneLoadData = flightSearchViewModel.isDoneLoadData()

        // then
        doneLoadData shouldBe false
    }

    @Test
    fun isFilterModelInitializedTrue() {
        // given

        // when
        val result = flightSearchViewModel.isFilterModelInitialized()

        // then
        result shouldBe true
    }

    @Test
    fun isFilterModelInitializedFalse() {
        // given
        val newViewModel = FlightSearchViewModel(
                flightSearchUseCase,
                flightSortAndFilterUseCase,
                flightSearchDeleteAllDataUseCase,
                flightSearchDeleteReturnDataUseCase,
                flightSearchJourneyByIdUseCase,
                flightSearchCombineUseCase,
                travelTickerUseCase,
                flightSearchStatisticUseCase,
                flightLowestPriceUseCase,
                flightAnalytics,
                flightSearchCache,
		mockk(),
                testDispatcherProvider)

        // when
        val result = newViewModel.isFilterModelInitialized()

        // then
        result shouldBe false
        newViewModel.isInFilterMode shouldBe false
    }

    @Test
    fun isOneWayTrue() {
        // given
        flightSearchViewModel.flightSearchPassData.isOneWay = true

        // when
        val isOneWay = flightSearchViewModel.isOneWay()

        // then
        isOneWay shouldBe true
    }

    @Test
    fun isOneWayFalse() {
        // given
        flightSearchViewModel.flightSearchPassData.isOneWay = false

        // when
        val isOneWay = flightSearchViewModel.isOneWay()

        // then
        isOneWay shouldBe false
    }

    @Test
    fun recountFilterCounterWithMinPrice() {
        // given
        coEvery { flightSearchStatisticUseCase.execute(any()) } returns SEARCH_STATISTICS_DATA
        flightSearchViewModel.filterModel = defaultFilterModel
        flightSearchViewModel.filterModel.priceMin = SEARCH_STATISTICS_DATA.minPrice + 1000
        flightSearchViewModel.filterModel.priceMax = SEARCH_STATISTICS_DATA.maxPrice

        // when
        val counter = flightSearchViewModel.recountFilterCounter()

        // then
        counter shouldBe 1
    }

    @Test
    fun recountFilterCounterWithMaxPrice() {
        // given
        coEvery { flightSearchStatisticUseCase.execute(any()) } returns SEARCH_STATISTICS_DATA
        flightSearchViewModel.filterModel = defaultFilterModel
        flightSearchViewModel.filterModel.priceMin = SEARCH_STATISTICS_DATA.minPrice
        flightSearchViewModel.filterModel.priceMax = SEARCH_STATISTICS_DATA.maxPrice - 1000

        // when
        val counter = flightSearchViewModel.recountFilterCounter()

        // then
        counter shouldBe 1
    }

    @Test
    fun recountFilterCounterWithMinAndMaxPrice() {
        // given
        coEvery { flightSearchStatisticUseCase.execute(any()) } returns SEARCH_STATISTICS_DATA
        flightSearchViewModel.filterModel = defaultFilterModel
        flightSearchViewModel.filterModel.priceMin = SEARCH_STATISTICS_DATA.minPrice + 1000
        flightSearchViewModel.filterModel.priceMax = SEARCH_STATISTICS_DATA.maxPrice - 1000

        // when
        val counter = flightSearchViewModel.recountFilterCounter()

        // then
        counter shouldBe 1
    }

    @Test
    fun recountFilterCounterWithoutFilter() {
        // given
        coEvery { flightSearchStatisticUseCase.execute(any()) } returns SEARCH_STATISTICS_DATA
        flightSearchViewModel.priceFilterStatistic = Pair(SEARCH_STATISTICS_DATA.minPrice, SEARCH_STATISTICS_DATA.maxPrice)
        flightSearchViewModel.filterModel = defaultFilterModel
        flightSearchViewModel.filterModel.priceMin = SEARCH_STATISTICS_DATA.minPrice
        flightSearchViewModel.filterModel.priceMax = SEARCH_STATISTICS_DATA.maxPrice

        // when
        val counter = flightSearchViewModel.recountFilterCounter()

        // then
        counter shouldBe 0
    }

    @Test
    fun sendQuickFilterTrackWhenLoggedIn() {
        // given
        val filterName = "Langsung"
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "dummy user id"

        // when
        flightSearchViewModel.sendQuickFilterTrack(filterName)

        // then
        verify { flightAnalytics.eventQuickFilterClick(filterName, any()) }
    }

    @Test
    fun sendQuickFilterTrackWhenNotLoggedIn() {
        // given
        val filterName = "Langsung"
        coEvery { userSession.isLoggedIn } returns false

        // when
        flightSearchViewModel.sendQuickFilterTrack(filterName)

        // then
        verify { flightAnalytics.eventQuickFilterClick(filterName, any()) }
    }

    @Test
    fun sendDetailClickTrack() {
        // given
        val adapterPosition = 0
        val journeyModel = JOURNEY_LIST_DATA[adapterPosition]

        // when
        flightSearchViewModel.sendDetailClickTrack(journeyModel, adapterPosition)

        // then
        verifySequence {
            flightAnalytics.eventSearchDetailClick(journeyModel, adapterPosition)
            flightAnalytics.eventProductDetailImpression(journeyModel, adapterPosition)
        }
    }

    @Test
    fun fetchPromoList_shouldReturnSuccessEmpty() {
        // given
        coEvery { flightLowestPriceUseCase.execute(any() as String, any()) } returns Success(PROMO_CHIPS_EMPTY)
        flightSearchViewModel.flightSearchPassData = defaultSearchData

        // when
        flightSearchViewModel.fetchPromoList(isReturnTrip = false)

        // then
        flightSearchViewModel.promoData.value is Success

        flightSearchViewModel.promoData.value is Success
        val promoData = (flightSearchViewModel.promoData.value as Success<FlightLowestPrice>).data.dataPromoChips
        promoData.size shouldBe 0
    }

    @Test
    fun fetchPromoList_oneWay_shouldReturnSuccessNotEmpty() {
        // given
        coEvery { flightLowestPriceUseCase.execute(any() as String, any()) } returns Success(PROMO_CHIPS)
        flightSearchViewModel.flightSearchPassData = defaultSearchData

        // when
        flightSearchViewModel.fetchPromoList(isReturnTrip = false)

        // then
        flightSearchViewModel.promoData.value is Success

        flightSearchViewModel.promoData.value is Success
        val promoData = (flightSearchViewModel.promoData.value as Success<FlightLowestPrice>).data.dataPromoChips
        promoData.size shouldBe 1
        promoData[0].date shouldBe PROMO_CHIPS.dataPromoChips[0].date
        promoData[0].airlinePrices shouldBe PROMO_CHIPS.dataPromoChips[0].airlinePrices
        promoData[0].airlinePrices[0] shouldBe PROMO_CHIPS.dataPromoChips[0].airlinePrices[0]
        promoData[0].airlinePrices[0].airlineID shouldBe PROMO_CHIPS.dataPromoChips[0].airlinePrices[0].airlineID
    }

    @Test
    fun fetchPromoList_roundTrip_shouldReturnSuccessNotEmpty() {
        // given
        coEvery { flightLowestPriceUseCase.execute(any() as String, any()) } returns Success(PROMO_CHIPS)
        flightSearchViewModel.flightSearchPassData = defaultSearchData

        // when
        flightSearchViewModel.fetchPromoList(isReturnTrip = true)
        
        // then
        flightSearchViewModel.promoData.value is Success
        val promoData = (flightSearchViewModel.promoData.value as Success<FlightLowestPrice>).data.dataPromoChips
        promoData.size shouldBe 1
        promoData[0].date shouldBe PROMO_CHIPS.dataPromoChips[0].date
        promoData[0].airlinePrices shouldBe PROMO_CHIPS.dataPromoChips[0].airlinePrices
        promoData[0].airlinePrices[0] shouldBe PROMO_CHIPS.dataPromoChips[0].airlinePrices[0]
        promoData[0].airlinePrices[0].airlineID shouldBe PROMO_CHIPS.dataPromoChips[0].airlinePrices[0].airlineID
    }

    @Test
    fun fetchPromoList_shouldReturnFail() {
        // given
        val fakeEntity = FlightSearchErrorEntity("1", "Error", "Error Dummy")
        coEvery { flightLowestPriceUseCase.execute(any() as String, any()) } coAnswers {
            throw FlightSearchThrowable().apply {
                errorList = arrayListOf(fakeEntity)
            }
        }

        // when
        try {
            flightSearchViewModel.fetchPromoList(isReturnTrip = true)
        } catch (t: Throwable) {
        }

        // then
        flightSearchViewModel.promoData.value is Fail
        val errorList = ((flightSearchViewModel.promoData.value as Fail).throwable as FlightSearchThrowable).errorList

        errorList.size shouldBe 1
        errorList[0].id shouldBe fakeEntity.id
        errorList[0].status shouldBe fakeEntity.status
        errorList[0].title shouldBe fakeEntity.title
    }

    @Test
    fun onPromotionChipsClickedTrackWhenLoggedIn_oneWay() {
        // given
        val position = 0
        val airlinePrice: AirlinePrice = PROMO_CHIPS.dataPromoChips[0].airlinePrices[0]
        val isReturn = false
        flightSearchViewModel.flightSearchPassData = defaultSearchData
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "dummy user id"

        // when
        flightSearchViewModel.onPromotionChipsClicked(position, airlinePrice = airlinePrice, isReturnTrip = isReturn)

        // then
        verify { flightAnalytics.eventFlightPromotionClick(position + 1, airlinePrice, flightSearchViewModel.flightSearchPassData,
                FlightAnalytics.Screen.SEARCH, any(), isReturn) }
    }

    @Test
    fun onPromotionChipsClickedTrackWhenLoggedIn_returnTrip() {
        // given
        val position = 0
        val airlinePrice: AirlinePrice = PROMO_CHIPS.dataPromoChips[0].airlinePrices[0]
        val isReturn = true
        flightSearchViewModel.flightSearchPassData = defaultSearchData
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "dummy user id"

        // when
        flightSearchViewModel.onPromotionChipsClicked(position, airlinePrice = airlinePrice, isReturnTrip = isReturn)

        // then
        verify { flightAnalytics.eventFlightPromotionClick(position + 1, airlinePrice, flightSearchViewModel.flightSearchPassData,
                FlightAnalytics.Screen.SEARCH, any(), isReturn) }
    }

    @Test
    fun onPromotionChipsClickedTrackWhenNotLoggedIn() {
        // given
        val position = 0
        val airlinePrice: AirlinePrice = PROMO_CHIPS.dataPromoChips[0].airlinePrices[0]
        val isReturn = false
        flightSearchViewModel.flightSearchPassData = defaultSearchData
        coEvery { userSession.isLoggedIn } returns false
        // when
        flightSearchViewModel.onPromotionChipsClicked(position, airlinePrice = airlinePrice, isReturnTrip = isReturn)

        // then
        verify { flightAnalytics.eventFlightPromotionClick(position + 1, airlinePrice, flightSearchViewModel.flightSearchPassData,
                FlightAnalytics.Screen.SEARCH, any(), isReturn) }
    }
}