package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.CardMapper
import com.tokopedia.sellerhomecommon.domain.model.GetCardDataResponse
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
class GetCardDataUseCaseTest {

    companion object {
        private const val RESPONSE_SUCCESS = "json/get_card_data_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository
    @RelaxedMockK
    lateinit var mapper: CardMapper

    private val getCardDataUseCase by lazy {
        GetCardDataUseCase(gqlRepository, mapper, CoroutineTestDispatchersProvider)
    }

    private val params = GetCardDataUseCase.getRequestParams(
            dataKey = ArgumentMatchers.anyList(),
            dynamicParameter = DynamicParameterModel()
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success when get card widget data`() = runBlocking {
        getCardDataUseCase.params = params
        val successResponse = TestHelper.createSuccessResponse<GetCardDataResponse>(RESPONSE_SUCCESS)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val result = getCardDataUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertTrue(!result.isNullOrEmpty())
    }

    @Test
    fun `should failed when get card widget data`() = runBlocking {
        getCardDataUseCase.params = params
        val errorResponse = TestHelper.createErrorResponse<GetCardDataResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        val result = getCardDataUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertTrue(result.isNullOrEmpty())
    }
}