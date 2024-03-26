package com.tokopedia.catalog.viewmodel

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.JsonObject
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.catalog.domain.GetCatalogProductListUseCase
import com.tokopedia.catalog.domain.GetProductListFromSearchUseCase
import com.tokopedia.catalog.domain.model.CatalogProductItem
import com.tokopedia.catalog.domain.model.CatalogProductListResponse
import com.tokopedia.catalog.domain.model.CatalogSearchProductForReimaganeResponse
import com.tokopedia.catalog.ui.model.CatalogProductAtcUiModel
import com.tokopedia.catalog.ui.model.CatalogProductListUiModel
import com.tokopedia.catalog.ui.viewmodel.CatalogProductListViewModel
import com.tokopedia.catalog.ui.viewmodel.CatalogSellerOfferingProductListViewModel
import com.tokopedia.common_category.model.filter.FilterResponse
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oldcatalog.CatalogTestUtils
import com.tokopedia.oldcatalog.model.raw.SearchFilterResponse
import com.tokopedia.oldcatalog.usecase.listing.CatalogDynamicFilterUseCase
import com.tokopedia.oldcatalog.usecase.listing.CatalogQuickFilterUseCase
import com.tokopedia.oldcatalog.viewmodel.CatalogProductListingViewModelTest
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import rx.Subscriber
import java.lang.reflect.Type
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class CatalogSellerOfferingProductListViewModelTest {

    // Initialization scope
    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var quickFilterUseCase: CatalogQuickFilterUseCase

    @RelaxedMockK
    lateinit var dynamicFilterUseCase: CatalogDynamicFilterUseCase

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var getNotificationUseCase: GetNotificationUseCase

    @RelaxedMockK
    lateinit var getProductListUseCase: GetCatalogProductListUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    val viewModel by lazy {
        spyk(
            CatalogSellerOfferingProductListViewModel(
                CoroutineTestDispatchersProvider,
                quickFilterUseCase,
                dynamicFilterUseCase,
                addToCartUseCase,
                getNotificationUseCase,
                getProductListUseCase,
                userSession
            )
        )
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    // Testing scope
    @Test
    fun `Get Catalog Product Quick Filter Response Success`() {
        val mockGqlResponse: GraphqlResponse =
            CatalogProductListingViewModelTest.createMockGraphqlResponse(
                getJsonObject("catalog_product_quick_filter_response.json"),
                SearchFilterResponse().javaClass
            )
        val data = mockGqlResponse.getData(SearchFilterResponse::class.java) as SearchFilterResponse
        every { quickFilterUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<DynamicFilterModel>).onNext(data.dynamicAttribute)
            (secondArg() as Subscriber<DynamicFilterModel>).onCompleted()
        }
        viewModel.quickFilterClicked
        viewModel.fetchQuickFilters(RequestParams())
        viewModel.quickFilterModel
        if (viewModel.quickFilterResult.value is Success) {
            assert(true)
        } else {
            assert(false)
        }
    }

    @Test
    fun `Get Catalog Product Quick Filter Response Fail`() {
        every { quickFilterUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<DynamicFilterModel>).onError(Throwable("No Data"))
        }
        viewModel.fetchQuickFilters(RequestParams())

        if (viewModel.quickFilterResult.value is Fail) {
            assert(true)
        } else {
            assert(false)
        }
    }

    @Test
    fun `Get Catalog Product Dynamic Filter Response Success`() {
        val mockGqlResponse: GraphqlResponse =
            CatalogProductListingViewModelTest.createMockGraphqlResponse(
                getJsonObject("catalog_product_dynamic_attribute_response.json"),
                FilterResponse().javaClass
            )
        val data = mockGqlResponse.getData(FilterResponse::class.java) as FilterResponse

        every { dynamicFilterUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<DynamicFilterModel>).onNext(data.dynamicAttribute)
            (secondArg() as Subscriber<DynamicFilterModel>).onCompleted()
        }
        viewModel.fetchDynamicAttribute(RequestParams())

        if (viewModel.dynamicFilterResult.value is Success) {
            assert(true)
        } else {
            assert(false)
        }
    }

    @Test
    fun `Get Catalog Product Dynamic Filter Response Fail`() {
        every { dynamicFilterUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<DynamicFilterModel>).onError(Throwable("No Data"))
        }
        viewModel.fetchDynamicAttribute(RequestParams())

        if (viewModel.mDynamicFilterModel.value is Fail) {
            assert(true)
        } else {
            assert(false)
        }
    }

    @Test
    fun `Get Catalog Product Response Success`() {
        val mockGqlResponse: GraphqlResponse =
            createMockGraphqlResponse(
                getJsonObject("catalog_seller_offering_product_listing_response.json"),
                CatalogProductListResponse().javaClass
            )
        val data = mockGqlResponse.getData(CatalogProductListResponse::class.java) as CatalogProductListResponse

        val param = RequestParams()
        coEvery {
            getProductListUseCase.execute(param)
        } returns data

        viewModel.fetchProductListing(param)
        viewModel.filterController
        viewModel.searchParameter
        viewModel.searchParametersMap
        assert(viewModel.productList.value is Success)
    }

    //
    @Test
    fun `Get Catalog Product Response Empty`() {
        val param = RequestParams()
        val mockGqlResponse: GraphqlResponse =
            createMockGraphqlResponse(
                getJsonObject("catalog_seller_offering_product_listing_empty_response.json"),
                CatalogProductListResponse().javaClass
            )
        val data =
            mockGqlResponse.getData(CatalogProductListResponse::class.java) as CatalogProductListResponse
        coEvery {
            getProductListUseCase.execute(param)
        } returns data

        viewModel.fetchProductListing(param)
        viewModel.filterController
        viewModel.searchParameter
        viewModel.searchParametersMap
        if (viewModel.productList.value is Success && (viewModel.productList.value as Success<CatalogProductListUiModel>).data.products.isEmpty()) {
            assert(true)
        } else {
            assert(false)
        }
    }

    @Test
    fun `Get Catalog Product Response Fail`() {
        val param = RequestParams()
        coEvery {
            getProductListUseCase.execute(param)
        } throws Throwable()

        viewModel.fetchProductListing(param)
        if (viewModel.productList.value is Fail) { assert(true) } else { assert(false) }
        viewModel.quickFilterOptionList = arrayListOf()
        viewModel.searchParameter = SearchParameter("")
        viewModel.filterController = FilterController()
        viewModel.quickFilterOptionList
        viewModel.quickFilterClicked
        viewModel.dynamicFilterModel
        viewModel.selectedSortIndicatorCount
    }

    @Test
    fun `Add To Cart Response Success`() {
        val paramViewModel = CatalogProductAtcUiModel()
        val paramUseCase = AddToCartRequestParams(
            productId = paramViewModel.productId,
            shopId = paramViewModel.shopId,
            quantity = paramViewModel.quantity,
            warehouseId = paramViewModel.warehouseId
        )
        coEvery {
            addToCartUseCase.setParams(paramUseCase)
            addToCartUseCase.executeOnBackground()
        } returns AddToCartDataModel(data = DataModel(success = 0), status = "")

        viewModel.addProductToCart(paramViewModel)

        assert(viewModel.errorsToaster.value is MessageErrorException)
    }

    @Test
    fun `Add To Cart Response Success with status ok`() {
        val paramViewModel = CatalogProductAtcUiModel()
        val paramUseCase = AddToCartRequestParams(
            productId = paramViewModel.productId,
            shopId = paramViewModel.shopId,
            quantity = paramViewModel.quantity,
            warehouseId = paramViewModel.warehouseId
        )
        val response = AddToCartDataModel(data = DataModel(success = 1), status = "OK", errorMessage = arrayListOf("error"))
        coEvery {
            addToCartUseCase.setParams(paramUseCase)
            addToCartUseCase.executeOnBackground()
        } returns response

        viewModel.addProductToCart(paramViewModel)

        assert(viewModel.textToaster.value == response.getAtcErrorMessage())
    }

    @Test
    fun `Add To Cart Response Fail`() {
        val paramViewModel = CatalogProductAtcUiModel()
        val paramUseCase = AddToCartRequestParams(
            productId = paramViewModel.productId,
            shopId = paramViewModel.shopId,
            quantity = paramViewModel.quantity,
            warehouseId = paramViewModel.warehouseId
        )
        val error = Throwable()
        coEvery {
            addToCartUseCase.setParams(paramUseCase)
            addToCartUseCase.executeOnBackground()
        } throws error

        viewModel.addProductToCart(paramViewModel)

        assert(viewModel.errorsToaster.value == error)
    }

    @Test
    fun `Refresh Notification Success`() {
        val response = TopNavNotificationModel(totalCart = 2)
        coEvery {
            getNotificationUseCase.executeOnBackground()
        } returns response

        viewModel.refreshNotification()

        assert(viewModel.totalCartItem.value == response.totalCart)
    }

    @Test
    fun `Refresh Notification Fail`() {
        val error = Throwable("Error")
        coEvery {
            getNotificationUseCase.executeOnBackground()
        } throws error

        viewModel.refreshNotification()

        assert(viewModel.errorsToaster.value == error)
    }

    @Test
    fun `User Not Login`() {
        val emptyUserId = ""
        userSession.userId = emptyUserId
        coEvery {
            viewModel.getUserId()
        } returns emptyUserId
        viewModel.getUserId()
        assert(!viewModel.isUserLoggedIn())
    }

    @Test
    fun `User already Login`() {
        val userId = "13131313"
        userSession.userId = userId

        coEvery {
            viewModel.getUserId()
        } returns userId
        assert(viewModel.isUserLoggedIn())
    }

    @Test
    fun `Get user id`() {
        val userId = "13131313"

        coEvery {
            userSession.userId
        } returns userId
        assert(viewModel.getUserId().isNotEmpty())
    }

    companion object {
        fun createMockGraphqlResponse(
            response: JsonObject,
            objectType: Class<Any>
        ): GraphqlResponse {
            val result = HashMap<Type, Any>()
            val errors = HashMap<Type, List<GraphqlError>>()
            val jsonObject: JsonObject = response
            val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
            val obj: Any = CommonUtils.fromJson(data, objectType)
            result[objectType] = obj
            return GraphqlResponse(result, errors, false)
        }

        private fun getJsonObject(pathString: String): JsonObject {
            return CommonUtils.fromJson(
                CatalogTestUtils.getJsonFromFile(pathString),
                JsonObject::class.java
            )
        }
    }
}
