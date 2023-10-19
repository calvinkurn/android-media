package com.tokopedia.oldcatalog.viewmodel

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.tokopedia.oldcatalog.CatalogTestUtils
import com.tokopedia.oldcatalog.model.raw.CatalogProductItem
import com.tokopedia.oldcatalog.model.raw.CatalogSearchProductResponse
import com.tokopedia.oldcatalog.model.raw.ProductListResponse
import com.tokopedia.oldcatalog.model.raw.SearchFilterResponse
import com.tokopedia.oldcatalog.usecase.listing.CatalogDynamicFilterUseCase
import com.tokopedia.oldcatalog.usecase.listing.CatalogGetProductListUseCase
import com.tokopedia.oldcatalog.usecase.listing.CatalogQuickFilterUseCase
import com.tokopedia.common_category.model.filter.FilterResponse
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.oldcatalog.viewmodel.CatalogDetailProductListingViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import rx.Subscriber
import java.lang.reflect.Type

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
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
    private var quickFilterOptionListObserver = mockk<Observer<Option>>(relaxed = true)
    private val quickFilterModelObserver = mockk<Observer<DynamicFilterModel>>(relaxed = true)
    private val quickFilterClickedObserver = mockk<Observer<Boolean>>(relaxed = true)
    private val dynamicFilterModelObserver = mockk<Observer<DynamicFilterModel>>(relaxed = true)
    private val selectedSortIndicatorCount = mockk<Observer<Int>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = CatalogDetailProductListingViewModel(quickFilterUseCase,dynamicFilterUseCase,getProductListUseCase)
        viewModel.mProductList.observeForever(productListObserver)
        viewModel.mQuickFilterModel.observeForever(quickFilterObserver)
        viewModel.mDynamicFilterModel.observeForever(dynamicFilterObserver)
        viewModel.quickFilterModel.observeForever(quickFilterModelObserver)
        viewModel.quickFilterClicked.observeForever(quickFilterClickedObserver)
        viewModel.dynamicFilterModel.observeForever(dynamicFilterModelObserver)
        viewModel.selectedSortIndicatorCount.observeForever(selectedSortIndicatorCount)
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
        viewModel.isPagingAllowed = false
        if(viewModel.mProductList.value is Success && viewModel.pageCount > 0 && !viewModel.isPagingAllowed) {
            assert(true)
        }else {
            assert(false)
        }
    }

    @Test
    fun `Get Catalog Product Response Success List More Than Limit`() {
        val productListResponse = ProductListResponse(CatalogSearchProductResponse.SearchProduct(
            CatalogSearchProductResponse.SearchProductHeader()))
        val productsList = arrayListOf<CatalogProductItem>()
        for(i in 0 until 10 ){
            productsList.add(mockk(relaxed = true))
        }
        productListResponse.searchProduct?.data = CatalogSearchProductResponse.SearchProductData(
            0,true,"", productsList
        )
        viewModel.comparisonCardIsAdded = false
        every { getProductListUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<ProductListResponse>).onNext(productListResponse)
            (secondArg() as Subscriber<ProductListResponse>).onCompleted()
        }
        viewModel.fetchProductListing(RequestParams())
        viewModel.isPagingAllowed = false
        if(viewModel.mProductList.value is Success && viewModel.pageCount > 0 && !viewModel.isPagingAllowed) {
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
        viewModel.catalogUrl = CatalogTestUtils.CATALOG_URL
        viewModel.catalogId = CatalogTestUtils.CATALOG_ID
        viewModel.catalogName = "Apple Iphone 12"
        every { getProductListUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<ProductListResponse>).onNext(productListResponse)
            (secondArg() as Subscriber<ProductListResponse>).onCompleted()
        }
        viewModel.catalogUrl
        viewModel.catalogName
        assert(viewModel.catalogId.isNotEmpty())
        viewModel.fetchProductListing(RequestParams())
        viewModel.filterController
        viewModel.searchParameter
        viewModel.searchParametersMap
        if(viewModel.mProductList.value is Success && (viewModel.mProductList.value as Success<List<CatalogProductItem>>).data.isEmpty()) {
            assert(true)
        }else {
            assert(false)
        }
        viewModel.onDetach()
    }

    @Test
    fun `Get Catalog Product Response Fail`() {
        every { getProductListUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<ProductListResponse>).onError(Throwable("No Data"))
        }
        viewModel.categoryId = CatalogTestUtils.CATALOG_ID
        viewModel.brand = "Apple"
        viewModel.fetchProductListing(RequestParams())
        if(viewModel.mProductList.value is Fail) { assert(true) }else { assert(false) }
        viewModel.quickFilterOptionList = arrayListOf()
        viewModel.searchParameter = SearchParameter("")
        viewModel.filterController = FilterController()
        viewModel.quickFilterOptionList
        viewModel.quickFilterClicked
        viewModel.dynamicFilterModel
        assert(viewModel.brand.isNotBlank())
        assert(viewModel.categoryId.isNotBlank())
    }

    @Test
    fun `Get Catalog Product Quick Filter Response Success`() {
        val mockGqlResponse: GraphqlResponse  = createMockGraphqlResponse(getJsonObject("catalog_product_quick_filter_response.json"),SearchFilterResponse().javaClass)
        val data = mockGqlResponse.getData(SearchFilterResponse::class.java) as SearchFilterResponse
        viewModel.comparisonCardIsAdded = true
        every { quickFilterUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<DynamicFilterModel>).onNext(data.dynamicAttribute)
            (secondArg() as Subscriber<DynamicFilterModel>).onCompleted()
        }
        assert(viewModel.comparisonCardIsAdded)
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

        if(viewModel.getDynamicFilterData().value is Success) {
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

    companion object {
        fun createMockGraphqlResponse(response : JsonObject, objectType : Class<Any>): GraphqlResponse {
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
}
