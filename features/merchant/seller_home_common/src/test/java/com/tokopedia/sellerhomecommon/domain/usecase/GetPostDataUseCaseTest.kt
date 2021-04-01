package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.PostMapper
import com.tokopedia.sellerhomecommon.domain.model.GetPostDataResponse
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.utils.TestHelper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
class GetPostDataUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_post_data_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository
    @RelaxedMockK
    lateinit var mapper: PostMapper
    private val getPostDataUseCase by lazy {
        GetPostDataUseCase(gqlRepository, mapper, CoroutineTestDispatchersProvider)
    }

    private val params = GetPostDataUseCase.getRequestParams(
            dataKey = ArgumentMatchers.anyList(),
            dynamicParameter = DynamicParameterModel()
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success get post list data`() = runBlocking {
        getPostDataUseCase.params = params
        val successResponse = TestHelper.createSuccessResponse<GetPostDataResponse>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val result = getPostDataUseCase.executeOnBackground()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        }

        assertTrue(!result.isNullOrEmpty())
    }

    @Test
    fun `should failed get post list data`() = runBlocking {
        getPostDataUseCase.params = params
        val errorResponse = TestHelper.createErrorResponse<GetPostDataResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        val result = getPostDataUseCase.executeOnBackground()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        }

        assertTrue(result.isNullOrEmpty())
    }
}