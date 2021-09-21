package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.PieChartMapper
import com.tokopedia.sellerhomecommon.domain.model.GetPieChartDataResponse
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
class GetPieChartDataUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_pie_chart_data_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    @RelaxedMockK
    lateinit var mapper: PieChartMapper

    private lateinit var getPieChartDataUseCase: GetPieChartDataUseCase

    private val params = GetPieChartDataUseCase.getRequestParams(
            dataKey = ArgumentMatchers.anyList(),
            dynamicParameter = DynamicParameterModel()
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getPieChartDataUseCase = GetPieChartDataUseCase(gqlRepository, mapper, CoroutineTestDispatchersProvider)
    }

    @Test
    fun `should success when get pie chart data`() = runBlocking {
        getPieChartDataUseCase.params = params

        val successResponse = TestHelper.createSuccessResponse<GetPieChartDataResponse>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val result = getPieChartDataUseCase.executeOnBackground()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        }

        Assert.assertTrue(!result.isNullOrEmpty())
    }

    @Test
    fun `should failed when get pie chart data`() = runBlocking {
        getPieChartDataUseCase.params = params
        val errorResponse = TestHelper.createErrorResponse<GetPieChartDataResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(RuntimeException::class.java)
        val result = getPieChartDataUseCase.executeOnBackground()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        }

        Assert.assertTrue(result.isNullOrEmpty())
    }
}