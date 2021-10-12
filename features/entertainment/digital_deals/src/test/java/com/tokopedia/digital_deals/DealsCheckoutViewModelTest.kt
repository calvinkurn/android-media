package com.tokopedia.digital_deals

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.digital_deals.data.*
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse
import com.tokopedia.digital_deals.view.viewmodel.DealsCheckoutViewModel
import com.tokopedia.digital_deals.view.viewmodel.DealsVerifyViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
        val actual = viewModel.mapCheckoutDealsInstant(mockDealsDetailInstant, mockDealsVerifyInstantResponse.eventVerify)

        //then
        assertEquals(mockDealsCheckoutGeneralInstantRequest, actual)
    }

    @Test
    fun mapCheckoutData_successMapCheckoutGeneralRequest(){
        //when
        val actual = viewModel.mapCheckoutDeals(mockDealsDetailGeneral, mockDealsVerifyGeneralResponse.eventVerify)

        //then
        assertEquals(mockDealsCheckoutGeneralRequest, actual)
    }

    @Test
    fun mapCheckoutData_successMapCheckoutInstantRequest_DataTypeNull(){
        //when
        mockDealsDetailInstant.checkoutDataType = null
        val actual = viewModel.mapCheckoutDealsInstant(mockDealsDetailInstant, mockDealsVerifyInstantResponse.eventVerify)

        //then
        assertEquals(mockDealsCheckoutGeneralInstantRequest, actual)
    }

    @Test
    fun mapCheckoutData_successMapCheckoutGeneralRequest_DataTypeNull(){
        //when
        mockDealsDetailGeneral.checkoutDataType = null
        val actual = viewModel.mapCheckoutDeals(mockDealsDetailGeneral, mockDealsVerifyGeneralResponse.eventVerify)

        //then
        assertEquals(mockDealsCheckoutGeneralRequest, actual)
    }

}