package com.tokopedia.flight.searchV4.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.dummy.*
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import com.tokopedia.flight.searchV4.domain.FlightComboKeyUseCase
import com.tokopedia.flight.searchV4.domain.FlightSearchJouneyByIdUseCase
import com.tokopedia.flight.searchV4.presentation.model.*
import com.tokopedia.flight.searchV4.presentation.model.filter.RefundableEnum
import com.tokopedia.flight.shouldBe
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 18/05/2020
 */
class FlightSearchReturnViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var flightAnalytics: FlightAnalytics

    private val flightSearchJourneyByIdUseCase = mockk<FlightSearchJouneyByIdUseCase>()
    private val flightComboKeyUseCase = mockk<FlightComboKeyUseCase>()
    private val testDispatcherProvider = CoroutineTestDispatchersProvider
    private val userSession = mockk<UserSessionInterface>()

    private lateinit var flightSearchReturnViewModel: FlightSearchReturnViewModel
    private val defaultPriceModel = FlightPriceModel(
            FlightFareModel("Rp1.500.000", "", "", "", "", "", 1500000, 0, 0, 0, 0, 0),
            null,
            "")
    private val defaultSearchPassData = FlightSearchPassDataModel(
            "2020-11-11", "2020-12-12", false, FlightPassengerModel(3, 2, 1),
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

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        flightSearchReturnViewModel = FlightSearchReturnViewModel(flightSearchJourneyByIdUseCase,
                flightComboKeyUseCase, flightAnalytics, userSession, testDispatcherProvider)
        flightSearchReturnViewModel.priceModel = defaultPriceModel
        flightSearchReturnViewModel.isBestPairing = true
        flightSearchReturnViewModel.isViewOnlyBestPairing = true
    }

    @Test
    fun getDepartureJourneyDetail_success() {
        // given
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } returns DEPARTURE_JOURNEY

        // when
        flightSearchReturnViewModel.getDepartureJourneyDetail()

        // then
        assert(flightSearchReturnViewModel.departureJourney.value != null)
        val departureJourney = flightSearchReturnViewModel.departureJourney.value!!
        departureJourney.duration shouldBe DEPARTURE_JOURNEY.duration
        departureJourney.totalTransit shouldBe DEPARTURE_JOURNEY.totalTransit
        departureJourney.totalNumeric shouldBe DEPARTURE_JOURNEY.totalNumeric
        departureJourney.term shouldBe DEPARTURE_JOURNEY.term
        departureJourney.specialTagText shouldBe DEPARTURE_JOURNEY.specialTagText
        departureJourney.routeList.size shouldBe DEPARTURE_JOURNEY.routeList.size
        departureJourney.showSpecialPriceTag shouldBe DEPARTURE_JOURNEY.showSpecialPriceTag
        departureJourney.isReturning shouldBe DEPARTURE_JOURNEY.isReturning
        departureJourney.isRefundable shouldBe DEPARTURE_JOURNEY.isRefundable
        departureJourney.isBestPairing shouldBe DEPARTURE_JOURNEY.isBestPairing
        departureJourney.id shouldBe DEPARTURE_JOURNEY.id
        departureJourney.fare.adult shouldBe DEPARTURE_JOURNEY.fare.adult
        departureJourney.fare.adultCombo shouldBe DEPARTURE_JOURNEY.fare.adultCombo
        departureJourney.fare.adultNumeric shouldBe DEPARTURE_JOURNEY.fare.adultNumeric
        departureJourney.fare.adultNumericCombo shouldBe DEPARTURE_JOURNEY.fare.adultNumericCombo
        departureJourney.fare.child shouldBe DEPARTURE_JOURNEY.fare.child
        departureJourney.fare.childCombo shouldBe DEPARTURE_JOURNEY.fare.childCombo
        departureJourney.fare.childNumeric shouldBe DEPARTURE_JOURNEY.fare.childNumeric
        departureJourney.fare.childNumericCombo shouldBe DEPARTURE_JOURNEY.fare.childNumericCombo
        departureJourney.fare.infant shouldBe DEPARTURE_JOURNEY.fare.infant
        departureJourney.fare.infantCombo shouldBe DEPARTURE_JOURNEY.fare.infantCombo
        departureJourney.fare.infantNumeric shouldBe DEPARTURE_JOURNEY.fare.infantNumeric
        departureJourney.fare.infantNumericCombo shouldBe DEPARTURE_JOURNEY.fare.infantNumericCombo
        departureJourney.durationMinute shouldBe DEPARTURE_JOURNEY.durationMinute
        departureJourney.departureTimeInt shouldBe DEPARTURE_JOURNEY.departureTimeInt
        departureJourney.departureAirportName shouldBe DEPARTURE_JOURNEY.departureAirportName
        departureJourney.departureAirportCity shouldBe DEPARTURE_JOURNEY.departureAirportCity
        departureJourney.comboPriceNumeric shouldBe DEPARTURE_JOURNEY.comboPriceNumeric
        departureJourney.comboId shouldBe DEPARTURE_JOURNEY.comboId
        departureJourney.beforeTotal shouldBe DEPARTURE_JOURNEY.beforeTotal
        departureJourney.arrivalTimeInt shouldBe DEPARTURE_JOURNEY.arrivalTimeInt
        departureJourney.arrivalAirportName shouldBe DEPARTURE_JOURNEY.arrivalAirportName
        departureJourney.arrivalAirportCity shouldBe DEPARTURE_JOURNEY.arrivalAirportCity
        departureJourney.airlineDataList.size shouldBe DEPARTURE_JOURNEY.airlineDataList.size
        departureJourney.addDayArrival shouldBe DEPARTURE_JOURNEY.addDayArrival

        flightSearchReturnViewModel.isBestPairing shouldBe true
        flightSearchReturnViewModel.isViewOnlyBestPairing shouldBe true
    }

    @Test
    fun getDepartureJourneyDetail_failed() {
        // given
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } coAnswers { throw Throwable("Error") }

        // when
        flightSearchReturnViewModel.getDepartureJourneyDetail()

        // then
        assert(flightSearchReturnViewModel.departureJourney.value == null)
    }

    @Test
    fun onFlightSelectFromDetail_withNullDepartureJourney_validReturnJourney() {
        // given
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } returnsMany listOf(DEPARTURE_JOURNEY, VALID_RETURN_JOURNEY)
        coEvery { flightComboKeyUseCase.execute(any(), any()) } returns COMBO_KEY
        val flightSearchPassDataModel = defaultSearchPassData
        flightSearchReturnViewModel.selectedFlightDepartureId = "dummyDepartureId"
        val selectedReturnId = "dummyReturnId"

        // when
        flightSearchReturnViewModel.onFlightSearchSelectFromDetail(flightSearchPassDataModel, selectedReturnId)

        // then
        verify {
            flightAnalytics.eventSearchProductClickFromDetail(flightSearchPassDataModel, VALID_RETURN_JOURNEY)
        }
        val returnJourney = flightSearchReturnViewModel.selectedReturnJourney!!
        returnJourney.duration shouldBe VALID_RETURN_JOURNEY.duration
        returnJourney.totalTransit shouldBe VALID_RETURN_JOURNEY.totalTransit
        returnJourney.totalNumeric shouldBe VALID_RETURN_JOURNEY.totalNumeric
        returnJourney.term shouldBe VALID_RETURN_JOURNEY.term
        returnJourney.specialTagText shouldBe VALID_RETURN_JOURNEY.specialTagText
        returnJourney.routeList.size shouldBe VALID_RETURN_JOURNEY.routeList.size
        returnJourney.showSpecialPriceTag shouldBe VALID_RETURN_JOURNEY.showSpecialPriceTag
        returnJourney.isReturning shouldBe VALID_RETURN_JOURNEY.isReturning
        returnJourney.isRefundable shouldBe VALID_RETURN_JOURNEY.isRefundable
        returnJourney.isBestPairing shouldBe VALID_RETURN_JOURNEY.isBestPairing
        returnJourney.id shouldBe VALID_RETURN_JOURNEY.id
        returnJourney.fare.adult shouldBe VALID_RETURN_JOURNEY.fare.adult
        returnJourney.fare.adultCombo shouldBe VALID_RETURN_JOURNEY.fare.adultCombo
        returnJourney.fare.adultNumeric shouldBe VALID_RETURN_JOURNEY.fare.adultNumeric
        returnJourney.fare.adultNumericCombo shouldBe VALID_RETURN_JOURNEY.fare.adultNumericCombo
        returnJourney.fare.child shouldBe VALID_RETURN_JOURNEY.fare.child
        returnJourney.fare.childCombo shouldBe VALID_RETURN_JOURNEY.fare.childCombo
        returnJourney.fare.childNumeric shouldBe VALID_RETURN_JOURNEY.fare.childNumeric
        returnJourney.fare.childNumericCombo shouldBe VALID_RETURN_JOURNEY.fare.childNumericCombo
        returnJourney.fare.infant shouldBe VALID_RETURN_JOURNEY.fare.infant
        returnJourney.fare.infantCombo shouldBe VALID_RETURN_JOURNEY.fare.infantCombo
        returnJourney.fare.infantNumeric shouldBe VALID_RETURN_JOURNEY.fare.infantNumeric
        returnJourney.fare.infantNumericCombo shouldBe VALID_RETURN_JOURNEY.fare.infantNumericCombo
        returnJourney.durationMinute shouldBe VALID_RETURN_JOURNEY.durationMinute
        returnJourney.departureTimeInt shouldBe VALID_RETURN_JOURNEY.departureTimeInt
        returnJourney.departureAirportName shouldBe VALID_RETURN_JOURNEY.departureAirportName
        returnJourney.departureAirportCity shouldBe VALID_RETURN_JOURNEY.departureAirportCity
        returnJourney.comboPriceNumeric shouldBe VALID_RETURN_JOURNEY.comboPriceNumeric
        returnJourney.comboId shouldBe VALID_RETURN_JOURNEY.comboId
        returnJourney.beforeTotal shouldBe VALID_RETURN_JOURNEY.beforeTotal
        returnJourney.arrivalTimeInt shouldBe VALID_RETURN_JOURNEY.arrivalTimeInt
        returnJourney.arrivalAirportName shouldBe VALID_RETURN_JOURNEY.arrivalAirportName
        returnJourney.arrivalAirportCity shouldBe VALID_RETURN_JOURNEY.arrivalAirportCity
        returnJourney.airlineDataList.size shouldBe VALID_RETURN_JOURNEY.airlineDataList.size
        returnJourney.addDayArrival shouldBe VALID_RETURN_JOURNEY.addDayArrival

        flightSearchReturnViewModel.searchErrorStringId.value shouldBe SearchErrorEnum.NO_ERRORS
        flightSearchReturnViewModel.priceModel.comboKey shouldBe COMBO_KEY
    }

    @Test
    fun onFlightSelectFromDetail_withDepartureJourney_notValidReturnJourney() {
        // given
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } returnsMany listOf(DEPARTURE_JOURNEY, NOT_VALID_RETURN_JOURNEY)
        coEvery { flightComboKeyUseCase.execute(any(), any()) } returns COMBO_KEY
        val flightSearchPassDataModel = FlightSearchPassDataModel(
                "2020-11-11", "2020-12-12", false, FlightPassengerModel(3, 2, 1),
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
        flightSearchReturnViewModel.selectedFlightDepartureId = "dummyDepartureId"
        val selectedReturnId = "dummyReturnId"

        // when
        flightSearchReturnViewModel.getDepartureJourneyDetail()
        flightSearchReturnViewModel.onFlightSearchSelectFromDetail(flightSearchPassDataModel, selectedReturnId)

        // then
        verify {
            flightAnalytics.eventSearchProductClickFromDetail(flightSearchPassDataModel, NOT_VALID_RETURN_JOURNEY)
        }

        flightSearchReturnViewModel.selectedReturnJourney shouldBe null
        flightSearchReturnViewModel.searchErrorStringId.value shouldBe SearchErrorEnum.ERROR_RETURN_JOURNEY_TIME
        flightSearchReturnViewModel.priceModel.comboKey shouldBe COMBO_KEY
    }

    @Test
    fun onFlightSelectFromDetail_withDepartureJourney_notValidDiffHourReturnJourney() {
        // given
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } returnsMany listOf(DEPARTURE_JOURNEY, NOT_VALID_DIFF_HOUR_RETURN_JOURNEY)
        coEvery { flightComboKeyUseCase.execute(any(), any()) } returns COMBO_KEY
        val flightSearchPassDataModel = FlightSearchPassDataModel(
                "2020-11-11", "2020-12-12", false, FlightPassengerModel(3, 2, 1),
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
        flightSearchReturnViewModel.selectedFlightDepartureId = "dummyDepartureId"
        val selectedReturnId = "dummyReturnId"

        // when
        flightSearchReturnViewModel.getDepartureJourneyDetail()
        flightSearchReturnViewModel.onFlightSearchSelectFromDetail(flightSearchPassDataModel, selectedReturnId)

        // then
        flightSearchReturnViewModel.selectedReturnJourney shouldBe null
        flightSearchReturnViewModel.searchErrorStringId.value shouldBe SearchErrorEnum.ERROR_RETURN_JOURNEY_TIME
        flightSearchReturnViewModel.priceModel.comboKey shouldBe COMBO_KEY
    }

    @Test
    fun onFlightSelectFromDetail_nullRoutes_notValidReturnJourney() {
        // given
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } returnsMany listOf(DEPARTURE_JOURNEY, FlightJourneyModel(
                "", "DummyId", true, false, "BTJ",
                "Bandara International Sultan Iskandar Muda", "",
                "10.00", 111111, "CGK", "12.40",
                "Bandara International Soekarno Hatta", "",
                212121, 0, 0, "2j 50m", 123123,
                "Rp1.500.000", 1500000, "", 0,
                false, "", false, RefundableEnum.NOT_REFUNDABLE,
                false, FlightFareModel("Rp1.500.000", "", "", "", "", "", 1500000, 0, 0, 0, 0, 0),
                arrayListOf(), arrayListOf(), "", ""
        ))
        coEvery { flightComboKeyUseCase.execute(any(), any()) } returns COMBO_KEY
        val flightSearchPassDataModel = FlightSearchPassDataModel(
                "2020-11-11", "2020-12-12", false, FlightPassengerModel(3, 2, 1),
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
        flightSearchReturnViewModel.selectedFlightDepartureId = "dummyDepartureId"
        val selectedReturnId = "dummyReturnId"

        // when
        flightSearchReturnViewModel.getDepartureJourneyDetail()
        flightSearchReturnViewModel.onFlightSearchSelectFromDetail(flightSearchPassDataModel, selectedReturnId)

        // then
        flightSearchReturnViewModel.selectedReturnJourney shouldBe null
        flightSearchReturnViewModel.searchErrorStringId.value shouldBe SearchErrorEnum.ERROR_RETURN_JOURNEY_TIME
        flightSearchReturnViewModel.priceModel.comboKey shouldBe COMBO_KEY
    }

    @Test
    fun onFlightSelectFromDetail_emptyRoutes_notValidReturnJourney() {
        // given
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } returnsMany listOf(DEPARTURE_JOURNEY, FlightJourneyModel(
                "", "DummyId", true, true, "BTJ",
                "Bandara International Sultan Iskandar Muda", "",
                "10.00", 111111, "CGK", "12.40",
                "Bandara International Soekarno Hatta", "",
                212121, 0, 0, "2j 50m", 123123,
                "Rp1.500.000", 1500000, "", 0,
                false, "", false, RefundableEnum.NOT_REFUNDABLE,
                false, FlightFareModel("Rp1.500.000", "", "", "", "", "", 1500000, 0, 0, 0, 0, 0),
                arrayListOf(), arrayListOf(), "", ""
        ))
        coEvery { flightComboKeyUseCase.execute(any(), any()) } returns COMBO_KEY
        val flightSearchPassDataModel = FlightSearchPassDataModel(
                "2020-11-11", "2020-12-12", false, FlightPassengerModel(3, 2, 1),
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
        flightSearchReturnViewModel.selectedFlightDepartureId = "dummyDepartureId"
        val selectedReturnId = "dummyReturnId"

        // when
        flightSearchReturnViewModel.getDepartureJourneyDetail()
        flightSearchReturnViewModel.onFlightSearchSelectFromDetail(flightSearchPassDataModel, selectedReturnId)

        // then
        flightSearchReturnViewModel.selectedReturnJourney shouldBe null
        flightSearchReturnViewModel.searchErrorStringId.value shouldBe SearchErrorEnum.ERROR_RETURN_JOURNEY_TIME
        flightSearchReturnViewModel.priceModel.comboKey shouldBe COMBO_KEY
    }

    @Test
    fun onFlightSelectFromDetail_failed() {
        // given
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } coAnswers { throw Throwable("Error") }
        val flightSearchPassDataModel = FlightSearchPassDataModel(
                "2020-11-11", "2020-12-12", false, FlightPassengerModel(3, 2, 1),
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
        flightSearchReturnViewModel.selectedFlightDepartureId = "dummyDepartureId"
        val selectedReturnId = "dummyReturnId"

        // when
        flightSearchReturnViewModel.onFlightSearchSelectFromDetail(flightSearchPassDataModel, selectedReturnId)

        // then
        flightSearchReturnViewModel.searchErrorStringId.value shouldBe SearchErrorEnum.ERROR_PICK_JOURNEY
    }

    @Test
    fun onFlightSearchSelected_whenLoggedIn_default() {
        // given
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "dummy user id"

        // when
        flightSearchReturnViewModel.onFlightSearchSelected(defaultSearchPassData)

        // then
        every { flightAnalytics.eventSearchProductClickV2FromList(defaultSearchPassData, any<FlightJourneyModel>(),
                FlightAnalytics.Screen.SEARCH, any()) }
    }

    @Test
    fun onFlightSearchSelected_whenNotLoggedIn_default() {
        // given
        coEvery { userSession.isLoggedIn } returns false

        // when
        flightSearchReturnViewModel.onFlightSearchSelected(defaultSearchPassData)

        // then
        every { flightAnalytics.eventSearchProductClickV2FromList(defaultSearchPassData, any<FlightJourneyModel>(),
                FlightAnalytics.Screen.SEARCH, any()) }
    }

    @Test
    fun onFlightSearchSelected_whenLoggedIn_validReturnJourney() {
        // given
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "dummy user id"
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } returns DEPARTURE_JOURNEY
        coEvery { flightComboKeyUseCase.execute(any(), any()) } returns COMBO_KEY
        val flightSearchPassDataModel = defaultSearchPassData
        flightSearchReturnViewModel.selectedFlightDepartureId = "dummyDepartureId"
        val adapterPosition = 0

        // when
        flightSearchReturnViewModel.onFlightSearchSelected(flightSearchPassDataModel, VALID_RETURN_JOURNEY, adapterPosition)

        // then
        verify {
            flightAnalytics.eventSearchProductClickV2FromList(flightSearchPassDataModel, VALID_RETURN_JOURNEY,
                    adapterPosition, FlightAnalytics.Screen.SEARCH, any())
        }
        val returnJourney = flightSearchReturnViewModel.selectedReturnJourney!!
        returnJourney.duration shouldBe VALID_RETURN_JOURNEY.duration
        returnJourney.totalTransit shouldBe VALID_RETURN_JOURNEY.totalTransit
        returnJourney.totalNumeric shouldBe VALID_RETURN_JOURNEY.totalNumeric
        returnJourney.term shouldBe VALID_RETURN_JOURNEY.term
        returnJourney.specialTagText shouldBe VALID_RETURN_JOURNEY.specialTagText
        returnJourney.routeList.size shouldBe VALID_RETURN_JOURNEY.routeList.size
        returnJourney.showSpecialPriceTag shouldBe VALID_RETURN_JOURNEY.showSpecialPriceTag
        returnJourney.isReturning shouldBe VALID_RETURN_JOURNEY.isReturning
        returnJourney.isRefundable shouldBe VALID_RETURN_JOURNEY.isRefundable
        returnJourney.isBestPairing shouldBe VALID_RETURN_JOURNEY.isBestPairing
        returnJourney.id shouldBe VALID_RETURN_JOURNEY.id
        returnJourney.fare.adult shouldBe VALID_RETURN_JOURNEY.fare.adult
        returnJourney.fare.adultCombo shouldBe VALID_RETURN_JOURNEY.fare.adultCombo
        returnJourney.fare.adultNumeric shouldBe VALID_RETURN_JOURNEY.fare.adultNumeric
        returnJourney.fare.adultNumericCombo shouldBe VALID_RETURN_JOURNEY.fare.adultNumericCombo
        returnJourney.fare.child shouldBe VALID_RETURN_JOURNEY.fare.child
        returnJourney.fare.childCombo shouldBe VALID_RETURN_JOURNEY.fare.childCombo
        returnJourney.fare.childNumeric shouldBe VALID_RETURN_JOURNEY.fare.childNumeric
        returnJourney.fare.childNumericCombo shouldBe VALID_RETURN_JOURNEY.fare.childNumericCombo
        returnJourney.fare.infant shouldBe VALID_RETURN_JOURNEY.fare.infant
        returnJourney.fare.infantCombo shouldBe VALID_RETURN_JOURNEY.fare.infantCombo
        returnJourney.fare.infantNumeric shouldBe VALID_RETURN_JOURNEY.fare.infantNumeric
        returnJourney.fare.infantNumericCombo shouldBe VALID_RETURN_JOURNEY.fare.infantNumericCombo
        returnJourney.durationMinute shouldBe VALID_RETURN_JOURNEY.durationMinute
        returnJourney.departureTimeInt shouldBe VALID_RETURN_JOURNEY.departureTimeInt
        returnJourney.departureAirportName shouldBe VALID_RETURN_JOURNEY.departureAirportName
        returnJourney.departureAirportCity shouldBe VALID_RETURN_JOURNEY.departureAirportCity
        returnJourney.comboPriceNumeric shouldBe VALID_RETURN_JOURNEY.comboPriceNumeric
        returnJourney.comboId shouldBe VALID_RETURN_JOURNEY.comboId
        returnJourney.beforeTotal shouldBe VALID_RETURN_JOURNEY.beforeTotal
        returnJourney.arrivalTimeInt shouldBe VALID_RETURN_JOURNEY.arrivalTimeInt
        returnJourney.arrivalAirportName shouldBe VALID_RETURN_JOURNEY.arrivalAirportName
        returnJourney.arrivalAirportCity shouldBe VALID_RETURN_JOURNEY.arrivalAirportCity
        returnJourney.airlineDataList.size shouldBe VALID_RETURN_JOURNEY.airlineDataList.size
        returnJourney.addDayArrival shouldBe VALID_RETURN_JOURNEY.addDayArrival

        flightSearchReturnViewModel.searchErrorStringId.value shouldBe SearchErrorEnum.NO_ERRORS
        flightSearchReturnViewModel.priceModel.comboKey shouldBe COMBO_KEY
    }

    @Test
    fun onFlightSearchSelected_whenNotLoggedIn_validReturnJourney() {
        // given
        coEvery { userSession.isLoggedIn } returns false
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } returns DEPARTURE_JOURNEY
        coEvery { flightComboKeyUseCase.execute(any(), any()) } returns COMBO_KEY
        val flightSearchPassDataModel = defaultSearchPassData
        flightSearchReturnViewModel.selectedFlightDepartureId = "dummyDepartureId"
        val adapterPosition = 0

        // when
        flightSearchReturnViewModel.onFlightSearchSelected(flightSearchPassDataModel, VALID_RETURN_JOURNEY, adapterPosition)

        // then
        verify {
            flightAnalytics.eventSearchProductClickV2FromList(flightSearchPassDataModel, VALID_RETURN_JOURNEY,
                    adapterPosition, FlightAnalytics.Screen.SEARCH, any())
        }
        val returnJourney = flightSearchReturnViewModel.selectedReturnJourney!!
        returnJourney.duration shouldBe VALID_RETURN_JOURNEY.duration
        returnJourney.totalTransit shouldBe VALID_RETURN_JOURNEY.totalTransit
        returnJourney.totalNumeric shouldBe VALID_RETURN_JOURNEY.totalNumeric
        returnJourney.term shouldBe VALID_RETURN_JOURNEY.term
        returnJourney.specialTagText shouldBe VALID_RETURN_JOURNEY.specialTagText
        returnJourney.routeList.size shouldBe VALID_RETURN_JOURNEY.routeList.size
        returnJourney.showSpecialPriceTag shouldBe VALID_RETURN_JOURNEY.showSpecialPriceTag
        returnJourney.isReturning shouldBe VALID_RETURN_JOURNEY.isReturning
        returnJourney.isRefundable shouldBe VALID_RETURN_JOURNEY.isRefundable
        returnJourney.isBestPairing shouldBe VALID_RETURN_JOURNEY.isBestPairing
        returnJourney.id shouldBe VALID_RETURN_JOURNEY.id
        returnJourney.fare.adult shouldBe VALID_RETURN_JOURNEY.fare.adult
        returnJourney.fare.adultCombo shouldBe VALID_RETURN_JOURNEY.fare.adultCombo
        returnJourney.fare.adultNumeric shouldBe VALID_RETURN_JOURNEY.fare.adultNumeric
        returnJourney.fare.adultNumericCombo shouldBe VALID_RETURN_JOURNEY.fare.adultNumericCombo
        returnJourney.fare.child shouldBe VALID_RETURN_JOURNEY.fare.child
        returnJourney.fare.childCombo shouldBe VALID_RETURN_JOURNEY.fare.childCombo
        returnJourney.fare.childNumeric shouldBe VALID_RETURN_JOURNEY.fare.childNumeric
        returnJourney.fare.childNumericCombo shouldBe VALID_RETURN_JOURNEY.fare.childNumericCombo
        returnJourney.fare.infant shouldBe VALID_RETURN_JOURNEY.fare.infant
        returnJourney.fare.infantCombo shouldBe VALID_RETURN_JOURNEY.fare.infantCombo
        returnJourney.fare.infantNumeric shouldBe VALID_RETURN_JOURNEY.fare.infantNumeric
        returnJourney.fare.infantNumericCombo shouldBe VALID_RETURN_JOURNEY.fare.infantNumericCombo
        returnJourney.durationMinute shouldBe VALID_RETURN_JOURNEY.durationMinute
        returnJourney.departureTimeInt shouldBe VALID_RETURN_JOURNEY.departureTimeInt
        returnJourney.departureAirportName shouldBe VALID_RETURN_JOURNEY.departureAirportName
        returnJourney.departureAirportCity shouldBe VALID_RETURN_JOURNEY.departureAirportCity
        returnJourney.comboPriceNumeric shouldBe VALID_RETURN_JOURNEY.comboPriceNumeric
        returnJourney.comboId shouldBe VALID_RETURN_JOURNEY.comboId
        returnJourney.beforeTotal shouldBe VALID_RETURN_JOURNEY.beforeTotal
        returnJourney.arrivalTimeInt shouldBe VALID_RETURN_JOURNEY.arrivalTimeInt
        returnJourney.arrivalAirportName shouldBe VALID_RETURN_JOURNEY.arrivalAirportName
        returnJourney.arrivalAirportCity shouldBe VALID_RETURN_JOURNEY.arrivalAirportCity
        returnJourney.airlineDataList.size shouldBe VALID_RETURN_JOURNEY.airlineDataList.size
        returnJourney.addDayArrival shouldBe VALID_RETURN_JOURNEY.addDayArrival

        flightSearchReturnViewModel.searchErrorStringId.value shouldBe SearchErrorEnum.NO_ERRORS
        flightSearchReturnViewModel.priceModel.comboKey shouldBe COMBO_KEY
    }

    @Test
    fun onFlightSearchSelectedWithDepartureJourney_whenLoggedIn_notValidReturnJourney() {
        // given
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "dummy user id"
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } returns DEPARTURE_JOURNEY
        coEvery { flightComboKeyUseCase.execute(any(), any()) } returns COMBO_KEY
        val flightSearchPassDataModel = defaultSearchPassData
        flightSearchReturnViewModel.selectedFlightDepartureId = "dummyDepartureId"

        // when
        flightSearchReturnViewModel.getDepartureJourneyDetail()
        flightSearchReturnViewModel.onFlightSearchSelected(flightSearchPassDataModel, NOT_VALID_RETURN_JOURNEY)

        // then
        flightSearchReturnViewModel.searchErrorStringId.value shouldBe SearchErrorEnum.ERROR_RETURN_JOURNEY_TIME
        flightSearchReturnViewModel.priceModel.comboKey shouldBe COMBO_KEY
    }

    @Test
    fun onFlightSearchSelectedWithDepartureJourney_whenNotLoggedIn_notValidReturnJourney() {
        // given
        coEvery { userSession.isLoggedIn } returns false
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } returns DEPARTURE_JOURNEY
        coEvery { flightComboKeyUseCase.execute(any(), any()) } returns COMBO_KEY
        val flightSearchPassDataModel = defaultSearchPassData
        flightSearchReturnViewModel.selectedFlightDepartureId = "dummyDepartureId"

        // when
        flightSearchReturnViewModel.getDepartureJourneyDetail()
        flightSearchReturnViewModel.onFlightSearchSelected(flightSearchPassDataModel, NOT_VALID_RETURN_JOURNEY)

        // then
        flightSearchReturnViewModel.searchErrorStringId.value shouldBe SearchErrorEnum.ERROR_RETURN_JOURNEY_TIME
        flightSearchReturnViewModel.priceModel.comboKey shouldBe COMBO_KEY
    }

    @Test
    fun onFlightSearchSelected_whenLoggedIn_failed() {
        // given
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "dummy user id"
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } coAnswers { throw Throwable("Error") }
        coEvery { flightComboKeyUseCase.execute(any(), any()) } returns COMBO_KEY
        val flightSearchPassDataModel = defaultSearchPassData
        flightSearchReturnViewModel.selectedFlightDepartureId = "dummyDepartureId"

        // when
        flightSearchReturnViewModel.onFlightSearchSelected(flightSearchPassDataModel, VALID_RETURN_JOURNEY)

        // then
        flightSearchReturnViewModel.searchErrorStringId.value shouldBe SearchErrorEnum.ERROR_PICK_JOURNEY
    }

    @Test
    fun onFlightSearchSelected_whenNotLoggedIn_failed() {
        // given
        coEvery { userSession.isLoggedIn } returns false
        coEvery { flightSearchJourneyByIdUseCase.execute(any()) } coAnswers { throw Throwable("Error") }
        coEvery { flightComboKeyUseCase.execute(any(), any()) } returns COMBO_KEY
        val flightSearchPassDataModel = defaultSearchPassData
        flightSearchReturnViewModel.selectedFlightDepartureId = "dummyDepartureId"

        // when
        flightSearchReturnViewModel.onFlightSearchSelected(flightSearchPassDataModel, VALID_RETURN_JOURNEY)

        // then
        flightSearchReturnViewModel.searchErrorStringId.value shouldBe SearchErrorEnum.ERROR_PICK_JOURNEY
    }

}