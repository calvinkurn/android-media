package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.bottomsheet

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.common.constant.ConstantValue
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_DEVICE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.data.ShoppingListDataFactory.AnotherOptionBottomSheet.createAvailableProducts
import com.tokopedia.tokopedianow.data.ShoppingListDataFactory.AnotherOptionBottomSheet.createRecommendationWidget
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.AnotherOptionBottomSheetVisitableExtension.addEmptyState
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.addErrorState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.ProductRecommendationMapper.mapRecommendedProducts
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListAnotherOptionBottomSheetViewModel.Companion.RECOMMENDATION_PAGE_NAME
import org.junit.Test

class ShoppingListLoadLayout: TokoNowShoppingListAnotherOptionBottomSheetViewModelFixture() {
    @Test
    fun `when getting product recommendation successfully then filtering recommended products with available products, the result should not be empty and would be the same as expected`() {
        // provide some variables
        val productRecommendationTargetId = "1231212"
        val userId = "1222121"

        val productRecommendation = createRecommendationWidget()
        val recommendedProducts = mapRecommendedProducts(productRecommendation).toMutableList()
        val availableProducts = createAvailableProducts().toMutableList()

        val param = GetRecommendationRequestParam(
            userId = userId.toIntSafely(),
            productIds = listOf(productRecommendationTargetId),
            pageName = RECOMMENDATION_PAGE_NAME,
            xDevice = X_DEVICE_RECOMMENDATION_PARAM,
            xSource = ConstantValue.X_SOURCE_RECOMMENDATION_PARAM,
            isTokonow = true
        )

        // stub section
        stubUserId(userId)

        stubProductRecommendation(
            param = param,
            response = productRecommendation
        )

        // add available products
        viewModel
            .availableProducts
            .addAll(availableProducts)

        // filter product recommendation with available list, product layout type will be 'PRODUCT_RECOMMENDATION' if the product recommendation is not available in available list other wise will be 'PRODUCT_RECOMMENDATION_ADDED'
        viewModel
            .loadLayout(productId = productRecommendationTargetId)

        // provide expected result
        val expectedResult = filterProductRecommendationWithAvailableProduct(
            recommendedProducts = recommendedProducts,
            availableProducts = availableProducts
        )

        verifyProductRecommendationUseCase(
            param = param
        )

        viewModel
            .layoutState
            .verifySuccess(expectedResult)
    }

    @Test
    fun `When getting product recommendation successfully then filtering recommended products with available products, the result should be empty and would be the same as expected`() {
        // provide some variables
        val productRecommendationTargetId = "1231212"
        val userId = "1222121"

        val productRecommendation = createRecommendationWidget(recommendationItemList = emptyList())
        val availableProducts = createAvailableProducts()
            .toMutableList()

        val param = GetRecommendationRequestParam(
            userId = userId.toIntSafely(),
            productIds = listOf(productRecommendationTargetId),
            pageName = RECOMMENDATION_PAGE_NAME,
            xDevice = X_DEVICE_RECOMMENDATION_PARAM,
            xSource = ConstantValue.X_SOURCE_RECOMMENDATION_PARAM,
            isTokonow = true
        )

        // stub section
        stubUserId(userId)

        stubProductRecommendation(
            param = param,
            response = productRecommendation
        )

        // add available products
        viewModel
            .availableProducts
            .addAll(availableProducts)

        // filter product recommendation with available list, product layout type will be 'PRODUCT_RECOMMENDATION' if the product recommendation is not available in available list other wise will be 'PRODUCT_RECOMMENDATION_ADDED'
        viewModel
            .loadLayout(productId = productRecommendationTargetId)

        // provide expected result
        val expectedResult: MutableList<Visitable<*>> = mutableListOf()

        expectedResult
            .addEmptyState()

        verifyProductRecommendationUseCase(
            param = param
        )

        viewModel
            .layoutState
            .verifySuccess(expectedResult)
    }

    @Test
    fun `When getting product recommendation failed then the result should return error state and would be the same as expected result`() {
        val productRecommendationTargetId = "1231212"
        val userId = "1222121"

        val availableProducts = createAvailableProducts()
            .toMutableList()

        val throwable = Throwable()

        val param = GetRecommendationRequestParam(
            userId = userId.toIntSafely(),
            productIds = listOf(productRecommendationTargetId),
            pageName = RECOMMENDATION_PAGE_NAME,
            xDevice = X_DEVICE_RECOMMENDATION_PARAM,
            xSource = ConstantValue.X_SOURCE_RECOMMENDATION_PARAM,
            isTokonow = true
        )

        stubUserId(userId)

        stubProductRecommendation(
            param = param,
            throwable = throwable
        )

        // add available products
        viewModel
            .availableProducts
            .addAll(availableProducts)

        // failed load layout
        viewModel
            .loadLayout(productId = productRecommendationTargetId)

        // provide expected result
        val expectedResult: MutableList<Visitable<*>> = mutableListOf()

        expectedResult
            .addErrorState(
                isFullPage = false,
                throwable = throwable
            )

        // verify section
        verifyProductRecommendationUseCase(
            param = param
        )

        viewModel
            .layoutState
            .verifyError(
                throwable = throwable,
                expectedResult = expectedResult
            )
    }
}
