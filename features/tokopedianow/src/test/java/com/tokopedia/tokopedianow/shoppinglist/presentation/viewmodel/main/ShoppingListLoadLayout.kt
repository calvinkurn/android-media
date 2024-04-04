package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.main

import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_DEVICE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_SOURCE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.model.TokoNowErrorUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowLocalLoadUiModel
import com.tokopedia.tokopedianow.data.ShoppingListDataFactory.Main
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.addErrorState
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.countSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.sumPriceSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.BottomBulkAtcModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListCartProductUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListEmptyUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListExpandCollapseUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListLoadingMoreUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListTopCheckAllUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListViewModel.Companion.INVALID_SHOP_ID
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListViewModel.Companion.PRODUCT_RECOMMENDATION_PAGE_NAME
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import org.junit.Test

class ShoppingListLoadLayout: TokoNowShoppingListViewModelFixture() {

    @Test
    fun `When processing to load layout, intersection is done between mini cart data and available list to get data for cart widget, the result should be contains cart widget because the intersection result is not empty`() {
        // create fake variables for stub
        val shopId = 222121L
        val miniCartData = Main.createMiniCart()
        val shoppingList = Main.createShoppingList()
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
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
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = miniCartData,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId
        )
        updateLayout()

        // verify cart product existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<ShoppingListCartProductUiModel>().isNotEmpty()
        )

        // other verifications
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

    @Test
    fun `When processing to load layout, mini cart data is null because there is invalid shop id, intersection is done between minicart and available list to get data for cart widget, the result should not contain cart widget because the intersection result is empty`() {
        // create fake variables for stub
        val shopId = INVALID_SHOP_ID
        val miniCartData = Main.createMiniCart()
        val shoppingList = Main.createShoppingList()
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
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
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = miniCartData,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId
        )
        updateLayout()

        // verify cart product existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<ShoppingListCartProductUiModel>().isEmpty()
        )

        // other verifications
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

    @Test
    fun `When processing to load layout, mini cart data is null because gql retrieval response is failed, intersection is done between minicart and available list to get data for cart widget, the result should not contain cart widget because the intersection result is empty`() {
        // create fake variables for stub
        val shopId = 222121L
        val shoppingList = Main.createShoppingList()
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
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
            throwable = MessageErrorException()
        )

        stubGetShoppingList(
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = null,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId
        )
        updateLayout()

        // verify cart product existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<ShoppingListCartProductUiModel>().isEmpty()
        )

        // other verifications
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

    @Test
    fun `When processing to load layout, product recommendation widget response doesn't have next page, the result for product recommendation should not have load more`() {
        // create fake variables for stub
        val shopId = 222121L
        val shoppingList = Main.createShoppingList()
        val recommendationWidget = Main.createRecommendationWidget(
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
            throwable = MessageErrorException()
        )

        stubGetShoppingList(
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = null,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId
        )
        updateLayout()

        // verify loading more existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<LoadingMoreModel>().isEmpty()
        )

        // other verifications
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

    @Test
    fun `When processing to load layout, product recommendation widget response has next page, the result for product recommendation should have load more`() {
        // create fake variables for stub
        val shopId = 222121L
        val shoppingList = Main.createShoppingList()
        val recommendationWidget = Main.createRecommendationWidget(
            hasNext = true,
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
            throwable = MessageErrorException()
        )

        stubGetShoppingList(
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = null,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId
        )
        updateLayout()

        // verify loading more existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<ShoppingListLoadingMoreUiModel>().isNotEmpty()
        )

        // other verifications
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
                false
            )

        viewModel
            .isNavToolbarScrollingBehaviourEnabled
            .verifyValue(
                true
            )
    }

    @Test
    fun `When processing to load layout, failed to retrieve product recommendation from gql, the result should not show product recommendation but rather show local load`() {
        // create fake variables for stub
        val shopId = 222121L
        val shoppingList = Main.createShoppingList()
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
            hasNext = true,
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
            throwable = MessageErrorException()
        )

        stubGetShoppingList(
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            throwable = MessageErrorException()
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = null,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId,
            isProductRecommendationError = true
        )
        updateLayout()

        // verify local load existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<TokoNowLocalLoadUiModel>().isNotEmpty()
        )

        // other verifications
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

    @Test
    fun `When processing to load layout, shopping list for both available and unavailable products are empty, the result should show empty state`() {
        // create fake variables for stub
        val shopId = 222121L
        val shoppingList = Main.createShoppingList(
            listAvailableItem = emptyList(),
            listUnavailableItem = emptyList()
        )
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
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
            throwable = MessageErrorException()
        )

        stubGetShoppingList(
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            throwable = MessageErrorException()
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = null,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId,
            isProductRecommendationError = true
        )
        updateLayout()

        // verify empty state existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<ShoppingListEmptyUiModel>().isNotEmpty()
        )

        // other verifications
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
                null
            )

        viewModel
            .isProductAvailable
            .verifyValue(
                false
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

    @Test
    fun `When processing to load layout, shopping list for available products are empty, the result should not show available products`() {
        // create fake variables for stub
        val shopId = 222121L
        val shoppingList = Main.createShoppingList(
            listAvailableItem = emptyList()
        )
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
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
            throwable = MessageErrorException()
        )

        stubGetShoppingList(
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            throwable = MessageErrorException()
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = null,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId,
            isProductRecommendationError = true
        )
        updateLayout()

        // verify empty state existing
        verifyIsTrue(
            expectedResult = !mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.productLayoutType == ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST }
        )

        // other verifications
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
                null
            )

        viewModel
            .isProductAvailable
            .verifyValue(
                false
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

    @Test
    fun `When processing to load layout, shopping list for available products are empty but mini cart is not null, the result should not show available products then also update mini cart data and product available for adjustment of bottom widgets`() {
        // create fake variables for stub
        val shopId = 222121L
        val miniCartData = Main.createMiniCart()
        val shoppingList = Main.createShoppingList(
            listAvailableItem = emptyList()
        )
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
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
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            throwable = MessageErrorException()
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = miniCartData,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId,
            isProductRecommendationError = true
        )
        updateLayout()

        // verify empty state existing
        verifyIsTrue(
            expectedResult = !mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.productLayoutType == ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST }
        )

        // other verifications
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
                null
            )

        viewModel
            .isProductAvailable
            .verifyValue(
                false
            )

        viewModel
            .miniCartState
            .verifySuccess(miniCartData)

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

    @Test
    fun `When processing to load layout, shopping list for unavailable products are empty, the result should not show unavailable products`() {
        // create fake variables for stub
        val shopId = 222121L
        val shoppingList = Main.createShoppingList(
            listUnavailableItem = emptyList()
        )
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
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
            throwable = MessageErrorException()
        )

        stubGetShoppingList(
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            throwable = MessageErrorException()
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = null,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId,
            isProductRecommendationError = true
        )
        updateLayout()

        // verify empty state existing
        verifyIsTrue(
            expectedResult = !mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.productLayoutType == ShoppingListProductLayoutType.UNAVAILABLE_SHOPPING_LIST }
        )

        // other verifications
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

    @Test
    fun `When processing to load layout, failed to retrieve shopping list from gql, the result should show error state`() {
        // create fake variables for stub
        val shopId = 222121L
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
            hasNext = false,
            title = "Rekomendasi Untuk Anda"
        )
        val shoppingListThrowable = MessageErrorException()

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

        stubGetShoppingList(
            throwable = shoppingListThrowable
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            throwable = MessageErrorException()
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = null,
            shoppingList = null,
            recommendationWidget = recommendationWidget,
            shopId = shopId,
            isProductRecommendationError = true
        )
        mutableLayout.addErrorState(
            isFullPage = true,
            throwable = shoppingListThrowable
        )

        // verify error state existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<TokoNowErrorUiModel>().isNotEmpty()
        )

        // other verifications
        viewModel
            .layoutState
            .verifyError(
                throwable = shoppingListThrowable,
                expectedResult = LayoutModel(
                    layout = mutableLayout,
                    isRequiredToScrollUp = false
                )
            )

        viewModel
            .bottomBulkAtcData
            .verifyValue(
                null
            )

        viewModel
            .isProductAvailable
            .verifyValue(
                false
            )

        viewModel
            .isPageImpressionTracked
            .verifyValue(
                false
            )

        viewModel
            .isCoachMarkShown
            .verifyValue(
                false
            )

        viewModel
            .isOnScrollNotNeeded
            .verifyValue(
                false
            )

        viewModel
            .isNavToolbarScrollingBehaviourEnabled
            .verifyValue(
                false
            )
    }

    @Test
    fun `When processing to load layout, shopping list for available products have been selected, then the result for top check all widget is selected as well`() {
        // create fake variables for stub
        val shopId = 222121L
        val shoppingList = Main.createShoppingList(
            listUnavailableItem = emptyList()
        )
        val newShoppingList = shoppingList.copy(
            listAvailableItem = shoppingList.listAvailableItem.map { it.copy(isSelected = true) }
        )
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
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
            throwable = MessageErrorException()
        )

        stubGetShoppingList(
            response = newShoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            throwable = MessageErrorException()
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = null,
            shoppingList = newShoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId,
            isProductRecommendationError = true
        )
        updateLayout()

        // verify empty state existing
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListTopCheckAllUiModel && it.isSelected }
        )

        // other verifications
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

    @Test
    fun `When processing to load layout, total shopping list for available products more than max total product displayed, the result should add expand collapse widget with collapse state`() {
        // create fake variables for stub
        val shopId = 222121L
        val shoppingList = Main.createShoppingList(
            listUnavailableItem = emptyList()
        )
        val newShoppingList = shoppingList.copy(
            listAvailableItem = shoppingList.listAvailableItem + shoppingList.listAvailableItem
        )
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
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
            throwable = MessageErrorException()
        )

        stubGetShoppingList(
            response = newShoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            throwable = MessageErrorException()
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = null,
            shoppingList = newShoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId,
            isProductRecommendationError = true
        )
        updateLayout()

        // verify empty state existing
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListExpandCollapseUiModel && it.productState == ShoppingListProductState.COLLAPSE }
        )

        // other verifications
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

    @Test
    fun `When processing to load layout, total shopping list for unavailable products more than max total product displayed, the result should add expand collapse widget with collapse state`() {
        // create fake variables for stub
        val shopId = 222121L
        val shoppingList = Main.createShoppingList(
            listAvailableItem = emptyList()
        )
        val newShoppingList = shoppingList.copy(
            listUnavailableItem = shoppingList.listUnavailableItem + shoppingList.listUnavailableItem
        )
        val recommendationWidget = Main.createRecommendationWidget(
            recommendationItemList = emptyList(),
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
            throwable = MessageErrorException()
        )

        stubGetShoppingList(
            response = newShoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            throwable = MessageErrorException()
        )

        // load layout
        viewModel.loadLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = null,
            shoppingList = newShoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId,
            isProductRecommendationError = true
        )
        updateLayout()

        // verify empty state existing
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListExpandCollapseUiModel && it.productState == ShoppingListProductState.COLLAPSE }
        )

        // other verifications
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
                null
            )

        viewModel
            .isProductAvailable
            .verifyValue(
                false
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

    @Test
    fun `When refreshing to load layout, the result should give the same result as first time load`() {
        // create fake variables for stub
        val shopId = 222121L
        val miniCartData = Main.createMiniCart()
        val shoppingList = Main.createShoppingList()
        val recommendationWidget = Main.createRecommendationWidget(
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
            response = shoppingList
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ZERO,
                userId = userSession.userId.toIntSafely(),
                pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                xSource = X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        // load layout
        viewModel.loadLayout()
        viewModel.refreshLayout()

        // update expected layout
        setDataToGlobalVariables(
            miniCartData = miniCartData,
            shoppingList = shoppingList,
            recommendationWidget = recommendationWidget,
            shopId = shopId,
            isProductRecommendationError = true
        )
        updateLayout()

        // other verifications
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
}
