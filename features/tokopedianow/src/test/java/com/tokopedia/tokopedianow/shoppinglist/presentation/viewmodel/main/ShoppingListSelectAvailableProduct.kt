package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.main

import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.areSelected
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.countSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.modifyProduct
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.sumPriceSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addLoadingState
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.modifyExpandCollapseProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.modifyTopCheckAll
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.updateProductSelections
import com.tokopedia.tokopedianow.shoppinglist.domain.model.SaveShoppingListStateActionParam
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.BottomBulkAtcModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListViewModel.Companion.DEBOUNCE_TIMES_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Test

/*
 still need to improve don't review this
 */
@ExperimentalCoroutinesApi
class ShoppingListSelectAvailableProduct: TokoNowShoppingListViewModelFixture() {
    @Test
    fun `When selecting all available products and successfully saving shopping list state then the result should change isSelected of all available products to be true`() = runTest {
        loadLayoutWithExpandCollapseWidget()

        val isSelected = true

        stubSaveShoppingListState(
            actionList = availableProducts.map { SaveShoppingListStateActionParam(productId = it.id, isSelected = it.isSelected) }
        )

        viewModel.selectAllAvailableProducts(
            state = ShoppingListProductState.EXPAND,
            isSelected = isSelected
        )

        advanceTimeBy(DEBOUNCE_TIMES_SHOPPING_LIST)

        availableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        filteredAvailableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        mutableLayout
            .modifyTopCheckAll(
                isSelected = isSelected
            )
            .modifyExpandCollapseProducts(
                state = ShoppingListProductState.EXPAND,
                productLayoutType = ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST,
                products = filteredAvailableProducts
            )

        // verify all products are selected
        verifyIsTrue(
            expectedResult = !availableProducts.any { !it.isSelected }
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.countSelectedItems(),
                    price = filteredAvailableProducts.sumPriceSelectedItems()
                )
            )

        viewModel
            .isTopCheckAllSelected
            .verifyValue(
                true
            )
    }

    @Test
    fun `When selecting all available products and successfully saving shopping list state then the result should change isSelected of all available products to be false`() = runTest {
        loadLayoutWithExpandCollapseWidget()

        val isSelected = false

        stubSaveShoppingListState(
            actionList = availableProducts.map { SaveShoppingListStateActionParam(productId = it.id, isSelected = it.isSelected) }
        )

        viewModel.selectAllAvailableProducts(
            state = ShoppingListProductState.EXPAND,
            isSelected = isSelected
        )

        advanceTimeBy(DEBOUNCE_TIMES_SHOPPING_LIST)

        availableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        filteredAvailableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        mutableLayout
            .modifyTopCheckAll(
                isSelected = isSelected
            )
            .modifyExpandCollapseProducts(
                state = ShoppingListProductState.EXPAND,
                productLayoutType = ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST,
                products = filteredAvailableProducts
            )

        // verify all products are selected
        verifyIsTrue(
            expectedResult = availableProducts.any { !it.isSelected }
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.countSelectedItems(),
                    price = filteredAvailableProducts.sumPriceSelectedItems()
                )
            )

        viewModel
            .isTopCheckAllSelected
            .verifyValue(
                isSelected
            )
    }

    @Test
    fun `When selecting all available products and failed saving shopping list state (no impact on current flow) then the result should change isSelected all available products to be true`() = runTest {
        loadLayoutWithExpandCollapseWidget()

        val isSelected = true

        stubSaveShoppingListState(
            actionList = availableProducts.map { SaveShoppingListStateActionParam(productId = it.id, isSelected = it.isSelected) },
            throwable = Throwable()
        )

        viewModel.selectAllAvailableProducts(
            state = ShoppingListProductState.EXPAND,
            isSelected = isSelected
        )

        advanceTimeBy(DEBOUNCE_TIMES_SHOPPING_LIST)

        availableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        filteredAvailableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        mutableLayout
            .modifyTopCheckAll(
                isSelected = isSelected
            )
            .modifyExpandCollapseProducts(
                state = ShoppingListProductState.EXPAND,
                productLayoutType = ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST,
                products = filteredAvailableProducts
            )

        // verify all products are selected
        verifyIsTrue(
            expectedResult = !availableProducts.any { !it.isSelected }
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.countSelectedItems(),
                    price = filteredAvailableProducts.sumPriceSelectedItems()
                )
            )

        viewModel
            .isTopCheckAllSelected
            .verifyValue(
                isSelected
            )
    }

    @Test
    fun `When selecting all available products and failed saving shopping list state (no impact on current flow) then the result should change isSelected of all available products to be false`() = runTest {
        loadLayoutWithExpandCollapseWidget()

        val isSelected = false

        stubSaveShoppingListState(
            actionList = availableProducts.map { SaveShoppingListStateActionParam(productId = it.id, isSelected = it.isSelected) },
            throwable = Throwable()
        )

        viewModel.selectAllAvailableProducts(
            state = ShoppingListProductState.EXPAND,
            isSelected = isSelected
        )

        advanceTimeBy(DEBOUNCE_TIMES_SHOPPING_LIST)

        availableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        filteredAvailableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        mutableLayout
            .modifyTopCheckAll(
                isSelected = isSelected
            )
            .modifyExpandCollapseProducts(
                state = ShoppingListProductState.EXPAND,
                productLayoutType = ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST,
                products = filteredAvailableProducts
            )

        // verify all products are selected
        verifyIsTrue(
            expectedResult = availableProducts.any { !it.isSelected }
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.countSelectedItems(),
                    price = filteredAvailableProducts.sumPriceSelectedItems()
                )
            )

        viewModel
            .isTopCheckAllSelected
            .verifyValue(
                isSelected
            )
    }

    @Test
    fun `When selecting all available products 2 times and successfully saving shopping list state, should cancel the job first then change isSelected all available products to be true`() = runTest {
        loadLayoutWithExpandCollapseWidget()

        val isSelected = true

        stubSaveShoppingListState(
            actionList = availableProducts.map { SaveShoppingListStateActionParam(productId = it.id, isSelected = it.isSelected) }
        )

        viewModel.selectAllAvailableProducts(
            state = ShoppingListProductState.EXPAND,
            isSelected = isSelected
        )

        viewModel.selectAllAvailableProducts(
            state = ShoppingListProductState.EXPAND,
            isSelected = isSelected
        )

        advanceTimeBy(DEBOUNCE_TIMES_SHOPPING_LIST)

        availableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        filteredAvailableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        mutableLayout
            .modifyTopCheckAll(
                isSelected = isSelected
            )
            .modifyExpandCollapseProducts(
                state = ShoppingListProductState.EXPAND,
                productLayoutType = ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST,
                products = filteredAvailableProducts
            )

        // verify all products are selected
        verifyIsTrue(
            expectedResult = !availableProducts.any { !it.isSelected }
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.countSelectedItems(),
                    price = filteredAvailableProducts.sumPriceSelectedItems()
                )
            )

        viewModel
            .isTopCheckAllSelected
            .verifyValue(
                true
            )
    }

    @Test
    fun `When selecting all available products 2 times and successfully saving shopping list state, should cancel the job first then change isSelected all available products to be false`() = runTest {
        loadLayoutWithExpandCollapseWidget()

        val isSelected = false

        stubSaveShoppingListState(
            actionList = availableProducts.map { SaveShoppingListStateActionParam(productId = it.id, isSelected = it.isSelected) }
        )

        viewModel.selectAllAvailableProducts(
            state = ShoppingListProductState.EXPAND,
            isSelected = isSelected
        )

        viewModel.selectAllAvailableProducts(
            state = ShoppingListProductState.EXPAND,
            isSelected = isSelected
        )

        advanceTimeBy(DEBOUNCE_TIMES_SHOPPING_LIST)

        availableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        filteredAvailableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        mutableLayout
            .modifyTopCheckAll(
                isSelected = isSelected
            )
            .modifyExpandCollapseProducts(
                state = ShoppingListProductState.EXPAND,
                productLayoutType = ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST,
                products = filteredAvailableProducts
            )

        // verify all products are selected
        verifyIsTrue(
            expectedResult = availableProducts.any { !it.isSelected }
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.countSelectedItems(),
                    price = filteredAvailableProducts.sumPriceSelectedItems()
                )
            )

        viewModel
            .isTopCheckAllSelected
            .verifyValue(
                isSelected
            )
    }

    @Test
    fun `When selecting all available products with only isSelected param then the result should change isSelected of all available products to be true`() = runTest {
        loadLayoutWithExpandCollapseWidget()

        val isSelected = true

        stubSaveShoppingListState(
            actionList = availableProducts.map { SaveShoppingListStateActionParam(productId = it.id, isSelected = it.isSelected) }
        )

        viewModel.selectAllAvailableProducts(
            isSelected = isSelected
        )

        advanceTimeBy(DEBOUNCE_TIMES_SHOPPING_LIST)

        availableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        filteredAvailableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        mutableLayout
            .modifyTopCheckAll(
                isSelected = isSelected
            )
            .modifyExpandCollapseProducts(
                state = ShoppingListProductState.COLLAPSE,
                productLayoutType = ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST,
                products = filteredAvailableProducts
            )

        // verify all products are selected
        verifyIsTrue(
            expectedResult = !availableProducts.any { !it.isSelected }
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.countSelectedItems(),
                    price = filteredAvailableProducts.sumPriceSelectedItems()
                )
            )

        viewModel
            .isTopCheckAllSelected
            .verifyValue(
                isSelected
            )
    }

    @Test
    fun `When selecting all available products with only isSelected param then the result should change isSelected of all available products to be false`() = runTest {
        loadLayoutWithExpandCollapseWidget()

        val isSelected = false

        stubSaveShoppingListState(
            actionList = availableProducts.map { SaveShoppingListStateActionParam(productId = it.id, isSelected = it.isSelected) }
        )

        viewModel.selectAllAvailableProducts(
            isSelected = isSelected
        )

        advanceTimeBy(DEBOUNCE_TIMES_SHOPPING_LIST)

        availableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        filteredAvailableProducts
            .updateProductSelections(
                isSelected = isSelected
            )

        mutableLayout
            .modifyTopCheckAll(
                isSelected = isSelected
            )
            .modifyExpandCollapseProducts(
                state = ShoppingListProductState.COLLAPSE,
                productLayoutType = ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST,
                products = filteredAvailableProducts
            )

        // verify all products are selected
        verifyIsTrue(
            expectedResult = availableProducts.any { !it.isSelected }
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.countSelectedItems(),
                    price = filteredAvailableProducts.sumPriceSelectedItems()
                )
            )

        viewModel
            .isTopCheckAllSelected
            .verifyValue(
                isSelected
            )
    }

    @Test
    fun `When selecting all available products with only isSelected param but ShoppingListTopCheckAllUiModel is not found then do nothing`() = runTest {
        val isSelected = true

        viewModel.selectAllAvailableProducts(
            isSelected = isSelected
        )

        // verify still in the first state
        viewModel
            .layoutState
            .verifyLoading(
                LayoutModel(
                    layout = mutableLayout.addLoadingState()
                )
            )
    }

    @Test
    fun `When selecting an available product should change isSelected from false to be true`() = runTest {
        loadLayoutWithExpandCollapseWidget()

        val isSelected = true
        val productId = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first().id

        viewModel.selectAvailableProduct(
            productId = productId,
            isSelected = isSelected
        )

        availableProducts
            .updateProductSelections(
                productId = productId,
                isSelected = isSelected
            )

        filteredAvailableProducts
            .updateProductSelections(
                productId = productId,
                isSelected = isSelected
            )

        val areAllAvailableProductsSelected = filteredAvailableProducts.areSelected()

        mutableLayout
            .modifyTopCheckAll(
                isSelected = areAllAvailableProductsSelected
            )
            .modifyProduct(
                productId = productId,
                isSelected = isSelected
            )

        // verify product already selected
        verifyIsTrue(
            expectedResult = (mutableLayout.first { it is ShoppingListHorizontalProductCardItemUiModel && it.id == productId } as ShoppingListHorizontalProductCardItemUiModel).isSelected
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.countSelectedItems(),
                    price = filteredAvailableProducts.sumPriceSelectedItems()
                )
            )

        viewModel
            .isTopCheckAllSelected
            .verifyValue(
                false
            )
    }

    @Test
    fun `When selecting an available product 2 times should change isSelected from false to be true to be false again`() = runTest {
        loadLayoutWithExpandCollapseWidget()

        var isSelected = true
        val productId = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first().id

        viewModel.selectAvailableProduct(
            productId = productId,
            isSelected = isSelected
        )

        isSelected = !isSelected
        viewModel.selectAvailableProduct(
            productId = productId,
            isSelected = isSelected
        )

        availableProducts
            .updateProductSelections(
                productId = productId,
                isSelected = isSelected
            )

        filteredAvailableProducts
            .updateProductSelections(
                productId = productId,
                isSelected = isSelected
            )

        val areAllAvailableProductsSelected = filteredAvailableProducts.areSelected()

        mutableLayout
            .modifyTopCheckAll(
                isSelected = areAllAvailableProductsSelected
            )
            .modifyProduct(
                productId = productId,
                isSelected = isSelected
            )

        // verify product already selected
        verifyIsTrue(
            expectedResult = !(mutableLayout.first { it is ShoppingListHorizontalProductCardItemUiModel && it.id == productId } as ShoppingListHorizontalProductCardItemUiModel).isSelected
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.countSelectedItems(),
                    price = filteredAvailableProducts.sumPriceSelectedItems()
                )
            )

        viewModel
            .isTopCheckAllSelected
            .verifyValue(
                false
            )
    }

    @Test
    fun `When selecting an available product should change isSelected from true to be false`() = runTest {
        `When selecting all available products with only isSelected param then the result should change isSelected of all available products to be true`()

        val isSelected = false
        val productId = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first().id

        viewModel.selectAvailableProduct(
            productId = productId,
            isSelected = isSelected
        )

        availableProducts
            .updateProductSelections(
                productId = productId,
                isSelected = isSelected
            )

        filteredAvailableProducts
            .updateProductSelections(
                productId = productId,
                isSelected = isSelected
            )

        val areAllAvailableProductsSelected = filteredAvailableProducts.areSelected()

        mutableLayout
            .modifyTopCheckAll(
                isSelected = areAllAvailableProductsSelected
            )
            .modifyProduct(
                productId = productId,
                isSelected = isSelected
            )

        // verify product already not selected
        verifyIsTrue(
            expectedResult = !(mutableLayout.first { it is ShoppingListHorizontalProductCardItemUiModel && it.id == productId } as ShoppingListHorizontalProductCardItemUiModel).isSelected
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                BottomBulkAtcModel(
                    counter = filteredAvailableProducts.countSelectedItems(),
                    price = filteredAvailableProducts.sumPriceSelectedItems()
                )
            )

        viewModel
            .isTopCheckAllSelected
            .verifyValue(
                false
            )
    }
}
