package com.tokopedia.vouchergame.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.common.topupbills.data.*
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.list.data.VoucherGameListData
import com.tokopedia.vouchergame.list.data.VoucherGameOperator
import com.tokopedia.vouchergame.list.usecase.VoucherGameListUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

class VoucherGameListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mapParams = mapOf<String, String>()
    private val errorMessage = "unable to retrieve data"
    lateinit var gqlResponseFail: GraphqlResponse

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @MockK
    lateinit var voucherGameListUseCase: VoucherGameListUseCase

    lateinit var voucherGameListViewModel: VoucherGameListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val graphqlError = GraphqlError()
        graphqlError.message = errorMessage
        gqlResponseFail = GraphqlResponse(
                mapOf(),
                mapOf(MessageErrorException::class.java to listOf(graphqlError)), false)

        voucherGameListViewModel =
                VoucherGameListViewModel(voucherGameListUseCase, graphqlRepository, Dispatchers.Unconfined)
    }

    @Test
    fun getVoucherGameOperators_Success() {
        val useCaseResultSuccess = VoucherGameListData(operators = listOf(VoucherGameOperator(id = 1)))
        coEvery {
            voucherGameListUseCase.getVoucherGameOperators(any(), any(), any(), any())
        } returns Success(useCaseResultSuccess)

        val observer = Observer<Result<VoucherGameListData>> {
            assert(it is Success)
            val response = it as Success
            assert(response.data.operators.isNotEmpty())
            assertEquals(response.data.operators[0].id, 1)
        }

        try {
            voucherGameListViewModel.voucherGameList.observeForever(observer)
            voucherGameListViewModel.getVoucherGameOperators("", mapOf(), "")
        } finally {
            voucherGameListViewModel.voucherGameList.removeObserver(observer)
        }
    }

    @Test
    fun getVoucherGameOperators_Fail() {
        coEvery {
            voucherGameListUseCase.getVoucherGameOperators(any(), any(), any(), any())
        } returns Fail(MessageErrorException(errorMessage))

        val observer = Observer<Result<VoucherGameListData>> {
            assert(it is Fail)
            assertEquals((it as Fail).throwable.message, errorMessage)
        }

        try {
            voucherGameListViewModel.voucherGameList.observeForever(observer)
            voucherGameListViewModel.getVoucherGameOperators("", mapParams, "")
        } finally {
            voucherGameListViewModel.voucherGameList.removeObserver(observer)
        }
    }

    @Test
    fun getVoucherGameMenuDetail_Success() {
        val telcoCatalogMenuDetailData = TelcoCatalogMenuDetailData(TopupBillsMenuDetail(
                TopupBillsCatalog(1),
                listOf(TopupBillsRecommendation(title = "recommendation")),
                listOf(TopupBillsPromo(1)),
                listOf(TopupBillsTicker(1)),
                listOf(TopupBillsBanner(1))))
        val gqlResponseSuccess = GraphqlResponse(
                mapOf(TelcoCatalogMenuDetailData::class.java to telcoCatalogMenuDetailData),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        val observer = Observer<Result<TopupBillsMenuDetail>> {
            assert(it is Success)
            val response = (it as Success).data
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

        try {
            voucherGameListViewModel.voucherGameMenuDetail.observeForever(observer)
            voucherGameListViewModel.getVoucherGameMenuDetail("", mapParams)
        } finally {
            voucherGameListViewModel.voucherGameMenuDetail.removeObserver(observer)
        }
    }

    @Test
    fun getVoucherGameMenuDetail_Fail() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        val observer = Observer<Result<TopupBillsMenuDetail>> {
            assert(it is Fail)
            assertEquals((it as Fail).throwable.message, errorMessage)
        }

        try {
            voucherGameListViewModel.voucherGameMenuDetail.observeForever(observer)
            voucherGameListViewModel.getVoucherGameMenuDetail("", mapParams)
        } finally {
            voucherGameListViewModel.voucherGameMenuDetail.removeObserver(observer)
        }
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