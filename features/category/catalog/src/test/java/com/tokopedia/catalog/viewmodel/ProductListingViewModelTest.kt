package com.tokopedia.catalog.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.tokopedia.catalog.CatalogTestUtils
import com.tokopedia.catalog.model.datamodel.CatalogDetailDataModel
import com.tokopedia.catalog.model.raw.CatalogProductItem
import com.tokopedia.catalog.model.raw.CatalogResponseData
import com.tokopedia.catalog.repository.catalogdetail.CatalogDetailRepository
import com.tokopedia.catalog.usecase.detail.CatalogDetailUseCase
import com.tokopedia.catalog.usecase.listing.CatalogCategoryProductUseCase
import com.tokopedia.catalog.usecase.listing.CatalogDynamicFilterUseCase
import com.tokopedia.catalog.usecase.listing.CatalogGetProductListUseCase
import com.tokopedia.catalog.usecase.listing.CatalogQuickFilterUseCase
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

class ProductListingViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var quickFilterUseCase = mockk<CatalogQuickFilterUseCase>()
    private var dynamicFilterUseCase = mockk<CatalogDynamicFilterUseCase>()
    private var getProductListUseCase = mockk<CatalogGetProductListUseCase>()

    private lateinit var viewModel : CatalogDetailProductListingViewModel
    private var catalogDetailObserver = mockk<Observer<Result<List<CatalogProductItem>>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = CatalogDetailProductListingViewModel(quickFilterUseCase,dynamicFilterUseCase,getProductListUseCase)
        viewModel.mProductList.observeForever(catalogDetailObserver)
    }

    @Test
    fun `Get Catalog Product Response Success`() {
        val mockGqlResponse: GraphqlResponse  = createMockGraphqlResponse(getJsonObject("catalog_product_dummy_response.json"))

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