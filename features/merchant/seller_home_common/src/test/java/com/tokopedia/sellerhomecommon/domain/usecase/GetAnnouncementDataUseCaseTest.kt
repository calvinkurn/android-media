package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.AnnouncementMapper
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetAnnouncementDataResponse
import com.tokopedia.sellerhomecommon.utils.TestHelper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.ArgumentMatchers

/**
 * Created By @ilhamsuaib on 11/11/20
 */

class GetAnnouncementDataUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_announcement_data_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    @RelaxedMockK
    lateinit var mapper: AnnouncementMapper

    private lateinit var getAnnouncementDataUseCase: GetAnnouncementDataUseCase

    private val params = GetPieChartDataUseCase.getRequestParams(
            dataKey = ArgumentMatchers.anyList(),
            dynamicParameter = DynamicParameterModel()
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        getAnnouncementDataUseCase = GetAnnouncementDataUseCase(gqlRepository, mapper, CoroutineTestDispatchersProvider)
    }

    @Test
    fun `should success when get announcement data`() = runBlocking {
        getAnnouncementDataUseCase.params = params

        val successResponse = TestHelper.createSuccessResponse<GetAnnouncementDataResponse>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val result = getAnnouncementDataUseCase.executeOnBackground()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        }

        Assert.assertTrue(!result.isNullOrEmpty())
    }

    @Test
    fun `should failed when get announcement data`() = runBlocking {
        getAnnouncementDataUseCase.params = params
        val errorResponse = TestHelper.createErrorResponse<GetAnnouncementDataResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(RuntimeException::class.java)
        val result = getAnnouncementDataUseCase.executeOnBackground()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        }

        Assert.assertTrue(result.isNullOrEmpty())
    }
}