package com.tokopedia.shop.search.domain.interactor

import com.google.gson.JsonObject
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.shop.search.data.model.UniverseSearchResponse
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.anyString
import java.io.File
import java.lang.reflect.Type

class GetSearchShopProductUseCaseTest {

    companion object {
        private const val UNIVERSE_SEARCH_RESPONSE_JSON_FILE_PATH = "json/gql_universe_search.json"
    }

    @RelaxedMockK
    lateinit var graphqlUseCase: MultiRequestGraphqlUseCase

    private val getSearchShopProductUseCase by lazy {
        GetSearchShopProductUseCase(graphqlUseCase)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testExecuteOnBackground() {
        runBlocking {
            coEvery {
                graphqlUseCase.executeOnBackground()
            } returns createMockGraphqlSuccessResponse()
            val response = getSearchShopProductUseCase.executeOnBackground()
            coVerify {
                graphqlUseCase.clearRequest()
                graphqlUseCase.addRequest(any())
                graphqlUseCase.executeOnBackground()
            }
            assertNotNull(response)
            assertNotEquals(0, response.universeSearch.data.size)
        }
    }

    private fun createMockGraphqlSuccessResponse(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = CommonUtils.fromJson(
                getJsonFromFile(UNIVERSE_SEARCH_RESPONSE_JSON_FILE_PATH),
                JsonObject::class.java
        )
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = UniverseSearchResponse::class.java
        val obj: Any = CommonUtils.fromJson(data, objectType)
        result[objectType] = obj
        return GraphqlResponse(result, errors, false)
    }

    private fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}