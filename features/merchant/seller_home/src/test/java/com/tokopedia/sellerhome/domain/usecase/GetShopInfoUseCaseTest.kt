package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhome.domain.mapper.ShopInfoMapper
import com.tokopedia.sellerhome.domain.model.GetShopInfoResponse
import com.tokopedia.sellerhome.utils.TestHelper
import com.tokopedia.sellerhome.view.model.ShopInfoUiModel
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
import org.mockito.Matchers.anyString

/**
 * Created By @ilhamsuaib on 13/03/20
 */

@ExperimentalCoroutinesApi
class GetShopInfoUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_shop_info_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    private val getShopInfoUseCase by lazy {
        GetShopInfoUseCase(gqlRepository, ShopInfoMapper())
    }

    private val params = GetShopInfoUseCase.getRequestParam(anyString())

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success when get shop info`() = runBlocking {
        getShopInfoUseCase.params = params

        val successResponse = TestHelper.createSuccessResponse<GetShopInfoResponse>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val actualShopInfo: ShopInfoUiModel? = getShopInfoUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertEquals(getExpectedShopInfo(), actualShopInfo)
    }

    @Test
    fun `should failed when get shop info`() = runBlocking {
        getShopInfoUseCase.params = params
        val errorResponse = TestHelper.createErrorResponse<GetShopInfoResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        val actualShopInfo: ShopInfoUiModel? = getShopInfoUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertNull(actualShopInfo)
    }

    private fun getExpectedShopInfo(): ShopInfoUiModel {
        return ShopInfoUiModel(
                shopAvatar = "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2018/10/19/1479278/1479278_fa306e59-f15f-4048-b2f7-8ddc5d798926.jpeg",
                shopName = "Tumbler Starbucks 123"
        )
    }
}