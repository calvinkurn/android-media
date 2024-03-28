package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.main

import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.doIf
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.modifyExpandCollapseProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.modifyExpandCollapseState
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.modifyTopCheckAllState
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListExpandCollapseUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.UNAVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState.EXPAND
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState.COLLAPSE
import org.junit.Test

class ShoppingListExpandCollapse: TokoNowShoppingListViewModelFixture() {
    private fun expandCollapseShoppingList(
        productState: ShoppingListProductState,
        productLayoutType: ShoppingListProductLayoutType
    ) {
        mutableLayout
            .modifyExpandCollapseProducts(
                state = productState,
                productLayoutType = productLayoutType,
                products = if (productLayoutType == AVAILABLE_SHOPPING_LIST) filteredAvailableProducts else filteredUnavailableProducts
            )
            .modifyExpandCollapseState(
                productState = productState,
                productLayoutType = productLayoutType
            )
            .doIf(
                predicate = productLayoutType == AVAILABLE_SHOPPING_LIST,
                then = {
                    modifyTopCheckAllState(
                        productState = productState
                    )
                }
            )
    }

    @Test
    fun `When expanding available shopping list should keep expand state on expand collapse widget`() {
        loadLayoutWithExpandCollapseWidget()
        viewModel.expandCollapseShoppingList(
            productState = EXPAND,
            productLayoutType = AVAILABLE_SHOPPING_LIST
        )
        expandCollapseShoppingList(
            productState = EXPAND,
            productLayoutType = AVAILABLE_SHOPPING_LIST
        )

        // verify expand collapse existing
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListExpandCollapseUiModel && it.productLayoutType == AVAILABLE_SHOPPING_LIST && it.productState == EXPAND }
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )
    }

    @Test
    fun `When collapsing available shopping list should keep collapse state on expand collapse widget`() {
        loadLayoutWithExpandCollapseWidget()
        viewModel.expandCollapseShoppingList(
            productState = COLLAPSE,
            productLayoutType = AVAILABLE_SHOPPING_LIST
        )
        expandCollapseShoppingList(
            productState = COLLAPSE,
            productLayoutType = AVAILABLE_SHOPPING_LIST
        )

        // verify expand collapse existing
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListExpandCollapseUiModel && it.productLayoutType == AVAILABLE_SHOPPING_LIST && it.productState == COLLAPSE }
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )
    }

    @Test
    fun `When expanding unavailable shopping list should keep expand state on expand collapse widget`() {
        loadLayoutWithExpandCollapseWidget()
        viewModel.expandCollapseShoppingList(
            productState = EXPAND,
            productLayoutType = UNAVAILABLE_SHOPPING_LIST
        )
        expandCollapseShoppingList(
            productState = EXPAND,
            productLayoutType = UNAVAILABLE_SHOPPING_LIST
        )

        // verify expand collapse existing
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListExpandCollapseUiModel && it.productLayoutType == UNAVAILABLE_SHOPPING_LIST && it.productState == EXPAND }
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )
    }

    @Test
    fun `When collapsing unavailable shopping list should keep collapse state on expand collapse widget`() {
        loadLayoutWithExpandCollapseWidget()
        viewModel.expandCollapseShoppingList(
            productState = COLLAPSE,
            productLayoutType = UNAVAILABLE_SHOPPING_LIST
        )
        expandCollapseShoppingList(
            productState = COLLAPSE,
            productLayoutType = UNAVAILABLE_SHOPPING_LIST
        )

        // verify expand collapse existing
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListExpandCollapseUiModel && it.productLayoutType == UNAVAILABLE_SHOPPING_LIST && it.productState == COLLAPSE }
        )

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )
    }
}
