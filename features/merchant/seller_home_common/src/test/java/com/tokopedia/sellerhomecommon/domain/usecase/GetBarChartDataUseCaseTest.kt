package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.BarChartMapper
import com.tokopedia.sellerhomecommon.domain.model.GetBarChartDataResponse
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.utils.TestHelper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.ArgumentMatchers

/**
 * Created By @ilhamsuaib on 22/07/20
 */

@ExperimentalCoroutinesApi
class GetBarChartDataUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_bar_chart_data_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    @RelaxedMockK
    lateinit var mapper: BarChartMapper

    private lateinit var getBarChartDataUseCase: GetBarChartDataUseCase

    private val params = GetPieChartDataUseCase.getRequestParams(
            dataKey = ArgumentMatchers.anyList(),
            dynamicParameter = DynamicParameterModel()
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getBarChartDataUseCase = GetBarChartDataUseCase(gqlRepository, mapper, CoroutineTestDispatchersProvider)
    }

    @Test
    fun `should success when get bar chart data`() = runBlocking {
        getBarChartDataUseCase.params = params

        val successResponse = TestHelper.createSuccessResponse<GetBarChartDataResponse>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val result = getBarChartDataUseCase.executeOnBackground()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        }

        Assert.assertTrue(!result.isNullOrEmpty())
    }

    @Test
    fun `should failed when get bar chart data`() = runBlocking {
        getBarChartDataUseCase.params = params
        val errorResponse = TestHelper.createErrorResponse<GetBarChartDataResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        val result = getBarChartDataUseCase.executeOnBackground()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        }

        Assert.assertTrue(result.isNullOrEmpty())
    }
}