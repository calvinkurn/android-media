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

        //then
        assertEquals(mockRequest, actual)

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
        val message = "Error Fetch History"
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