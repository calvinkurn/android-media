package com.tokopedia.flight.booking.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.flight.R
import com.tokopedia.flight.booking.data.*
import com.tokopedia.flight.booking.data.mapper.FlightBookingMapper
import com.tokopedia.flight.common.data.model.FlightError
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
import com.tokopedia.flight.dummy.*
import com.tokopedia.flight.passenger.constant.FlightBookingPassenger
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityMetaModel
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityModel
import com.tokopedia.flight.passenger.view.model.FlightBookingPassengerModel
import com.tokopedia.flight.shouldBe
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.model.FlightCancelVoucher
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.reflect.Type

/**
 * @author by furqan on 29/06/2020
 */
@RunWith(JUnit4::class)
class FlightBookingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcherProvider = CoroutineTestDispatchersProvider
    private val travelTickerUseCase = mockk<TravelTickerCoroutineUseCase>()

    @RelaxedMockK
    private lateinit var graphqlRepository: GraphqlRepository

    private lateinit var viewModel: FlightBookingViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = FlightBookingViewModel(graphqlRepository, travelTickerUseCase, testDispatcherProvider)
    }

    @Test
    fun init_allLiveDataShouldBeEmptyList() {
        // given

        // when
        val flightViewModel = FlightBookingViewModel(graphqlRepository, travelTickerUseCase, testDispatcherProvider)
        flightViewModel.verifyRetryCount = 2
        flightViewModel.retryCount = 1
        flightViewModel.pastVerifyParam = "dummy"
        flightViewModel.isStillLoading = true

        // then
        flightViewModel.flightPriceData.value!!.size shouldBe 0
        flightViewModel.flightOtherPriceData.value!!.size shouldBe 0
        flightViewModel.flightAmenityPriceData.value!!.size shouldBe 0
        flightViewModel.flightPassengersData.value!!.size shouldBe 0
        flightViewModel.retryCount shouldBe 1
        flightViewModel.verifyRetryCount shouldBe 2
        flightViewModel.pastVerifyParam shouldBe "dummy"
        flightViewModel.isStillLoading shouldBe true

        val promo = flightViewModel.flightPromoResult.value!!
        promo.isCouponActive shouldBe 0
        promo.isCouponEnable shouldBe false
        promo.promoData.typePromo shouldBe 0
        promo.promoData.promoCode shouldBe ""
        promo.promoData.description shouldBe ""
        promo.promoData.amount shouldBe 0
        promo.promoData.state shouldBe TickerCheckoutView.State.EMPTY
    }

    @Test
    fun setAndGetFlightDetailModel() {
        // given
        val flightDetailModel = DUMMY_BOOKING_FLIGHT_DETAIL

        // when
        viewModel.flightDetailModels = flightDetailModel
        val departureJourney = viewModel.getDepartureJourney()
        val returnJourney = viewModel.getReturnJourney()

        // then
        departureJourney!!.flightClass shouldBe flightDetailModel[0].flightClass
        departureJourney.countInfant shouldBe flightDetailModel[0].countInfant
        departureJourney.countChild shouldBe flightDetailModel[0].countChild
        departureJourney.countAdult shouldBe flightDetailModel[0].countAdult
        departureJourney.airlineDataList.size shouldBe flightDetailModel[0].airlineDataList.size
        departureJourney.arrivalTime shouldBe flightDetailModel[0].arrivalTime
        departureJourney.departureTime shouldBe flightDetailModel[0].departureTime
        departureJourney.routeList.size shouldBe flightDetailModel[0].routeList.size
        departureJourney.infantNumericPrice shouldBe flightDetailModel[0].infantNumericPrice
        departureJourney.childNumericPrice shouldBe flightDetailModel[0].childNumericPrice
        departureJourney.adultNumericPrice shouldBe flightDetailModel[0].adultNumericPrice
        departureJourney.totalNumeric shouldBe flightDetailModel[0].totalNumeric
        departureJourney.isRefundable shouldBe flightDetailModel[0].isRefundable
        departureJourney.beforeTotal shouldBe flightDetailModel[0].beforeTotal
        departureJourney.totalTransit shouldBe flightDetailModel[0].totalTransit
        departureJourney.arrivalAirportCity shouldBe flightDetailModel[0].arrivalAirportCity
        departureJourney.departureAirportCity shouldBe flightDetailModel[0].departureAirportCity
        departureJourney.id shouldBe flightDetailModel[0].id
        departureJourney.term shouldBe flightDetailModel[0].term

        returnJourney!!.flightClass shouldBe flightDetailModel[1].flightClass
        returnJourney.countInfant shouldBe flightDetailModel[1].countInfant
        returnJourney.countChild shouldBe flightDetailModel[1].countChild
        returnJourney.countAdult shouldBe flightDetailModel[1].countAdult
        returnJourney.airlineDataList.size shouldBe flightDetailModel[1].airlineDataList.size
        returnJourney.arrivalTime shouldBe flightDetailModel[1].arrivalTime
        returnJourney.departureTime shouldBe flightDetailModel[1].departureTime
        returnJourney.routeList.size shouldBe flightDetailModel[1].routeList.size
        returnJourney.infantNumericPrice shouldBe flightDetailModel[1].infantNumericPrice
        returnJourney.childNumericPrice shouldBe flightDetailModel[1].childNumericPrice
        returnJourney.adultNumericPrice shouldBe flightDetailModel[1].adultNumericPrice
        returnJourney.totalNumeric shouldBe flightDetailModel[1].totalNumeric
        returnJourney.isRefundable shouldBe flightDetailModel[1].isRefundable
        returnJourney.beforeTotal shouldBe flightDetailModel[1].beforeTotal
        returnJourney.totalTransit shouldBe flightDetailModel[1].totalTransit
        returnJourney.arrivalAirportCity shouldBe flightDetailModel[1].arrivalAirportCity
        returnJourney.departureAirportCity shouldBe flightDetailModel[1].departureAirportCity
        returnJourney.id shouldBe flightDetailModel[1].id
        returnJourney.term shouldBe flightDetailModel[1].term
    }

    @Test
    fun setAndGetFlightDetailModelWithNullReturnJourney() {
        // given
        val flightDetailModel = DUMMY_BOOKING_FLIGHT_DETAIL

        // when
        viewModel.flightDetailModels = arrayListOf(flightDetailModel[0])
        val departureJourney = viewModel.getDepartureJourney()
        val returnJourney = viewModel.getReturnJourney()

        // then
        departureJourney!!.flightClass shouldBe flightDetailModel[0].flightClass
        departureJourney.countInfant shouldBe flightDetailModel[0].countInfant
        departureJourney.countChild shouldBe flightDetailModel[0].countChild
        departureJourney.countAdult shouldBe flightDetailModel[0].countAdult
        departureJourney.airlineDataList.size shouldBe flightDetailModel[0].airlineDataList.size
        departureJourney.arrivalTime shouldBe flightDetailModel[0].arrivalTime
        departureJourney.departureTime shouldBe flightDetailModel[0].departureTime
        departureJourney.routeList.size shouldBe flightDetailModel[0].routeList.size
        departureJourney.infantNumericPrice shouldBe flightDetailModel[0].infantNumericPrice
        departureJourney.childNumericPrice shouldBe flightDetailModel[0].childNumericPrice
        departureJourney.adultNumericPrice shouldBe flightDetailModel[0].adultNumericPrice
        departureJourney.totalNumeric shouldBe flightDetailModel[0].totalNumeric
        departureJourney.isRefundable shouldBe flightDetailModel[0].isRefundable
        departureJourney.beforeTotal shouldBe flightDetailModel[0].beforeTotal
        departureJourney.totalTransit shouldBe flightDetailModel[0].totalTransit
        departureJourney.arrivalAirportCity shouldBe flightDetailModel[0].arrivalAirportCity
        departureJourney.departureAirportCity shouldBe flightDetailModel[0].departureAirportCity
        departureJourney.id shouldBe flightDetailModel[0].id
        departureJourney.term shouldBe flightDetailModel[0].term

        returnJourney shouldBe null
    }

    @Test
    fun getPriceData_defaultShouldReturnEmptyList() {
        // given

        // when
        val priceData = viewModel.getPriceData()

        // then
        priceData.size shouldBe 0
    }

    @Test
    fun getOtherPriceData_defaultShouldReturnEmptyList() {
        // given

        // when
        val otherPriceData = viewModel.getOtherPriceData()

        // then
        otherPriceData.size shouldBe 0
    }

    @Test
    fun getAmenityPriceData_defaultShouldReturnEmptyList() {
        // given

        // when
        val amenityPriceData = viewModel.getAmenityPriceData()

        // then
        amenityPriceData.size shouldBe 0
    }

    @Test
    fun getProfile_failedToFetch_profileShouldFail() {
        // given
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable("Failed to Fetch") }

        // when
        viewModel.getProfile("")

        // then
        assert(viewModel.profileResult.value != null)
        assert(viewModel.profileResult.value!! is Fail)
        (viewModel.profileResult.value!! as Fail).throwable.message shouldBe "Failed to Fetch"
    }

    @Test
    fun getProfile_phoneStartWith62_fetchSuccess() {
        // given
        val profile = DUMMY_PROFILE
        profile.profileInfo.phone = "6281111111111"
        val gqlResponse = GraphqlResponse(
                mapOf<Type, Any>(
                        ProfilePojo::class.java to DUMMY_PROFILE
                ),
                mapOf(),
                false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        // when
        viewModel.getProfile("")

        // then
        assert(viewModel.profileResult.value != null)
        assert(viewModel.profileResult.value!! is Success)

        val data = (viewModel.profileResult.value as Success).data
        data.phone shouldBe "81111111111"
        data.profilePicture shouldBe profile.profileInfo.profilePicture
        data.userId shouldBe profile.profileInfo.userId
        data.fullName shouldBe profile.profileInfo.fullName
        data.firstName shouldBe profile.profileInfo.firstName
        data.email shouldBe profile.profileInfo.email
        data.birthday shouldBe profile.profileInfo.birthday
        data.gender shouldBe profile.profileInfo.gender
        data.isPhoneVerified shouldBe profile.profileInfo.isPhoneVerified
        data.isCreatedPassword shouldBe profile.profileInfo.isCreatedPassword
        data.isLoggedIn shouldBe profile.profileInfo.isLoggedIn

        viewModel.getUserId() shouldBe DUMMY_PROFILE.profileInfo.userId
    }

    @Test
    fun getProfile_phoneStartWithPlus_fetchSuccess() {
        // given
        val profile = DUMMY_PROFILE
        profile.profileInfo.phone = "+6281111111111"
        val gqlResponse = GraphqlResponse(
                mapOf<Type, Any>(
                        ProfilePojo::class.java to DUMMY_PROFILE
                ),
                mapOf(),
                false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        // when
        viewModel.getProfile("")

        // then
        assert(viewModel.profileResult.value != null)
        assert(viewModel.profileResult.value!! is Success)

        val data = (viewModel.profileResult.value as Success).data
        data.phone shouldBe "81111111111"
        data.profilePicture shouldBe profile.profileInfo.profilePicture
        data.userId shouldBe profile.profileInfo.userId
        data.fullName shouldBe profile.profileInfo.fullName
        data.firstName shouldBe profile.profileInfo.firstName
        data.email shouldBe profile.profileInfo.email
        data.birthday shouldBe profile.profileInfo.birthday
        data.gender shouldBe profile.profileInfo.gender
        data.isPhoneVerified shouldBe profile.profileInfo.isPhoneVerified
        data.isCreatedPassword shouldBe profile.profileInfo.isCreatedPassword
        data.isLoggedIn shouldBe profile.profileInfo.isLoggedIn
    }

    @Test
    fun getProfile_phoneStartWithZero_fetchSuccess() {
        // given
        val profile = DUMMY_PROFILE
        profile.profileInfo.phone = "081111111111"
        val gqlResponse = GraphqlResponse(
                mapOf<Type, Any>(
                        ProfilePojo::class.java to DUMMY_PROFILE
                ),
                mapOf(),
                false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        // when
        viewModel.getProfile("")

        // then
        assert(viewModel.profileResult.value != null)
        assert(viewModel.profileResult.value!! is Success)

        val data = (viewModel.profileResult.value as Success).data
        data.phone shouldBe "81111111111"
        data.profilePicture shouldBe profile.profileInfo.profilePicture
        data.userId shouldBe profile.profileInfo.userId
        data.fullName shouldBe profile.profileInfo.fullName
        data.firstName shouldBe profile.profileInfo.firstName
        data.email shouldBe profile.profileInfo.email
        data.birthday shouldBe profile.profileInfo.birthday
        data.gender shouldBe profile.profileInfo.gender
        data.isPhoneVerified shouldBe profile.profileInfo.isPhoneVerified
        data.isCreatedPassword shouldBe profile.profileInfo.isCreatedPassword
        data.isLoggedIn shouldBe profile.profileInfo.isLoggedIn
    }

    @Test
    fun onTravelAsPassenger_withExistedProfile() {
        // given
        val profile = DUMMY_PROFILE
        profile.profileInfo.phone = "081111111111"
        profile.profileInfo.birthday = "2020-11-11T10:10:10Z"
        val gqlResponse = GraphqlResponse(
                mapOf<Type, Any>(
                        ProfilePojo::class.java to DUMMY_PROFILE
                ),
                mapOf(),
                false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        viewModel.getProfile("")
        val userName = "Dummy User Name"
        val bookingParam = DUMMY_BOOKING_MODEL
        bookingParam.isMandatoryDob = true
        viewModel.setFlightBookingParam(bookingParam)

        // when
        val passenger = viewModel.onTravellerAsPassenger(userName)

        // then
        passenger.passengerLocalId shouldBe 1
        passenger.type shouldBe FlightBookingPassenger.ADULT
        passenger.flightBookingLuggageMetaViewModels.size shouldBe 0
        passenger.flightBookingMealMetaViewModels.size shouldBe 0
        passenger.headerTitle shouldBe userName
        passenger.passengerFirstName shouldBe userName
        passenger.passengerBirthdate shouldBe "2020-11-11"
    }

    @Test
    fun onTravelAsPassenger_withoutExistedProfile() {
        // given
        val userName = "Dummy User Name"
        val bookingParam = DUMMY_BOOKING_MODEL
        bookingParam.isMandatoryDob = false
        viewModel.setFlightBookingParam(bookingParam)

        // when
        val passenger = viewModel.onTravellerAsPassenger(userName)

        // then
        passenger.passengerLocalId shouldBe 1
        passenger.type shouldBe FlightBookingPassenger.ADULT
        passenger.flightBookingLuggageMetaViewModels.size shouldBe 0
        passenger.flightBookingMealMetaViewModels.size shouldBe 0
        passenger.headerTitle shouldBe userName
        passenger.passengerFirstName shouldBe userName
    }

    @Test
    fun setPassengerModels() {
        // given

        // when
        viewModel.setPassengerModels(DUMMY_BOOKING_PASSENGER)

        // then
        viewModel.flightPassengersData.value!!.size shouldBe DUMMY_BOOKING_PASSENGER.size
        val passengerData = viewModel.flightPassengersData.value!!
        for ((index, item) in passengerData.withIndex()) {
            item.flightBookingLuggageMetaViewModels.size shouldBe DUMMY_BOOKING_PASSENGER[index].flightBookingLuggageMetaViewModels.size
            item.flightBookingMealMetaViewModels.size shouldBe DUMMY_BOOKING_PASSENGER[index].flightBookingMealMetaViewModels.size
            item.headerTitle shouldBe DUMMY_BOOKING_PASSENGER[index].headerTitle
            item.passengerId shouldBe DUMMY_BOOKING_PASSENGER[index].passengerId
            item.passengerBirthdate shouldBe DUMMY_BOOKING_PASSENGER[index].passengerBirthdate
            item.passengerFirstName shouldBe DUMMY_BOOKING_PASSENGER[index].passengerFirstName
            item.passengerLastName shouldBe DUMMY_BOOKING_PASSENGER[index].passengerLastName
        }
    }

    @Test
    fun setPriceData() {
        // given

        // when
        viewModel.setPriceData(DUMMY_CART_PRICE)

        // then
        viewModel.flightPriceData.value!!.size shouldBe DUMMY_CART_PRICE.size
        val flightCart = viewModel.flightPriceData.value!!

        for ((index, item) in flightCart.withIndex()) {
            item.label shouldBe DUMMY_CART_PRICE[index].label
            item.price shouldBe DUMMY_CART_PRICE[index].price
            item.priceDetailId shouldBe DUMMY_CART_PRICE[index].priceDetailId
            item.priceNumeric shouldBe DUMMY_CART_PRICE[index].priceNumeric
        }
    }

    @Test
    fun setOtherPriceData() {
        // given

        // when
        viewModel.setOtherPriceData(DUMMY_OTHER_PRICE)

        // then
        viewModel.flightOtherPriceData.value!!.size shouldBe DUMMY_OTHER_PRICE.size
        val flightCart = viewModel.flightOtherPriceData.value!!

        for ((index, item) in flightCart.withIndex()) {
            item.label shouldBe DUMMY_OTHER_PRICE[index].label
            item.price shouldBe DUMMY_OTHER_PRICE[index].price
            item.priceDetailId shouldBe DUMMY_OTHER_PRICE[index].priceDetailId
            item.priceNumeric shouldBe DUMMY_OTHER_PRICE[index].priceNumeric
        }
    }

    @Test
    fun setAmenityPriceData() {
        // given

        // when
        viewModel.setAmenityPriceData(DUMMY_AMENITY_PRICE)

        // then
        viewModel.flightAmenityPriceData.value!!.size shouldBe DUMMY_AMENITY_PRICE.size
        val flightCart = viewModel.flightAmenityPriceData.value!!

        for ((index, item) in flightCart.withIndex()) {
            item.label shouldBe DUMMY_AMENITY_PRICE[index].label
            item.price shouldBe DUMMY_AMENITY_PRICE[index].price
            item.priceDetailId shouldBe DUMMY_AMENITY_PRICE[index].priceDetailId
            item.priceNumeric shouldBe DUMMY_AMENITY_PRICE[index].priceNumeric
        }
    }

    @Test
    fun updatePromoData() {
        // given
        val promoData = PromoData(1, "CODE", "Dummy Promo", "Promo", 10000, TickerCheckoutView.State.EMPTY)

        // when
        viewModel.updatePromoData(promoData)

        // then
        viewModel.flightPromoResult.value!!.promoData.typePromo shouldBe promoData.typePromo
        viewModel.flightPromoResult.value!!.promoData.promoCode shouldBe promoData.promoCode
        viewModel.flightPromoResult.value!!.promoData.description shouldBe promoData.description
        viewModel.flightPromoResult.value!!.promoData.title shouldBe promoData.title
        viewModel.flightPromoResult.value!!.promoData.amount shouldBe promoData.amount
        viewModel.flightPromoResult.value!!.promoData.state shouldBe promoData.state
    }

    @Test
    fun updatePriceData() {
        // given

        // when
        viewModel.updateFlightPriceData(DUMMY_CART_PRICE)

        // then
        viewModel.flightPriceData.value!!.size shouldBe DUMMY_CART_PRICE.size
        val flightCart = viewModel.flightPriceData.value!!

        for ((index, item) in flightCart.withIndex()) {
            item.label shouldBe DUMMY_CART_PRICE[index].label
            item.price shouldBe DUMMY_CART_PRICE[index].price
            item.priceDetailId shouldBe DUMMY_CART_PRICE[index].priceDetailId
            item.priceNumeric shouldBe DUMMY_CART_PRICE[index].priceNumeric
        }
    }

    @Test
    fun getUserId_beforeSet_shouldBeEmpty() {
        // given

        // when
        val userId = viewModel.getUserId()

        // then
        userId shouldBe ""
    }

    @Test
    fun getInvoiceId_beforeSet_shouldBeEmpty() {
        // given

        // when
        val invoiceId = viewModel.getInvoiceId()

        // then
        invoiceId shouldBe ""
    }

    @Test
    fun getMeals_beforeSet_shouldBeEmpty() {
        // given

        // when
        val meals = viewModel.getMealViewModels()

        // then
        meals.size shouldBe 0
    }

    @Test
    fun getLuggage_beforeSet_shouldBeEmpty() {
        // given

        // when
        val luggage = viewModel.getLuggageViewModels()

        // then
        luggage.size shouldBe 0
    }

    @Test
    fun getPassengers_beforeSet_shouldBeEmpty() {
        // given

        // when
        val passengers = viewModel.getPassengerModels()

        // then
        passengers.size shouldBe 0
    }

    @Test
    fun changeFlightBookingParam() {
        // given
        val flightBookingModel = DUMMY_BOOKING_MODEL

        // when
        viewModel.setFlightBookingParam(flightBookingModel)
        val currentBookingParam = viewModel.getFlightBookingParam()

        // then
        currentBookingParam.cartId shouldBe flightBookingModel.cartId
        currentBookingParam.departureDate shouldBe flightBookingModel.departureDate
        currentBookingParam.departureId shouldBe flightBookingModel.departureId
        currentBookingParam.departureTerm shouldBe flightBookingModel.departureTerm
        currentBookingParam.insurances.size shouldBe flightBookingModel.insurances.size
        currentBookingParam.isDomestic shouldBe flightBookingModel.isDomestic
        currentBookingParam.isMandatoryDob shouldBe flightBookingModel.isMandatoryDob
        currentBookingParam.returnId shouldBe flightBookingModel.returnId
        currentBookingParam.returnTerm shouldBe flightBookingModel.returnTerm
        currentBookingParam.searchParam.departureDate shouldBe flightBookingModel.searchParam.departureDate
        currentBookingParam.searchParam.isOneWay shouldBe flightBookingModel.searchParam.isOneWay
        currentBookingParam.searchParam.linkUrl shouldBe flightBookingModel.searchParam.linkUrl
        currentBookingParam.searchParam.returnDate shouldBe flightBookingModel.searchParam.returnDate
        currentBookingParam.searchParam.searchRequestId shouldBe flightBookingModel.searchParam.searchRequestId
        currentBookingParam.searchParam.flightClass.id shouldBe flightBookingModel.searchParam.flightClass.id
        currentBookingParam.searchParam.flightClass.title shouldBe flightBookingModel.searchParam.flightClass.title
        currentBookingParam.searchParam.departureAirport.airportCode shouldBe flightBookingModel.searchParam.departureAirport.airportCode
        currentBookingParam.searchParam.departureAirport.airportName shouldBe flightBookingModel.searchParam.departureAirport.airportName
        currentBookingParam.searchParam.departureAirport.countryName shouldBe flightBookingModel.searchParam.departureAirport.countryName
        currentBookingParam.searchParam.arrivalAirport.airportCode shouldBe flightBookingModel.searchParam.arrivalAirport.airportCode
        currentBookingParam.searchParam.arrivalAirport.airportName shouldBe flightBookingModel.searchParam.arrivalAirport.airportName
        currentBookingParam.searchParam.arrivalAirport.countryName shouldBe flightBookingModel.searchParam.arrivalAirport.countryName
        currentBookingParam.searchParam.flightPassengerModel.adult shouldBe flightBookingModel.searchParam.flightPassengerModel.adult
        currentBookingParam.searchParam.flightPassengerModel.children shouldBe flightBookingModel.searchParam.flightPassengerModel.children
        currentBookingParam.searchParam.flightPassengerModel.infant shouldBe flightBookingModel.searchParam.flightPassengerModel.infant
        currentBookingParam.flightPriceModel.comboKey shouldBe flightBookingModel.flightPriceModel.comboKey
        currentBookingParam.flightPriceModel.departurePrice!!.adult shouldBe flightBookingModel.flightPriceModel.departurePrice!!.adult
        currentBookingParam.flightPriceModel.departurePrice!!.adultCombo shouldBe flightBookingModel.flightPriceModel.departurePrice!!.adultCombo
        currentBookingParam.flightPriceModel.departurePrice!!.adultNumeric shouldBe flightBookingModel.flightPriceModel.departurePrice!!.adultNumeric
        currentBookingParam.flightPriceModel.departurePrice!!.adultNumericCombo shouldBe flightBookingModel.flightPriceModel.departurePrice!!.adultNumericCombo
        currentBookingParam.flightPriceModel.departurePrice!!.child shouldBe flightBookingModel.flightPriceModel.departurePrice!!.child
        currentBookingParam.flightPriceModel.departurePrice!!.childCombo shouldBe flightBookingModel.flightPriceModel.departurePrice!!.childCombo
        currentBookingParam.flightPriceModel.departurePrice!!.childNumeric shouldBe flightBookingModel.flightPriceModel.departurePrice!!.childNumeric
        currentBookingParam.flightPriceModel.departurePrice!!.childNumericCombo shouldBe flightBookingModel.flightPriceModel.departurePrice!!.childNumericCombo
        currentBookingParam.flightPriceModel.departurePrice!!.infant shouldBe flightBookingModel.flightPriceModel.departurePrice!!.infant
        currentBookingParam.flightPriceModel.departurePrice!!.infantCombo shouldBe flightBookingModel.flightPriceModel.departurePrice!!.infantCombo
        currentBookingParam.flightPriceModel.departurePrice!!.infantNumeric shouldBe flightBookingModel.flightPriceModel.departurePrice!!.infantNumeric
        currentBookingParam.flightPriceModel.departurePrice!!.infantNumericCombo shouldBe flightBookingModel.flightPriceModel.departurePrice!!.infantNumericCombo
        currentBookingParam.flightPriceModel.returnPrice!!.adult shouldBe flightBookingModel.flightPriceModel.returnPrice!!.adult
        currentBookingParam.flightPriceModel.returnPrice!!.adultCombo shouldBe flightBookingModel.flightPriceModel.returnPrice!!.adultCombo
        currentBookingParam.flightPriceModel.returnPrice!!.adultNumeric shouldBe flightBookingModel.flightPriceModel.returnPrice!!.adultNumeric
        currentBookingParam.flightPriceModel.returnPrice!!.adultNumericCombo shouldBe flightBookingModel.flightPriceModel.returnPrice!!.adultNumericCombo
        currentBookingParam.flightPriceModel.returnPrice!!.child shouldBe flightBookingModel.flightPriceModel.returnPrice!!.child
        currentBookingParam.flightPriceModel.returnPrice!!.childCombo shouldBe flightBookingModel.flightPriceModel.returnPrice!!.childCombo
        currentBookingParam.flightPriceModel.returnPrice!!.childNumeric shouldBe flightBookingModel.flightPriceModel.returnPrice!!.childNumeric
        currentBookingParam.flightPriceModel.returnPrice!!.childNumericCombo shouldBe flightBookingModel.flightPriceModel.returnPrice!!.childNumericCombo
        currentBookingParam.flightPriceModel.returnPrice!!.infant shouldBe flightBookingModel.flightPriceModel.returnPrice!!.infant
        currentBookingParam.flightPriceModel.returnPrice!!.infantCombo shouldBe flightBookingModel.flightPriceModel.returnPrice!!.infantCombo
        currentBookingParam.flightPriceModel.returnPrice!!.infantNumeric shouldBe flightBookingModel.flightPriceModel.returnPrice!!.infantNumeric
        currentBookingParam.flightPriceModel.returnPrice!!.infantNumericCombo shouldBe flightBookingModel.flightPriceModel.returnPrice!!.infantNumericCombo
    }

    @Test
    fun getSearchParam() {
        // given
        val flightBookingModel = DUMMY_BOOKING_MODEL

        // when
        viewModel.setFlightBookingParam(flightBookingModel)
        viewModel.setSearchParam(
                flightBookingModel.departureId,
                flightBookingModel.returnId,
                flightBookingModel.departureTerm,
                flightBookingModel.returnTerm,
                flightBookingModel.searchParam,
                flightBookingModel.flightPriceModel
        )
        val searchParam = viewModel.getSearchParam()

        // then
        searchParam.departureDate shouldBe flightBookingModel.searchParam.departureDate
        searchParam.isOneWay shouldBe flightBookingModel.searchParam.isOneWay
        searchParam.linkUrl shouldBe flightBookingModel.searchParam.linkUrl
        searchParam.returnDate shouldBe flightBookingModel.searchParam.returnDate
        searchParam.searchRequestId shouldBe flightBookingModel.searchParam.searchRequestId
        searchParam.flightClass.id shouldBe flightBookingModel.searchParam.flightClass.id
        searchParam.flightClass.title shouldBe flightBookingModel.searchParam.flightClass.title
        searchParam.departureAirport.airportCode shouldBe flightBookingModel.searchParam.departureAirport.airportCode
        searchParam.departureAirport.airportName shouldBe flightBookingModel.searchParam.departureAirport.airportName
        searchParam.departureAirport.countryName shouldBe flightBookingModel.searchParam.departureAirport.countryName
        searchParam.arrivalAirport.airportCode shouldBe flightBookingModel.searchParam.arrivalAirport.airportCode
        searchParam.arrivalAirport.airportName shouldBe flightBookingModel.searchParam.arrivalAirport.airportName
        searchParam.arrivalAirport.countryName shouldBe flightBookingModel.searchParam.arrivalAirport.countryName
        searchParam.flightPassengerModel.adult shouldBe flightBookingModel.searchParam.flightPassengerModel.adult
        searchParam.flightPassengerModel.children shouldBe flightBookingModel.searchParam.flightPassengerModel.children
        searchParam.flightPassengerModel.infant shouldBe flightBookingModel.searchParam.flightPassengerModel.infant

    }

    @Test
    fun getBookingParams() {
        // given
        val flightBookingModel = DUMMY_BOOKING_MODEL

        // when
        viewModel.setFlightBookingParam(flightBookingModel)
        viewModel.setCartId(DUMMY_BOOKING_MODEL.cartId)
        val flightPriceModel = viewModel.getFlightPriceModel()
        val mandatoryDob = viewModel.getMandatoryDOB()
        val departureId = viewModel.getDepartureId()
        val returnId = viewModel.getReturnId()
        val departureTerm = viewModel.getDepartureTerm()
        val returnTerm = viewModel.getReturnTerm()
        val departureDate = viewModel.getDepartureDate()
        val isDomestic = viewModel.flightIsDomestic()
        val cartId = viewModel.getCartId()

        // then
        flightPriceModel.comboKey shouldBe flightBookingModel.flightPriceModel.comboKey
        flightPriceModel.departurePrice!!.adult shouldBe flightBookingModel.flightPriceModel.departurePrice!!.adult
        flightPriceModel.departurePrice!!.adultCombo shouldBe flightBookingModel.flightPriceModel.departurePrice!!.adultCombo
        flightPriceModel.departurePrice!!.adultNumeric shouldBe flightBookingModel.flightPriceModel.departurePrice!!.adultNumeric
        flightPriceModel.departurePrice!!.adultNumericCombo shouldBe flightBookingModel.flightPriceModel.departurePrice!!.adultNumericCombo
        flightPriceModel.departurePrice!!.child shouldBe flightBookingModel.flightPriceModel.departurePrice!!.child
        flightPriceModel.departurePrice!!.childCombo shouldBe flightBookingModel.flightPriceModel.departurePrice!!.childCombo
        flightPriceModel.departurePrice!!.childNumeric shouldBe flightBookingModel.flightPriceModel.departurePrice!!.childNumeric
        flightPriceModel.departurePrice!!.childNumericCombo shouldBe flightBookingModel.flightPriceModel.departurePrice!!.childNumericCombo
        flightPriceModel.departurePrice!!.infant shouldBe flightBookingModel.flightPriceModel.departurePrice!!.infant
        flightPriceModel.departurePrice!!.infantCombo shouldBe flightBookingModel.flightPriceModel.departurePrice!!.infantCombo
        flightPriceModel.departurePrice!!.infantNumeric shouldBe flightBookingModel.flightPriceModel.departurePrice!!.infantNumeric
        flightPriceModel.departurePrice!!.infantNumericCombo shouldBe flightBookingModel.flightPriceModel.departurePrice!!.infantNumericCombo
        flightPriceModel.returnPrice!!.adult shouldBe flightBookingModel.flightPriceModel.returnPrice!!.adult
        flightPriceModel.returnPrice!!.adultCombo shouldBe flightBookingModel.flightPriceModel.returnPrice!!.adultCombo
        flightPriceModel.returnPrice!!.adultNumeric shouldBe flightBookingModel.flightPriceModel.returnPrice!!.adultNumeric
        flightPriceModel.returnPrice!!.adultNumericCombo shouldBe flightBookingModel.flightPriceModel.returnPrice!!.adultNumericCombo
        flightPriceModel.returnPrice!!.child shouldBe flightBookingModel.flightPriceModel.returnPrice!!.child
        flightPriceModel.returnPrice!!.childCombo shouldBe flightBookingModel.flightPriceModel.returnPrice!!.childCombo
        flightPriceModel.returnPrice!!.childNumeric shouldBe flightBookingModel.flightPriceModel.returnPrice!!.childNumeric
        flightPriceModel.returnPrice!!.childNumericCombo shouldBe flightBookingModel.flightPriceModel.returnPrice!!.childNumericCombo
        flightPriceModel.returnPrice!!.infant shouldBe flightBookingModel.flightPriceModel.returnPrice!!.infant
        flightPriceModel.returnPrice!!.infantCombo shouldBe flightBookingModel.flightPriceModel.returnPrice!!.infantCombo
        flightPriceModel.returnPrice!!.infantNumeric shouldBe flightBookingModel.flightPriceModel.returnPrice!!.infantNumeric
        flightPriceModel.returnPrice!!.infantNumericCombo shouldBe flightBookingModel.flightPriceModel.returnPrice!!.infantNumericCombo

        mandatoryDob shouldBe flightBookingModel.isMandatoryDob
        departureId shouldBe flightBookingModel.departureId
        returnId shouldBe flightBookingModel.returnId
        departureTerm shouldBe flightBookingModel.departureTerm
        returnTerm shouldBe flightBookingModel.returnTerm
        departureDate shouldBe flightBookingModel.departureDate
        isDomestic shouldBe flightBookingModel.isDomestic
        cartId shouldBe flightBookingModel.cartId
    }

    @Test
    fun validateDataAndVerifyCart_contactNameEmpty_validationFailed() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = ""
        val contactEmail = ""
        val contactPhone = ""
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""

        // when
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.errorToastMessageData.value shouldBe R.string.flight_booking_contact_name_empty_error
    }

    @Test
    fun validateDataAndVerifyCart_contactNameAlphanumeric_validationFailed() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy n4m3"
        val contactEmail = ""
        val contactPhone = ""
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""

        // when
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.errorToastMessageData.value shouldBe R.string.flight_booking_contact_name_alpha_space_error
    }

    @Test
    fun validateDataAndVerifyCart_contactEmailEmpty_validationFailed() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = ""
        val contactPhone = ""
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""

        // when
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.errorToastMessageData.value shouldBe R.string.flight_booking_contact_email_empty_error
    }

    @Test
    fun validateDataAndVerifyCart_contactEmailNotValid_validationFailed() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummy email"
        val contactPhone = ""
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""

        // when
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.errorToastMessageData.value shouldBe R.string.flight_booking_contact_email_invalid_error
    }

    @Test
    fun validateDataAndVerifyCart_contactEmailContainsProhibitedChars_validationFailed() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail+01@gmail.com"
        val contactPhone = ""
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""

        // when
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.errorToastMessageData.value shouldBe R.string.flight_booking_contact_email_invalid_error
    }

    @Test
    fun validateDataAndVerifyCart_contactPhoneEmpty_validationFailed() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = ""
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""

        // when
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.errorToastMessageData.value shouldBe R.string.flight_booking_contact_phone_empty_error
    }

    @Test
    fun validateDataAndVerifyCart_contactPhoneAlphanumeric_validationFailed() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "O81313131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""

        // when
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.errorToastMessageData.value shouldBe R.string.flight_booking_contact_phone_invalid_error
    }

    @Test
    fun validateDataAndVerifyCart_contactPhoneMoreThan13_validationFailed() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "08131313131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""

        // when
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.errorToastMessageData.value shouldBe R.string.flight_booking_contact_phone_max_length_error
    }

    @Test
    fun validateDataAndVerifyCart_contactPhoneLessThan9_validationFailed() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "08131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""

        // when
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.errorToastMessageData.value shouldBe R.string.flight_booking_contact_phone_min_length_error
    }

    @Test
    fun validateDataAndVerifyCart_passengerFirstNameNull_validationFailed() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "0813131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""
        val passenger = FlightBookingPassengerModel()

        // when
        viewModel.setPassengerModels(arrayListOf(passenger))
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.errorToastMessageData.value shouldBe R.string.flight_booking_passenger_not_fullfilled_error
    }

    @Test
    fun validateDataAndVerifyCart_passengerLastNameNull_validationFailed() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "0813131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""
        val passenger = FlightBookingPassengerModel().apply {
            passengerFirstName = "Dummy First"
        }

        // when
        viewModel.setPassengerModels(arrayListOf(passenger))
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.errorToastMessageData.value shouldBe R.string.flight_booking_passenger_not_fullfilled_error
    }

    @Test
    fun validateDataAndVerifyCart_validationSuccess() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "0813131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""
        val passenger = FlightBookingPassengerModel().apply {
            passengerFirstName = "Dummy First"
            passengerLastName = "Last"
        }

        // when
        viewModel.setPassengerModels(arrayListOf(passenger))
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.errorToastMessageData.value shouldBe 0
    }

    @Test
    fun validateDataAndVerifyCart_samePassVerifyParam_validationSuccess() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "0813131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""
        val passenger = FlightBookingPassengerModel().apply {
            passengerFirstName = "Dummy First"
            passengerLastName = "Last"
            flightBookingMealMetaViewModels = arrayListOf()
            flightBookingLuggageMetaViewModels = arrayListOf()
        }
        val cartMeta = FlightVerifyParam.MetaData("dummy",
                contactName, contactEmail, contactPhone, contactCountry,
                "127.0.0.1", "Android",
                arrayListOf(FlightVerifyParam.Passenger(
                        type = passenger.type,
                        title = passenger.passengerTitleId,
                        firstName = passenger.passengerFirstName,
                        lastName = passenger.passengerLastName
                )),
                arrayListOf())

        // when
        viewModel.pastVerifyParam = Gson().toJson(cartMeta)
        viewModel.setPassengerModels(arrayListOf(passenger))
        viewModel.setCartId("dummy")
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.errorToastMessageData.value shouldBe 0
    }

    @Test
    fun validateDataAndVerifyCart_international_validationSuccess() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "0813131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""
        val passenger = FlightBookingPassengerModel().apply {
            passengerFirstName = "Dummy First"
            passengerLastName = "Last"
            passengerBirthdate = "2020-11-11T10:10:10Z"
            flightBookingMealMetaViewModels = arrayListOf(
                    FlightBookingAmenityMetaModel().apply {
                        arrivalId = "BTJ"
                        departureId = "CGK"
                        journeyId = "dummyJourneyId"
                        key = ""
                        description = ""
                        amenities = arrayListOf(
                                FlightBookingAmenityModel().apply {
                                    id = "1"
                                    title = "makanan"
                                    price = "Rp100.000"
                                    priceNumeric = 100000
                                    departureId = "dummy"
                                    arrivalId = "dummy"
                                    amenityType = FlightBookingMapper.AMENITY_MEAL
                                }
                        )
                    }
            )
            flightBookingLuggageMetaViewModels = arrayListOf(
                    FlightBookingAmenityMetaModel().apply {
                        arrivalId = "BTJ"
                        departureId = "CGK"
                        journeyId = "dummyJourneyId"
                        key = ""
                        description = ""
                        amenities = arrayListOf(
                                FlightBookingAmenityModel().apply {
                                    id = "1"
                                    title = "bagasi"
                                    price = "Rp100.000"
                                    priceNumeric = 100000
                                    departureId = "dummy"
                                    arrivalId = "dummy"
                                    amenityType = FlightBookingMapper.AMENITY_LUGGAGE
                                }
                        )
                    }
            )
            passportNationality = TravelCountryPhoneCode("ID", "Indonesia", 62)
            passportNumber = "A123456"
            passportIssuerCountry = TravelCountryPhoneCode("ID", "Indonesia", 62)
            passportExpiredDate = "2020-11-11T01:01:01Z"
        }
        val cartMeta = FlightVerifyParam.MetaData("dummy",
                contactName, contactEmail, contactPhone, contactCountry,
                "127.0.0.1", "Android",
                arrayListOf(FlightVerifyParam.Passenger(
                        type = passenger.type,
                        title = passenger.passengerTitleId,
                        firstName = passenger.passengerFirstName,
                        lastName = passenger.passengerLastName
                )),
                arrayListOf())
        val bookingModel = DUMMY_BOOKING_INTERNATIONAL_MODEL

        // when
        viewModel.setFlightBookingParam(bookingModel)
        viewModel.setOtherPriceData(DUMMY_OTHER_PRICE)
        viewModel.pastVerifyParam = Gson().toJson(cartMeta)
        viewModel.setPassengerModels(arrayListOf(passenger))
        viewModel.setCartId("dummy")
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.errorToastMessageData.value shouldBe 0
    }

    @Test
    fun validateDataAndVerifyCart_internationalWithoutPassport_validationSuccess() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "0813131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""
        val passenger = FlightBookingPassengerModel().apply {
            passengerFirstName = "Dummy First"
            passengerLastName = "Last"
            flightBookingMealMetaViewModels = arrayListOf(
                    FlightBookingAmenityMetaModel().apply {
                        arrivalId = "BTJ"
                        departureId = "CGK"
                        journeyId = "dummyJourneyId"
                        key = ""
                        description = ""
                        amenities = arrayListOf(
                                FlightBookingAmenityModel().apply {
                                    id = "1"
                                    title = "makanan"
                                    price = "Rp100.000"
                                    priceNumeric = 100000
                                    departureId = "dummy"
                                    arrivalId = "dummy"
                                    amenityType = FlightBookingMapper.AMENITY_MEAL
                                }
                        )
                    }
            )
            flightBookingLuggageMetaViewModels = arrayListOf(
                    FlightBookingAmenityMetaModel().apply {
                        arrivalId = "BTJ"
                        departureId = "CGK"
                        journeyId = "dummyJourneyId"
                        key = ""
                        description = ""
                        amenities = arrayListOf(
                                FlightBookingAmenityModel().apply {
                                    id = "1"
                                    title = "bagasi"
                                    price = "Rp100.000"
                                    priceNumeric = 100000
                                    departureId = "dummy"
                                    arrivalId = "dummy"
                                    amenityType = FlightBookingMapper.AMENITY_LUGGAGE
                                }
                        )
                    }
            )
            passportNationality = null
            passportNumber = null
            passportIssuerCountry = null
            passportExpiredDate = null
        }
        val cartMeta = FlightVerifyParam.MetaData("dummy",
                contactName, contactEmail, contactPhone, contactCountry,
                "127.0.0.1", "Android",
                arrayListOf(FlightVerifyParam.Passenger(
                        type = passenger.type,
                        title = passenger.passengerTitleId,
                        firstName = passenger.passengerFirstName,
                        lastName = passenger.passengerLastName
                )),
                arrayListOf())
        val bookingModel = DUMMY_BOOKING_INTERNATIONAL_MODEL

        // when
        viewModel.setFlightBookingParam(bookingModel)
        viewModel.setOtherPriceData(DUMMY_OTHER_PRICE)
        viewModel.pastVerifyParam = Gson().toJson(cartMeta)
        viewModel.setPassengerModels(arrayListOf(passenger))
        viewModel.setCartId("dummy")
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.errorToastMessageData.value shouldBe 0
    }

    @Test
    fun onCancelAppliedVoucher() {
        // given
        val result = hashMapOf<Type, Any>(
                FlightCancelVoucher.Response::class.java to FlightCancelVoucher.Response()
        )
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.getReseponse(any()) } returns gqlResponse

        // when
        viewModel.onCancelAppliedVoucher("")

        // then
        coVerify { graphqlRepository.getReseponse(any(), any()) }
    }

    @Test
    fun verifyCartData_failedToVerify() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "0813131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""
        val passenger = FlightBookingPassengerModel().apply {
            passengerFirstName = "Dummy First"
            passengerLastName = "Last"
            flightBookingMealMetaViewModels = arrayListOf()
            flightBookingLuggageMetaViewModels = arrayListOf()
        }
        coEvery { graphqlRepository.getReseponse(any()) } coAnswers { throw Throwable() }

        // when
        viewModel.setPassengerModels(arrayListOf(passenger))
        viewModel.setCartId("dummy")
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.isStillLoading shouldBe false
        assert(viewModel.flightVerifyResult.value is Fail)
    }

    @Test
    fun verifyCartData_successToVerifyAndGetVoucher() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "0813131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""
        val passenger = FlightBookingPassengerModel().apply {
            passengerFirstName = "Dummy First"
            passengerLastName = "Last"
            flightBookingMealMetaViewModels = arrayListOf()
            flightBookingLuggageMetaViewModels = arrayListOf()
        }
        val responseVerify = hashMapOf<Type, Any>(
                FlightVerify.Response::class.java to FlightVerify.Response(
                        FlightVerify.FlightVerifyMetaAndData(
                                data = FlightVerify(
                                        arrayListOf(
                                                FlightVerify.FlightVerifyCart(
                                                        metaData = FlightVerify.CartMetaData(
                                                                "",
                                                                "dummyInvoiceId"
                                                        )
                                                )
                                        ),
                                        promo = FlightVerify.Promo(
                                                code = "DUMMY"
                                        )
                                ),
                                meta = FlightVerify.Meta(
                                        needRefresh = false
                                )
                        )
                ))
        val gqlResponseVerify = GraphqlResponse(responseVerify, mapOf<Type, List<GraphqlError>>(), false)
        val responseCheckVoucher = hashMapOf<Type, Any>(
                FlightVoucher.Response::class.java to FlightVoucher.Response(
                        flightVoucher = FlightVoucher(message = "Promo")
                )
        )
        val gqlReponseVoucher = GraphqlResponse(responseCheckVoucher, mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returnsMany listOf(gqlResponseVerify, gqlReponseVoucher)

        // when
        viewModel.setPassengerModels(arrayListOf(passenger))
        viewModel.updatePromoData(PromoData(1, "Testing"))
        viewModel.setCartId("dummy")
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.isStillLoading shouldBe false
        viewModel.verifyRetryCount shouldBe 0
        val data = (viewModel.flightVerifyResult.value!! as Success).data
        data.data.promo.code shouldBe "DUMMY"

        viewModel.getInvoiceId() shouldBe "dummyInvoiceId"
    }

    @Test
    fun verifyCartData_successToVerifyFailedToGetVoucher() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "0813131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""
        val passenger = FlightBookingPassengerModel().apply {
            passengerFirstName = "Dummy First"
            passengerLastName = "Last"
            flightBookingMealMetaViewModels = arrayListOf()
            flightBookingLuggageMetaViewModels = arrayListOf()
        }
        val responseVerify = hashMapOf<Type, Any>(
                FlightVerify.Response::class.java to FlightVerify.Response(
                        FlightVerify.FlightVerifyMetaAndData(
                                data = FlightVerify(
                                        arrayListOf(
                                                FlightVerify.FlightVerifyCart()
                                        ),
                                        promo = FlightVerify.Promo(
                                                code = "DUMMY"
                                        )
                                ),
                                meta = FlightVerify.Meta(
                                        needRefresh = false
                                )
                        )
                ))
        val gqlResponseVerify = GraphqlResponse(responseVerify, mapOf<Type, List<GraphqlError>>(), false)
        val gqlVoucherException = Exception(Gson().toJson(
                listOf(FlightError("4").apply {
                    status = "Failed"
                    message = "Error to fetch"
                    title = "title"
                })
        ))
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseVerify andThenThrows gqlVoucherException

        // when
        viewModel.setPassengerModels(arrayListOf(passenger))
        viewModel.updatePromoData(PromoData(1, "Testing"))
        viewModel.setCartId("dummy")
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.isStillLoading shouldBe false
        viewModel.verifyRetryCount shouldBe 0
        val data = (viewModel.flightVerifyResult.value!! as Success).data
        data.data.promo.code shouldBe "DUMMY"
        viewModel.flightPromoResult.value!!.isCouponEnable shouldBe true
    }

    @Test
    fun refreshCartId_failedToRefresh() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "0813131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""
        val passenger = FlightBookingPassengerModel().apply {
            passengerFirstName = "Dummy First"
            passengerLastName = "Last"
            flightBookingMealMetaViewModels = arrayListOf()
            flightBookingLuggageMetaViewModels = arrayListOf()
        }
        coEvery { graphqlRepository.getReseponse(any()) } coAnswers { throw Throwable() }

        // when
        viewModel.pastVerifyParam = "nothing"
        viewModel.setPassengerModels(arrayListOf(passenger))
        viewModel.setCartId("dummy")
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        assert(viewModel.flightCartResult.value is Fail)
    }

    @Test
    fun refreshCartId_successToRefresh() {
        // given
        val query = ""
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "0813131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""
        val passenger = FlightBookingPassengerModel().apply {
            passengerFirstName = "Dummy First"
            passengerLastName = "Last"
            flightBookingMealMetaViewModels = arrayListOf()
            flightBookingLuggageMetaViewModels = arrayListOf()
        }
        val atcResponse = hashMapOf<Type, Any>(
                FlightAddToCartData.Response::class.java to FlightAddToCartData.Response(
                        FlightAddToCartData("new cart id")
                )
        )
        val gqlResponse = GraphqlResponse(atcResponse, mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        // when
        viewModel.pastVerifyParam = "nothing"
        viewModel.setPassengerModels(arrayListOf(passenger))
        viewModel.setCartId("dummy")
        viewModel.validateDataAndVerifyCart(query, totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        // then
        viewModel.getFlightBookingParam().cartId shouldBe "new cart id"
    }

    @Test
    fun proceedCheckoutWithoutLuggage_withNullPassengers() {
        // given

        // when
        viewModel.proceedCheckoutWithoutLuggage("", "", 10000,
                "dummy contact name", "dummy@email.com", "628111111111",
                "Indonesia")

        // then
        viewModel.flightPassengersData.value!!.size shouldBe 0
    }

    @Test
    fun proceedCheckoutWithoutLuggage_withPassengers() {
        // given
        val passengerModels = DUMMY_BOOKING_PASSENGER
        passengerModels[0].flightBookingMealMetaViewModels = DUMMY_MEALS_AMENITIES
        passengerModels[0].flightBookingLuggageMetaViewModels = DUMMY_LUGGAGE_AMENITIES

        // when
        viewModel.setPassengerModels(passengerModels)
        viewModel.proceedCheckoutWithoutLuggage("", "", 10000,
                "dummy contact name", "dummy@email.com", "628111111111",
                "Indonesia")

        // then
        viewModel.flightPassengersData.value!!.size shouldBe 1
        viewModel.flightPassengersData.value!![0].flightBookingLuggageMetaViewModels.size shouldBe 0
    }

    @Test
    fun checkoutCart_failedToCheckout() {
        // given
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "0813131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""
        val passenger = FlightBookingPassengerModel().apply {
            passengerFirstName = "Dummy First"
            passengerLastName = "Last"
            flightBookingMealMetaViewModels = arrayListOf()
            flightBookingLuggageMetaViewModels = arrayListOf()
        }
        val responseVerify = hashMapOf<Type, Any>(
                FlightVerify.Response::class.java to FlightVerify.Response(
                        FlightVerify.FlightVerifyMetaAndData(
                                data = FlightVerify(
                                        arrayListOf(
                                                FlightVerify.FlightVerifyCart()
                                        ),
                                        promo = FlightVerify.Promo(
                                                code = "DUMMY"
                                        )
                                ),
                                meta = FlightVerify.Meta(
                                        needRefresh = false
                                )
                        )
                ))
        val gqlResponseVerify = GraphqlResponse(responseVerify, mapOf<Type, List<GraphqlError>>(), false)
        val responseCheckVoucher = hashMapOf<Type, Any>(
                FlightVoucher.Response::class.java to FlightVoucher.Response(
                        flightVoucher = FlightVoucher(message = "Promo")
                )
        )
        val gqlReponseVoucher = GraphqlResponse(responseCheckVoucher, mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returnsMany listOf(gqlResponseVerify, gqlReponseVoucher) andThenThrows Throwable("Failed to Fetch")

        // when
        viewModel.setPassengerModels(arrayListOf(passenger))
        viewModel.updatePromoData(PromoData(1, "Testing"))
        viewModel.setCartId("dummy")
        viewModel.validateDataAndVerifyCart("", totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        viewModel.checkOutCart("", 10000)

        // then
        assert(viewModel.flightCheckoutResult.value is Fail)
        (viewModel.flightCheckoutResult.value as Fail).throwable.message shouldBe "Failed to Fetch"
    }

    @Test
    fun checkoutCart_successToCheckout() {
        // given
        val totalPrice = 1000
        val contactName = "Dummy name"
        val contactEmail = "Dummyemail@gmail.com"
        val contactPhone = "0813131313"
        val contactCountry = ""
        val checkVoucherQuery = ""
        val addToCartQuery = ""
        val idempotencyKey = ""
        val getCartQuery = ""
        val passenger = FlightBookingPassengerModel().apply {
            passengerFirstName = "Dummy First"
            passengerLastName = "Last"
            flightBookingMealMetaViewModels = arrayListOf()
            flightBookingLuggageMetaViewModels = arrayListOf()
        }
        val responseVerify = hashMapOf<Type, Any>(
                FlightVerify.Response::class.java to FlightVerify.Response(
                        FlightVerify.FlightVerifyMetaAndData(
                                data = FlightVerify(
                                        arrayListOf(
                                                FlightVerify.FlightVerifyCart()
                                        ),
                                        promo = FlightVerify.Promo(
                                                code = "DUMMY"
                                        )
                                ),
                                meta = FlightVerify.Meta(
                                        needRefresh = false
                                )
                        )
                ))
        val gqlResponseVerify = GraphqlResponse(responseVerify, mapOf<Type, List<GraphqlError>>(), false)
        val responseCheckVoucher = hashMapOf<Type, Any>(
                FlightVoucher.Response::class.java to FlightVoucher.Response(
                        flightVoucher = FlightVoucher(message = "Promo")
                )
        )
        val gqlReponseVoucher = GraphqlResponse(responseCheckVoucher, mapOf(), false)
        val responseCheckout = hashMapOf<Type, Any>(
                FlightCheckoutData.Response::class.java to FlightCheckoutData.Response(DUMMY_CHECKOUT)
        )
        val gqlResponseCheckout = GraphqlResponse(responseCheckout, mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returnsMany listOf(gqlResponseVerify, gqlReponseVoucher, gqlResponseCheckout)

        // when
        viewModel.setPassengerModels(arrayListOf(passenger))
        viewModel.updatePromoData(PromoData(1, "Testing"))
        viewModel.setCartId("dummy")
        viewModel.validateDataAndVerifyCart("", totalPrice, contactName, contactEmail,
                contactPhone, contactCountry, checkVoucherQuery,
                addToCartQuery, idempotencyKey, getCartQuery)

        viewModel.checkOutCart("", 10000)

        // then
        assert(viewModel.flightCheckoutResult.value is Success<FlightCheckoutData>)
        val checkoutData = (viewModel.flightCheckoutResult.value as Success).data
        checkoutData.thanksURL shouldBe DUMMY_CHECKOUT.thanksURL
        checkoutData.redirectUrl shouldBe DUMMY_CHECKOUT.redirectUrl
        checkoutData.queryString shouldBe DUMMY_CHECKOUT.queryString
        checkoutData.id shouldBe DUMMY_CHECKOUT.id
        checkoutData.callbackUrlSuccess shouldBe DUMMY_CHECKOUT.callbackUrlSuccess
        checkoutData.callbackURLFailed shouldBe DUMMY_CHECKOUT.callbackURLFailed
    }

    @Test
    fun addToCart_failedAddToCart() {
        // given
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable("Failed") }

        // when
        viewModel.addToCart("", idempotencyKey = "")

        // then
        assert(viewModel.flightCartResult.value is Fail)
        (viewModel.flightCartResult.value as Fail).throwable.message shouldBe "Failed"
    }

    @Test
    fun addToCart_successAddToCart() {
        // given
        val gqlResponse = GraphqlResponse(
                mapOf<Type, Any>(
                        FlightAddToCartData.Response::class.java to DUMMY_ATC
                ),
                mapOf(),
                false
        )
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        // when
        viewModel.addToCart("", idempotencyKey = "")

        // then
        viewModel.getFlightBookingParam().cartId shouldBe DUMMY_ATC.addToCartData.id
    }

    @Test
    fun updateFlightDetailPriceData() {
        // given
        val flightDetails = DUMMY_BOOKING_FLIGHT_DETAIL
        val newPrices = DUMMY_BOOKING_NEW_PRICE

        // when
        viewModel.flightDetailModels = flightDetails
        viewModel.updateFlightDetailPriceData(newPrices)

        // then
        for ((index, item) in viewModel.flightDetailModels.withIndex()) {
            item.id shouldBe newPrices[index].id
            item.adultNumericPrice shouldBe newPrices[index].fare.adultNumeric
            item.childNumericPrice shouldBe newPrices[index].fare.childNumeric
            item.infantNumericPrice shouldBe newPrices[index].fare.infantNumeric
            item.totalNumeric shouldBe (newPrices[index].fare.adultNumeric + newPrices[index].fare.childNumeric + newPrices[index].fare.infantNumeric)
        }
    }

    @Test
    fun resetFirstPassenger() {
        // given
        val passengers = DUMMY_BOOKING_PASSENGER

        // when
        viewModel.setPassengerModels(passengers)
        viewModel.resetFirstPassenger()

        // then
        val newPassengers = viewModel.getPassengerModels()
        newPassengers[0].passengerLocalId shouldBe 1
        newPassengers[0].type shouldBe FlightBookingPassenger.ADULT
        newPassengers[0].flightBookingLuggageMetaViewModels.size shouldBe 0
        newPassengers[0].flightBookingMealMetaViewModels.size shouldBe 0
        newPassengers[0].headerTitle shouldBe "Penumpang dewasa"
    }

    @Test
    fun resetFirstPassenger_withEmptyPassengers() {
        // given

        // when
        viewModel.resetFirstPassenger()

        // then
        viewModel.getPassengerModels().size shouldBe 0
    }

    @Test
    fun onPassengerResultReceived_noPassengerFound() {
        // given
        val newPassenger = FlightBookingPassengerModel()

        // when
        viewModel.onPassengerResultReceived(newPassenger)

        // then
        viewModel.flightPassengersData.value!!.size shouldBe 0
    }

    @Test
    fun onPassengerResultReceived() {
        // given
        val passengers = DUMMY_BOOKING_PASSENGER
        val newPassenger = DUMMY_BOOKING_PASSENGER[0]
        newPassenger.flightBookingMealMetaViewModels = arrayListOf(DUMMY_MEALS_AMENITIES[0])
        newPassenger.flightBookingLuggageMetaViewModels = DUMMY_LUGGAGE_AMENITIES

        // when
        viewModel.setPassengerModels(passengers)
        viewModel.onPassengerResultReceived(newPassenger)

        // then
        viewModel.flightPassengersData.value!!.size shouldBe 1
    }

    @Test
    fun onPassengerResultReceivedWithOneLuggage() {
        // given
        val passengers = DUMMY_BOOKING_PASSENGER
        val newPassenger = DUMMY_BOOKING_PASSENGER[0]
        newPassenger.flightBookingMealMetaViewModels = arrayListOf(DUMMY_MEALS_AMENITIES[0])
        newPassenger.flightBookingLuggageMetaViewModels = arrayListOf(DUMMY_LUGGAGE_AMENITIES[0])

        // when
        viewModel.setPassengerModels(passengers)
        viewModel.onPassengerResultReceived(newPassenger)

        // then
        viewModel.flightPassengersData.value!!.size shouldBe 1
    }

    @Test
    fun onInsuranceChanges_checked() {
        // given
        val insurance = DUMMY_INSURANCE

        // when
        viewModel.onInsuranceChanges(insurance, true)

        // then
        val otherPriceData = viewModel.flightOtherPriceData.value!!
        otherPriceData.size shouldBe 1
        otherPriceData[0].priceNumeric shouldBe insurance.totalPriceNumeric
        otherPriceData[0].price shouldBe FlightCurrencyFormatUtil.convertToIdrPrice(insurance.totalPriceNumeric)
        otherPriceData[0].priceDetailId shouldBe insurance.id
    }

    @Test
    fun onInsuranceChanges_unchecked() {
        // given
        val insurance = DUMMY_INSURANCE
        viewModel.onInsuranceChanges(insurance, true)

        // when
        viewModel.onInsuranceChanges(insurance, false)

        // then
        val otherPriceData = viewModel.flightOtherPriceData.value!!
        otherPriceData.size shouldBe 0
        viewModel.getFlightBookingParam().insurances.size shouldBe 0
    }

    @Test
    fun fetchTickerData_successFetchData_tickerDataShouldBeSameAsData() {
        // given
        coEvery {
            travelTickerUseCase.execute(any(), any())
        } returns Success(TICKER_DATA)

        // when
        viewModel.fetchTickerData()

        // then
        assert(viewModel.tickerData.value is Success<TravelTickerModel>)
        val tickerData = (viewModel.tickerData.value as Success<TravelTickerModel>).data

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
        viewModel.fetchTickerData()

        // then
        assert(viewModel.tickerData.value is Fail)
    }

}