package com.tokopedia.officialstore.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.officialstore.category.data.model.OfficialStoreCategories
import com.tokopedia.officialstore.category.domain.GetOfficialStoreCategoriesUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.anyString

class OfficialStoreCategoriesUseCaseTest {

//    companion object {
//        private const val OFFICIAL_STORE_CATEGORIES_JSON_FILE_PATH = "json/official_store_categories.json"
//    }

    @RelaxedMockK
    lateinit var graphqlUseCase: MultiRequestGraphqlUseCase

    @RelaxedMockK
    lateinit var gqlResponse: GraphqlResponse

    private lateinit var getOfficialStoreCategoriesUseCase: GetOfficialStoreCategoriesUseCase

//    private val officialStoreCategoriesUseCase by lazy {
//        GetOfficialStoreCategoriesUseCase(graphqlUseCase, anyString())
//    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getOfficialStoreCategoriesUseCase = GetOfficialStoreCategoriesUseCase(graphqlUseCase, anyString())
    }

    @Test
    fun given_error_is_null_or_empty__when_get_categories__should_return_categories() {
        runBlocking {
            val categories = OfficialStoreCategories()
            val gqlErrorResponse = null

            val categoriesResponse = createCategoriesResponse(categories)
            coEvery { graphqlUseCase.executeOnBackground() } returns gqlResponse
            coEvery { gqlResponse.getData<OfficialStoreCategories.Response>(any()) } returns categoriesResponse

            onGetError_thenReturn(gqlErrorResponse)
            val expectedCategories = OfficialStoreCategories()
            val actualCategories = getOfficialStoreCategoriesUseCase.executeOnBackground()

            verifyGraphqlUseCaseCalled()
            Assert.assertEquals(expectedCategories, actualCategories)
        }
    }

    private fun createCategoriesResponse(categories: OfficialStoreCategories):
            OfficialStoreCategories.Response {
        val categoriesResult = categories
        return OfficialStoreCategories.Response(categoriesResult)
    }

    private fun onGetError_thenReturn(gqlErrorResponse: List<GraphqlError>?) {
        coEvery { gqlResponse.getError(OfficialStoreCategories.Response::class.java) } returns gqlErrorResponse
    }

    private fun verifyGraphqlUseCaseCalled() {
        coVerify { graphqlUseCase.executeOnBackground() }
    }










//    private val officialStoreCategoriesUseCase by lazy {
//        GetOfficialStoreCategoriesUseCase(graphqlUseCase, anyString())
//    }

//    @Before
//    fun setup() {
//        MockKAnnotations.init(this)
//    }
//
//    @Test
//    fun testExecuteBackground() {
//        runBlocking {
//            coEvery {
//                graphqlUseCase.executeOnBackground()
//            } returns createMockGraphqlSuccessResponse()
//            val response = officialStoreCategoriesUseCase.executeOnBackground()
//            coVerify {
//                graphqlUseCase.clearRequest()
//                graphqlUseCase.addRequest(any())
//                graphqlUseCase.executeOnBackground()
//            }
//            assertNotNull(response)
//        }
//    }
//
//    private fun createMockGraphqlSuccessResponse(): GraphqlResponse {
//        val result = HashMap<Type, Any>()
//        val errors = HashMap<Type, List<GraphqlError>>()
//        val jsonObject: JsonObject = CommonUtils.fromJson(
//                getJsonFromFile(OFFICIAL_STORE_CATEGORIES_JSON_FILE_PATH),
//                JsonObject::class.java
//        )
//        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
//        val objectType = OfficialStoreCategories::class.java
//        val obj: Any = CommonUtils.fromJson(data, objectType)
//        result[objectType] = obj
//        return GraphqlResponse(result, errors, false)
//    }
//
//    private fun getJsonFromFile(path: String): String {
//        val uri = ClassLoader.getSystemClassLoader().getResource(path)
//        val file = File(uri.path)
//        return String(file.readBytes())
//    }


}