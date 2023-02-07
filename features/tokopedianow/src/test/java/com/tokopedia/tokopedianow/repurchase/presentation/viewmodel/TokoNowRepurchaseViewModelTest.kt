package com.tokopedia.tokopedianow.repurchase.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.data.model.response.Header
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse.CategoryListResponse
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse.CategoryListResponse.CategoryResponse
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.data.createCategoryGridLayout
import com.tokopedia.tokopedianow.data.createChooseAddress
import com.tokopedia.tokopedianow.data.createChooseAddressLayout
import com.tokopedia.tokopedianow.data.createDateFilterLayout
import com.tokopedia.tokopedianow.data.createEmptyStateLayout
import com.tokopedia.tokopedianow.data.createRepurchaseLoadingLayout
import com.tokopedia.tokopedianow.data.createRepurchaseProductUiModel
import com.tokopedia.tokopedianow.data.createSortFilterLayout
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAddToCartTracker
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.VALUE.REPURCHASE_TOKONOW
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_HISTORY_FILTER
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_HISTORY_SEARCH
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_RESULT
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_OOC
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.ERROR_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.PRODUCT_REPURCHASE
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseProductMapper.mapToProductListUiModel
import com.tokopedia.tokopedianow.repurchase.domain.model.TokoNowRepurchasePageResponse.GetRepurchaseProductListResponse
import com.tokopedia.tokopedianow.repurchase.domain.model.TokoNowRepurchasePageResponse.GetRepurchaseProductMetaResponse
import com.tokopedia.tokopedianow.repurchase.domain.param.GetRepurchaseProductListParam
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseLayoutUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.SelectedDateFilter
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.SelectedSortFilter
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Assert.assertEquals
import org.junit.Test

class TokoNowRepurchaseViewModelTest: TokoNowRepurchaseViewModelTestFixture() {
    @Test
    fun `when tracking with setting screenName should give the same result`() {
        viewModel.trackOpeningScreen(REPURCHASE_TOKONOW)
        verifyTrackOpeningScreen()
    }

    @Test
    fun `when showing loading layout should run and give the success result`() {
        viewModel.showLoading()

        val layout = RepurchaseLayoutUiModel(
            layoutList = createRepurchaseLoadingLayout(),
            state = TokoNowLayoutState.LOADING
        )

        verifyShowLayoutSuccess(layout)
    }

    @Test
    fun `when showing empty state no history search layout should run and give the success result`() {
        viewModel.showEmptyState(EMPTY_STATE_NO_HISTORY_SEARCH)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createEmptyStateLayout(EMPTY_STATE_NO_HISTORY_SEARCH),
            state = TokoNowLayoutState.EMPTY
        )

        verifyEmptyStateNoHistoryLayoutSuccess(layout)
    }

    @Test
    fun `when showing empty state no history filter layout should run and give the success result`() {
        viewModel.showEmptyState(EMPTY_STATE_NO_HISTORY_FILTER)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createEmptyStateLayout(EMPTY_STATE_NO_HISTORY_FILTER),
            state = TokoNowLayoutState.EMPTY
        )

        verifyEmptyStateNoHistoryLayoutSuccess(layout)
    }

    @Test
    fun `when showing empty state ooc layout should run and give the success result`() {
        val serviceType = "2h"
        val localCacheModel = LocalCacheModel(
            service_type = serviceType
        )

        viewModel.setLocalCacheModel(localCacheModel)
        viewModel.showEmptyState(EMPTY_STATE_OOC)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createEmptyStateLayout(EMPTY_STATE_OOC, serviceType),
            state = TokoNowLayoutState.EMPTY
        )

        verifyEmptyStateOocLayoutSuccess(layout)
    }

    @Test
    fun `given localCacheModel not set when showEmptyState should add empty state to layout list`() {
        viewModel.showEmptyState(EMPTY_STATE_OOC)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createEmptyStateLayout(EMPTY_STATE_OOC),
            state = TokoNowLayoutState.EMPTY
        )

        verifyEmptyStateOocLayoutSuccess(layout)
    }

    @Test
    fun `when showing error state failed layout should run and give the success result`() {
        viewModel.showEmptyState(ERROR_STATE_FAILED_TO_FETCH_DATA)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createEmptyStateLayout(ERROR_STATE_FAILED_TO_FETCH_DATA),
            state = TokoNowLayoutState.EMPTY
        )

        verifyEmptyStateFailedLayoutSuccess(layout)
    }

    @Test
    fun `when showing empty state no result layout should run and give the success result`() {
        viewModel.showEmptyState(EMPTY_STATE_NO_RESULT)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createEmptyStateLayout(EMPTY_STATE_NO_RESULT),
            state = TokoNowLayoutState.EMPTY
        )

        verifyEmptyStateNoResultLayoutSuccess(layout)
    }

    @Test
    fun `when showing choose address widget layout should run and give the success result`() {
        viewModel.getLayoutList()

        val layout = RepurchaseLayoutUiModel(
            layoutList = createChooseAddressLayout(),
            state = TokoNowLayoutState.SHOW
        )

        verifyChooseAddressWidgetLayoutSuccess(layout)
    }

    @Test
    fun `when getting category list layout should run and give the success result`() {
        val warehouseId = "1"
        val response = CategoryListResponse(
            header = Header(),
            data = listOf(
                CategoryResponse(
                    id = "3",
                    name = "Category 3",
                    url = "tokopedia://",
                    appLinks = "tokoepdia://",
                    imageUrl = "tokopedia://",
                    parentId = "5",
                    childList = listOf(),
                    isAdult = 0
                ),
                CategoryResponse(
                    id = "4",
                    name = "Category 4",
                    url = "tokopedia://",
                    appLinks = "tokoepdia://",
                    imageUrl = "tokopedia://",
                    parentId = "5",
                    childList = listOf(),
                    isAdult = 1
                )
            )
        )

        onGetCategoryList_thenReturn(response)

        privateLocalCacheModel.set(viewModel, LocalCacheModel(warehouse_id = warehouseId))

        viewModel.showEmptyState(EMPTY_STATE_NO_RESULT)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createCategoryGridLayout(response, warehouseId),
            state = TokoNowLayoutState.SHOW
        )

        verifyGetCategoryListUseCaseCalled()
        verifyGetCategoryGridLayoutSuccess(layout)
    }

    @Test
    fun `when getting category list layout and map the data with different data should give the success result with mapped data`() {
        val warehouseId = "1"
        var response = CategoryListResponse(
            header = Header(),
            data = listOf()
        )

        onGetCategoryList_thenReturn(response)

        viewModel.showEmptyState(EMPTY_STATE_NO_RESULT)

        privateLocalCacheModel.set(viewModel, LocalCacheModel(warehouse_id = warehouseId))

        response = CategoryListResponse(
            header = Header(),
            data = listOf(
                CategoryResponse(
                    id = "3",
                    name = "Category 3",
                    url = "tokopedia://",
                    appLinks = "tokoepdia://",
                    imageUrl = "tokopedia://",
                    parentId = "5",
                    childList = listOf(),
                    isAdult = 0
                ),
                CategoryResponse(
                    id = "4",
                    name = "Category 4",
                    url = "tokopedia://",
                    appLinks = "tokoepdia://",
                    imageUrl = "tokopedia://",
                    parentId = "5",
                    childList = listOf(),
                    isAdult = 1
                )
            )
        )

        onGetCategoryList_thenReturn(response)

        viewModel.getCategoryMenu()

        val layout = RepurchaseLayoutUiModel(
            layoutList = createCategoryGridLayout(response, warehouseId),
            state = TokoNowLayoutState.SHOW
        )

        verifyGetCategoryListUseCaseCalled()
        verifyGetCategoryGridLayoutSuccess(layout)
    }

    @Test
    fun `when getting category list layout and map the data with empty list should give the success result with empty list`() {
        val warehouseId = "1"
        val response = CategoryListResponse(
            header = Header(),
            data = listOf(
                CategoryResponse(
                    id = "3",
                    name = "Category 3",
                    url = "tokopedia://",
                    appLinks = "tokoepdia://",
                    imageUrl = "tokopedia://",
                    parentId = "5",
                    childList = listOf(),
                    isAdult = 0
                ),
                CategoryResponse(
                    id = "4",
                    name = "Category 4",
                    url = "tokopedia://",
                    appLinks = "tokoepdia://",
                    imageUrl = "tokopedia://",
                    parentId = "5",
                    childList = listOf(),
                    isAdult = 1
                )
            )
        )

        onGetCategoryList_thenReturn(response)

        viewModel.showEmptyState(EMPTY_STATE_NO_RESULT)

        privateLocalCacheModel.set(viewModel, LocalCacheModel(warehouse_id = warehouseId))

        onGetCategoryList_thenReturn(Throwable())

        viewModel.getCategoryMenu()

        val layout = RepurchaseLayoutUiModel(
            layoutList = createCategoryGridLayout(
                CategoryListResponse(
                    data = listOf(),
                    header = Header()
                ),
                warehouseId = warehouseId
            ),
            state = TokoNowLayoutState.SHOW
        )

        verifyGetCategoryListUseCaseCalled()
        verifyGetCategoryGridLayoutSuccess(layout)
    }

    @Test
    fun `when getting sort filter layout should run and give the success result`() {
        onGetRepurchaseProductList_thenReturn(
            GetRepurchaseProductListResponse(
                meta = GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(RepurchaseProduct())
            )
        )

        viewModel.getLayoutList()
        viewModel.getLayoutData()

        val layout = RepurchaseLayoutUiModel(
            layoutList = createSortFilterLayout(),
            state = TokoNowLayoutState.UPDATE
        )

        verifySortFilterLayoutSuccess(layout)
    }

    @Test
    fun `when applying sort filter layout should run and give the success result`() {
        onGetRepurchaseProductList_thenReturn(
            GetRepurchaseProductListResponse(
                meta = GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(RepurchaseProduct())
            )
        )

        viewModel.getLayoutList()
        viewModel.getLayoutData()
        viewModel.applySortFilter(2)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createSortFilterLayout(2),
            state = TokoNowLayoutState.UPDATE
        )

        verifySortFilterLayoutSuccess(layout)
    }

    @Test
    fun `when applying date filter layout should run and give the success result`() {
        onGetRepurchaseProductList_thenReturn(
            GetRepurchaseProductListResponse(
                meta = GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(RepurchaseProduct())
            )
        )

        viewModel.getLayoutList()
        viewModel.getLayoutData()
        viewModel.applyDateFilter(SelectedDateFilter())

        val layout = RepurchaseLayoutUiModel(
            layoutList = createDateFilterLayout(SelectedDateFilter()),
            state = TokoNowLayoutState.UPDATE
        )

        verifySortFilterLayoutSuccess(layout)
    }

    @Test
    fun `when removing choose address widget layout should run and give the success result`() {
        `when showing choose address widget layout should run and give the success result`()

        viewModel.removeChooseAddressWidget()

        verifyChooseAddressWidgetLayoutRemovedSuccess()
    }

    @Test
    fun `when getting chooseAddress should run and give the success result`() {
        onGetChooseAddress_thenReturn(createChooseAddress())

        viewModel.getChooseAddress(TokoNowHomeFragment.SOURCE)

        verifyGetChooseAddress()

        val expectedResponse = createChooseAddress().response
        verfifyGetChooseAddressSuccess(expectedResponse)
    }

    @Test
    fun `when getting chooseAddress should throw chooseAddress's exception and get failed result`() {
        onGetChooseAddress_thenReturn(Exception())

        viewModel.getChooseAddress(TokoNowHomeFragment.SOURCE)

        verifyGetChooseAddressFail()
    }

    @Test
    fun `when getLayoutData throw exception should do nothing`() {
        onGetLayoutList_thenReturnNull()

        viewModel.getLayoutData()

        viewModel.getLayout
            .verifyValueEquals(null)
    }

    @Test
    fun `given getLayoutData success when getMiniCart should set atc quantity`() {
        val miniCartResponse = MiniCartSimplifiedData()
        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(stock = 0))
        )

        onGetMiniCart_thenReturn(miniCartResponse)
        onGetRepurchaseProductList_thenReturn(productListResponse)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.getLayoutData()
        viewModel.getMiniCart(listOf("1"), "1")

        val layoutList = listOf(createRepurchaseProductUiModel(
            productCard = TokoNowProductCardViewUiModel(
                isSimilarProductShown = true,
                isWishlistShown = true,
                needToShowQuantityEditor = true
            ),
            position = 1
        ))

        val expectedResult = Success(
            RepurchaseLayoutUiModel(
                layoutList = layoutList,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetMiniCartUseCaseCalled()

        viewModel.atcQuantity
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when getMiniCart success should set miniCart value success`() {
        val response = MiniCartSimplifiedData()

        onGetMiniCart_thenReturn(response)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.getMiniCart(listOf("1"), "1")

        verifyGetMiniCartUseCaseCalled()

        viewModel.miniCart
            .verifySuccessEquals(Success(response))
    }

    @Test
    fun `when getMiniCart twice should set miniCart value success`() {
        val response = MiniCartSimplifiedData()

        onGetMiniCart_thenReturn(response)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.getMiniCart(listOf("1"), "1")
        viewModel.getMiniCart(listOf("1"), "1")

        verifyGetMiniCartUseCaseCalled()

        viewModel.miniCart
            .verifySuccessEquals(Success(response))
    }

    @Test
    fun `when getMiniCart error should set miniCart value FAIL`() {
        val error = NullPointerException()

        onGetMiniCart_throwException(error)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.getMiniCart(listOf("1"), "1")

        verifyGetMiniCartUseCaseCalled()

        viewModel.miniCart
            .verifyErrorEquals(Fail(error))
    }

    @Test
    fun `when getMiniCart throw exception should set miniCart value FAIL`() {
        val error = NullPointerException()

        onGetMiniCart_throwException(error)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.getMiniCart(listOf("1"), "1")

        verifyGetMiniCartUseCaseCalled()

        viewModel.miniCart
            .verifyErrorEquals(Fail(error))
    }

    @Test
    fun `given shopId empty when getMiniCart should NOT call get mini cart use case`() {
        val shopId = listOf<String>()
        val response = MiniCartSimplifiedData()

        onGetMiniCart_thenReturn(response)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.getMiniCart(shopId, "1")

        verifyGetMiniCartUseCaseNotCalled()
    }

    @Test
    fun `given warehouseId empty when getMiniCart should NOT call get mini cart use case`() {
        val warehouseId = ""
        val response = MiniCartSimplifiedData()

        onGetMiniCart_thenReturn(response)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.getMiniCart(listOf("1"), warehouseId)

        verifyGetMiniCartUseCaseNotCalled()
    }

    @Test
    fun `given user not loggedIn when getMiniCart should NOT call get mini cart use case`() {
        val isLoggedIn = false
        val response = MiniCartSimplifiedData()

        onGetMiniCart_thenReturn(response)
        onGetUserLoggedIn_thenReturn(isLoggedIn)

        viewModel.getMiniCart(listOf("1"), "1")

        verifyGetMiniCartUseCaseNotCalled()
    }

    @Test
    fun `when showEmptyState throw exception should do nothing`() {
        onGetLayoutList_thenReturnNull()

        viewModel.showEmptyState("")

        viewModel.getLayout
            .verifyValueEquals(null)
    }

    @Test
    fun `when setProductAddToCartQuantity _getLayout has value should do nothing`() {
        onGetRepurchaseProductList_thenReturn(
            GetRepurchaseProductListResponse(
                meta = GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(RepurchaseProduct())
            )
        )

        viewModel.getLayoutData()

        onGetLayoutList_thenReturnNull()

        viewModel.setProductAddToCartQuantity(MiniCartSimplifiedData())

        viewModel.atcQuantity
            .verifyValueEquals(null)
    }

    @Test
    fun `when setProductAddToCartQuantity throw exception should do nothing`() {
        viewModel.setProductAddToCartQuantity(MiniCartSimplifiedData())

        viewModel.atcQuantity
            .verifyValueEquals(null)
    }

    @Test
    fun `given mini cart item is null when onClickAddToCart should set miniCartAdd success`() {
        val productId = "1"
        val quantity = 5
        val shopId = "5"
        val type = ""

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(
                id = productId,
                stock = 0,
                shop = RepurchaseProduct.Shop(id = shopId)
            ))
        )
        val addToCartResponse = AddToCartDataModel()

        onGetRepurchaseProductList_thenReturn(productListResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getLayoutData()
        viewModel.onClickAddToCart(productId, quantity, type, shopId)

        verifyAddToCartUseCaseCalled()

        viewModel.miniCartAdd
            .verifySuccessEquals(Success(AddToCartDataModel()))

        viewModel.updateToolbarNotification
            .verifyValueEquals(true)
    }

    @Test
    fun `given mini cart item is null when addToCart error should set miniCartAdd fail`() {
        val productId = "1"
        val quantity = 5
        val shopId = "5"
        val type = ""

        val addToCartError = NullPointerException()
        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(
                id = productId,
                stock = 0,
                shop = RepurchaseProduct.Shop(id = shopId)
            ))
        )

        onGetRepurchaseProductList_thenReturn(productListResponse)
        onAddToCart_thenReturn(addToCartError)

        viewModel.getLayoutData()
        viewModel.onClickAddToCart(productId, quantity, type, shopId)

        verifyAddToCartUseCaseCalled()

        viewModel.miniCartAdd
            .verifyErrorEquals(Fail(addToCartError))
    }

    @Test
    fun `given mini cart item is null when onClickAddToCart should hit add to cart tracker`() {
        val productId1 = "1"
        val productId2 = "2"
        val quantity = 5
        val shopId = "5"
        val type = PRODUCT_REPURCHASE

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(
                id = productId1,
                stock = 0,
                shop = RepurchaseProduct.Shop(id = shopId)
            ), RepurchaseProduct(
                id = productId2,
                stock = 0,
                shop = RepurchaseProduct.Shop(id = shopId)
            ))
        )
        val addToCartResponse = AddToCartDataModel()

        onGetRepurchaseProductList_thenReturn(productListResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getLayoutData()
        viewModel.onClickAddToCart(productId2, quantity, type, shopId)

        verifyAddToCartUseCaseCalled()

        viewModel.repurchaseAddToCartTracker
            .verifyValueEquals(
                RepurchaseAddToCartTracker(
                    cartId = "",
                    quantity = quantity,
                    data =  RepurchaseProductUiModel(
                        shopId = "5",
                        category = "",
                        categoryId = "",
                        parentId = "",
                        productCardModel = TokoNowProductCardViewUiModel(
                            productId = "2",
                            isSimilarProductShown = true,
                            isWishlistShown = true,
                            needToShowQuantityEditor = true
                        ),
                        position = 2
                    )
                )
            )
    }

    @Test
    fun `given mini cart item is null when onClickAddToCart should not hit add to cart tracker`() {
        val productId1 = "1"
        val productId2 = "2"
        val productId3 = "3"
        val quantity = 5
        val shopId = "5"
        val type = PRODUCT_REPURCHASE

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(
                id = productId1,
                stock = 0,
                shop = RepurchaseProduct.Shop(id = shopId)
            ), RepurchaseProduct(
                id = productId2,
                stock = 0,
                shop = RepurchaseProduct.Shop(id = shopId)
            ))
        )
        val addToCartResponse = AddToCartDataModel()

        onGetRepurchaseProductList_thenReturn(productListResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getLayoutData()
        viewModel.onClickAddToCart(productId3, quantity, type, shopId)

        verifyAddToCartUseCaseCalled()

        viewModel.repurchaseAddToCartTracker
            .verifyValueEquals(null)
    }


    @Test
    fun `given quantity is 0 when onClickAddToCart should set miniCartRemove success`() {
        val warehouseId = "1"
        val productId = "1"
        val quantity = 0
        val shopId = "5"
        val type = ""

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(
                id = productId,
                stock = 0,
                shop = RepurchaseProduct.Shop(id = shopId)
            ))
        )

        val miniCartItems = mapOf(MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(productId = productId, quantity = 1))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetUserLoggedIn_thenReturn(isLoggedIn = true)
        onGetRepurchaseProductList_thenReturn(productListResponse)
        onRemoveItemCart_thenReturn(RemoveFromCartData())
        onGetMiniCart_thenReturn(miniCartResponse)

        viewModel.getLayoutData()
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.onClickAddToCart(productId, quantity, type, shopId)

        verifyGetMiniCartUseCaseCalled()
        verifyDeleteCartUseCaseCalled()

        viewModel.miniCartRemove
            .verifySuccessEquals(Success(Pair(productId, "")))

        viewModel.updateToolbarNotification
            .verifyValueEquals(true)
    }

    @Test
    fun `given quantity is 0 when remove cart item error should set miniCartRemove fail`() {
        val warehouseId = "1"
        val productId = "1"
        val quantity = 0
        val shopId = "5"
        val type = ""

        val removeItemCartError = NullPointerException()
        val miniCartItems = mapOf(MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(productId = productId, quantity = 1))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(
                id = productId,
                stock = 0,
                shop = RepurchaseProduct.Shop(id = shopId)
            ))
        )

        onGetUserLoggedIn_thenReturn(isLoggedIn = true)
        onGetRepurchaseProductList_thenReturn(productListResponse)
        onRemoveItemCart_thenReturn(removeItemCartError)
        onGetMiniCart_thenReturn(miniCartResponse)

        viewModel.getLayoutData()
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.onClickAddToCart(productId, quantity, type, shopId)

        verifyGetMiniCartUseCaseCalled()
        verifyDeleteCartUseCaseCalled()

        viewModel.miniCartRemove
            .verifyErrorEquals(Fail(removeItemCartError))
    }

    @Test
    fun `given mini cart item is NOT null when onClickAddToCart should set miniCartUpdate success`() {
        val warehouseId = "1"
        val productId = "1"
        val quantity = 5
        val shopId = "5"
        val type = ""

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(
                id = productId,
                stock = 0,
                shop = RepurchaseProduct.Shop(id = shopId)
            ))
        )

        val updateCartResponse = UpdateCartV2Data()
        val miniCartItems = mapOf(MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(productId = productId, quantity = 1))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetUserLoggedIn_thenReturn(isLoggedIn = true)
        onGetRepurchaseProductList_thenReturn(productListResponse)
        onUpdateItemCart_thenReturn(updateCartResponse)
        onGetMiniCart_thenReturn(miniCartResponse)

        viewModel.getLayoutData()
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.onClickAddToCart(productId, quantity, type, shopId)

        verifyGetMiniCartUseCaseCalled()
        verifyUpdateCartUseCaseCalled()

        viewModel.miniCartUpdate
            .verifySuccessEquals(Success(updateCartResponse))

        viewModel.updateToolbarNotification
            .verifyValueEquals(true)
    }

    @Test
    fun `given mini cart item is NOT null when update cart item error should set miniCartUpdate fail`() {
        val warehouseId = "1"
        val productId = "1"
        val quantity = 5
        val shopId = "5"
        val type = ""

        val updateCartError = NullPointerException()
        val miniCartItems = mapOf(MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(productId = productId, quantity = 1))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(
                id = productId,
                stock = 0,
                shop = RepurchaseProduct.Shop(id = shopId)
            ))
        )

        onGetUserLoggedIn_thenReturn(isLoggedIn = true)
        onGetRepurchaseProductList_thenReturn(productListResponse)
        onUpdateItemCart_thenReturn(updateCartError)
        onGetMiniCart_thenReturn(miniCartResponse)

        viewModel.getLayoutData()
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.onClickAddToCart(productId, quantity, type, shopId)

        verifyGetMiniCartUseCaseCalled()
        verifyUpdateCartUseCaseCalled()

        viewModel.miniCartUpdate
            .verifyErrorEquals(Fail(updateCartError))
    }

    @Test
    fun `given product not found in miniCartItems when onClickAddToCart should do nothing`() {
        val warehouseId = "1"
        val productId = "1"
        val quantity = 0
        val shopId = "5"
        val type = ""

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(
                id = productId,
                stock = 0,
                shop = RepurchaseProduct.Shop(id = shopId)
            ))
        )

        val addToCartResponse = AddToCartDataModel()
        val miniCartItems = mapOf(MiniCartItemKey("5") to MiniCartItem.MiniCartItemProduct(productId = "5", quantity = 1))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetUserLoggedIn_thenReturn(isLoggedIn = true)
        onGetRepurchaseProductList_thenReturn(productListResponse)
        onAddToCart_thenReturn(addToCartResponse)
        onGetMiniCart_thenReturn(miniCartResponse)

        viewModel.getLayoutData()
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.onClickAddToCart(productId, quantity, type, shopId)

        verifyGetMiniCartUseCaseCalled()

        viewModel.miniCartAdd.verifyValueEquals(null)
    }

    @Test
    fun `given product list response NOT empty when applyCategoryFilter should set getLayout success`() {
        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(
                id = "1",
                stock = 0,
                shop = RepurchaseProduct.Shop(id = "1001")
            ))
        )

        onGetRepurchaseProductList_thenReturn(productListResponse)

        viewModel.applyCategoryFilter(SelectedSortFilter())

        val layoutList = listOf(createRepurchaseProductUiModel(
            shopId = "1001",
            productCard = TokoNowProductCardViewUiModel(
                productId = "1",
                isSimilarProductShown = true,
                isWishlistShown = true,
                needToShowQuantityEditor = true
            ),
            position = 1
        ))

        val expectedResult = Success(
            RepurchaseLayoutUiModel(
                layoutList = layoutList,
                state = TokoNowLayoutState.LOADED
            )
        )

        verifyGetProductUseCaseCalled()

        viewModel.getLayout
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given product list response is EMPTY when applyCategoryFilter should show empty state`() {
        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf()
        )

        onGetRepurchaseProductList_thenReturn(productListResponse)

        viewModel.applyCategoryFilter(SelectedSortFilter())

        val emptyState = RepurchaseEmptyStateNoHistoryUiModel(
            R.string.tokopedianow_repurchase_empty_state_no_history_title_filter,
            R.string.tokopedianow_repurchase_empty_state_no_history_desc_filter
        )

        verifyGetProductUseCaseCalled()
        verifyLayoutListContains(emptyState)
    }

    @Test
    fun `when applyCategoryFilter throw exception should do nothing`() {
        onGetLayoutList_thenReturnNull()

        viewModel.applyCategoryFilter(SelectedSortFilter())

        viewModel.getLayout
            .verifyValueEquals(null)
    }

    @Test
    fun `given product list response NOT empty when applyDateFilter should set getLayout success`() {
        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(
                id = "1",
                stock = 0,
                shop = RepurchaseProduct.Shop(id = "1001")
            ))
        )

        onGetRepurchaseProductList_thenReturn(productListResponse)

        viewModel.applyDateFilter(SelectedDateFilter())

        val layoutList = listOf(createRepurchaseProductUiModel(
            shopId = "1001",
            productCard = TokoNowProductCardViewUiModel(
                productId = "1",
                isSimilarProductShown = true,
                isWishlistShown = true,
                needToShowQuantityEditor = true
            ),
            position = 1
        ))

        val expectedResult = Success(
            RepurchaseLayoutUiModel(
                layoutList = layoutList,
                state = TokoNowLayoutState.LOADED
            )
        )

        verifyGetProductUseCaseCalled()

        viewModel.getLayout
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given product list response is EMPTY when applyDateFilter should show empty state`() {
        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf()
        )

        onGetRepurchaseProductList_thenReturn(productListResponse)

        viewModel.applyDateFilter(SelectedDateFilter())

        val emptyState = RepurchaseEmptyStateNoHistoryUiModel(
            R.string.tokopedianow_repurchase_empty_state_no_history_title_filter,
            R.string.tokopedianow_repurchase_empty_state_no_history_desc_filter
        )

        verifyGetProductUseCaseCalled()
        verifyLayoutListContains(emptyState)
    }

    @Test
    fun `when applyDateFilter throw exception should do nothing`() {
        onGetLayoutList_thenReturnNull()

        viewModel.applyDateFilter(SelectedDateFilter())

        viewModel.getLayout
            .verifyValueEquals(null)
    }

    @Test
    fun `given product list response NOT empty when applySortFilter should set getLayout success`() {
        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(
                id = "1",
                stock = 0,
                shop = RepurchaseProduct.Shop(id = "1001")
            ))
        )

        onGetRepurchaseProductList_thenReturn(productListResponse)

        viewModel.applySortFilter(0)

        val layoutList = listOf(createRepurchaseProductUiModel(
            shopId = "1001",
            productCard = TokoNowProductCardViewUiModel(
                productId = "1",
                isSimilarProductShown = true,
                isWishlistShown = true,
                needToShowQuantityEditor = true
            ),
            position = 1
        ))

        val expectedResult = Success(
            RepurchaseLayoutUiModel(
                layoutList = layoutList,
                state = TokoNowLayoutState.LOADED
            )
        )

        verifyGetProductUseCaseCalled()

        viewModel.getLayout
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given product list response is EMPTY when applySortFilter should show empty state`() {
        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf()
        )

        onGetRepurchaseProductList_thenReturn(productListResponse)

        viewModel.applySortFilter(1)

        val emptyState = RepurchaseEmptyStateNoHistoryUiModel(
            R.string.tokopedianow_repurchase_empty_state_no_history_title_filter,
            R.string.tokopedianow_repurchase_empty_state_no_history_desc_filter
        )

        verifyGetProductUseCaseCalled()
        verifyLayoutListContains(emptyState)
    }

    @Test
    fun `when applySortFilter throw exception should do nothing`() {
        onGetLayoutList_thenReturnNull()

        viewModel.applySortFilter(0)

        viewModel.getLayout
            .verifyValueEquals(null)
    }

    @Test
    fun `when getAddToCartQuantity success should set atcQuantity value success`() {
        val miniCartResponse = MiniCartSimplifiedData()
        val localCacheModel = LocalCacheModel(
            warehouse_id = "1",
            shop_id = "1001"
        )

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(
                id = "111",
                stock = 0,
                shop = RepurchaseProduct.Shop(id = "222")
            ))
        )


        onGetMiniCart_thenReturn(miniCartResponse)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)
        onGetRepurchaseProductList_thenReturn(productListResponse)

        viewModel.getLayoutData()
        viewModel.setLocalCacheModel(localCacheModel)
        viewModel.getAddToCartQuantity()

        val expectedResult = Success(
            RepurchaseLayoutUiModel(
                layoutList = productListResponse.products.mapToProductListUiModel(),
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetMiniCartUseCaseCalled()

        viewModel.atcQuantity
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when getAddToCartQuantity error should set atcQuantity value FAIL`() {
        val error = NullPointerException()
        val localCacheModel = LocalCacheModel(
            warehouse_id = "1",
            shop_id = "1001"
        )

        onGetMiniCart_throwException(error)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.setLocalCacheModel(localCacheModel)
        viewModel.getAddToCartQuantity()

        verifyGetMiniCartUseCaseCalled()

        viewModel.atcQuantity
            .verifyErrorEquals(Fail(error))
    }

    @Test
    fun `when getAddToCartQuantity throw exception should set atcQuantity value FAIL`() {
        val error = NullPointerException()
        val localCacheModel = LocalCacheModel(
            warehouse_id = "1",
            shop_id = "1001"
        )

        onGetMiniCart_throwException(error)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.setLocalCacheModel(localCacheModel)
        viewModel.getAddToCartQuantity()

        verifyGetMiniCartUseCaseCalled()

        viewModel.atcQuantity
            .verifyErrorEquals(Fail(error))
    }

    @Test
    fun `given shopId empty when getAddToCartQuantity should NOT call get mini cart use case`() {
        val shopId = ""
        val localCacheModel = LocalCacheModel(
            shop_id = shopId,
            warehouse_id = "100"
        )
        val miniCartResponse = MiniCartSimplifiedData()

        onGetMiniCart_thenReturn(miniCartResponse)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.setLocalCacheModel(localCacheModel)
        viewModel.getAddToCartQuantity()

        verifyGetMiniCartUseCaseNotCalled()
    }

    @Test
    fun `given warehouseId empty when getAddToCartQuantity should NOT call get mini cart use case`() {
        val warehouseId = ""
        val localCacheModel = LocalCacheModel(
            shop_id = "1",
            warehouse_id = warehouseId
        )
        val miniCartResponse = MiniCartSimplifiedData()

        onGetMiniCart_thenReturn(miniCartResponse)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.setLocalCacheModel(localCacheModel)
        viewModel.getAddToCartQuantity()

        verifyGetMiniCartUseCaseNotCalled()
    }

    @Test
    fun `given user not loggedIn when getAddToCartQuantity should NOT call get mini cart use case`() {
        val isLoggedIn = false
        val localCacheModel = LocalCacheModel(
            warehouse_id = "1",
            shop_id = "1001"
        )
        val response = MiniCartSimplifiedData()

        onGetMiniCart_thenReturn(response)
        onGetUserLoggedIn_thenReturn(isLoggedIn)

        viewModel.setLocalCacheModel(localCacheModel)
        viewModel.getAddToCartQuantity()

        verifyGetMiniCartUseCaseNotCalled()
    }

    @Test
    fun `given localCacheModel not set when getAddToCartQuantity should NOT call get mini cart use case`() {
        val isLoggedIn = false
        val response = MiniCartSimplifiedData()

        onGetMiniCart_thenReturn(response)
        onGetUserLoggedIn_thenReturn(isLoggedIn)

        viewModel.getAddToCartQuantity()

        verifyGetMiniCartUseCaseNotCalled()
    }

    @Test
    fun `given localCacheModel when getLayoutData should call use case with warehouseId from localCacheModel`() {
        val warehouseId = "200"
        val localCacheModel = LocalCacheModel(warehouse_id = warehouseId)

        viewModel.setLocalCacheModel(localCacheModel)
        viewModel.getLayoutData()

        val expectedParam = GetRepurchaseProductListParam(
            warehouseID = warehouseId,
            sort = 2, // FREQUENTLY_BOUGHT
            dateStart = "",
            dateEnd = "",
            totalScan = 0,
            catIds = null,
            page = 1
        )

        verifyGetProductUseCaseCalled(expectedParam)
    }

    @Test
    fun `given clearSelectedFilters when getLayoutData should call use case with default filter params`() {
        viewModel.clearSelectedFilters()
        viewModel.getLayoutData()

        val expectedParam = GetRepurchaseProductListParam(
            warehouseID = "",
            sort = 2, // FREQUENTLY_BOUGHT
            dateStart = "",
            dateEnd = "",
            totalScan = 0,
            catIds = null,
            page = 1
        )

        verifyGetProductUseCaseCalled(expectedParam)
    }

    @Test
    fun `when clearSelectedFilters should set all filter to default value`() {
        viewModel.clearSelectedFilters()

        val expectedSortFilter = 2 // FREQUENTLY_BOUGHT
        val expectedDateFilter = SelectedDateFilter()
        val expectedCategoryFilter = null

        val actualSortFilter = viewModel.getSelectedSortFilter()
        val actualDateFilter = viewModel.getSelectedDateFilter()
        val actualCategoryFilter = viewModel.getSelectedCategoryFilter()

        assertEquals(expectedSortFilter, actualSortFilter)
        assertEquals(expectedDateFilter, actualDateFilter)
        assertEquals(expectedCategoryFilter, actualCategoryFilter)
    }

    @Test
    fun `given product list is EMPTY when getLayoutData should add empty state no result`() {
        val productList = emptyList<RepurchaseProduct>()

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = productList
        )

        onGetRepurchaseProductList_thenReturn(productListResponse)

        viewModel.getLayoutData()

        val emptyState = TokoNowEmptyStateNoResultUiModel()

        verifyGetProductUseCaseCalled()
        verifyLayoutListContains(emptyState)
    }

    @Test
    fun `given scrolledToLastItem and hasNext true when onScrollProductList should set loadMore success`() {
        val hasNext = true
        val index = IntArray(1) { 0 }

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = hasNext,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(id = "1"))
        )

        onGetRepurchaseProductList_thenReturn(productListResponse)

        viewModel.getLayoutData()

        val loadMoreResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = hasNext,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(id = "2"))
        )

        onGetRepurchaseProductList_thenReturn(loadMoreResponse)

        viewModel.onScrollProductList(index, 1)

        val layoutList = listOf(
            createRepurchaseProductUiModel(
                productCard = TokoNowProductCardViewUiModel(
                    productId = "1",
                    isSimilarProductShown = true,
                    isWishlistShown = true,
                    needToShowQuantityEditor = true
                ),
                position = 1
            ),
            createRepurchaseProductUiModel(
                productCard = TokoNowProductCardViewUiModel(
                    productId = "2",
                    isSimilarProductShown = true,
                    isWishlistShown = true,
                    needToShowQuantityEditor = true
                ),
                position = 1
            )
        )

        val expectedResult = Success(RepurchaseLayoutUiModel(
            layoutList = layoutList,
            state = TokoNowLayoutState.LOAD_MORE
        ))

        verifyGetProductUseCaseCalled(times = 2)

        viewModel.loadMore
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given miniCartData when onScrollProductList should update product quantity`() {
        val hasNext = true
        val index = IntArray(1) { 0 }
        val quantity = 2

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = hasNext,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(id = "1"))
        )

        val miniCartItems = mapOf(MiniCartItemKey("2") to MiniCartItem.MiniCartItemProduct(
            productId = "2",
            productParentId = "0",
            quantity = quantity
        ))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetMiniCart_thenReturn(miniCartResponse)
        onGetRepurchaseProductList_thenReturn(productListResponse)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.getLayoutData()
        viewModel.getMiniCart(listOf("1"), warehouseId = "2")

        val loadMoreResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = hasNext,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(
                id = "2",
                stock = 10,
                minOrder = 1,
                maxOrder = 10
            ))
        )

        onGetRepurchaseProductList_thenReturn(loadMoreResponse)

        viewModel.onScrollProductList(index, 1)

        val layoutList = listOf(
            createRepurchaseProductUiModel(
                productCard = TokoNowProductCardViewUiModel(
                    productId = "1",
                    isSimilarProductShown = true,
                    isWishlistShown = true,
                    needToShowQuantityEditor = true
                ),
                position = 1
            ),
            createRepurchaseProductUiModel(
                productCard = TokoNowProductCardViewUiModel(
                    productId = "2",
                    orderQuantity = quantity,
                    minOrder = 1,
                    maxOrder = 10,
                    availableStock = 10,
                    needToShowQuantityEditor = true,
                    isSimilarProductShown = true,
                    isWishlistShown = true
                ),
                position = 1
            )
        )

        val expectedResult = Success(RepurchaseLayoutUiModel(
            layoutList = layoutList,
            state = TokoNowLayoutState.LOAD_MORE
        ))

        verifyGetProductUseCaseCalled(times = 2)

        viewModel.loadMore
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given loadMore error when onScrollProductList should NOT set loadMore value`() {
        val hasNext = true
        val index = IntArray(1) { 0 }
        val loadMoreError = NullPointerException()

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = hasNext,
                totalScan = 1,
            ),
            products = listOf(RepurchaseProduct(id = "1"))
        )

        onGetRepurchaseProductList_thenReturn(productListResponse)

        viewModel.getLayoutData()

        onGetRepurchaseProductList_thenReturn(loadMoreError)

        viewModel.onScrollProductList(index, 1)

        verifyGetProductUseCaseCalled(times = 2)

        viewModel.loadMore
            .verifyValueEquals(null)
    }

    @Test
    fun `given scrolledToLastItem false when onScrollProductList should call getProductList use case ONCE`() {
        val hasNext = true

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = hasNext,
                totalScan = 1,
            ),
            products = emptyList()
        )

        onGetRepurchaseProductList_thenReturn(productListResponse)

        viewModel.getLayoutData()
        viewModel.onScrollProductList(IntArray(0), 3)

        verifyGetProductUseCaseCalled(times = 1)
    }

    @Test
    fun `given hasNext false when onScrollProductList should call getProductList use case ONCE`() {
        val hasNext = false
        val index = IntArray(1) { 0 }

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = hasNext,
                totalScan = 1,
            ),
            products = emptyList()
        )

        onGetRepurchaseProductList_thenReturn(productListResponse)

        viewModel.getLayoutData()
        viewModel.onScrollProductList(index, 1)

        verifyGetProductUseCaseCalled(times = 1)
    }

    @Test
    fun `given index null when onScrollProductList should call getProductList use case ONCE`() {
        val hasNext = true
        val index = null

        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = hasNext,
                totalScan = 1,
            ),
            products = emptyList()
        )

        onGetRepurchaseProductList_thenReturn(productListResponse)

        viewModel.getLayoutData()
        viewModel.onScrollProductList(index, 1)

        verifyGetProductUseCaseCalled(times = 1)
    }

    @Test
    fun `given hasNext null when onScrollProductList should NOT call getProductList use case`() {
        val index = IntArray(1) { 0 }

        viewModel.onScrollProductList(index, 1)

        verifyGetProductUseCaseNotCalled()
    }

    @Test
    fun `given productListMeta null when loadMoreProduct should set loadMore success`()  {
        val productListResponse = GetRepurchaseProductListResponse(
            meta = GetRepurchaseProductMetaResponse(
                page = 1,
                hasNext = false,
                totalScan = 1,
            ),
            products = listOf()
        )

        onGetRepurchaseProductList_thenReturn(productListResponse)

        callPrivateLoadMoreProduct()

        val expectedResult = Success(RepurchaseLayoutUiModel(
            layoutList = emptyList(),
            state = TokoNowLayoutState.LOAD_MORE
        ))

        verifyGetProductUseCaseCalled()

        viewModel.loadMore
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given current serviceType 15m when switchService success should switch service to 2h`() {
        val currentServiceType = "15m"

        val localCacheModel = LocalCacheModel(
            service_type = currentServiceType
        )

        val userPreferenceData = SetUserPreference.SetUserPreferenceData(
            shopId = "1",
            warehouseId = "2",
            serviceType = "2h",
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "2",
                    serviceType = "2h"
                )
            )
        )

        onSetUserPreference_thenReturn(userPreferenceData)

        viewModel.setLocalCacheModel(localCacheModel)
        viewModel.switchService()

        val expectedResult = Success(
            SetUserPreference.SetUserPreferenceData(
                shopId = "1",
                warehouseId = "2",
                serviceType = "2h",
                warehouses = listOf(
                    WarehouseData(
                        warehouseId = "2",
                        serviceType = "2h"
                    )
                )
            )
        )

        verifySetUserPreferenceUseCaseCalled(
            localCacheModel = localCacheModel,
            serviceType = "2h"
        )

        viewModel.setUserPreference
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given current serviceType OOC when switchService success should switch service to 2h`() {
        val currentServiceType = "ooc"

        val localCacheModel = LocalCacheModel(
            service_type = currentServiceType
        )

        val userPreferenceData = SetUserPreference.SetUserPreferenceData(
            shopId = "1",
            warehouseId = "2",
            serviceType = "2h",
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "2",
                    serviceType = "2h"
                )
            )
        )

        onSetUserPreference_thenReturn(userPreferenceData)

        viewModel.setLocalCacheModel(localCacheModel)
        viewModel.switchService()

        val expectedResult = Success(
            SetUserPreference.SetUserPreferenceData(
                shopId = "1",
                warehouseId = "2",
                serviceType = "2h",
                warehouses = listOf(
                    WarehouseData(
                        warehouseId = "2",
                        serviceType = "2h"
                    )
                )
            )
        )

        verifySetUserPreferenceUseCaseCalled(
            localCacheModel = localCacheModel,
            serviceType = "2h"
        )

        viewModel.setUserPreference
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given current serviceType 2h when switchService success should switch service to 15m`() {
        val currentServiceType = "2h"

        val localCacheModel = LocalCacheModel(
            service_type = currentServiceType
        )

        val userPreferenceData = SetUserPreference.SetUserPreferenceData(
            shopId = "1",
            warehouseId = "2",
            serviceType = "15m",
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "2",
                    serviceType = "15m"
                )
            )
        )

        onSetUserPreference_thenReturn(userPreferenceData)

        viewModel.setLocalCacheModel(localCacheModel)
        viewModel.switchService()

        val expectedResult = Success(
            SetUserPreference.SetUserPreferenceData(
                shopId = "1",
                warehouseId = "2",
                serviceType = "15m",
                warehouses = listOf(
                    WarehouseData(
                        warehouseId = "2",
                        serviceType = "15m"
                    )
                )
            )
        )

        verifySetUserPreferenceUseCaseCalled(
            localCacheModel = localCacheModel,
            serviceType = "15m"
        )

        viewModel.setUserPreference
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given localCacheModel null when switchService error should not call set user preference use case`() {
        viewModel.switchService()

        verifySetUserPreferenceUseCaseNotCalled()

        viewModel.setUserPreference
            .verifyValueEquals(null)
    }

    @Test
    fun `when switchService error should set live data value fail`() {
        val localCacheModel = LocalCacheModel(
            service_type = "2h"
        )
        val error = NullPointerException()

        onSetUserPreference_thenReturn(error)

        viewModel.setLocalCacheModel(localCacheModel)
        viewModel.switchService()

        val expectedResult = Fail(NullPointerException())

        verifySetUserPreferenceUseCaseCalled(
            localCacheModel = localCacheModel,
            serviceType = "15m"
        )

        viewModel.setUserPreference
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `when showing empty state no result should show product recommendation as well`() {
        viewModel.showEmptyState(EMPTY_STATE_NO_RESULT)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createEmptyStateLayout(EMPTY_STATE_NO_RESULT),
            state = TokoNowLayoutState.EMPTY
        )

        verifyProductRecommendationWidgetLayoutSuccess(layout)
    }

    @Test
    fun `when removing product recommendation widget layout should run and give the success result`() {
        `when showing empty state no result should show product recommendation as well`()

        viewModel.removeProductRecommendationWidget()

        verifyProductRecommendationWidgetRemovedSuccess()
    }

    @Test
    fun `when updating wishlist status success and the status turns out as we expected`() {
        /**
         * create test data
         */
        val productId = "1000"
        val fieldName = "layoutList"
        val fieldValue = mutableListOf<Visitable<*>>(
            createRepurchaseProductUiModel(
                position = 1,
                productCard = TokoNowProductCardViewUiModel(
                    productId = productId,
                    hasBeenWishlist = false,
                    needToShowQuantityEditor = true
                )
            )
        )
        val expectedValue = createRepurchaseProductUiModel(
            position = 1,
            productCard = TokoNowProductCardViewUiModel(
                productId = productId,
                hasBeenWishlist = true,
                needToShowQuantityEditor = true
            )
        )

        /**
         * mock private field from viewModel
         */
        viewModel.mockPrivateField(
            name = fieldName,
            value = fieldValue
        )

        /**
         * update wishlist status
         */
        viewModel.updateWishlistStatus(
            productId = productId,
            hasBeenWishlist = true
        )

        /**
         * verify the data test
         */
        val actualValue = (viewModel.getLayout.value as Success).data.layoutList.first()
        assertEquals(expectedValue, actualValue)
    }
}
