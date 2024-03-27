package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.main

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.common.constant.ConstantValue
import com.tokopedia.tokopedianow.data.ShoppingListDataFactory
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.countSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.doIf
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.sumPriceSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.modifyExpandCollapseProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.modifyExpandCollapseState
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.modifyTopCheckAllState
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.BottomBulkAtcModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListExpandCollapseUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListViewModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.UNAVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState.EXPAND
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState.COLLAPSE
import org.junit.Test

class ShoppingListExpandCollapse: TokoNowShoppingListViewModelFixture() {
    private fun loadLayout() {
        // create fake variables for stub
        val shopId = 222121L
        val miniCartData = ShoppingListDataFactory.Main.createMiniCart()
        val shoppingList = ShoppingListDataFactory.Main.createShoppingList()
        val newShoppingList = shoppingList.copy(
            listAvailableItem = shoppingList.listAvailableItem + shoppingList.listAvailableItem,
            listUnavailableItem = shoppingList.listUnavailableItem + shoppingList.listUnavailableItem
        )
        val recommendationWidget = ShoppingListDataFactory.Main.createRecommendationWidget(
            hasNext = false,
            title = "Rekomendasi Untuk Anda"
        )

        // stub section
        stubOutOfCoverage(
            isOoc = false
        )

        stubShopId(
            shopId = shopId
        )

        stubLoggedIn(
            isLoggedIn = true
        )

        stubGetMiniCart(
            response = miniCartData
        )

        stubGetShoppingList(
            response = newShoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = TokoNowShoppingListViewModel.PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = ConstantValue.X_DEVICE_RECOMMENDATION_PARAM,
                xSource = ConstantValue.X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = miniCartData,
            shoppingList = newShoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId,
            isProductRecommendationError = true
        )
        updateLayout()

        // other verification
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout,
                    isRequiredToScrollUp = true
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
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .miniCartState
            .verifyIsError()

        viewModel
            .isPageImpressionTracked
            .verifyValue(
                true
            )

        viewModel
            .isCoachMarkShown
            .verifyValue(
                true
            )

        viewModel
            .isOnScrollNotNeeded
            .verifyValue(
                true
            )

        viewModel
            .isNavToolbarScrollingBehaviourEnabled
            .verifyValue(
                true
            )
    }

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
        loadLayout()
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
        loadLayout()
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
        loadLayout()
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
        loadLayout()
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
