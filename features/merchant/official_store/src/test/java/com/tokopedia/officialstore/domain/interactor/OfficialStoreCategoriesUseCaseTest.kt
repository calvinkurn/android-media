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

    @RelaxedMockK
    lateinit var graphqlUseCase: MultiRequestGraphqlUseCase

    @RelaxedMockK
    lateinit var gqlResponse: GraphqlResponse

    private lateinit var getOfficialStoreCategoriesUseCase: GetOfficialStoreCategoriesUseCase

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

    @Test
    fun given_error_is_NOT_null_or_empty__when_get_categories__should_throw_MessageErrorException() {
        runBlocking {
            val categories = OfficialStoreCategories()
            val gqlError = GraphqlError()
            val gqlErrorResponse = listOf(gqlError)

            val categoriesResponse = createCategoriesResponse(categories)
            coEvery { graphqlUseCase.executeOnBackground() } returns gqlResponse
            coEvery { gqlResponse.getData<OfficialStoreCategories.Response>(any()) } returns categoriesResponse

            onGetError_thenReturn(gqlErrorResponse)
            getOfficialStoreCategoriesUseCase.executeOnBackground()
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

}