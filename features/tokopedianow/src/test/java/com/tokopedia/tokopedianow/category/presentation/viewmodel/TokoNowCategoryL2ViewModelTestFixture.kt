package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.CategoryGetDetailModular
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryDetailUseCase
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryLayoutUseCase
import com.tokopedia.tokopedianow.common.domain.model.GetTargetedTickerResponse
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
open class TokoNowCategoryL2ViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private lateinit var getCategoryLayoutUseCase: GetCategoryLayoutUseCase
    private lateinit var getCategoryDetailUseCase: GetCategoryDetailUseCase
    private lateinit var addressData: TokoNowLocalAddress
    private lateinit var getTargetedTickerUseCase: GetTargetedTickerUseCase
    private lateinit var getShopAndWarehouseUseCase: GetChosenAddressWarehouseLocUseCase
    private lateinit var getMiniCartUseCase: GetMiniCartListSimplifiedUseCase
    private lateinit var addToCartUseCase: AddToCartUseCase
    private lateinit var updateCartUseCase: UpdateCartUseCase
    private lateinit var deleteCartUseCase: DeleteCartUseCase
    private lateinit var affiliateService: NowAffiliateService
    private lateinit var userSession: UserSessionInterface

    protected lateinit var viewModel: TokoNowCategoryL2ViewModel

    protected val shopId = 10525L
    protected val warehouseId = 1125L
    protected val warehouses = listOf(
        WarehouseData("151245", "fc"),
        WarehouseData("151246", "hub")
    )

    protected val supportedLayoutTypes = listOf(
        "featured-product",
        "product-list-filter",
        "static-text",
        "product-list-infinite-scroll"
    )

    protected val getTargetedTickerResponse = "category/get_targeted_ticker_block_add_to_cart_false.json"
        .jsonToObject<GetTargetedTickerResponse>()
    protected val getCategoryLayoutResponse = "category/category_get_detail_modular.json"
        .jsonToObject<CategoryGetDetailModular>()
    protected val getCategoryDetailResponse = "category/category-detail.json"
        .jsonToObject<CategoryDetailResponse>()

    protected val categoryIdL1 = "12556"
    protected val categoryIdL2 = "12557"

    private val tickerPage = GetTargetedTickerUseCase.CATEGORY_L2


    @Before
    fun setUp() {
        getCategoryLayoutUseCase = mockk(relaxed = true)
        getCategoryDetailUseCase = mockk(relaxed = true)
        addressData = mockk(relaxed = true)
        getTargetedTickerUseCase = mockk(relaxed = true)
        getShopAndWarehouseUseCase = mockk(relaxed = true)
        getMiniCartUseCase = mockk(relaxed = true)
        addToCartUseCase = mockk(relaxed = true)
        updateCartUseCase = mockk(relaxed = true)
        deleteCartUseCase = mockk(relaxed = true)
        affiliateService = mockk(relaxed = true)
        userSession = mockk(relaxed = true)

        viewModel = TokoNowCategoryL2ViewModel(
            getCategoryLayoutUseCase = getCategoryLayoutUseCase,
            getCategoryDetailUseCase = getCategoryDetailUseCase,
            addressData = addressData,
            getTargetedTickerUseCase = getTargetedTickerUseCase,
            getShopAndWarehouseUseCase = getShopAndWarehouseUseCase,
            getMiniCartUseCase = getMiniCartUseCase,
            addToCartUseCase = addToCartUseCase,
            updateCartUseCase = updateCartUseCase,
            deleteCartUseCase = deleteCartUseCase,
            affiliateService = affiliateService,
            userSession = userSession,
            dispatchers = coroutineTestRule.dispatchers
        ).apply {
            categoryIdL1 = this@TokoNowCategoryL2ViewModelTestFixture.categoryIdL1
            categoryIdL2 = this@TokoNowCategoryL2ViewModelTestFixture.categoryIdL2
        }

        onGetShopId_thenReturn(shopId)
        onGetWarehouseId_thenReturn(warehouseId)
        onGetTicker_thenReturn(warehouseId, getTargetedTickerResponse)
    }

    private fun onGetShopId_thenReturn(shopId: Long) {
        every { addressData.getShopId() } returns shopId
    }

    private fun onGetTicker_thenReturn(warehouseId: Long, response: GetTargetedTickerResponse) {
        coEvery {
            getTargetedTickerUseCase.execute(tickerPage, warehouseId.toString())
        } returns response
    }

    protected fun onGetWarehouseId_thenReturn(warehouseId: Long) {
        every { addressData.getWarehouseId() } returns warehouseId
    }

    protected fun onGetWarehousesData_thenReturn(warehouses: List<WarehouseData>) {
        every { addressData.getWarehousesData() } returns warehouses
    }

    protected fun onGetAddressData_thenReturn(data: LocalCacheModel) {
        every { addressData.getAddressData() } returns data
    }

    protected fun onGetCategoryLayout_thenReturn(response: CategoryGetDetailModular) {
        coEvery {
            getCategoryLayoutUseCase.execute(categoryIdL2)
        } returns response
    }

    protected fun onGetCategoryDetail_thenReturn(response: CategoryDetailResponse) {
        coEvery {
            getCategoryDetailUseCase.execute(warehouses, categoryIdL1)
        } returns response
    }
}
