package com.tokopedia.seller.menu.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.TopAdsDepositDataModel
import com.tokopedia.sellerhome.domain.usecase.TopAdsDashboardDepositUseCase
import com.tokopedia.seller.menu.utils.TestHelper
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
import org.mockito.ArgumentMatchers.anyLong

@ExperimentalCoroutinesApi
class TopAdsDashboardDepositUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/top_ads_dashboard_deposit_success_response"
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
        com.tokopedia.sellerhome.domain.usecase.TopAdsDashboardDepositUseCase(gqlRepository)
    }

    @Test
    fun `Success get top ads dashboard deposit`() = runBlocking {

        val successResponse = TestHelper.createSuccessResponse<TopAdsDepositDataModel>(
            SUCCESS_RESPONSE
        )
        val successTopAdsDashboardDeposit = 0f

        coEvery {
            gqlRepository.response(any(), any())
        } returns successResponse

        useCase.params = com.tokopedia.sellerhome.domain.usecase.TopAdsDashboardDepositUseCase.createRequestParams(anyLong())
        val topAdsDashboardDeposit = useCase.executeOnBackground()

        coVerify {
            gqlRepository.response(any(), any())
        }

        assertEquals(topAdsDashboardDeposit, successTopAdsDashboardDeposit)
    }

    @Test
    fun `Failed get top ads dashboard deposit`() = runBlocking {

        val errorResponse = TestHelper.createErrorResponse<TopAdsDepositDataModel>()

        coEvery {
            gqlRepository.response(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        useCase.params = com.tokopedia.sellerhome.domain.usecase.TopAdsDashboardDepositUseCase.createRequestParams(anyLong())
        val topAdsDashboardDeposit = useCase.executeOnBackground()

        coVerify {
            gqlRepository.response(any(), any())
        }

        assertNull(topAdsDashboardDeposit)
    }

}