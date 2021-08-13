package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.CarouselMapper
import com.tokopedia.sellerhomecommon.domain.model.GetCarouselDataResponse
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
class GetCarouselDataUseCaseTest {

    companion object {
        private const val RESPONSE_SUCCESS = "json/get_carousel_data_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository
    @RelaxedMockK
    lateinit var mapper: CarouselMapper

    private val getCarouselDataUseCase by lazy {
        GetCarouselDataUseCase(gqlRepository, mapper, CoroutineTestDispatchersProvider)
    }

    private val params = GetCarouselDataUseCase.getRequestParams(listOf(ArgumentMatchers.anyString()))

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success get carousel data`() = runBlocking {
        getCarouselDataUseCase.params = params
        val successResponse = TestHelper.createSuccessResponse<GetCarouselDataResponse>(RESPONSE_SUCCESS)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val carouselData = getCarouselDataUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertTrue(!carouselData.isNullOrEmpty())
    }

    @Test
    fun `should failed get carousel data`() = runBlocking {
        getCarouselDataUseCase.params = params
        val errorResponse = TestHelper.createErrorResponse<GetCarouselDataResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        val carouselData = getCarouselDataUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertTrue(carouselData.isNullOrEmpty())
    }
}