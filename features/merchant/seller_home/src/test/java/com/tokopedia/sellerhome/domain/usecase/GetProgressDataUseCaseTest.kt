package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhome.domain.mapper.ProgressMapper
import com.tokopedia.sellerhome.domain.model.ProgressDataResponse
import com.tokopedia.sellerhome.utils.TestHelper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString

/**
 * Created By @ilhamsuaib on 2020-02-24
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
        GetProgressDataUseCase(gqlRepository, mapper)
    }

    private val params = GetProgressDataUseCase.getRequestParams(
            shopId = anyString(),
            dataKey = anyList(),
            date = anyString()
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success get progress data`() = runBlocking {
        getProgressDataUseCase.params = params
        val successResponse = TestHelper.createSuccessResponse<ProgressDataResponse>(SUCCESS_RESPONSE)

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
        val errorResponse = TestHelper.createErrorResponse<ProgressDataResponse>()

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