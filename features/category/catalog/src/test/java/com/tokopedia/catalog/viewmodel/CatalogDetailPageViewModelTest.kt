package com.tokopedia.catalog.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tkpd.atcvariant.usecase.GetProductVariantAggregatorUseCase
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.catalog.ui.model.CatalogDetailUiModel
import com.tokopedia.catalog.ui.model.CatalogProductAtcUiModel
import com.tokopedia.catalog.ui.model.NavigationProperties
import com.tokopedia.catalog.ui.model.PriceCtaProperties
import com.tokopedia.catalog.ui.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oldcatalog.usecase.detail.CatalogDetailUseCase
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorUiData
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.AfterEach

class CatalogDetailPageViewModelTest {

    // Initialization scope
    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var catalogDetailUseCase: CatalogDetailUseCase

    @RelaxedMockK
    lateinit var getNotificationUseCase: GetNotificationUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var aggregatorUseCase: GetProductVariantAggregatorUseCase

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    private val viewModel by lazy {
        spyk(
            CatalogDetailPageViewModel(
                CoroutineTestDispatchersProvider,
                catalogDetailUseCase,
                getNotificationUseCase,
                aggregatorUseCase,
                userSession,
                addToCartUseCase
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
    fun `When getProductCatalog is Success, Then should invoke catalogDetailDataModel Success`() {
        coEvery {
            catalogDetailUseCase.getCatalogDetailV4(any(), any(), any())
        } answers {
            thirdArg<MutableLiveData<Result<CatalogDetailUiModel>>>().postValue(
                Success(
                    CatalogDetailUiModel(
                        widgets = emptyList(),
                        navigationProperties = NavigationProperties(),
                        priceCtaProperties = PriceCtaProperties(),
                        productSortingStatus = 0,
                        catalogUrl = ""
                    )
                )
            )
        }
        viewModel.getProductCatalog("41", "")
        val result = viewModel.catalogDetailDataModel.getOrAwaitValue()
        assert(result is Success)
    }

    @Test
    fun `When getProductCatalog is Error, Then should invoke catalogDetailDataModel Fail`() {
        val error = Throwable("Error")
        coEvery {
            catalogDetailUseCase.getCatalogDetailV4(any(), any(), any())
        } throws error

        viewModel.getProductCatalog("41", "")
        val result = viewModel.catalogDetailDataModel.getOrAwaitValue()
        assert(result is Fail)
    }

    @Test
    fun `When getProductCatalogComparisons is Success, Then should invoke comparisonUiModel to non-null`() {
        coEvery {
            catalogDetailUseCase.getCatalogDetailV4Comparison(any(), any())
        } returns ComparisonUiModel()

        viewModel.getProductCatalogComparisons("", listOf())

        assert(viewModel.comparisonUiModel.getOrAwaitValue() != null)
    }

    @Test
    fun `When getProductCatalogComparisons is Error, Then should invoke errorsToasterGetComparison`() {
        coEvery {
            catalogDetailUseCase.getCatalogDetailV4Comparison(any(), any())
        } throws Throwable("Error")

        viewModel.getProductCatalogComparisons("", listOf())

        assert(viewModel.errorsToasterGetComparison.getOrAwaitValue().message == "Error")
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When emitScrollEvent called, Then should emit scrollEvents`() = runTest {
        var result: String? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.scrollEvents.collectLatest {
                result = it
            }
        }

        viewModel.emitScrollEvent("Highlight")
        job.cancel()

        assert(result == "Highlight")
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

        assert(viewModel.addToCartDataModel.value?.isStatusError() == false)
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
    fun `getVariantInfo Response Success`() {
        val testData = listOf("Merah", "L")
        coEvery {
            aggregatorUseCase.executeOnBackground(any())
        } returns ProductVariantAggregatorUiData(
            variantData = ProductVariant(
                defaultChild = "123",
                children = listOf(
                    VariantChild(
                        productId = "123",
                        optionName = testData
                    )
                )
            )
        )

        viewModel.getVariantInfo()

        assert(viewModel.variantName.getOrAwaitValue() == testData.joinToString())
    }

    @Test
    fun `getVariantInfo Response Fail`() {
        val error = MessageErrorException("error")
        coEvery {
            aggregatorUseCase.executeOnBackground(any())
        } throws error

        viewModel.getVariantInfo()

        assert(viewModel.errorsToaster.getOrAwaitValue().message == "error")
    }
}
