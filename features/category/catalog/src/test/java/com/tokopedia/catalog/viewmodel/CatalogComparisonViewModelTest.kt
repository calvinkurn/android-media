package com.tokopedia.catalog.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.tokopedia.catalog.CatalogTestUtils
import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel
import com.tokopedia.catalog.model.raw.CatalogComparisonProductsResponse
import com.tokopedia.catalog.model.raw.CatalogProductReviewResponse
import com.tokopedia.catalog.repository.CatalogComparisonProductRepository
import com.tokopedia.catalog.usecase.detail.CatalogComparisonProductUseCase
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class CatalogComparisonViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val repository : CatalogComparisonProductRepository = mockk(relaxed = true)
    private var useCase = spyk(CatalogComparisonProductUseCase(repository))

    private lateinit var viewModel : CatalogProductComparisonViewModel
    private var catalogDetailObserver = mockk<Observer<ArrayList<BaseCatalogDataModel>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = CatalogProductComparisonViewModel(useCase)
        viewModel.getDataItems().observeForever(catalogDetailObserver)
    }

    @Test
    fun `Get Catalog Comparison Response Success`() {
        val mockGqlResponse : GraphqlResponse  = createMockGraphqlResponse(getJsonObject("catalog_comparison_dummy_response.json"))
        val data = mockGqlResponse.getData<CatalogComparisonProductsResponse>(
            CatalogComparisonProductsResponse::class.java)
        coEvery { repository.getComparisonProducts(any(),any(), any(),any(),any(), any()) } returns mockGqlResponse
        viewModel.getComparisonProducts(CatalogTestUtils.CATALOG_ID,"","","",1,"","")
        assertEquals(viewModel.getDataItems().value, data.catalogComparisonList?.catalogComparisonList)
    }

    @Test
    fun `Get Catalog Comparison Response Fail`() {
        runBlocking {
            coEvery { repository.getComparisonProducts(any(),any(), any(),any(),any(), any()) } throws Exception()
            viewModel.getComparisonProducts(CatalogTestUtils.CATALOG_ID,"","","",1,"","")
            assertEquals(viewModel.getHasMoreItems().value , false)
        }
    }

    @Test
    fun `Get Catalog Comparison Response Excpetion`() {
        val exception = "Validate Data Exception"
        runBlocking {
            coEvery { repository.getComparisonProducts(any(),any(), any(),any(),any(), any()) } throws Exception(exception)
            viewModel.getComparisonProducts(CatalogTestUtils.CATALOG_ID,"","","",1,"","")
            assertEquals(viewModel.getError().value ,exception)
        }
    }

   companion object {
       fun createMockGraphqlResponse(response : JsonObject): GraphqlResponse {
           val result = HashMap<Type, Any>()
           val errors = HashMap<Type, List<GraphqlError>>()
           val jsonObject: JsonObject = response
           val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
           val objectType = CatalogComparisonProductsResponse::class.java
           val obj: Any = CommonUtils.fromJson(data, objectType)
           result[objectType] = obj
           return GraphqlResponse(result, errors, false)
       }

       fun getJsonObject(pathString : String) : JsonObject {
           return CommonUtils.fromJson(
               CatalogTestUtils.getJsonFromFile(pathString),
               JsonObject::class.java
           )
       }
   }
}