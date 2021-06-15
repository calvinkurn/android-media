package com.tokopedia.catalog.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.tokopedia.catalog.CatalogTestUtils
import com.tokopedia.catalog.model.raw.*
import com.tokopedia.catalog.usecase.listing.CatalogDynamicFilterUseCase
import com.tokopedia.catalog.usecase.listing.CatalogGetProductListUseCase
import com.tokopedia.catalog.usecase.listing.CatalogQuickFilterUseCase
import com.tokopedia.common_category.model.filter.FilterResponse
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber
import java.lang.reflect.Type

class CatalogProductListingViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var quickFilterUseCase = mockk<CatalogQuickFilterUseCase>(relaxed = true)
    private var dynamicFilterUseCase = mockk<CatalogDynamicFilterUseCase>(relaxed = true)
    private var getProductListUseCase = mockk<CatalogGetProductListUseCase>(relaxed = true)

    private lateinit var viewModel : CatalogDetailProductListingViewModel
    private var productListObserver = mockk<Observer<Result<List<CatalogProductItem>>>>(relaxed = true)
    private var quickFilterObserver = mockk<Observer<Result<DynamicFilterModel>>>(relaxed = true)
    private var dynamicFilterObserver = mockk<Observer<Result<DynamicFilterModel>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = CatalogDetailProductListingViewModel(quickFilterUseCase,dynamicFilterUseCase,getProductListUseCase)
        viewModel.mProductList.observeForever(productListObserver)
        viewModel.mQuickFilterModel.observeForever(quickFilterObserver)
        viewModel.mDynamicFilterModel.observeForever(dynamicFilterObserver)

    }

    @Test
    fun `Get Catalog Product Response Success`() {
        val mockGqlResponse: GraphqlResponse  = createMockGraphqlResponse(getJsonObject("catalog_product_listing_response.json"),CatalogSearchProductResponse().javaClass)
        val data = mockGqlResponse.getData(CatalogSearchProductResponse::class.java) as CatalogSearchProductResponse
        val productListResponse = ProductListResponse(data.searchProduct)

        every { getProductListUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<ProductListResponse>).onNext(productListResponse)
            (secondArg() as Subscriber<ProductListResponse>).onCompleted()
        }
        viewModel.fetchProductListing(RequestParams())

        if(viewModel.mProductList.value is Success) {
            assert(true)
        }else {
            assert(false)
        }
    }

    @Test
    fun `Get Catalog Product Response Empty`() {
        val mockGqlResponse: GraphqlResponse  = createMockGraphqlResponse(getJsonObject("catalog_empty_dummy_response.json"),CatalogSearchProductResponse().javaClass)
        val data = mockGqlResponse.getData(CatalogSearchProductResponse::class.java) as CatalogSearchProductResponse
        val productListResponse = ProductListResponse(data.searchProduct)

        every { getProductListUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<ProductListResponse>).onNext(productListResponse)
            (secondArg() as Subscriber<ProductListResponse>).onCompleted()
        }
        viewModel.fetchProductListing(RequestParams())

        if(viewModel.mProductList.value is Success && (viewModel.mProductList.value as Success<List<CatalogProductItem>>).data.isEmpty()) {
            assert(true)
        }else {
            assert(false)
        }
    }

    @Test
    fun `Get Catalog Product Quick Filter Response Success`() {
        val mockGqlResponse: GraphqlResponse  = createMockGraphqlResponse(getJsonObject("catalog_product_quick_filter_response.json"),SearchFilterResponse().javaClass)
        val data = mockGqlResponse.getData(SearchFilterResponse::class.java) as SearchFilterResponse

        every { quickFilterUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<DynamicFilterModel>).onNext(data.dynamicAttribute)
            (secondArg() as Subscriber<DynamicFilterModel>).onCompleted()
        }
        viewModel.fetchQuickFilters(RequestParams())

        if(viewModel.mQuickFilterModel.value is Success) {
            assert(true)
        }else {
            assert(false)
        }
    }

    @Test
    fun `Get Catalog Product Quick Filter Response Fail`() {
        every { quickFilterUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<DynamicFilterModel>).onError(Throwable("No Data"))
        }
        viewModel.fetchQuickFilters(RequestParams())

        if(viewModel.mQuickFilterModel.value is Fail) {
            assert(true)
        }else {
            assert(false)
        }
    }

    @Test
    fun `Get Catalog Product Dynamic Filter Response Success`() {
        val mockGqlResponse: GraphqlResponse  = createMockGraphqlResponse(getJsonObject("catalog_product_dynamic_attribute_response.json"), FilterResponse().javaClass)
        val data = mockGqlResponse.getData(FilterResponse::class.java) as FilterResponse

        every { dynamicFilterUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<DynamicFilterModel>).onNext(data.dynamicAttribute)
            (secondArg() as Subscriber<DynamicFilterModel>).onCompleted()
        }
        viewModel.fetchDynamicAttribute(RequestParams())

        if(viewModel.mDynamicFilterModel.value is Success) {
            assert(true)
        }else {
            assert(false)
        }
    }

    @Test
    fun `Get Catalog Product Dynamic Filter Response Fail`() {
        every { dynamicFilterUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<DynamicFilterModel>).onError(Throwable("No Data"))
        }
        viewModel.fetchDynamicAttribute(RequestParams())

        if(viewModel.mDynamicFilterModel.value is Fail) {
            assert(true)
        }else {
            assert(false)
        }
    }

    private fun createMockGraphqlResponse(response : JsonObject, objectType : Class<Any>): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = response
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
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