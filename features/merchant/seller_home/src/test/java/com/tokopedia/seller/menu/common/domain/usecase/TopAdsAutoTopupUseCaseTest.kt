package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.TopAdsAutoTopupDataModel
import com.tokopedia.sellerhome.utils.TestHelper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Matchers.anyString

@ExperimentalCoroutinesApi
class TopAdsAutoTopupUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/top_ads_auto_topup_success_response"
    }

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    @get:Rule
    val expectedException = ExpectedException.none()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private val useCase by lazy {
        TopAdsAutoTopupUseCase(gqlRepository)
    }

    @Test
    fun `Success get top ads auto topup`() = runBlocking {

        val successResponse = TestHelper.createSuccessResponse<TopAdsAutoTopupDataModel>(SUCCESS_RESPONSE)
        val successIsTopAdsAutoTopup = false

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        useCase.params = TopAdsAutoTopupUseCase.createRequestParams(anyString())
        val isTopAdsAutoTopup = useCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertEquals(isTopAdsAutoTopup, successIsTopAdsAutoTopup)
    }

    @Test
    fun `Failed get top ads auto topup`() = runBlocking {

        val errorResponse = TestHelper.createErrorResponse<TopAdsAutoTopupDataModel>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        useCase.params = TopAdsAutoTopupUseCase.createRequestParams(anyString())
        val isTopAdsAutoTopup = useCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertNull(isTopAdsAutoTopup)
    }
}