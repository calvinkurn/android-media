package com.tokopedia.digital_deals

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.digital_deals.data.*
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse
import com.tokopedia.digital_deals.view.viewmodel.DealsCheckoutViewModel
import com.tokopedia.digital_deals.view.viewmodel.DealsVerifyViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class DealsCheckoutViewModelTest {

    @get: Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider

    private lateinit var viewModel: DealsCheckoutViewModel
    private lateinit var mockDealsVerifyInstantResponse: DealsVerifyResponse
    private lateinit var mockDealsVerifyGeneralResponse: DealsVerifyResponse
    private lateinit var mockDealsCheckoutGeneralRequest: DealCheckoutGeneral
    private lateinit var mockDealsCheckoutGeneralInstantRequest: DealCheckoutGeneralInstant
    private lateinit var mockDealsCheckoutGeneralResponse: DealsCheckoutResponse
    private lateinit var mockDealsCheckoutGeneralInstantResponse: DealsCheckoutInstantResponse
    private lateinit var mockDealsDetailInstant: DealsDetailsResponse
    private lateinit var mockDealsDetailGeneral: DealsDetailsResponse
    private val promoCodes = listOf("FNVTEST1")


    var graphqlRepository: GraphqlRepository = mockk()

    @Before
    fun setup(){
        mockDealsVerifyInstantResponse = Gson().fromJson(
                DealsJsonMapper.getJson("deals_verify.json"),
                DealsVerifyResponse::class.java
        )

        mockDealsVerifyGeneralResponse = Gson().fromJson(
                DealsJsonMapper.getJson("deals_verify_general.json"),
                DealsVerifyResponse::class.java
        )

        mockDealsDetailInstant = Gson().fromJson(
                DealsJsonMapper.getJson("deals_detail.json"),
                DealsDetailsResponse::class.java
        )

        mockDealsDetailGeneral = Gson().fromJson(
                DealsJsonMapper.getJson("deals_detail_general.json"),
                DealsDetailsResponse::class.java
        )

        mockDealsCheckoutGeneralRequest = Gson().fromJson(
                DealsJsonMapper.getJson("deals_checkout_general_request.json"),
                DealCheckoutGeneral::class.java
        )

        mockDealsCheckoutGeneralInstantRequest = Gson().fromJson(
                DealsJsonMapper.getJson("deals_checkout_instant_request.json"),
                DealCheckoutGeneralInstant::class.java
        )

        mockDealsCheckoutGeneralResponse = Gson().fromJson(
                DealsJsonMapper.getJson("deals_checkout_general_response.json"),
                DealsCheckoutResponse::class.java
        )

        mockDealsCheckoutGeneralInstantResponse = Gson().fromJson(
                DealsJsonMapper.getJson("deals_checkout_instant_response.json"),
                DealsCheckoutInstantResponse::class.java
        )


        viewModel = DealsCheckoutViewModel(
                graphqlRepository, dispatcher
        )
    }

    @Test
    fun mapCheckoutData_successMapCheckoutInstantRequest(){
        //when
        val actual = viewModel.mapCheckoutDealsInstant(mockDealsDetailInstant, mockDealsVerifyInstantResponse.eventVerify, promoCodes)

        //then
        assertEquals(mockDealsCheckoutGeneralInstantRequest, actual)
    }

    @Test
    fun mapCheckoutData_successMapCheckoutGeneralRequest(){
        //when
        val actual = viewModel.mapCheckoutDeals(mockDealsDetailGeneral, mockDealsVerifyGeneralResponse.eventVerify, promoCodes)

        //then
        assertEquals(mockDealsCheckoutGeneralRequest, actual)
    }

    @Test
    fun mapCheckoutData_successMapCheckoutInstantRequest_DataTypeNull(){
        //when
        mockDealsDetailInstant.checkoutDataType = null
        val actual = viewModel.mapCheckoutDealsInstant(mockDealsDetailInstant, mockDealsVerifyInstantResponse.eventVerify, promoCodes)

        //then
        assertEquals(mockDealsCheckoutGeneralInstantRequest, actual)
    }

    @Test
    fun mapCheckoutData_successMapCheckoutGeneralRequest_DataTypeNull(){
        //when
        mockDealsDetailGeneral.checkoutDataType = null
        val actual = viewModel.mapCheckoutDeals(mockDealsDetailGeneral, mockDealsVerifyGeneralResponse.eventVerify, promoCodes)

        //then
        assertEquals(mockDealsCheckoutGeneralRequest, actual)
    }


    @Test
    fun checkoutGeneralDeals_success_shouldsuccess(){
        //given
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = DealsCheckoutResponse::class.java
        result[objectType] = mockDealsCheckoutGeneralResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        viewModel.checkoutGeneral(mockDealsCheckoutGeneralRequest)

        //then
        val actualData = viewModel.dealsCheckoutResponse.value
        assertEquals(mockDealsCheckoutGeneralResponse, actualData)
    }


    @Test
    fun checkoutGeneralDeals_error_shoulderror(){
        //given
        val message = "Error Fetch Checkout General"
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val errorGql = GraphqlError()
        val objectType = DealsCheckoutResponse::class.java
        errorGql.message = message
        errors[objectType] = listOf(errorGql)
        val gqlResponseError = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseError

        //when
        viewModel.checkoutGeneral(mockDealsCheckoutGeneralRequest)

        //then
        val actualData = viewModel.errorGeneralValue.value
        assertEquals(actualData?.message, message)
    }


    @Test
    fun checkoutInstantDeals_success_shouldsuccess(){
        //given
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = DealsCheckoutInstantResponse::class.java
        result[objectType] = mockDealsCheckoutGeneralInstantResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        viewModel.checkoutGeneralInstant(mockDealsCheckoutGeneralInstantRequest)

        //then
        val actualData = viewModel.dealsCheckoutInstantResponse.value
        assertEquals(mockDealsCheckoutGeneralInstantResponse, actualData)
    }


    @Test
    fun checkoutInstantDeals_error_shoulderror(){
        //given
        val message = "Error Fetch Checkout Instant"
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val errorGql = GraphqlError()
        val objectType = DealsCheckoutInstantResponse::class.java
        errorGql.message = message
        errors[objectType] = listOf(errorGql)
        val gqlResponseError = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseError

        //when
        viewModel.checkoutGeneralInstant(mockDealsCheckoutGeneralInstantRequest)

        //then
        val actualData = viewModel.errorGeneralValue.value
        assertEquals(actualData?.message, message)
    }

}