package com.tokopedia.deals.brand_detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.deals.DealsJsonMapper
import com.tokopedia.deals.brand_detail.data.DealsBrandDetail
import com.tokopedia.deals.brand_detail.ui.viewmodel.DealsBrandDetailViewModel
import com.tokopedia.deals.common.data.DealsNearestLocationParam
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

class DealsBrandDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider

    private lateinit var viewModel: DealsBrandDetailViewModel
    private lateinit var mockResponse: DealsBrandDetail

    private val mapParams = mapOf<String, String>()


    var graphqlRepository: GraphqlRepository = mockk()

    @Before
    fun setup(){
        mockResponse = Gson().fromJson(
                DealsJsonMapper.getJson("brand_detail_mock.json"),
                DealsBrandDetail::class.java
        )

        viewModel = DealsBrandDetailViewModel(
                graphqlRepository, dispatcher
        )
    }

    @Test
    fun getbranddetail_success_shouldsuccess(){
        //given
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = DealsBrandDetail::class.java
        result[objectType] = mockResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        viewModel.getBrandDetail(mapParams)

        //then
        val actualData = viewModel.brandDetail.value
        assert(actualData is Success)
        assertEquals((actualData as Success).data, mockResponse)
    }

    @Test
    fun getbranddetail_error_shoulderror(){
        //given
        val message = "Error Fetch History"
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val errorGql = GraphqlError()
        val objectType = DealsBrandDetail::class.java
        errorGql.message = message
        errors[objectType] = listOf(errorGql)
        val gqlResponseError = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseError

        //when
        viewModel.getBrandDetail(mapParams)

        //then
        val actualData = viewModel.brandDetail.value
        assert(actualData is Fail)
        assertEquals((actualData as Fail).throwable.message, message)
    }

    @Test
    fun createparam_paramscreated_shouldcreated(){
        //given
        val coordinates = "-6.2087634,106.845599"
        val seoUrl = "klik-dokter-6519"

        //when
        val actual = viewModel.createParams(coordinates, seoUrl)

        //then
        assertEquals(actual, mapOf(DealsBrandDetailViewModel.PARAM_BRAND_DETAIL to arrayListOf(
                DealsNearestLocationParam(DealsNearestLocationParam.PARAM_SEO_URL, seoUrl),
                DealsNearestLocationParam(DealsNearestLocationParam.PARAM_COORDINATES, coordinates),
                DealsNearestLocationParam(DealsNearestLocationParam.PARAM_SIZE, DealsNearestLocationParam.VALUE_SIZE_PRODUCT),
                DealsNearestLocationParam(DealsNearestLocationParam.PARAM_PAGE, DealsNearestLocationParam.VALUE_PAGE_BRAND)
        )))
    }


}