package com.tokopedia.flight.bookingV3.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.flight.R
import com.tokopedia.flight.bookingV3.data.FlightAddToCartData
import com.tokopedia.flight.bookingV3.data.FlightVerify
import com.tokopedia.flight.bookingV3.data.FlightVerifyParam
import com.tokopedia.flight.bookingV3.data.FlightVoucher
import com.tokopedia.flight.bookingV3.data.mapper.FlightBookingMapper
import com.tokopedia.flight.common.data.model.FlightError
import com.tokopedia.flight.dummy.*
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
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
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

    private val testDispatcherProvider = TravelTestDispatcherProvider()

    @RelaxedMockK
    private lateinit var graphqlRepository: GraphqlRepository

    private lateinit var viewModel: FlightBookingViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = FlightBookingViewModel(graphqlRepository, testDispatcherProvider)
    }

    @Test
    fun init_allLiveDataShouldBeEmptyList() {
        // given

        // when
        val flightViewModel = FlightBookingViewModel(graphqlRepository, testDispatcherProvider)
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
        coEvery { graphqlRepository.getReseponse(any()) } coAnswers { throw Throwable("Failed to Fetch") }

        // when
        viewModel.getProfile("")

        // then
        assert(viewModel.profileResult.value != null)
        assert(viewModel.profileResult.value!! is Fail)
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
                listOf(FlightError("123").apply {
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
    fun checkOutCart_failToCheckout() {
        // given
//        coEvery { graphqlRepository.getReseponse(any()) } coAnswers { throw Throwable() }

        // when
//        viewModel.checkOutCart("", 1000000)

        // then
//        assert(viewModel.flightCheckoutResult.value!! is Fail)
    }

}