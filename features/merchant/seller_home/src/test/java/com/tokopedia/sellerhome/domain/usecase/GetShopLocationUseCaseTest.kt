package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.domain.model.ShopInfoLocation
import com.tokopedia.sellerhome.utils.TestHelper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.rules.ExpectedException
import org.mockito.Matchers.anyString

/**
 * Created By @ilhamsuaib on 19/03/20
 */

@ExperimentalCoroutinesApi
class GetShopLocationUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_shop_location_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    private val getShopLocationUseCase by lazy {
        GetShopLocationUseCase(gqlRepository)
    }

    private val params = GetShopInfoUseCase.getRequestParam(anyString())

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success when get shop location`() = runBlocking {

        val successResponse = TestHelper.createSuccessResponse<ShopInfoLocation>(SUCCESS_RESPONSE)

        getShopLocationUseCase.params = params

        coEvery {
            gqlRepository.response(any(), any())
        } returns successResponse

        val actualShopLocation = getShopLocationUseCase.executeOnBackground()

        coVerify {
            gqlRepository.response(any(), any())
        }

        val expectedShopLocation = ShippingLoc(13)
        assertEquals(expectedShopLocation, actualShopLocation)
    }

    @Test
    fun `should failed when get shop location`() = runBlocking {
        val errorResponse = TestHelper.createErrorResponse<ShopInfoLocation>()
        getShopLocationUseCase.params = params

        coEvery {
            gqlRepository.response(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        val actualShopLocation = getShopLocationUseCase.executeOnBackground()

        coVerify {
            gqlRepository.response(any(), any())
        }

        assertNull(actualShopLocation)
    }
}