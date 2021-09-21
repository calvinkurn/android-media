package com.tokopedia.shop.common.domain.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GQLGetShopInfoUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var gqlUseCase: MultiRequestGraphqlUseCase
    @RelaxedMockK
    lateinit var gqlResponse: GraphqlResponse

    private lateinit var getShopInfoUseCase: GQLGetShopInfoUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getShopInfoUseCase = GQLGetShopInfoUseCase(gqlQuery, gqlUseCase)
    }

    @Test
    fun given_error_is_null_or_empty__when_get_shop_info__should_return_shop_info() {
        runBlocking {
            val shopInfo = ShopInfo()
            val gqlErrorResponse = null

            onGetShopInfo_thenReturn(shopInfo)
            onGetError_thenReturn(gqlErrorResponse)

            val expectedShopInfo = ShopInfo()
            val actualShopInfo = getShopInfoUseCase.executeOnBackground()

            verifyGraphqlUseCaseCalled()
            assertEquals(expectedShopInfo, actualShopInfo)
        }
    }

    @Test(expected = MessageErrorException::class)
    fun given_error_is_NOT_null_or_empty__when_get_shop_info__should_throw_MessageErrorException() {
        runBlocking {
            val shopInfo = ShopInfo()
            val gqlError = GraphqlError()
            val gqlErrorResponse = listOf(gqlError)

            onGetShopInfo_thenReturn(shopInfo)
            onGetError_thenReturn(gqlErrorResponse)

            getShopInfoUseCase.executeOnBackground()
        }
    }

    //region stub
    private fun onGetError_thenReturn(gqlErrorResponse: List<GraphqlError>?) {
        coEvery { gqlResponse.getError(ShopInfo.Response::class.java) } returns gqlErrorResponse
    }

    private fun onGetShopInfo_thenReturn(shopInfo: ShopInfo) {
        val shopInfoResponse = createShopInfoResponse(shopInfo)
        coEvery { gqlUseCase.executeOnBackground() } returns gqlResponse
        coEvery { gqlResponse.getData<ShopInfo.Response>(any()) } returns shopInfoResponse
    }
    //endregion

    //region verification
    private fun verifyGraphqlUseCaseCalled() {
        coVerify { gqlUseCase.executeOnBackground() }
    }
    //endregion

    //region private methods
    private fun createShopInfoResponse(shopInfo: ShopInfo): ShopInfo.Response {
        val shopInfoResult = ShopInfo.Result(listOf(shopInfo))
        return ShopInfo.Response(shopInfoResult)
    }
    //endregion

    companion object {
        private const val gqlQuery = "query"
    }
}