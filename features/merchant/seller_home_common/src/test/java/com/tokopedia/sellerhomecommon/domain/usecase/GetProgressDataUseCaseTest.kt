package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.ProgressMapper
import com.tokopedia.sellerhomecommon.domain.model.GetProgressDataResponse
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
class GetProgressDataUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_progress_data_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository
    @RelaxedMockK
    lateinit var mapper: ProgressMapper
    private val getProgressDataUseCase by lazy {
        GetProgressDataUseCase(gqlRepository, mapper, CoroutineTestDispatchersProvider)
    }

    private val params = GetProgressDataUseCase.getRequestParams(
            dataKey = ArgumentMatchers.anyList(),
            date = ArgumentMatchers.anyString()
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success get progress data`() = runBlocking {
        getProgressDataUseCase.params = params
        val successResponse = TestHelper.createSuccessResponse<GetProgressDataResponse>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val progressData = getProgressDataUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertTrue(!progressData.isNullOrEmpty())
    }

    @Test
    fun `should failed get progress data`() = runBlocking {
        getProgressDataUseCase.params = params
        val errorResponse = TestHelper.createErrorResponse<GetProgressDataResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        val progressData = getProgressDataUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertTrue(progressData.isNullOrEmpty())
    }
}