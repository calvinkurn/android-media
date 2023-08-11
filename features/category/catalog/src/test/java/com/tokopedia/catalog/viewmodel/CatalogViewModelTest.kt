package com.tokopedia.oldcatalog.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.tokopedia.oldcatalog.CatalogTestUtils
import com.tokopedia.oldcatalog.model.datamodel.CatalogDetailDataModel
import com.tokopedia.oldcatalog.model.raw.CatalogProductItem
import com.tokopedia.oldcatalog.model.raw.CatalogResponseData
import com.tokopedia.oldcatalog.model.raw.CatalogSearchProductResponse
import com.tokopedia.oldcatalog.model.raw.ProductListResponse
import com.tokopedia.catalog.Repository.catalogdetail.CatalogDetailRepository
import com.tokopedia.oldcatalog.usecase.detail.CatalogDetailUseCase
import com.tokopedia.oldcatalog.usecase.listing.CatalogGetProductListUseCase
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber
import java.lang.reflect.Type

class CatalogViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = UnconfinedTestRule()

    private val catalogDetailRepository : CatalogDetailRepository = mockk(relaxed = true)
    private var catalogDetailUseCase = spyk(CatalogDetailUseCase(catalogDetailRepository))
    private var getProductListUseCase = mockk<CatalogGetProductListUseCase>(relaxed = true)

    private lateinit var viewModel : CatalogDetailPageViewModel
    private var catalogDetailObserver = mockk<Observer<Result<CatalogDetailDataModel>>>(relaxed = true)
    private var productCountObserver = mockk<Observer<Int>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = CatalogDetailPageViewModel(catalogDetailUseCase,getProductListUseCase)
        viewModel.catalogDetailDataModel.observeForever(catalogDetailObserver)
        viewModel.mProductCount.observeForever(productCountObserver)
    }

    @Test
    fun `Get Catalog Detail Response Success`() {
        val mockGqlResponse: GraphqlResponse  = createMockGraphqlResponse(getJsonObject("catalog_detail_dummy_response.json"))
        runBlocking {
            coEvery { catalogDetailRepository.getCatalogDetail(any(),any(),any(), any()) } returns mockGqlResponse
            catalogDetailUseCase.getCatalogDetail(CatalogTestUtils.CATALOG_ID,CatalogTestUtils.COMPARISION_ID,CatalogTestUtils.USER_ID,CatalogTestUtils.DEVICE,viewModel.catalogDetailDataModel)
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
            coEvery { catalogDetailRepository.getCatalogDetail(any(),any(),any(), any()) } returns mockGqlResponse
            viewModel.getProductCatalog(CatalogTestUtils.CATALOG_ID,CatalogTestUtils.COMPARISION_ID,CatalogTestUtils.USER_ID,CatalogTestUtils.DEVICE)
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
            coEvery { catalogDetailRepository.getCatalogDetail(any(),any(), any(),any()) } returns mockGqlResponse
            catalogDetailUseCase.getCatalogDetail(CatalogTestUtils.CATALOG_ID,CatalogTestUtils.COMPARISION_ID,CatalogTestUtils.USER_ID,CatalogTestUtils.DEVICE,viewModel.catalogDetailDataModel)
            if(viewModel.catalogDetailDataModel.value is Fail){
                assert(true)
            }else {
                assert(false)
            }

        }
    }

    @Test
    fun `Get Catalog Product Response Exception`() {
        runBlocking {
            coEvery { catalogDetailRepository.getCatalogDetail(any(),any(), any(),any()) } throws Exception()
            viewModel.getProductCatalog(CatalogTestUtils.CATALOG_ID,CatalogTestUtils.COMPARISION_ID,CatalogTestUtils.USER_ID,CatalogTestUtils.DEVICE)
            assert(viewModel.catalogDetailDataModel.value is Fail)
        }
    }


    companion object {
        fun createMockGraphqlResponse(response : JsonObject): GraphqlResponse {
            val result = HashMap<Type, Any>()
            val errors = HashMap<Type, List<GraphqlError>>()
            val jsonObject: JsonObject = response
            val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
            val objectType = CatalogResponseData::class.java
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

    @Test
    fun `Get Catalog Product Count Response Success`() {
        val mockGqlResponse: GraphqlResponse  = CatalogProductListingViewModelTest.createMockGraphqlResponse(getJsonObject("catalog_product_listing_response.json"),CatalogSearchProductResponse().javaClass)
        val data = mockGqlResponse.getData(CatalogSearchProductResponse::class.java) as CatalogSearchProductResponse
        val productListResponse = ProductListResponse(data.searchProduct)

        every { getProductListUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<ProductListResponse>).onNext(productListResponse)
            (secondArg() as Subscriber<ProductListResponse>).onCompleted()
        }
        viewModel.fetchProductListing(RequestParams())
        val count = viewModel.mProductCount.value
        assert(count != 0)
    }

    @Test
    fun `Get Catalog Product Count Response Zero`() {
        val mockGqlResponse: GraphqlResponse  = CatalogProductListingViewModelTest.createMockGraphqlResponse(getJsonObject("catalog_product_listing_response.json"),CatalogSearchProductResponse().javaClass)
        val data = mockGqlResponse.getData(CatalogSearchProductResponse::class.java) as CatalogSearchProductResponse
        val productListResponse = ProductListResponse(null)
        every { getProductListUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<ProductListResponse>).onNext(productListResponse)
            (secondArg() as Subscriber<ProductListResponse>).onCompleted()
        }
        viewModel.fetchProductListing(RequestParams())
        val count = viewModel.mProductCount.value
        assert(count == 0)
    }

    @Test
    fun `Get Catalog Product Count Response Zero Null`() {
        val mockGqlResponse: GraphqlResponse  = CatalogProductListingViewModelTest.createMockGraphqlResponse(getJsonObject("catalog_product_listing_response.json"),CatalogSearchProductResponse().javaClass)
        val data = mockGqlResponse.getData(CatalogSearchProductResponse::class.java) as CatalogSearchProductResponse
        every { getProductListUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<ProductListResponse>).onNext(null)
            (secondArg() as Subscriber<ProductListResponse>).onCompleted()
        }
        viewModel.fetchProductListing(RequestParams())
        val count = viewModel.mProductCount.value
        assert(count == 0)
    }

    @Test
    fun `Get Catalog Product Count Response Fail`() {
        every { getProductListUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<ProductListResponse>).onError(Throwable("No Data"))
        }
        viewModel.fetchProductListing(RequestParams())
        assert(viewModel.mProductCount.value == 0)
    }
}
