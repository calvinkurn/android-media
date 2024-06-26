package com.tokopedia.hotel.booking

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.hotel.DummyHotelGqlQueryInterfaceImpl
import com.tokopedia.hotel.booking.data.model.HotelCart
import com.tokopedia.hotel.booking.data.model.HotelCheckoutParam
import com.tokopedia.hotel.booking.data.model.HotelCheckoutResponse
import com.tokopedia.hotel.booking.data.model.TokopointsSumCoupon
import com.tokopedia.hotel.booking.presentation.viewmodel.HotelBookingViewModel
import com.tokopedia.hotel.roomlist.util.HotelUtil
import com.tokopedia.promocheckout.common.domain.model.FlightCancelVoucher
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.data.entity.TravelUpsertContactModel
import com.tokopedia.travel.passenger.domain.GetContactListUseCase
import com.tokopedia.travel.passenger.domain.UpsertContactListUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.reflect.Type

/**
 * @author by jessica on 27/03/20
 */

@RunWith(JUnit4::class)
class HotelBookingViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var hotelBookingViewModel: HotelBookingViewModel

    private val graphqlRepository = mockk<GraphqlRepository>()

    @RelaxedMockK
    lateinit var getContactListUseCase: GetContactListUseCase

    @RelaxedMockK
    lateinit var upsertContactListUseCase: UpsertContactListUseCase

    private val travelTickerCoroutineUseCase = mockk<TravelTickerCoroutineUseCase>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(HotelUtil.Companion)
        hotelBookingViewModel = HotelBookingViewModel(graphqlRepository, getContactListUseCase, upsertContactListUseCase,
                travelTickerCoroutineUseCase, dispatcher)
    }

    @Test
    fun getContactList_updateContactList() {
        //given
        val contacts = listOf(TravelContactListModel.Contact(firstName = "Jessica", lastName = "Sean"),
                TravelContactListModel.Contact(fullName = "Jee Sean", firstName = "Jee", lastName = "See"),
                TravelContactListModel.Contact(fullName = "Lalala")
        )
        coEvery {
            getContactListUseCase.execute(any(), any(), any())
        } returns contacts

        //when
        hotelBookingViewModel.getContactList(DummyHotelGqlQueryInterfaceImpl())

        //then
        assert((hotelBookingViewModel.contactListResult.value as List<TravelContactListModel.Contact>).isNotEmpty())
        assert((hotelBookingViewModel.contactListResult.value as List<TravelContactListModel.Contact>)[0].fullName == "Jessica Sean")
        assert((hotelBookingViewModel.contactListResult.value as List<TravelContactListModel.Contact>)[1].fullName == "Jee Sean")
        assert((hotelBookingViewModel.contactListResult.value as List<TravelContactListModel.Contact>)[2].fullName == "Lalala")
    }

    @Test
    fun updateContactList_shouldUpsertContactList() {
        //given
        coEvery {
            upsertContactListUseCase.execute(any(), any())
        } returns Success(TravelUpsertContactModel.Response(TravelUpsertContactModel.Response.SuccessResponse(true)))

        //when
        hotelBookingViewModel.updateContactList(DummyHotelGqlQueryInterfaceImpl(), TravelUpsertContactModel.Contact())

    }

    @Test
    fun getCartData_shouldReturnSuccess() {
        //given
        val cart = HotelCart.Response(HotelCart("123"))
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(HotelCart.Response::class.java to cart),
                mapOf<Type, List<GraphqlError>>(),
                false)
        coEvery {
            graphqlRepository.response(any(), any())
        } returns graphqlSuccessResponse

        //when
        hotelBookingViewModel.getCartData(DummyHotelGqlQueryInterfaceImpl(), "")

        //then
        assert(hotelBookingViewModel.hotelCartResult.value is Success)
        assert((hotelBookingViewModel.hotelCartResult.value as Success<HotelCart.Response>).data.response.cartID == "123")
        assert((hotelBookingViewModel.promoData.value?.promoCode == cart.response.appliedVoucher.code))
    }

    @Test
    fun getCartData_shouldReturnFail() {
        //given
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(),
                mapOf<Type, List<GraphqlError>>(HotelCart.Response::class.java to listOf(GraphqlError())),
                false)
        coEvery {
            graphqlRepository.response(any(), any())
        } returns graphqlSuccessResponse

        //when
        hotelBookingViewModel.getCartData(DummyHotelGqlQueryInterfaceImpl(), "")

        //then
        assert(hotelBookingViewModel.hotelCartResult.value is Fail)
    }

    @Test
    fun checkoutCart_shouldBeSuccess() {
        //given
        val checkoutResponse = HotelCheckoutResponse.Response(HotelCheckoutResponse(redirectUrl = "www.tokopedia.com"))
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(HotelCheckoutResponse.Response::class.java to checkoutResponse),
                mapOf<Type, List<GraphqlError>>(),
                false)
        coEvery {
            graphqlRepository.response(any(), any())
        } returns graphqlSuccessResponse

        //when
        hotelBookingViewModel.checkoutCart(DummyHotelGqlQueryInterfaceImpl(), HotelCheckoutParam())

        //then
        assert(hotelBookingViewModel.hotelCheckoutResult.value is Success)
        assert((hotelBookingViewModel.hotelCheckoutResult.value as Success<HotelCheckoutResponse>).data.redirectUrl == "www.tokopedia.com")
    }

    @Test
    fun checkoutCartWithTokenNotEmpty_shouldBeSuccess() {
        //given
        val checkoutResponse = HotelCheckoutResponse.Response(HotelCheckoutResponse(redirectUrl = "www.tokopedia.com"))
        val graphqlSuccessResponse = GraphqlResponse(
            mapOf<Type, Any>(HotelCheckoutResponse.Response::class.java to checkoutResponse),
            mapOf<Type, List<GraphqlError>>(),
            false)
        coEvery {
            graphqlRepository.response(any(), any())
        } returns graphqlSuccessResponse

        every {
            HotelUtil.md5(any())
        } returns "2022"

        //when
        hotelBookingViewModel.checkoutCart(DummyHotelGqlQueryInterfaceImpl(), HotelCheckoutParam())

        //then
        assert(hotelBookingViewModel.hotelCheckoutResult.value is Success)
        assert((hotelBookingViewModel.hotelCheckoutResult.value as Success<HotelCheckoutResponse>).data.redirectUrl == "www.tokopedia.com")
    }

    @Test
    fun checkoutCartWithTokenEmpty_shouldBeSuccess() {
        //given
        val checkoutResponse = HotelCheckoutResponse.Response(HotelCheckoutResponse(redirectUrl = "www.tokopedia.com"))
        val graphqlSuccessResponse = GraphqlResponse(
            mapOf<Type, Any>(HotelCheckoutResponse.Response::class.java to checkoutResponse),
            mapOf<Type, List<GraphqlError>>(),
            false)
        coEvery {
            graphqlRepository.response(any(), any())
        } returns graphqlSuccessResponse

        every {
            HotelUtil.md5(any())
        } returns ""

        //when
        hotelBookingViewModel.checkoutCart(DummyHotelGqlQueryInterfaceImpl(), HotelCheckoutParam())

        //then
        assert(hotelBookingViewModel.hotelCheckoutResult.value is Success)
        assert((hotelBookingViewModel.hotelCheckoutResult.value as Success<HotelCheckoutResponse>).data.redirectUrl == "www.tokopedia.com")
    }

    @Test
    fun checkOutCart_shouldBeFail() {
        //given
        val graphqlErrorResponse = GraphqlResponse(
                mapOf<Type, Any>(),
                mapOf<Type, List<GraphqlError>>(HotelCheckoutResponse.Response::class.java to listOf(GraphqlError())),
                false)
        coEvery {
            graphqlRepository.response(any(), any())
        } returns graphqlErrorResponse

        //when
        hotelBookingViewModel.checkoutCart(DummyHotelGqlQueryInterfaceImpl(), HotelCheckoutParam())

        //then
        assert(hotelBookingViewModel.hotelCheckoutResult.value is Fail)
    }


    @Test
    fun onCancelAppliedVoucherWithAttributesTrue_shouldBeSuccess() {
        //given
        val expected = PromoData(state = TickerCheckoutView.State.ACTIVE)
        val flightCancelResponse = FlightCancelVoucher(attributes = FlightCancelVoucher.Attributes(success = true))
        val graphqlSuccessResponse = GraphqlResponse(
            mapOf<Type, Any>(FlightCancelVoucher.Response::class.java to FlightCancelVoucher.Response(flightCancelResponse)),
            mapOf<Type, List<GraphqlError>>(),
            false)

        coEvery {
            graphqlRepository.response(any(), any())
        } returns graphqlSuccessResponse

        //when
        hotelBookingViewModel.applyPromoData(expected)
        hotelBookingViewModel.onCancelAppliedVoucher(DummyHotelGqlQueryInterfaceImpl())

        //then
        hotelBookingViewModel.promoData.verifyValueEquals(expected)
    }

    @Test
    fun onCancelAppliedVoucherWithAttributesFalse_shouldBeError() {
        //given
        val expected = PromoData(state = TickerCheckoutView.State.FAILED)
        val flightCancelResponse = FlightCancelVoucher(attributes = FlightCancelVoucher.Attributes(success = false))
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(FlightCancelVoucher.Response::class.java to FlightCancelVoucher.Response(flightCancelResponse)),
                mapOf<Type, List<GraphqlError>>(),
                false)

        coEvery {
            graphqlRepository.response(any(), any())
        } returns graphqlSuccessResponse

        //when
        hotelBookingViewModel.applyPromoData(expected)
        hotelBookingViewModel.onCancelAppliedVoucher(DummyHotelGqlQueryInterfaceImpl())

        //then
        hotelBookingViewModel.promoData.verifyValueEquals(expected)
    }

    @Test
    fun onCancelAppliedVoucher_shouldBeError() {
        //given
        val expected = PromoData(state = TickerCheckoutView.State.FAILED)
        val graphqlErrorResponse = GraphqlResponse(
                mapOf<Type, Any>(),
                mapOf<Type, List<GraphqlError>>(FlightCancelVoucher.Response::class.java to listOf(GraphqlError())),
                false)
        coEvery {
            graphqlRepository.response(any(), any())
        } returns graphqlErrorResponse

        //when
        hotelBookingViewModel.applyPromoData(expected)
        hotelBookingViewModel.onCancelAppliedVoucher(DummyHotelGqlQueryInterfaceImpl())

        //then
        hotelBookingViewModel.promoData.verifyValueEquals(expected)
    }

    @Test
    fun getTokopointsSumCoupon_isSuccess_shouldReturnData() {
        //given
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(TokopointsSumCoupon.Response::class.java to TokopointsSumCoupon.Response(TokopointsSumCoupon(sumCouponUnitOpt = "33 Kupon"))),
                mapOf<Type, List<GraphqlError>>(),
                false)
        coEvery {
            graphqlRepository.response(any(), any())
        } returns graphqlSuccessResponse

        //when
        hotelBookingViewModel.getTokopointsSumCoupon(DummyHotelGqlQueryInterfaceImpl())

        //then
        assert((hotelBookingViewModel.tokopointSumCouponResult.value as String).equals("33 Kupon"))
    }

    @Test
    fun getTokopointsSumCoupon_isFail_shouldNotUpdateData() {
        //given
        val graphqlFailResponse = GraphqlResponse(
                mapOf<Type, Any>(),
                mapOf<Type, List<GraphqlError>>(TokopointsSumCoupon.Response::class.java to listOf(GraphqlError())),
                false)
        coEvery {
            graphqlRepository.response(any(), any())
        } returns graphqlFailResponse

        //when
        hotelBookingViewModel.getTokopointsSumCoupon(DummyHotelGqlQueryInterfaceImpl())

        //then
        assert((hotelBookingViewModel.tokopointSumCouponResult.value as String).isEmpty())
    }

    @Test
    fun getTickerData_shouldData() {
        //given
        val title = "Title ABC"
        val message = "this is a message"
        val response = TravelTickerModel(title = title, message = message, url = "", type = 0, status = 0,
                endTime = "", startTime = "", instances = 0, page = "", isPeriod = true)
        coEvery {
            travelTickerCoroutineUseCase.execute(any(), any())
        } returns Success(response)

        //when
        hotelBookingViewModel.fetchTickerData()

        //then
        val actual = hotelBookingViewModel.tickerData.value
        assert(actual is Success)
        assert((actual as Success).data.title == title)
        assert(actual.data.message == message)
    }

    @Test
    fun getTickerData_shouldReturnFail() {
        //given
        coEvery {
            travelTickerCoroutineUseCase.execute(any(), any())
        } returns Fail(Throwable())

        //when
        hotelBookingViewModel.fetchTickerData()

        //then
        val actual = hotelBookingViewModel.tickerData.value
        assert(actual is Fail)
    }

    @Test
    fun applyPromoData_onPromoApply(){
        //given
        var promoData = PromoData()
        promoData.apply {
            promoCode = "TOPED"
            description = "Potensi Cashback senilai Rp. 10.000"
            amount = 1000L
        }

        //when
        hotelBookingViewModel.applyPromoData(promoData)

        //then
        assert(hotelBookingViewModel.promoData.value?.promoCode == promoData.promoCode)
        assert(hotelBookingViewModel.promoData.value?.description == promoData.description)
        assert(hotelBookingViewModel.promoData.value?.amount == promoData.amount)
    }

    @Test
    fun applyPromoData_onReset(){
        //given
        var promoData = PromoData()

        //when
        hotelBookingViewModel.applyPromoData(promoData)

        //then
        assert(hotelBookingViewModel.promoData.value?.promoCode == "")
    }
}