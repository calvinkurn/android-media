package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.MultiLineGraphMapper
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetMultiLineGraphResponse
import com.tokopedia.sellerhomecommon.utils.TestHelper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
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
import org.mockito.ArgumentMatchers

/**
 * Created By @ilhamsuaib on 27/10/20
 */

@ExperimentalCoroutinesApi
class GetMultiLineGraphUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_multi_line_graph_data_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    @RelaxedMockK
    lateinit var mapper: MultiLineGraphMapper

    lateinit var getMultiLineGraphUseCase: GetMultiLineGraphUseCase
    lateinit var params: RequestParams

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        params = GetMultiLineGraphUseCase.getRequestParams(
                dataKey = ArgumentMatchers.anyList(),
                dynamicParameter = DynamicParameterModel()
        )
        getMultiLineGraphUseCase = GetMultiLineGraphUseCase(gqlRepository, mapper, CoroutineTestDispatchersProvider)
    }

    @Test
    fun `should success get multi line data`() = runBlocking {
        getMultiLineGraphUseCase.params = params
        val successResponse = TestHelper.createSuccessResponse<GetMultiLineGraphResponse>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val data = getMultiLineGraphUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        Assert.assertTrue(!data.isNullOrEmpty())
    }

    @Test
    fun `should failed get multi line data`() = runBlocking {
        getMultiLineGraphUseCase.params = params
        val errorResponse = TestHelper.createErrorResponse<GetMultiLineGraphResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(RuntimeException::class.java)
        val multiLineGraphData = getMultiLineGraphUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        Assert.assertTrue(multiLineGraphData.isNullOrEmpty())
    }
}