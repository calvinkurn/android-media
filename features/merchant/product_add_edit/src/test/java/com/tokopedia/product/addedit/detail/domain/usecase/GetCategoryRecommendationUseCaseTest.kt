package com.tokopedia.product.addedit.detail.domain.usecase

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.product.addedit.detail.domain.mapper.CategoryRecommendationMapper
import com.tokopedia.product.addedit.detail.domain.model.GetCategoryRecommendationResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
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
class GetCategoryRecommendationUseCaseTest {
    companion object {
        private const val SUCCESS_RESPONSE = "json/get_category_recommendation_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    private val mapper: CategoryRecommendationMapper = CategoryRecommendationMapper()

    private val usecase by lazy {
        GetCategoryRecommendationUseCase(gqlRepository, mapper)
    }

    private val params = GetCategoryRecommendationUseCase.createRequestParams("baju")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

//    @Test
//    fun `should success on get category recommendation`() = runBlocking {
//        usecase.params = params
//        val successResponse = createSuccessResponse<GetCategoryRecommendationResponse>(SUCCESS_RESPONSE)
//
//        coEvery {
//            gqlRepository.getReseponse(any(), any())
//        } returns successResponse
//
//        val result = usecase.executeOnBackground()
//
//        coVerify {
//            gqlRepository.getReseponse(any(), any())
//        }
//
//        Assert.assertTrue(result.isNotEmpty())
//    }

    fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

    inline fun <reified T> parseJson(json: String): T {
        return Gson().fromJson(json, T::class.java)
    }

    inline fun <reified T : Any> createSuccessResponse(jsonPath: String): GraphqlResponse {
        val mockJsonResponse = getJsonFromFile(jsonPath)
        val response: JsonObject = parseJson(mockJsonResponse)
        val jsonData = response.get("data").toString()
        val data: T = parseJson(jsonData)
        val result = mapOf<Type, Any>(T::class.java to data)
        val error = mapOf<Type, List<GraphqlError>>(T::class.java to emptyList())
        return GraphqlResponse(result, error, false)
    }
}