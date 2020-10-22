package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.ReputationShopsResult
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
import org.mockito.Matchers.anyInt

@ExperimentalCoroutinesApi
class GetShopBadgeUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_shop_badge_success_response"
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
        GetShopBadgeUseCase(gqlRepository)
    }

    @Test
    fun `Success get shop badge`() = runBlocking {

        val successResponse = TestHelper.createSuccessResponse<ReputationShopsResult>(SUCCESS_RESPONSE)
        val successShopBadge = "https://success.shop.name/avatar.png"

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        useCase.params = GetShopBadgeUseCase.createRequestParams(anyInt())
        val shopBadge = useCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertEquals(shopBadge, successShopBadge)
    }

    @Test
    fun `Failed get shop badge`() = runBlocking {

        val errorResponse = TestHelper.createErrorResponse<ReputationShopsResult>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        useCase.params = GetShopBadgeUseCase.createRequestParams(anyInt())
        val shopBadge = useCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertNull(shopBadge)

    }
}