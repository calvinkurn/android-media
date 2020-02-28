package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhome.domain.mapper.CarouselMapper
import com.tokopedia.sellerhome.domain.model.CarouselDataResponse
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
import org.mockito.ArgumentMatchers.anyString

/**
 * Created By @ilhamsuaib on 2020-02-24
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
        GetCarouselDataUseCase(gqlRepository, mapper)
    }

    private val params = GetCarouselDataUseCase.getRequestParams(listOf(anyString()))

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success get carousel data`() = runBlocking {
        getCarouselDataUseCase.params = params
        val successResponse = TestHelper.createSuccessResponse<CarouselDataResponse>(RESPONSE_SUCCESS)

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
        val errorResponse = TestHelper.createErrorResponse<CarouselDataResponse>()

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