package com.tokopedia.tokopedianow.repurchase.presentation.viewmodel

import com.tokopedia.abstraction.common.data.model.response.Header
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.data.*
import com.tokopedia.tokopedianow.data.createChooseAddressLayout
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAddToCartTracker
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_HISTORY_FILTER
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_HISTORY_SEARCH
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_RESULT
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_OOC
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.ERROR_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.PRODUCT_REPURCHASE
import com.tokopedia.tokopedianow.repurchase.domain.model.TokoNowRepurchasePageResponse.*
import com.tokopedia.tokopedianow.repurchase.domain.param.GetRepurchaseProductListParam
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseLayoutUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Assert.assertEquals
import org.junit.Test

class TokoNowRepurchaseViewModelTest: TokoNowRepurchaseViewModelTestFixture() {
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
        onGetCategoryList_thenReturn(CategoryListResponse(
            header = Header(),
            data = listOf(
                CategoryResponse(
                    id = "3",
                    name = "Category 3",
                    url = "tokopedia://",
                    appLinks = "tokoepdia://",
                    imageUrl = "tokopedia://",
                    parentId = "5",
                    childList = listOf()
                )
            )
        ))

        privateLocalCacheModel.set(viewModel, LocalCacheModel(warehouse_id = "1"))

        viewModel.showEmptyState(EMPTY_STATE_NO_RESULT)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createCategoryGridLayout(),
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
            isStockEmpty = true,
            productCard = ProductCardModel(
                isOutOfStock = true,
                hasSimilarProductButton = true
            )
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
    fun `when getMiniCart error should set miniCart value FAIL`() {
        val error = NullPointerException()

        onGetMiniCart_thenReturn(error)
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
    fun `when setProductAddToCartQuantity throw exception should do nothing`() {
        onGetLayoutList_thenReturnNull()

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
                        id="2",
                        shopId = "5",
                        category = "",
                        categoryId = "",
                        isStockEmpty = true,
                        parentId = "",
                        productCard = ProductCardModel(
                            hasSimilarProductButton = true,
                            isOutOfStock = true
                        )
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

        val miniCartItems = listOf(MiniCartItem(productId = productId, quantity = 1))
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
    }

    @Test
    fun `given quantity is 0 when remove cart item error should set miniCartRemove fail`() {
        val warehouseId = "1"
        val productId = "1"
        val quantity = 0
        val shopId = "5"
        val type = ""

        val removeItemCartError = NullPointerException()
        val miniCartItems = listOf(MiniCartItem(productId = productId, quantity = 1))
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
        val miniCartItems = listOf(MiniCartItem(productId = productId, quantity = 1))
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
    }

    @Test
    fun `given mini cart item is NOT null when update cart item error should set miniCartUpdate fail`() {
        val warehouseId = "1"
        val productId = "1"
        val quantity = 5
        val shopId = "5"
        val type = ""

        val updateCartError = NullPointerException()
        val miniCartItems = listOf(MiniCartItem(productId = productId, quantity = 1))
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
    fun `given product not found in miniCartItems when onClickAddToCart should add product to cart`() {
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
        val miniCartItems = listOf(MiniCartItem(productId = "5", quantity = 1))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetUserLoggedIn_thenReturn(isLoggedIn = true)
        onGetRepurchaseProductList_thenReturn(productListResponse)
        onAddToCart_thenReturn(addToCartResponse)
        onGetMiniCart_thenReturn(miniCartResponse)

        viewModel.getLayoutData()
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.onClickAddToCart(productId, quantity, type, shopId)

        verifyGetMiniCartUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.miniCartAdd
            .verifySuccessEquals(Success(addToCartResponse))
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
            id = "1",
            isStockEmpty = true,
            shopId = "1001",
            productCard = ProductCardModel(
                isOutOfStock = true,
                hasSimilarProductButton = true
            )
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
            id = "1",
            isStockEmpty = true,
            shopId = "1001",
            productCard = ProductCardModel(
                isOutOfStock = true,
                hasSimilarProductButton = true
            )
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
            id = "1",
            isStockEmpty = true,
            shopId = "1001",
            productCard = ProductCardModel(
                isOutOfStock = true,
                hasSimilarProductButton = true
            )
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

        onGetMiniCart_thenReturn(miniCartResponse)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.setLocalCacheModel(localCacheModel)
        viewModel.getAddToCartQuantity()

        val expectedResult = Success(
            RepurchaseLayoutUiModel(
                layoutList = emptyList(),
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

        onGetMiniCart_thenReturn(error)
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
                id = "1",
                isStockEmpty = true,
                productCard = ProductCardModel(
                    isOutOfStock = true,
                    hasSimilarProductButton = true
                )
            ),
            createRepurchaseProductUiModel(
                id = "2",
                isStockEmpty = true,
                productCard = ProductCardModel(
                    isOutOfStock = true,
                    hasSimilarProductButton = true
                )
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

        val miniCartItems = listOf(MiniCartItem(
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
                minOrder = 0,
                maxOrder = 10
            ))
        )

        onGetRepurchaseProductList_thenReturn(loadMoreResponse)

        viewModel.onScrollProductList(index, 1)

        val layoutList = listOf(
            createRepurchaseProductUiModel(
                id = "1",
                isStockEmpty = true,
                productCard = ProductCardModel(
                    isOutOfStock = true,
                    hasSimilarProductButton = true
                )
            ),
            createRepurchaseProductUiModel(
                id = "2",
                productCard = ProductCardModel(
                    nonVariant = ProductCardModel.NonVariant(
                        quantity = quantity,
                        minQuantity = 0,
                        maxQuantity = 10
                    )
                )
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
}