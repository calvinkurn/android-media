package com.tokopedia.shop.score.domain.interactor

import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import java.io.File
import java.lang.reflect.Type

/**
 * Created by rizqiaryansa on 2020-03-03.
 */

class GetShopPageHomeLayoutUseCaseTest {

    companion object {
        private const val SHOP_PAGE_GET_LAYOUT_SUCCESS_RESPONSE_JSON_FILE_PATH = "json/gql_success_shop_page_get_layout.json"
        private const val SHOP_PAGE_GET_LAYOUT_ERROR_RESPONSE_JSON_FILE_PATH = "json/gql_error_shop_page_get_layout.json"
    }

    @RelaxedMockK
    lateinit var graphQlUseCase: MultiRequestGraphqlUseCase

    private val getShopPageHomeLayoutUseCase by lazy {
        GetShopPageHomeLayoutUseCase(graphQlUseCase)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testExecuteOnBackground() {
        runBlocking {
            coEvery {
                graphQlUseCase.executeOnBackground()
            } returns createMockGraphQlSuccessResponse()
            val response = getShopPageHomeLayoutUseCase.executeOnBackground()
            coVerify {
                graphQlUseCase.clearRequest()
                graphQlUseCase.addRequest(any())
                graphQlUseCase.executeOnBackground()
            }
            Assert.assertNotNull(response)
            Assert.assertNotEquals(0, response.listWidget.size)
        }
    }

    @Test
    @Throws(MessageErrorException::class)
    fun `request with param not shop id after then output response error`() {
        runBlocking {
            coEvery {
                graphQlUseCase.executeOnBackground()
            } returns createMockGraphQlErrorResponse()
        }
    }

    private fun createMockGraphQlSuccessResponse(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = CommonUtils.fromJson(
                SHOP_PAGE_GET_LAYOUT_SUCCESS_RESPONSE_JSON_FILE_PATH.getJsonFromFile(),
                JsonObject::class.java
        )
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = ShopLayoutWidget.Response::class.java
        val obj: Any = CommonUtils.fromJson(data, objectType)
        result[objectType] = obj
        return GraphqlResponse(result, errors, false)
    }

    private fun createMockGraphQlErrorResponse(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = CommonUtils.fromJson(
                SHOP_PAGE_GET_LAYOUT_ERROR_RESPONSE_JSON_FILE_PATH.getJsonFromFile(),
                JsonObject::class.java
        )
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.ERROR)
        val objectType = GraphqlError::class.java
        val obj: Any = CommonUtils.fromJson(data, objectType)
        result[objectType] = obj
        return GraphqlResponse(result, errors, false)
    }


    private fun String.getJsonFromFile(): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(this)
        val file = File(uri.path)
        return String(file.readBytes())
    }

}