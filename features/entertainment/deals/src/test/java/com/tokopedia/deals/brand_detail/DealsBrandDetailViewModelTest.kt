package com.tokopedia.deals.brand_detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.deals.DealsJsonMapper
import com.tokopedia.deals.data.entity.DealsBrandDetail
import com.tokopedia.deals.domain.GetBrandDetailsUseCase
import com.tokopedia.deals.ui.brand_detail.DealsBrandDetailViewModel
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import java.lang.reflect.Type

class DealsBrandDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider

    private lateinit var viewModel: DealsBrandDetailViewModel
    private lateinit var mockResponse: DealsBrandDetail

    private val mapParams = mapOf<String, String>()

    var getBrandDetail: GetBrandDetailsUseCase = mockk()

    @Before
    fun setup() {
        mockResponse = Gson().fromJson(
            DealsJsonMapper.getJson("brand_detail_mock.json"),
            DealsBrandDetail::class.java
        )

        viewModel = DealsBrandDetailViewModel(
            getBrandDetail,
            dispatcher
        )
    }

    @Test
    fun getbranddetail_success_shouldsuccess() {
        // given
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = DealsBrandDetail::class.java
        result[objectType] = mockResponse

        coEvery { getBrandDetail.invoke(any()) } returns mockResponse

        // when
        viewModel.getBrandDetail("", "")

        // then
        val actualData = viewModel.brandDetail.getOrAwaitValue()
        assert(actualData is Success)
        assertEquals((actualData as Success).data, mockResponse)
    }

    @Test
    fun getbranddetail_error_shoulderror() {
        // given
        val message = "Error Fetch History"
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val errorGql = GraphqlError()
        val objectType = DealsBrandDetail::class.java
        errorGql.message = message
        errors[objectType] = listOf(errorGql)
        val gqlResponseError = GraphqlResponse(result, errors, false)

        coEvery { getBrandDetail.invoke(any()) } throws Exception(message)

        // when
        viewModel.getBrandDetail(anyString(), anyString())

        // then
        val actualData = viewModel.brandDetail.getOrAwaitValue()
        assert(actualData is Fail)
        assertEquals((actualData as Fail).throwable.message, message)
    }
}
