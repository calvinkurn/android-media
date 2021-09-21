package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.LineGraphMapper
import com.tokopedia.sellerhomecommon.domain.model.GetLineGraphDataResponse
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.utils.TestHelper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.ArgumentMatchers

/**
 * Created By @ilhamsuaib on 21/05/20
 */

@ExperimentalCoroutinesApi
class GetLineGraphDataUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_line_graph_data_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository
    @RelaxedMockK
    lateinit var mapper: LineGraphMapper
    private val getLineGraphDataUseCase by lazy {
        GetLineGraphDataUseCase(gqlRepository, mapper, CoroutineTestDispatchersProvider)
    }

    private val params = GetLineGraphDataUseCase.getRequestParams(
            dataKey = ArgumentMatchers.anyList(),
            dynamicParameter = DynamicParameterModel()
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success get line graph data`() = runBlocking {
        getLineGraphDataUseCase.params = params
        val successResponse = TestHelper.createSuccessResponse<GetLineGraphDataResponse>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val lineGraphData = getLineGraphDataUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertTrue(!lineGraphData.isNullOrEmpty())
    }

    @Test
    fun `should failed get line graph data`() = runBlocking {
        getLineGraphDataUseCase.params = params
        val errorResponse = TestHelper.createErrorResponse<GetLineGraphDataResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        val lineGraphData = getLineGraphDataUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertTrue(lineGraphData.isNullOrEmpty())
    }
}