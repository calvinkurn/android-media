package com.tokopedia.sellerhome.settings.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.settings.domain.entity.Balance
import com.tokopedia.sellerhome.settings.domain.entity.Info
import com.tokopedia.sellerhome.settings.domain.entity.ShopInfo
import com.tokopedia.sellerhome.settings.domain.entity.ShopInfoMoengage
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

/**
 * created by nakama on 25/03/20
 */

@ExperimentalCoroutinesApi
class GetSettingShopInfoUseCaseTest {

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
        GetSettingShopInfoUseCase(gqlRepository)
    }

    @Test
    fun `Success get setting shop info`() = runBlocking {

        val successResponse = TestHelper.createSuccessResponse<ShopInfo>(SUCCESS_RESPONSE)
        val successShopInfo =
                ShopInfo(
                        shopInfoMoengage = ShopInfoMoengage(
                                info = Info(
                                        shopName = "success_shop_name",
                                        shopAvatar = "https://success.shop.name/avatar.png"
                                )
                        ),
                        balance = Balance(
                                sellerBalance = 100000f,
                                buyerBalance = 100000f
                        )
                )

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        useCase.params = GetSettingShopInfoUseCase.createRequestParams(anyInt())
        val shopInfo = useCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertEquals(shopInfo,successShopInfo)

    }

    @Test
    fun `Failed get setting shop info`() = runBlocking {

        val errorResponse = TestHelper.createErrorResponse<ShopInfo>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        useCase.params = GetSettingShopInfoUseCase.createRequestParams(anyInt())
        val shopInfo = useCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertNull(shopInfo)
    }

}