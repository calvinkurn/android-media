package com.tokopedia.catalog.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.tokopedia.catalog.CatalogTestUtils
import com.tokopedia.catalog.model.datamodel.CatalogDetailDataModel
import com.tokopedia.catalog.model.raw.CatalogResponseData
import com.tokopedia.catalog.repository.catalogdetail.CatalogDetailRepository
import com.tokopedia.catalog.usecase.detail.CatalogDetailUseCase
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
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class CatalogViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val catalogDetailRepository : CatalogDetailRepository = mockk(relaxed = true)
    private var catalogDetailUseCase = spyk(CatalogDetailUseCase(catalogDetailRepository))

    private lateinit var viewModel : CatalogDetailPageViewModel
    private var catalogDetailObserver = mockk<Observer<Result<CatalogDetailDataModel>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = CatalogDetailPageViewModel(catalogDetailUseCase)
        viewModel.catalogDetailDataModel.observeForever(catalogDetailObserver)
    }

    @Test
    fun `Get Catalog Detail Response Success`() {
        val mockGqlResponse: GraphqlResponse  = createMockGraphqlResponse(getJsonObject("catalog_detail_dummy_response.json"))
        runBlocking {
            coEvery { catalogDetailRepository.getCatalogDetail(any(),any(), any()) } returns mockGqlResponse
            catalogDetailUseCase.getCatalogDetail(CatalogTestUtils.CATALOG_ID,CatalogTestUtils.USER_ID,CatalogTestUtils.DEVICE,viewModel.catalogDetailDataModel)
            if(viewModel.catalogDetailDataModel.value is Success){
                assert(true)
            }else {
                assert(false)
            }

        }
    }

    @Test
    fun `Get Catalog Detail View Model Response Success`() {
        val mockGqlResponse: GraphqlResponse  = createMockGraphqlResponse(getJsonObject("catalog_detail_dummy_response.json"))
        runBlocking {
            coEvery { catalogDetailRepository.getCatalogDetail(any(),any(), any()) } returns mockGqlResponse
            viewModel.getProductCatalog(CatalogTestUtils.CATALOG_ID,CatalogTestUtils.USER_ID,CatalogTestUtils.DEVICE)
            if(viewModel.getCatalogResponseData().value is Success){
                assert(true)
            }else {
                assert(false)
            }
        }
    }


    @Test
    fun `Get Catalog Detail Response Fail`() {
        val mockGqlResponse: GraphqlResponse  = createMockGraphqlResponse(getJsonObject("catalog_empty_dummy_response.json"))
        runBlocking {
            coEvery { catalogDetailRepository.getCatalogDetail(any(),any(), any()) } returns mockGqlResponse
            catalogDetailUseCase.getCatalogDetail(CatalogTestUtils.CATALOG_ID,CatalogTestUtils.USER_ID,CatalogTestUtils.DEVICE,viewModel.catalogDetailDataModel)
            if(viewModel.catalogDetailDataModel.value is Fail){
                assert(true)
            }else {
                assert(false)
            }

        }
    }

    private fun createMockGraphqlResponse(response : JsonObject): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = response
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = CatalogResponseData::class.java
        val obj: Any = CommonUtils.fromJson(data, objectType)
        result[objectType] = obj
        return GraphqlResponse(result, errors, false)
    }

    private fun getJsonObject(pathString : String) : JsonObject {
        return CommonUtils.fromJson(
                CatalogTestUtils.getJsonFromFile(pathString),
                JsonObject::class.java
        )
    }
}