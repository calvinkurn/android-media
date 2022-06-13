package com.tokopedia.digital_deals

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.digital_deals.data.DealCheckoutGeneral
import com.tokopedia.digital_deals.data.DealCheckoutGeneralInstant
import com.tokopedia.digital_deals.data.DealCheckoutGeneralInstantNoPromo
import com.tokopedia.digital_deals.data.DealCheckoutGeneralNoPromo
import com.tokopedia.digital_deals.data.DealsCheckoutInstantResponse
import com.tokopedia.digital_deals.data.DealsCheckoutResponse
import com.tokopedia.digital_deals.data.DealsVerifyResponse
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse
import com.tokopedia.digital_deals.view.viewmodel.DealsCheckoutViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
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
    private lateinit var mockDealsCheckoutGeneralRequestNoPromo: DealCheckoutGeneralNoPromo
    private lateinit var mockDealsCheckoutGeneralInstantRequest: DealCheckoutGeneralInstant
    private lateinit var mockDealsCheckoutGeneralInstantRequestNoPromo: DealCheckoutGeneralInstantNoPromo
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

        mockDealsCheckoutGeneralRequestNoPromo = Gson().fromJson(
            DealsJsonMapper.getJson("deals_checkout_general_no_promo_request.json"),
            DealCheckoutGeneralNoPromo::class.java
        )

        mockDealsCheckoutGeneralInstantRequest = Gson().fromJson(
                DealsJsonMapper.getJson("deals_checkout_instant_request.json"),
                DealCheckoutGeneralInstant::class.java
        )

        mockDealsCheckoutGeneralInstantRequestNoPromo = Gson().fromJson(
            DealsJsonMapper.getJson("deals_checkout_instant_no_promo_request.json"),
            DealCheckoutGeneralInstantNoPromo::class.java
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
    fun mapCheckoutData_successMapCheckoutGeneralRequestNoPromo(){
        //when
        val actual = viewModel.mapCheckoutDeals(mockDealsDetailGeneral, mockDealsVerifyGeneralResponse.eventVerify)

        //then
        assertEquals(mockDealsCheckoutGeneralRequestNoPromo, actual)
    }

    @Test
    fun mapCheckoutData_successMapCheckoutInstantRequestNoPromo(){
        //when
        val actual = viewModel.mapCheckoutDealsInstant(mockDealsDetailInstant, mockDealsVerifyInstantResponse.eventVerify)

        //then
        assertEquals(mockDealsCheckoutGeneralInstantRequestNoPromo, actual)
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
    fun mapCheckoutData_successMapCheckoutInstantRequestNoPromo_DataTypeNull(){
        //when
        val actual = viewModel.mapCheckoutDealsInstant(mockDealsDetailInstant, mockDealsVerifyInstantResponse.eventVerify)

        //then
        assertEquals(mockDealsCheckoutGeneralInstantRequestNoPromo, actual)
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
    fun mapCheckoutData_successMapCheckoutGeneralRequestNoPromo_DataTypeNull(){
        //when
        mockDealsDetailInstant.checkoutDataType = null
        val actual = viewModel.mapCheckoutDeals(mockDealsDetailGeneral, mockDealsVerifyGeneralResponse.eventVerify)

        //then
        assertEquals(mockDealsCheckoutGeneralRequestNoPromo, actual)
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

        coVerify { graphqlRepository.response(any(), any()) }
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

        coVerify { graphqlRepository.response(any(), any()) }
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

        coVerify { graphqlRepository.response(any(), any()) }
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

        coVerify { graphqlRepository.response(any(), any()) }
    }

}