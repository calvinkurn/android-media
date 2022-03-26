package com.tokopedia.seller.menu.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.ShopTotalFollowers
import com.tokopedia.seller.menu.common.domain.usecase.GetShopTotalFollowersUseCase
import com.tokopedia.seller.menu.utils.TestHelper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.ArgumentMatchers.anyLong

@ExperimentalCoroutinesApi
class GetShopTotalFollowersUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_shop_total_followers_success_response"
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
        GetShopTotalFollowersUseCase(gqlRepository)
    }

    @Test
    fun `Success get total followers`() = runBlocking {

        val successResponse = TestHelper.createSuccessResponse<ShopTotalFollowers>(SUCCESS_RESPONSE)
        val successTotalFollowers = 0L

        coEvery {
            gqlRepository.response(any(), any())
        } returns successResponse

        useCase.params = GetShopTotalFollowersUseCase.createRequestParams(anyLong())
        val totalFollowers = useCase.executeOnBackground()

        coVerify {
            gqlRepository.response(any(), any())
        }

        assertEquals(totalFollowers, successTotalFollowers)

    }

    @Test
    fun `Failed get total followers`() = runBlocking {

        val errorResponse = TestHelper.createErrorResponse<ShopTotalFollowers>()

        coEvery {
            gqlRepository.response(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        useCase.params = GetShopTotalFollowersUseCase.createRequestParams(anyLong())
        val totalFollowers = useCase.executeOnBackground()

        coVerify {
            gqlRepository.response(any(), any())
        }

        Assert.assertNull(totalFollowers)
    }
}