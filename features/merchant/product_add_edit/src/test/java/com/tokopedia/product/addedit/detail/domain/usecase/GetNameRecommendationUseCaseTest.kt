package com.tokopedia.product.addedit.detail.domain.usecase

import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.product.addedit.detail.domain.UniverseSearchResponse
import com.tokopedia.product.addedit.util.UnitTestFileUtils
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.io.File
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
class GetNameRecommendationUseCaseTest {
    companion object {
        private const val GET_SEARCH_NAME_PRODUCT_RECOMMENDATION_SUCCESS = "json/get_universe_search_success_response.json"
        private const val GET_SEARCH_NAME_PRODUCT_RECOMMENDATION_FAILED = "json/get_universe_search_failed_response.json"
    }

    @RelaxedMockK
    lateinit var graphqlRepository: GraphqlRepository

    @get:Rule
    val expectedException: ExpectedException = ExpectedException.none()

    private val getSearchShopProductUseCase by lazy {
        GetNameRecommendationUseCase(graphqlRepository)
    }

    @Before
    fun setup() {
        mockkObject(GetNameRecommendationUseCase)
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success on search name suggestion `() {

        getSearchShopProductUseCase.requestParams = GetNameRecommendationUseCase.createRequestParam(0, "batik")

        runBlocking {
            coEvery {
                graphqlRepository.response(any(), any())
            } returns createMockGraphQlSuccessResponse()

            val response = getSearchShopProductUseCase.executeOnBackground()
            coVerify {
                graphqlRepository.response(any(), any())
            }
            Assert.assertNotNull(response)
            Assert.assertNotEquals(0, response.size)
        }
    }

    @Test
    @Throws(MessageErrorException::class)
    fun `should error on search name suggestion`() {

        getSearchShopProductUseCase.requestParams = GetNameRecommendationUseCase.createRequestParam(123, "@/")

        runBlocking {
            coEvery {
                graphqlRepository.response(any(), any())
            } returns createMockGraphQlErrorResponse()
        }
    }


    private fun createMockGraphQlSuccessResponse(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = CommonUtils.fromJson(
                GET_SEARCH_NAME_PRODUCT_RECOMMENDATION_SUCCESS.getJsonFromFile(),
                JsonObject::class.java
        )
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = UniverseSearchResponse::class.java
        val obj: Any = CommonUtils.fromJson(data, objectType)
        result[objectType] = obj
        return GraphqlResponse(result, errors, false)
    }

    private fun createMockGraphQlErrorResponse(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = CommonUtils.fromJson(
                GET_SEARCH_NAME_PRODUCT_RECOMMENDATION_FAILED.getJsonFromFile(),
                JsonObject::class.java
        )
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.ERROR)
        val objectType = GraphqlError::class.java
        val obj: Any = CommonUtils.fromJson(data, objectType)
        result[objectType] = obj
        return GraphqlResponse(result, errors, false)
    }

    private fun String.getJsonFromFile(): String {
        return UnitTestFileUtils.getJsonFromAsset(this)
    }
}
