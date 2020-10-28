package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.ShopStatusResponse
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
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
class ShopStatusTypeUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/shop_status_type_success_response"
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
        ShopStatusTypeUseCase(gqlRepository)
    }

    @Test
    fun `Success get shop status type`() = runBlocking {

        val successResponse = TestHelper.createSuccessResponse<ShopStatusResponse>(SUCCESS_RESPONSE)
        val successShopType = ShopType.OfficialStore

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        useCase.params = ShopStatusTypeUseCase.createRequestParams(anyInt())
        val shopType = useCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertEquals(shopType, successShopType)
    }

    @Test
    fun `Failed get shop status type`() = runBlocking {

        val errorResponse = TestHelper.createErrorResponse<ShopStatusResponse>()
        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        useCase.params = ShopStatusTypeUseCase.createRequestParams(anyInt())
        val shopType = useCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertNull(shopType)
    }
}