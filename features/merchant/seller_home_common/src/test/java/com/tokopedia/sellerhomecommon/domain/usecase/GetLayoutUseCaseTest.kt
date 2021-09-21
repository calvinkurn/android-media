package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.LayoutMapper
import com.tokopedia.sellerhomecommon.domain.model.GetLayoutResponse
import com.tokopedia.sellerhomecommon.utils.TestHelper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
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
import org.mockito.ArgumentMatchers.anyString

/**
 * Created By @ilhamsuaib on 09/06/20
 */

@ExperimentalCoroutinesApi
class GetLayoutUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_layout_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository
    @RelaxedMockK
    lateinit var mapper: LayoutMapper
    private val getLayoutUseCase by lazy {
        GetLayoutUseCase(gqlRepository, mapper, CoroutineTestDispatchersProvider)
    }

    private val params = GetLayoutUseCase.getRequestParams(anyString(), anyString())

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success when get layout`() = runBlocking {
        getLayoutUseCase.params = params
        val successResponse = TestHelper.createSuccessResponse<GetLayoutResponse>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val resultLayout = getLayoutUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertTrue(!resultLayout.isNullOrEmpty())
    }

    @Test
    fun `should failed when get layout`() = runBlocking {
        getLayoutUseCase.params = params
        val successResponse = TestHelper.createErrorResponse<GetLayoutResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        expectedException.expect(MessageErrorException::class.java)
        val resultLayout = getLayoutUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertTrue(resultLayout.isNullOrEmpty())
    }
}