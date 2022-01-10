package com.tokopedia.digital_deals

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.digital_deals.data.DealsVerifyRequest
import com.tokopedia.digital_deals.data.DealsVerifyResponse
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse
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

class DealsVerifyViewModelTest {

    @get: Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider

    private lateinit var viewModel: DealsVerifyViewModel
    private lateinit var mockResponse: DealsVerifyResponse
    private lateinit var mockRequest: DealsVerifyRequest
    private lateinit var mockDealsDetail: DealsDetailsResponse

    var graphqlRepository: GraphqlRepository = mockk()

    @Before
    fun setup(){
        mockResponse = Gson().fromJson(
            DealsJsonMapper.getJson("deals_verify.json"),
            DealsVerifyResponse::class.java
        )

        mockRequest = Gson().fromJson(
                DealsJsonMapper.getJson("deals_verify_request.json"),
                DealsVerifyRequest::class.java
        )

        mockDealsDetail = Gson().fromJson(
                DealsJsonMapper.getJson("deals_detail.json"),
                DealsDetailsResponse::class.java
        )

        viewModel = DealsVerifyViewModel(
                graphqlRepository, dispatcher
        )
    }

    @Test
    fun mapVerifyData_success_successMapVerify(){
        //given
        val quantity = 1

        //when
        val actual = viewModel.mapVerifyRequest(quantity, mockDealsDetail)
        val startDate = viewModel.getDateMilis(mockDealsDetail.minStartDate)
        val endDate = viewModel.getDateMilis(mockDealsDetail.maxEndDate)

        //then
        assertEquals(mockRequest.book, actual.book)
        assertEquals(mockRequest.checkout, actual.checkout)
        assertEquals(mockRequest.cartdata.error, actual.cartdata.error)
        assertEquals(mockRequest.cartdata.errorDescription, actual.cartdata.errorDescription)
        assertEquals(mockRequest.cartdata.status, actual.cartdata.status)
        assertEquals(mockRequest.cartdata.metadata.categoryName, actual.cartdata.metadata.categoryName)
        assertEquals(mockRequest.cartdata.metadata.productIds, actual.cartdata.metadata.productIds)
        assertEquals(mockRequest.cartdata.metadata.productNames, actual.cartdata.metadata.productNames)
        assertEquals(mockRequest.cartdata.metadata.providerIds, actual.cartdata.metadata.providerIds)
        assertEquals(mockRequest.cartdata.metadata.totalPrice, actual.cartdata.metadata.totalPrice)
        assertEquals(mockRequest.cartdata.metadata.itemMaps[0].categoryId, actual.cartdata.metadata.itemMaps[0].categoryId)
        assertEquals(startDate, actual.cartdata.metadata.itemMaps[0].startTime)
        assertEquals(endDate, actual.cartdata.metadata.itemMaps[0].endTime)
        assertEquals(mockRequest.cartdata.metadata.itemMaps[0].flagID, actual.cartdata.metadata.itemMaps[0].flagID)
        assertEquals(mockRequest.cartdata.metadata.itemMaps[0].id, actual.cartdata.metadata.itemMaps[0].id)
        assertEquals(mockRequest.cartdata.metadata.itemMaps[0].locationDesc, actual.cartdata.metadata.itemMaps[0].locationDesc)
        assertEquals(mockRequest.cartdata.metadata.itemMaps[0].name, actual.cartdata.metadata.itemMaps[0].name)
        assertEquals(mockRequest.cartdata.metadata.itemMaps[0].packageId, actual.cartdata.metadata.itemMaps[0].packageId)
        assertEquals(mockRequest.cartdata.metadata.itemMaps[0].packageName, actual.cartdata.metadata.itemMaps[0].packageName)
        assertEquals(mockRequest.cartdata.metadata.itemMaps[0].providerId, actual.cartdata.metadata.itemMaps[0].providerId)
        assertEquals(mockRequest.cartdata.metadata.itemMaps[0].categoryId, actual.cartdata.metadata.itemMaps[0].categoryId)
        assertEquals(mockRequest.cartdata.metadata.itemMaps[0].productName, actual.cartdata.metadata.itemMaps[0].productName)
        assertEquals(mockRequest.cartdata.metadata.itemMaps[0].price, actual.cartdata.metadata.itemMaps[0].price)
        assertEquals(mockRequest.cartdata.metadata.itemMaps[0].quantity, actual.cartdata.metadata.itemMaps[0].quantity)
        assertEquals(mockRequest.cartdata.metadata.itemMaps[0].totalPrice, actual.cartdata.metadata.itemMaps[0].totalPrice)
        assertEquals(mockRequest.cartdata.metadata.itemMaps[0].scheduleTimestamp, actual.cartdata.metadata.itemMaps[0].scheduleTimestamp)
        assertEquals(mockRequest.cartdata.metadata.itemMaps[0].productImage, actual.cartdata.metadata.itemMaps[0].productImage)

    }

    @Test
    fun verifyDeals_success_shouldsuccess(){
        //given
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = DealsVerifyResponse::class.java
        result[objectType] = mockResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        viewModel.verify(mockRequest)

        //then
        val actualData = viewModel.dealsVerify.value
        assert(actualData is Success)
        assertEquals(mockResponse, (actualData as Success).data)
    }


    @Test
    fun verifyDeals_error_shoulderror(){
        //given
        val message = "Error Fetch Verify"
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val errorGql = GraphqlError()
        val objectType = DealsVerifyResponse::class.java
        errorGql.message = message
        errors[objectType] = listOf(errorGql)
        val gqlResponseError = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseError

        //when
        viewModel.verify(mockRequest)

        //then
        val actualData = viewModel.dealsVerify.value
        assert(actualData is Fail)
        assertEquals((actualData as Fail).throwable.message, message)
    }
}