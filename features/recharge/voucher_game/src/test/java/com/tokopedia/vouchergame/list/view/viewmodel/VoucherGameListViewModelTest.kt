package com.tokopedia.vouchergame.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.data.*
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.vouchergame.list.data.VoucherGameListData
import com.tokopedia.vouchergame.list.data.VoucherGameOperator
import com.tokopedia.vouchergame.list.usecase.VoucherGameListUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class VoucherGameListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mapParams = mapOf<String, String>()
    lateinit var gqlResponseFail: GraphqlResponse

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @MockK
    lateinit var voucherGameListUseCase: VoucherGameListUseCase

    lateinit var voucherGameListViewModel: VoucherGameListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = MessageErrorException::class.java

        result[objectType] = null
        errors[objectType] = listOf(GraphqlError())

        gqlResponseFail = GraphqlResponse(result, errors, false)

        voucherGameListViewModel =
                VoucherGameListViewModel(voucherGameListUseCase, graphqlRepository, CoroutineTestDispatchersProvider)
    }

    @Test
    fun getVoucherGameOperators_Success() {
        val useCaseResultSuccess = VoucherGameListData(operators = listOf(VoucherGameOperator(id = 1)))
        coEvery {
            voucherGameListUseCase.getVoucherGameOperators(any(), any(), any(), any())
        } returns Success(useCaseResultSuccess)

        voucherGameListViewModel.getVoucherGameOperators("", mapParams, "")
        val actualData = voucherGameListViewModel.voucherGameList.value
        assert(actualData is Success)
        val response = actualData as Success
        assert(response.data.operators.isNotEmpty())
        assertEquals(response.data.operators[0].id, 1)
    }

    @Test
    fun getVoucherGameOperators_Fail() {
        coEvery {
            voucherGameListUseCase.getVoucherGameOperators(any(), any(), any(), any())
        } returns Fail(MessageErrorException())

        voucherGameListViewModel.getVoucherGameOperators("", mapParams, "")
        val actualData = voucherGameListViewModel.voucherGameList.value
        assert(actualData is Fail)
    }

    @Test
    fun getVoucherGameMenuDetail_Success() {
        val telcoCatalogMenuDetailData = TelcoCatalogMenuDetailData(TopupBillsMenuDetail(
                TopupBillsCatalog(1),
                listOf(TopupBillsRecommendation(title = "recommendation")),
                listOf(TopupBillsPromo(1)),
                listOf(TopupBillsTicker(1)),
                listOf(TopupBillsBanner(1))))
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = TelcoCatalogMenuDetailData::class.java
        result[objectType] = telcoCatalogMenuDetailData
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        voucherGameListViewModel.getVoucherGameMenuDetail("", mapParams)
        val actualData = voucherGameListViewModel.voucherGameMenuDetail.value
        assert(actualData is Success)
        val response = (actualData as Success).data
        assertEquals(response.catalog.id, 1)
        assert(response.recommendations.isNotEmpty())
        assertEquals(response.recommendations[0].title, "recommendation")
        assert(response.promos.isNotEmpty())
        assertEquals(response.promos[0].id, 1)
        assert(response.tickers.isNotEmpty())
        assertEquals(response.tickers[0].id, 1)
        assert(response.banners.isNotEmpty())
        assertEquals(response.banners[0].id, 1)
    }

    @Test
    fun getVoucherGameMenuDetail_Fail() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        voucherGameListViewModel.getVoucherGameMenuDetail("", mapParams)
        val actualData = voucherGameListViewModel.voucherGameMenuDetail.value
        assert(actualData is Fail)
    }

    @Test
    fun createParams() {
        coEvery { voucherGameListUseCase.createParams(1) } returns mapParams

        val params = voucherGameListViewModel.createParams(1)
        assertEquals(params, mapParams)
    }

    @Test
    fun createMenuDetailParams() {
        val expectedResult = mapOf(VoucherGameListViewModel.PARAM_MENU_ID to 1)
        val params = voucherGameListViewModel.createMenuDetailParams(1)
        assertEquals(params, expectedResult)
    }
}