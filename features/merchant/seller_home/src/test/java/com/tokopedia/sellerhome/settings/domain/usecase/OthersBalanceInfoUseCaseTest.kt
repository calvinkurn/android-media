package com.tokopedia.sellerhome.settings.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.OtherBalanceResponse
import com.tokopedia.seller.menu.common.domain.entity.OthersBalance
import com.tokopedia.seller.menu.common.domain.usecase.BalanceInfoUseCase
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

/**
 * created by nakama on 25/03/20
 */

@ExperimentalCoroutinesApi
class OthersBalanceInfoUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_setting_shop_info_success_response"
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
        BalanceInfoUseCase(gqlRepository)
    }

    @Test
    fun `Success get setting shop info`() = runBlocking {

        val successResponse = TestHelper.createSuccessResponse<OtherBalanceResponse>(SUCCESS_RESPONSE)
        val successShopInfo =
            OthersBalance(
                sellerBalance = 100000f,
                buyerBalance = 100000f
            )

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val shopInfo = useCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertEquals(shopInfo,successShopInfo)

    }

    @Test
    fun `Failed get setting shop info`() = runBlocking {

        val errorResponse = TestHelper.createErrorResponse<OtherBalanceResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        val shopInfo = useCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertNull(shopInfo)
    }

}