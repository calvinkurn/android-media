package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.CategoryGetDetailModular
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.category.mapper.TickerMapper
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2TabData
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel
import com.tokopedia.tokopedianow.common.domain.mapper.AceSearchParamMapper
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse.CategoryListResponse
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse.ProductAdsResponse
import com.tokopedia.tokopedianow.common.domain.model.GetTargetedTickerResponse
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.common.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetProductAdsUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.AddressMapper
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.GetFeedbackFieldModel
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetFeedbackFieldToggleUseCase
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetProductCountUseCase
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetSortFilterUseCase
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
open class TokoNowCategoryL2TabViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private lateinit var getSortFilterUseCase: GetSortFilterUseCase
    private lateinit var getCategoryProductUseCase: GetCategoryProductUseCase
    private lateinit var getProductAdsUseCase: GetProductAdsUseCase
    private lateinit var getProductCountUseCase: GetProductCountUseCase
    private lateinit var getCategoryListUseCase: GetCategoryListUseCase
    private lateinit var getFeedbackToggleUseCase: GetFeedbackFieldToggleUseCase
    private lateinit var getMiniCartUseCase: GetMiniCartListSimplifiedUseCase
    private lateinit var getTargetedTickerUseCase: GetTargetedTickerUseCase
    private lateinit var addToCartUseCase: AddToCartUseCase
    private lateinit var updateCartUseCase: UpdateCartUseCase
    private lateinit var deleteCartUseCase: DeleteCartUseCase
    private lateinit var aceSearchParamMapper: AceSearchParamMapper
    private lateinit var addressData: TokoNowLocalAddress
    private lateinit var affiliateService: NowAffiliateService
    private lateinit var userSession: UserSessionInterface

    protected lateinit var viewModel: TokoNowCategoryL2TabViewModel
    protected lateinit var filterController: FilterController

    protected val userId = "1941"
    protected val shopId = 10525L
    protected val warehouseId = 1125L

    protected val categoryTitle = "Bahan Makanan"
    protected var categoryIdL1 = "12556"
    protected var categoryIdL2 = "12557"

    protected val requestParamsSlot = slot<RequestParams>()
    protected val requestParams by lazy { requestParamsSlot.captured }

    protected val data: CategoryL2TabData
        get() {
            val tickerData = TickerMapper.mapTickerData(getTargetedTickerResponse)
            val componentList = getCategoryLayoutResponse.components

            return CategoryL2TabData(
                title = categoryTitle,
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = categoryIdL2,
                tickerData = tickerData,
                componentList = componentList
            )
        }

    protected val warehouses = listOf(
        WarehouseData("151245", "fc"),
        WarehouseData("151246", "hub")
    )
    protected val localCacheModel = LocalCacheModel(
        warehouses = createLocalWarehousesData(),
        warehouse_id = warehouseId.toString(),
        shop_id = shopId.toString(),
        city_id = "1245",
        address_id = "3455",
        district_id = "1606",
        lat = "784915.125",
        long = "125995.234",
        postal_code = "1660"
    )

    protected val getProductResponse = "category/ace-search-product-1-aneka-sayuran.json"
        .jsonToObject<AceSearchProductModel>()
    protected val getProductAdsResponse = "category/get_product_ads_response.json"
        .jsonToObject<ProductAdsResponse>()
    protected val getTargetedTickerResponse = "category/get_targeted_ticker_block_add_to_cart_false.json"
        .jsonToObject<GetTargetedTickerResponse>()
    protected val getCategoryLayoutResponse = "category/category_get_detail_modular.json"
        .jsonToObject<CategoryGetDetailModular>()
    protected val getQuickFilterResponse = "category/get_quick_filter_response.json"
        .jsonToObject<DynamicFilterModel>()
    protected val getCategoryFilterResponse = "category/get_category_filter_response.json"
        .jsonToObject<DynamicFilterModel>()
    protected val getCategoryListResponse = "category/get_category_list_response.json"
        .jsonToObject<CategoryListResponse>()

    private val uniqueId = "someUniqueId"
    private val queryParams = mapOf<String, String>()
    private val tickerPage = GetTargetedTickerUseCase.CATEGORY_L2

    @Before
    fun setUp() {
        getSortFilterUseCase = mockk(relaxed = true)
        getCategoryProductUseCase = mockk(relaxed = true)
        getProductAdsUseCase = mockk(relaxed = true)
        getProductCountUseCase = mockk(relaxed = true)
        getCategoryListUseCase = mockk(relaxed = true)
        getFeedbackToggleUseCase = mockk(relaxed = true)
        getTargetedTickerUseCase = mockk(relaxed = true)
        getMiniCartUseCase = mockk(relaxed = true)
        addToCartUseCase = mockk(relaxed = true)
        updateCartUseCase = mockk(relaxed = true)
        deleteCartUseCase = mockk(relaxed = true)
        affiliateService = mockk(relaxed = true)
        addressData = mockk(relaxed = true)
        userSession = mockk(relaxed = true)

        aceSearchParamMapper = AceSearchParamMapper(
            userSession,
            addressData
        )
        aceSearchParamMapper.uniqueId = uniqueId

        filterController = FilterController()

        viewModel = TokoNowCategoryL2TabViewModel(
            getSortFilterUseCase = getSortFilterUseCase,
            getCategoryProductUseCase = getCategoryProductUseCase,
            getProductAdsUseCase = getProductAdsUseCase,
            getProductCountUseCase = getProductCountUseCase,
            getCategoryListUseCase = getCategoryListUseCase,
            getFeedbackToggleUseCase = getFeedbackToggleUseCase,
            getTargetedTickerUseCase = getTargetedTickerUseCase,
            getMiniCartUseCase = getMiniCartUseCase,
            addToCartUseCase = addToCartUseCase,
            updateCartUseCase = updateCartUseCase,
            deleteCartUseCase = deleteCartUseCase,
            aceSearchParamMapper = aceSearchParamMapper,
            affiliateService = affiliateService,
            addressData = addressData,
            userSession = userSession,
            dispatchers = coroutineTestRule.dispatchers
        )

        onGetUserId_thenReturn(userId)
        onGetShopId_thenReturn(shopId)
        onGetWarehouseId_thenReturn(warehouseId)
        onGetAddressData_thenReturn(localCacheModel)
        onGetWarehousesData_thenReturn(warehouses)
        onGetIsUserLoggedIn_thenReturn(false)
    }

    private fun onGetUserId_thenReturn(userId: String) {
        every { userSession.userId } returns userId
    }

    private fun onGetShopId_thenReturn(shopId: Long) {
        every { addressData.getShopId() } returns shopId
    }

    private fun onGetWarehousesData_thenReturn(warehouses: List<WarehouseData>) {
        every { addressData.getWarehousesData() } returns warehouses
    }

    protected fun onGetIsUserLoggedIn_thenReturn(isLoggedIn: Boolean) {
        every { userSession.isLoggedIn } returns isLoggedIn
    }

    protected fun onGetShopId_throwsError() {
        every { addressData.getShopId() } throws NullPointerException()
    }

    protected fun onGetWarehouseId_thenReturn(warehouseId: Long) {
        every { addressData.getWarehouseId() } returns warehouseId
    }

    protected fun onGetAddressData_thenReturn(data: LocalCacheModel) {
        every { addressData.getAddressData() } returns data
    }

    protected fun onGetWarehouseId_throwsError() {
        every { addressData.getWarehouseId() } throws NullPointerException()
    }

    protected fun onGetProductList(
        withQueryParams: Map<String?, Any?> = createGetProductQueryParams(categoryIdL1, categoryIdL2),
        thenReturn: AceSearchProductModel
    ) {
        coEvery { getCategoryProductUseCase.execute(withQueryParams) } returns thenReturn
    }

    protected fun onGetProductList_throwsError() {
        coEvery { getCategoryProductUseCase.execute(any()) } throws NullPointerException()
    }

    protected fun onGetProductAds(
        withQueryParams: Map<String?, Any> = createGetProductAdsParams(categoryIdL2),
        thenReturn: ProductAdsResponse
    ) {
        coEvery { getProductAdsUseCase.execute(withQueryParams) } returns thenReturn
    }

    protected fun onGetProductAds_throwsError() {
        coEvery { getProductAdsUseCase.execute(any()) } throws NullPointerException()
    }

    protected fun onGetQuickFilter(
        withQueryParams: Map<String?, Any?>,
        thenReturn: DynamicFilterModel
    ) {
        coEvery { getSortFilterUseCase.execute(withQueryParams) } returns thenReturn
    }

    protected fun onGetQuickFilter_thenReturn(
        response: DynamicFilterModel
    ) {
        val queryParams = createRequestQueryParams(
            source = "quick_filter_tokonow_directory"
        )
        coEvery { getSortFilterUseCase.execute(queryParams) } returns response
    }

    protected fun onGetCategoryFilter(
        withQueryParams: Map<String?, Any?>,
        thenReturn: DynamicFilterModel
    ) {
        coEvery { getSortFilterUseCase.execute(withQueryParams) } returns thenReturn
    }

    protected fun onGetCategoryFilter_thenReturn(response: DynamicFilterModel) {
        val queryParams = createGetCategoryFilterQueryParams()
        coEvery { getSortFilterUseCase.execute(queryParams) } returns response
    }

    protected fun onGetSortFilterFilter(
        withQueryParams: Map<String?, Any?> = createRequestQueryParams(),
        thenReturn: DynamicFilterModel
    ) {
        coEvery { getSortFilterUseCase.execute(withQueryParams) } returns thenReturn
    }

    protected fun onGetSortFilterFilter_throwsError() {
        coEvery { getSortFilterUseCase.execute(any()) } throws NullPointerException()
    }

    protected fun onGetTicker(withWarehouseId: Long = warehouseId, thenReturn: GetTargetedTickerResponse) {
        coEvery {
            getTargetedTickerUseCase.execute(tickerPage, withWarehouseId.toString())
        } returns thenReturn
    }

    protected fun onGetCategoryList_thenReturn(response: CategoryListResponse) {
        coEvery { getCategoryListUseCase.execute(warehouses, 1) } returns response
    }

    protected fun onGetFeedbackToggle_thenReturn(response: GetFeedbackFieldModel.Data) {
        coEvery { getFeedbackToggleUseCase.execute() } returns response
    }

    protected fun onGetCategoryList_throwsError() {
        coEvery { getCategoryListUseCase.execute(warehouses, 1) } throws NullPointerException()
    }

    protected fun onGetProductCount_thenReturn(productCount: String) {
        coEvery {
            getProductCountUseCase.execute(any(), any(), capture(requestParamsSlot))
        } answers {
            firstArg<(String) -> Unit>().invoke(productCount)
        }
    }

    protected fun onGetProductCount_throwsError() {
        coEvery {
            getProductCountUseCase.execute(any(), any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(NullPointerException())
        }
    }

    protected fun onAddToCart_thenReturn(response: AddToCartDataModel) {
        coEvery {
            addToCartUseCase.execute(any(), any())
        } answers {
            firstArg<(AddToCartDataModel) -> Unit>().invoke(response)
        }
    }

    protected fun onUpdateCartItem_thenReturn(response: UpdateCartV2Data) {
        coEvery {
            updateCartUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(response)
        }
    }

    protected fun onDeleteCartItem_thenReturn(response: RemoveFromCartData) {
        coEvery {
            deleteCartUseCase.execute(any(), any())
        } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(response)
        }
    }

    protected fun onCreateAffiliateLink_thenReturn(affiliateLink: String) {
        every { affiliateService.createAffiliateLink(any()) } returns affiliateLink
    }

    protected fun setVisitableList(visitableList: List<Visitable<*>>?) {
        viewModel.mockPrivateField("visitableList", visitableList)
    }

    private fun verifyGetProductAdsUseCaseCalled(queryParams: Map<String?, Any>) {
        coVerify { getProductAdsUseCase.execute(queryParams) }
    }

    protected fun verifyGetProductUseCaseCalled(queryParams: Map<String?, Any?>) {
        coVerify { getCategoryProductUseCase.execute(queryParams) }
    }

    protected fun verifyGetProductUseCaseCalled(times: Int) {
        coVerify(exactly = times) { getCategoryProductUseCase.execute(any()) }
    }

    protected fun verifyGetProductUseCaseNotCalled() {
        coVerify(exactly = 0) { getCategoryProductUseCase.execute(any()) }
    }

    protected fun verifyGetCategoryListUseCaseNotCalled() {
        coVerify(exactly = 0) { getCategoryListUseCase.execute(warehouses, 1) }
    }

    protected fun verifyGetSortFilterUseCaseCalled(
        queryParams: Map<String?, Any?> = createRequestQueryParams(),
        times: Int = 1
    ) {
        coVerify(exactly = times) { getSortFilterUseCase.execute(queryParams) }
    }

    protected fun verifyGetProductCountUseCaseCalled(expectedRequestParams: RequestParams) {
        val actualParams = requestParams.parameters
        val expectedParams = expectedRequestParams.parameters

        expectedParams.forEach {
            val actualParam = actualParams[it.key]
            val expectedParam = expectedParams[it.key]
            assertEquals(expectedParam, actualParam)
        }
    }

    protected fun verifyAddToCartUseCaseCalled() {
        coVerify { addToCartUseCase.execute(any(), any()) }
    }

    protected fun verifyUpdateCartUseCaseCalled() {
        coVerify { updateCartUseCase.execute(any(), any()) }
    }

    protected fun verifyDeleteCartUseCaseCalled() {
        coVerify { deleteCartUseCase.execute(any(), any()) }
    }

    protected fun verifyCreateAffiliateLinkCalled(withUri: String) {
        verify { affiliateService.createAffiliateLink(withUri) }
    }

    protected fun verifyGetMiniCartUseCaseCalled() {
        coVerify { getMiniCartUseCase.executeOnBackground() }
    }

    protected fun verifyGetMiniCartUseCaseNotCalled() {
        coVerify(exactly = 0) { getMiniCartUseCase.executeOnBackground() }
    }

    protected fun verifyRequestQueryParams(
        expectedGetProductQueryParams: Map<String?, Any?> = createGetProductQueryParams(categoryIdL1, categoryIdL2),
        expectedGetProductAdsQueryParams: Map<String?, Any> = createGetProductAdsParams(categoryIdL2)
    ) {
        verifyGetProductUseCaseCalled(expectedGetProductQueryParams)
        verifyGetProductAdsUseCaseCalled(expectedGetProductAdsQueryParams)
        verifySortFilterQueryParams()
    }

    protected fun verifySortFilterQueryParams() {
        val expectedGetQuickFilterQueryParams = createRequestQueryParams(
            source = "quick_filter_tokonow_directory"
        )

        val expectedGetCategoryFilterQueryParams = createGetCategoryFilterQueryParams()

        verifyGetSortFilterUseCaseCalled(expectedGetQuickFilterQueryParams)
        verifyGetSortFilterUseCaseCalled(expectedGetCategoryFilterQueryParams)
    }

    protected inline fun<reified T> LiveData<List<Visitable<*>>>.verifyVisitableNotExists() {
        val item = value.orEmpty().firstOrNull { it is T }
        assertEquals(null, item)
    }

    protected fun verifyVisitableList(expectedVisitableList: List<Visitable<*>>) {
        val actualVisitableList = viewModel.visitableListLiveData.value.orEmpty()

        actualVisitableList.forEachIndexed { index, actualVisitableItem ->
            when (actualVisitableItem) {
                is CategoryQuickFilterUiModel -> {
                    val expectedVisitableItem =
                        expectedVisitableList[index] as CategoryQuickFilterUiModel
                    verifyQuickFilterUiModel(expectedVisitableItem, actualVisitableItem)
                }

                is TokoNowEmptyStateNoResultUiModel -> {
                    val expectedVisitableItem =
                        expectedVisitableList[index] as TokoNowEmptyStateNoResultUiModel
                    verifyEmptyStateUiModel(expectedVisitableItem, actualVisitableItem)
                }

                else -> {
                    val expectedVisitableItem = expectedVisitableList[index]
                    assertEquals(expectedVisitableItem, actualVisitableItem)
                }
            }
        }
    }

    protected fun crateGetTargetedTickerOutOfStockResponse(): GetTargetedTickerResponse {
        val metadata = GetTargetedTickerResponse.GetTargetedTicker.TickerResponse.Metadata(
            type = "oosCategoryIDs",
            values = listOf(categoryIdL2)
        )
        val oosTickerResponse = getTargetedTickerResponse.getTargetedTicker!!.tickers[0]
            .copy(metadata = listOf(metadata))

        val getTargetedTickerOos = getTargetedTickerResponse
            .getTargetedTicker!!.copy(tickers = listOf(oosTickerResponse))

        return getTargetedTickerResponse.copy(getTargetedTicker = getTargetedTickerOos)
    }

    protected fun createGetProductQueryParams(srpPageId: String, sc: String): MutableMap<String?, Any?> {
        return mutableMapOf<String?, Any?>().apply {
            put("user_cityId", localCacheModel.city_id)
            put("user_addressId", localCacheModel.address_id)
            put("user_districtId", localCacheModel.district_id)
            put("user_lat", localCacheModel.lat)
            put("user_long", localCacheModel.long)
            put("user_postCode", localCacheModel.postal_code)
            put("warehouses", AddressMapper.mapToWarehouses(localCacheModel))
            put("srp_page_id", srpPageId)
            put("sc", sc)
            put("navsource", "category_tokonow_directory")
            put("source", "category_tokonow_directory")
            put("page", 1)
            put("use_page", true)
            put("rows", 9)
            put("unique_id", uniqueId)
            put("ob", "23")
            put("device", "android")
        }
    }

    protected fun createGetProductAdsParams(sc: String): MutableMap<String?, Any> {
        val warehouseIds = AddressMapper.mapToWarehouseIds(localCacheModel)

        return mutableMapOf<String?, Any>().apply {
            put("sc", sc)
            put("src", "directory_tokonow")
            put("page", 1)
            put("user_warehouseId", warehouseIds)
            put("item", 20)
            put("device", "android")
            put("userId", userId)
            put("ep", "product")
            put("user_lat", localCacheModel.lat)
            put("user_long", localCacheModel.long)
            put("user_cityId", localCacheModel.city_id)
            put("user_districtId", localCacheModel.district_id)
            put("user_postCode", localCacheModel.postal_code)
            put("user_addressId", localCacheModel.address_id)
        }
    }

    protected fun createRequestQueryParams(
        source: String = "tokonow_directory",
        srpPageId: String = categoryIdL1,
        sc: String = categoryIdL2,
        rows: Int = 12,
        page: Int? = 1
    ): MutableMap<String?, Any?> {
        return mutableMapOf<String?, Any?>().apply {
            val aceSearchParams = createAceSearchParams(source, srpPageId, sc, rows, page)
            val filterParams = createFilterParams(source)

            putAll(aceSearchParams)
            putAll(filterParams)
        }
    }

    protected fun createGetCategoryFilterQueryParams(
        srpPageId: String = categoryIdL1,
        sc: String = categoryIdL2
    ): Map<String?, Any?> {
        val mapParameter = mutableMapOf<String, String>()
        val categoryFilterQueryParams = mutableMapOf<String?, Any?>()

        createRequestQueryParams(
            source = "category_tokonow_directory",
            srpPageId = srpPageId,
            sc = sc
        ).forEach {
            it.key?.let { key ->
                mapParameter[key] = it.value.toString()
            }
        }

        val filterParams = FilterHelper
            .createParamsWithoutExcludes(mapParameter)

        categoryFilterQueryParams.putAll(filterParams)
        return categoryFilterQueryParams
    }

    protected fun createGetProductCountRequestParams(mapParameter: Map<String, String>): RequestParams {
        val getProductCountParams = createRequestQueryParams(rows = 0, page = null)
        getProductCountParams.putAll(FilterHelper.createParamsWithoutExcludes(mapParameter))

        val getProductCountRequestParams = RequestParams.create()
        getProductCountRequestParams.putAll(getProductCountParams)

        return getProductCountRequestParams
    }

    private fun verifyQuickFilterUiModel(
        expectedVisitableItem: CategoryQuickFilterUiModel,
        actualVisitableItem: CategoryQuickFilterUiModel
    ) {
        assertEquals(expectedVisitableItem.id, actualVisitableItem.id)
        assertEquals(expectedVisitableItem.mapParameter, actualVisitableItem.mapParameter)
        assertEquals(expectedVisitableItem.state, actualVisitableItem.state)

        actualVisitableItem.itemList.forEachIndexed { index, actualItem ->
            val expectedItem = expectedVisitableItem.itemList[index]
            assertEquals(expectedItem.chipType, actualItem.chipType)
            assertEquals(expectedItem.showNewNotification, actualItem.showNewNotification)

            val actualOptions = actualItem.filter.options
            val expectedOptions = expectedItem.filter.options

            actualOptions.forEachIndexed { idx, actualOption ->
                val expectedOption = expectedOptions[idx]
                assertEquals(expectedOption.name, actualOption.name)
                assertEquals(expectedOption.key, actualOption.key)
                assertEquals(expectedOption.value, actualOption.value)
            }
        }
    }

    private fun verifyEmptyStateUiModel(
        expectedVisitableItem: TokoNowEmptyStateNoResultUiModel,
        actualVisitableItem: TokoNowEmptyStateNoResultUiModel
    ) {
        assertEquals(expectedVisitableItem.id, actualVisitableItem.id)
        assertEquals(expectedVisitableItem.activeFilterList, actualVisitableItem.activeFilterList)
        assertEquals(expectedVisitableItem.defaultTitle, actualVisitableItem.defaultTitle)
        assertEquals(expectedVisitableItem.defaultDescription, actualVisitableItem.defaultDescription)
        assertEquals(expectedVisitableItem.defaultImage, actualVisitableItem.defaultImage)
        assertEquals(expectedVisitableItem.defaultTextPrimaryButton, actualVisitableItem.defaultTextPrimaryButton)
        assertEquals(expectedVisitableItem.defaultUrlPrimaryButton, actualVisitableItem.defaultUrlPrimaryButton)
        assertEquals(expectedVisitableItem.excludeFilter, actualVisitableItem.excludeFilter)
        assertEquals(expectedVisitableItem.enablePrimaryButton, actualVisitableItem.enablePrimaryButton)
        assertEquals(expectedVisitableItem.enableSecondaryButton, actualVisitableItem.enableSecondaryButton)
    }

    private fun createFilterParams(source: String): MutableMap<String, String> {
        return FilterHelper.createParamsWithoutExcludes(queryParams)
            .toMutableMap()
            .also {
                it["navsource"] = source
                it["source"] = source
            }
    }

    private fun createAceSearchParams(
        source: String,
        srpPageId: String,
        sc: String,
        rows: Int,
        page: Int?
    ): MutableMap<String?, Any?> {
        return aceSearchParamMapper.createRequestParams(
            source = source,
            srpPageId = srpPageId,
            sc = sc,
            rows = rows,
            page = page
        )
    }

    private fun createLocalWarehousesData(): List<LocalWarehouseModel> {
        return warehouses.map {
            LocalWarehouseModel(
                warehouse_id = it.warehouseId.toLong(),
                service_type = it.serviceType
            )
        }
    }
}
