package com.tokopedia.entertainment.pdp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.entertainment.pdp.EventJsonMapper.getJson
import com.tokopedia.entertainment.pdp.data.EventContentByIdEntity
import com.tokopedia.entertainment.pdp.data.EventPDPContentCombined
import com.tokopedia.entertainment.pdp.data.EventProductDetailEntity
import com.tokopedia.entertainment.pdp.data.checkout.*
import com.tokopedia.entertainment.pdp.network_api.EventCheckoutRepository
import com.tokopedia.entertainment.pdp.usecase.EventProductDetailUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.promocheckout.common.domain.model.event.*
import com.tokopedia.travelcalendar.domain.TravelCalendarHolidayUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EventPDPCheckoutViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var eventCheckoutViewModel: EventCheckoutViewModel

    @MockK
    lateinit var graphqlRepository: MultiRequestGraphqlUseCase

    @MockK
    lateinit var userSessionInterface: UserSessionInterface

    @MockK
    lateinit var eventProductDetailUseCase: EventProductDetailUseCase

    @MockK
    lateinit var usecaseHoliday: TravelCalendarHolidayUseCase

    @MockK
    lateinit var repository: EventCheckoutRepository


    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        eventCheckoutViewModel = EventCheckoutViewModel(Dispatchers.Unconfined, eventProductDetailUseCase, repository)
    }

    @Test
    fun `DataCheckout_NotNullData_Shownotnulldata`(){
        Assert.assertNotNull(eventProductDetailUseCase)
        Assert.assertNotNull(usecaseHoliday)
        Assert.assertNotNull(graphqlRepository)
        Assert.assertNotNull(eventCheckoutViewModel)
        Assert.assertNotNull(userSessionInterface)
    }

    @Test
    fun `ProductdetailData_SuccessShowProduct_ShowActualResult`(){
        //given
        val contentMock = Gson().fromJson(getJson("content_mock.json"), EventContentByIdEntity::class.java)
        val pdpMock = Gson().fromJson(getJson("pdp_mock.json"), EventProductDetailEntity::class.java)

        coEvery {
            eventProductDetailUseCase.executeUseCase("", "", true, "")
        } returns Success(EventPDPContentCombined(contentMock, pdpMock))

        //when
        eventCheckoutViewModel.getDataProductDetail("", "", "")


        //then
        Assert.assertNotNull(eventCheckoutViewModel.eventProductDetail.value)
        Assert.assertEquals(eventCheckoutViewModel.eventProductDetail.value, pdpMock)
    }

    @Test
    fun `Productdetaildata_FailShowProduct_FailActualResult`(){
        //given
        coEvery {
            eventProductDetailUseCase.executeUseCase("", "", true, "")
        } returns Fail(Throwable("Error Fail Get PDP Data"))

        //when
        eventCheckoutViewModel.getDataProductDetail("", "", "")

        //then
        Assert.assertNotNull(eventCheckoutViewModel.isError.value)
        Assert.assertNull(eventCheckoutViewModel.eventProductDetail.value)
        Assert.assertEquals(eventCheckoutViewModel.isError.value?.message, "Error Fail Get PDP Data")
    }

    @Test
    fun `VerifyCart_SuccessVerifyCart_ShowActualResult`(){
        //given
        val cartItemlist = mutableListOf<CartItemVerify>()
        val cartItemVerify = CartItemVerify(ConfigurationVerify(12345), productId = 144, productName = "PRODUCTNAME144")
        var cartItem = CartItem(title = "CART_TEST", quantity = 100,price = 155)
        val dataCart = Cart(cartItems = listOf(cartItem))
        val eventVerifyResponse = EventVerifyResponse("CONFIG123", Data(dataCart, Status(result = "SUCCESS")), status = "SUCCESS")

        cartItemlist.add(cartItemVerify)

        val eventVerifyBody = EventVerifyBody(cartItemlist.toList(), "DEVICE_TEST", "TEST123")

        coEvery { repository.postVerify(eventCheckoutViewModel.createMapParam(true), eventVerifyBody)} returns eventVerifyResponse

        //when
        eventCheckoutViewModel.checkVerify(true, eventVerifyBody)

        //then
        Assert.assertNull(eventCheckoutViewModel.errorValue.value)

        Assert.assertNotNull(eventCheckoutViewModel.eventVerifyResponse.value)
        Assert.assertEquals(eventCheckoutViewModel.eventVerifyResponse.value, eventVerifyResponse)
    }

    @Test
    fun `CheckoutCart_SuccessCheckoutCart_ShowActualResult`(){
        //given
        var cartItem = CartItem(title = "CART_TEST", quantity = 100,price = 155)
        val dataCart = Cart(cartItems = listOf(cartItem))
        val eventcheckoutResponse = EventCheckoutResponse(serverProcessTime = "10000", status = "success", config = Any(), data = DataPayment(status = "success"))

        coEvery { repository.postCheckout(dataCart)} returns eventcheckoutResponse

        //when
        eventCheckoutViewModel.checkCheckout(dataCart)

        //then
        Assert.assertNull(eventCheckoutViewModel.errorValue.value)

        Assert.assertNotNull(eventCheckoutViewModel.eventCheckoutResponse.value)
        Assert.assertEquals(eventCheckoutViewModel.eventCheckoutResponse.value, eventcheckoutResponse)
        Assert.assertNull(eventCheckoutViewModel.errorGeneralValue.value)
    }

    @Test
    fun `VerifyCart_FailedVerifyCart_FailedActualResult`(){
        //given
        val error_msg = "ERROR123"
        val cartItemlist = mutableListOf<CartItemVerify>()
        val cartItemVerify = CartItemVerify(ConfigurationVerify(12345), productId = 144, productName = "PRODUCTNAME144")
        var cartItem = CartItem(title = "CART_TEST", quantity = 100,price = 155)
        val dataCart = Cart(cartItems = listOf(cartItem), error = error_msg)
        val eventVerifyResponse = EventVerifyResponse("CONFIG123", Data(dataCart, Status(result = "FAIL")), status = "SUCCESS")

        cartItemlist.add(cartItemVerify)

        val eventVerifyBody = EventVerifyBody(cartItemlist.toList(), "DEVICE_TEST", "TEST123")

        coEvery { repository.postVerify(eventCheckoutViewModel.createMapParam(true), eventVerifyBody)} returns eventVerifyResponse

        //when
        eventCheckoutViewModel.checkVerify(true, eventVerifyBody)


        //then
        Assert.assertNotNull(eventCheckoutViewModel.errorValue.value)

        Assert.assertNull(eventCheckoutViewModel.eventVerifyResponse.value)
        Assert.assertEquals(error_msg, eventVerifyResponse.data.cart.error)
    }

    @Test
    fun `CheckoutCart_FailCheckoutCart_FailActualResult`(){
        //given
        val error_msg = "ERROR144"
        var cartItem = CartItem(title = "CART_TEST", quantity = 100,price = 155)
        val dataCart = Cart(cartItems = listOf(cartItem))
        val eventcheckoutResponse = EventCheckoutResponse(serverProcessTime = "10000", status = "error", config = Any(), data = DataPayment(status = "error", error = error_msg))

        coEvery { repository.postCheckout(dataCart) } returns eventcheckoutResponse

        //when
        eventCheckoutViewModel.checkCheckout(dataCart)

        //then
        Assert.assertNotNull(eventCheckoutViewModel.errorValue.value)

        Assert.assertNull(eventCheckoutViewModel.eventCheckoutResponse.value)
        Assert.assertEquals(error_msg, eventCheckoutViewModel.errorValue.value)
    }


    @Test
    fun `CheckoutVerifyBookMapper_FalseCheckoutBookMapper_ShowFalse`(){
        //given
        val book = false

        //when
        var result = eventCheckoutViewModel.createMapParam(book)

        //then
        assert(!result.get("book")!!)
    }

    @Test
    fun `CheckoutVerifyBookMapper_TrueCheckoutBookMapper_ShowTrue`(){
        //given
        val book = true

        //when
        var result = eventCheckoutViewModel.createMapParam(book)

        //then
        assert(result.get("book")!!)
    }
}