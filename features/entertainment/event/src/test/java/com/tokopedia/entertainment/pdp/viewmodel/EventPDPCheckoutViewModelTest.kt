package com.tokopedia.entertainment.pdp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.entertainment.pdp.EventJsonMapper.getJson
import com.tokopedia.entertainment.pdp.data.EventContentByIdEntity
import com.tokopedia.entertainment.pdp.data.EventPDPContentCombined
import com.tokopedia.entertainment.pdp.data.EventProductDetailEntity
import com.tokopedia.entertainment.pdp.data.checkout.*
import com.tokopedia.entertainment.pdp.usecase.EventProductDetailUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.travelcalendar.domain.TravelCalendarHolidayUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class EventPDPCheckoutViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var eventCheckoutViewModel: EventCheckoutViewModel

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @MockK
    lateinit var userSessionInterface: UserSessionInterface

    @MockK
    lateinit var eventProductDetailUseCase: EventProductDetailUseCase

    @MockK
    lateinit var usecaseHoliday: TravelCalendarHolidayUseCase


    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        eventCheckoutViewModel = EventCheckoutViewModel(Dispatchers.Unconfined, eventProductDetailUseCase, graphqlRepository)
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
        val error = Throwable("Error Fail Get PDP Data")
        coEvery {
            eventProductDetailUseCase.executeUseCase("", "", true, "")
        } returns Fail(error)

        //when
        eventCheckoutViewModel.getDataProductDetail("", "", "")

        //then
        Assert.assertNotNull(eventCheckoutViewModel.isError.value)
        Assert.assertNull(eventCheckoutViewModel.eventProductDetail.value)
        Assert.assertEquals(eventCheckoutViewModel.isError.value?.message, error.message)
    }

    @Test
    fun `CheckoutEvent_SuccessCheckout_ShouldSuccessCheckout`(){

        val checkoutMock = Gson().fromJson(getJson("checkout_mock.json"), EventCheckoutResponse::class.java)

        val result = HashMap<Type, Any>()
        result[EventCheckoutResponse::class.java] = checkoutMock
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(),any()) } returns gqlResponse

        eventCheckoutViewModel.checkoutEvent("", CheckoutGeneralV2Params())

        val actual = eventCheckoutViewModel.eventCheckoutResponse.value
        assertEquals(actual, checkoutMock)
    }


    @Test
    fun `CheckoutEvent_FailCheckout_ShouldFailCheckout`(){
        //given
        val error = Throwable("Error Checkout")
        coEvery { graphqlRepository.getReseponse(any(),any()) } coAnswers {throw error}

        //when
        eventCheckoutViewModel.checkoutEvent("",CheckoutGeneralV2Params())

        //then
        val actual = eventCheckoutViewModel.errorGeneralValue.value
        assert(actual?.message.equals(error.message))

    }

    @Test
    fun `CheckoutInstantEvent_SuccessCheckout_ShouldSuccessCheckoutInstant`(){

        val checkoutMock = Gson().fromJson(getJson("checkout_instant_mock.json"), EventCheckoutInstantResponse::class.java)

        val result = HashMap<Type, Any>()
        result[EventCheckoutInstantResponse::class.java] = checkoutMock
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(),any()) } returns gqlResponse

        eventCheckoutViewModel.checkoutEventInstant(CheckoutGeneralV2InstantParams())

        val actual = eventCheckoutViewModel.eventCheckoutInstantResponse.value
        assertEquals(actual, checkoutMock)
    }

    @Test
    fun `CheckoutInstantEvent_FailCheckout_ShouldFailCheckout`(){
        //given
        val error = Throwable("Error Checkout")
        coEvery { graphqlRepository.getReseponse(any(),any()) } coAnswers {throw error}

        //when
        eventCheckoutViewModel.checkoutEventInstant(CheckoutGeneralV2InstantParams())

        //then
        val actual = eventCheckoutViewModel.errorGeneralValue.value
        assert(actual?.message.equals(error.message))

    }

}