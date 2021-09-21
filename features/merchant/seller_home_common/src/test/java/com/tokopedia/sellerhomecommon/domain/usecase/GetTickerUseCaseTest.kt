package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.TickerMapper
import com.tokopedia.sellerhomecommon.domain.model.GetTickerResponse
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import com.tokopedia.sellerhomecommon.utils.TestHelper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.lang.RuntimeException

/**
 * Created By @ilhamsuaib on 03/09/20
 */

@ExperimentalCoroutinesApi
class GetTickerUseCaseTest {

    companion object {
        private const val TICKER_PAGE_NAME = "seller-statistic"
        private const val SUCCESS_RESPONSE = "json/get_tickers_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    @RelaxedMockK
    lateinit var mapper: TickerMapper

    private val params = GetTickerUseCase.createParams(TICKER_PAGE_NAME)

    private lateinit var getTickerUseCase: GetTickerUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getTickerUseCase = GetTickerUseCase(gqlRepository, mapper, CoroutineTestDispatchersProvider)
    }

    @Test
    fun `should success when get ticker`() = runBlocking {
        getTickerUseCase.params = params

        val isFromCache = false
        val successResponse = TestHelper.createSuccessResponse<GetTickerResponse>(SUCCESS_RESPONSE)
        val expectedTickers = listOf(TickerItemUiModel(
                id = "253",
                title = "ticker seller",
                type = 1,
                message = "Bantu kami menjadi lebih baik dengan membagikan pengalamanmu <a href=\"https://docs.google.com/forms/d/1t-KeapZJwOeYOBnbXDEmzRJiUqMBicE9cQIauc40qMU\">di sini</a><br>",
                color = "#cde4c3",
                redirectUrl = "https://docs.google.com/forms/d/1t-KeapZJwOeYOBnbXDEmzRJiUqMBicE9cQIauc40qMU"
        ))

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        coEvery {
            mapper.mapRemoteDataToUiData(any(), isFromCache)
        } returns expectedTickers

        val result = getTickerUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        coVerify {
            mapper.mapRemoteDataToUiData(any(), isFromCache)
        }

        Assert.assertEquals(expectedTickers, result)
    }

    @Test
    fun `should throw runtime exception when get error ticker response`() = runBlocking {
        getTickerUseCase.params = params

        val errorResponse = TestHelper.createErrorResponse<GetTickerResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(RuntimeException::class.java)
        val result = getTickerUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        Assert.assertTrue(result.isNullOrEmpty())
    }
}