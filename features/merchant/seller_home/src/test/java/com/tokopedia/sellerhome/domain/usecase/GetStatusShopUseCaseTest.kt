package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhome.domain.model.*
import com.tokopedia.sellerhome.utils.TestHelper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.rules.ExpectedException
import org.mockito.Matchers.anyString

/**
 * Created By @ilhamsuaib on 13/03/20
 */

@ExperimentalCoroutinesApi
class GetStatusShopUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_status_shop_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    private val getStatusShopUseCase by lazy {
        GetStatusShopUseCase(gqlRepository)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    private val params = GetStatusShopUseCase.createRequestParams(anyString())

    @Test
    fun `should success when get status shop`() = runBlocking {
        getStatusShopUseCase.params = params
        val successResponse = TestHelper.createSuccessResponse<GetShopStatusResponse>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val actualStatusShop = getStatusShopUseCase.executeOnBackground()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        }

        assertEquals(getExpectedStatusShop(), actualStatusShop)
    }

    @Test
    fun `should failed when get status shop`() = runBlocking {
        getStatusShopUseCase.params = params
        val errorResponse = TestHelper.createErrorResponse<GetShopStatusResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        val actualStatusShop: GetShopStatusResponse? = getStatusShopUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertNull(actualStatusShop)
    }

    private fun getExpectedStatusShop(): GetShopStatusResponse {
        return GetShopStatusResponse(
                result = ShopStatusData(
                        header = ShopStatusHeader(
                                processTime = 0.001736436f,
                                message = ArrayList(),
                                reason = "",
                                errorCode = ""
                        ),
                        data = ShopStatusModel(
                                shopId = 1479278,
                                officialStore = OfficialStore(
                                        status = "active",
                                        error = ""
                                ),
                                powerMerchant = PowerMerchant(
                                        autoExtend = AutoExtend(
                                                status = "on",
                                                tkpdProductId = 115
                                        ),
                                        expiredTime = "2020-04-08 15:08:36",
                                        shopPopup = false,
                                        status = "active"
                                )
                        )
                )
        )
    }
}