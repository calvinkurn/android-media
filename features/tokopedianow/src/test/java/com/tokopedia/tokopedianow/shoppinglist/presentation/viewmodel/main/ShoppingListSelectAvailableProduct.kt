package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.main

import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.countSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.sumPriceSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.modifyExpandCollapseProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.modifyTopCheckAll
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.updateProductSelections
import com.tokopedia.tokopedianow.shoppinglist.domain.model.SaveShoppingListStateActionParam
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.BottomBulkAtcModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
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
    fun `test`() = runTest {
        val isSelected = true

        loadLayoutWithExpandCollapseWidget()

        stubSaveShoppingListState(
            actionList = availableProducts.map { SaveShoppingListStateActionParam(productId = it.id, isSelected = it.isSelected) }
        )

        viewModel.selectAllAvailableProducts(
            state = ShoppingListProductState.EXPAND,
            isSelected = isSelected
        )

        advanceTimeBy(DEBOUNCE_TIMES_SHOPPING_LIST + 100)

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
    fun `test 2`() = runTest {
        val isSelected = true

        loadLayoutWithExpandCollapseWidget()

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
}
