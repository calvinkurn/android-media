package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.bottomsheet

import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_DEVICE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_SOURCE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.data.ShoppingListDataFactory.AnotherOptionBottomSheet.createAvailableProducts
import com.tokopedia.tokopedianow.data.ShoppingListDataFactory.AnotherOptionBottomSheet.createRecommendationWidget
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.addProduct
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.ProductRecommendationMapper.mapRecommendedProducts
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListAnotherOptionBottomSheetViewModel.Companion.RECOMMENDATION_PAGE_NAME
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response.Data.WishlistAddV2
import org.junit.Test

class ShoppingListAddToWishlist: TokoNowShoppingListAnotherOptionBottomSheetViewModelFixture() {
    @Test
    fun `When adding product to wishlist successfully then the result should return layout with new available product and success toaster`() {
        // provide some variables
        val productRecommendationTargetId = "1231212"
        val userId = "1222121"
        val recommendationWidget: RecommendationWidget = createRecommendationWidget()

        val recommendedProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mapRecommendedProducts(recommendationWidget).toMutableList()
        val availableProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = createAvailableProducts().toMutableList()
        val recommendedProductsFiltered = filterProductRecommendationWithAvailableProduct(
            recommendedProducts = recommendedProducts,
            availableProducts = availableProducts
        )

        val wishlistProduct = recommendedProducts.first()

        val param = GetRecommendationRequestParam(
            userId = userId.toIntSafely(),
            productIds = listOf(productRecommendationTargetId),
            pageName = RECOMMENDATION_PAGE_NAME,
            xDevice = X_DEVICE_RECOMMENDATION_PARAM,
            xSource = X_SOURCE_RECOMMENDATION_PARAM,
            isTokonow = true
        )

        // stub section
        stubUserId(userId)

        stubProductRecommendation(
            param = param,
            response = recommendationWidget
        )

        stubAddToWishlist(
            response = Success(data = WishlistAddV2(success = true))
        )

        // add available products
        viewModel
            .availableProducts
            .addAll(availableProducts)

        // filter product recommendation with available list, product layout type will be 'PRODUCT_RECOMMENDATION' if the product recommendation is not available in available list other wise will be 'PRODUCT_RECOMMENDATION_ADDED'
        viewModel
            .loadLayout(productId = productRecommendationTargetId)

        // add product recommendation to available list and re-filter
        viewModel
            .addToWishlist(wishlistProduct)

        // provide expected result
        availableProducts
            .addProduct(
                wishlistProduct.copy(
                    productLayoutType = ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST,
                    isSelected = true,
                    state = TokoNowLayoutState.SHOW
                )
            )

        val expectedResult = filterProductRecommendationWithAvailableProduct(
            recommendedProducts = recommendedProducts,
            availableProducts = availableProducts
        )

        // verify section
        verifyProductRecommendationUseCase(
            param = param
        )

        verifyAddToWishlistUseCase(
            productId = recommendedProductsFiltered.first().id,
            userId = userId
        )

        viewModel
            .layoutState
            .verifySuccess(expectedResult)

        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_to_add_product_to_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_for_cta),
                    type = Toaster.TYPE_NORMAL,
                    event = ToasterModel.Event.ADD_WISHLIST,
                    any = recommendedProductsFiltered.first()
                )
            )

    }

    @Test
    fun `When adding product to wishlist successfully but the response is false for success field then should return the same layout as the previous one and failed toaster`() {
        // provide some variables
        val productRecommendationTargetId = "1231212"
        val userId = "1222121"
        val recommendationWidget: RecommendationWidget = createRecommendationWidget()

        val recommendedProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mapRecommendedProducts(recommendationWidget).toMutableList()
        val availableProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = createAvailableProducts().toMutableList()
        val recommendedProductsFiltered = filterProductRecommendationWithAvailableProduct(
            recommendedProducts = recommendedProducts,
            availableProducts = availableProducts
        )

        val wishlistProduct = recommendedProducts.first()

        val param = GetRecommendationRequestParam(
            userId = userId.toIntSafely(),
            productIds = listOf(productRecommendationTargetId),
            pageName = RECOMMENDATION_PAGE_NAME,
            xDevice = X_DEVICE_RECOMMENDATION_PARAM,
            xSource = X_SOURCE_RECOMMENDATION_PARAM,
            isTokonow = true
        )

        // stub section
        stubUserId(userId)

        stubProductRecommendation(
            param = param,
            response = recommendationWidget
        )

        stubAddToWishlist(
            response = Success(data = WishlistAddV2(success = false))
        )

        // add available products
        viewModel
            .availableProducts
            .addAll(availableProducts)

        // filter product recommendation with available list, product layout type will be 'PRODUCT_RECOMMENDATION' if the product recommendation is not available in available list other wise will be 'PRODUCT_RECOMMENDATION_ADDED'
        viewModel
            .loadLayout(productId = productRecommendationTargetId)

        // add product recommendation to available list and re-filter
        viewModel
            .addToWishlist(wishlistProduct)

        // provide expected result
        val expectedResult = recommendedProductsFiltered

        // verify section
        verifyProductRecommendationUseCase(
            param = param
        )

        verifyAddToWishlistUseCase(
            productId = wishlistProduct.id,
            userId = userId
        )

        viewModel
            .layoutState
            .verifySuccess(expectedResult)

        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_add_product_to_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = Toaster.TYPE_ERROR,
                    event = ToasterModel.Event.ADD_WISHLIST,
                    any = wishlistProduct
                )
            )
    }

    @Test
    fun `When adding product to wishlist failed from usecase then should return the same layout as the previous one and failed toaster`() {
        // provide some variables
        val productRecommendationTargetId = "1231212"
        val userId = "1222121"
        val recommendationWidget: RecommendationWidget = createRecommendationWidget()

        val recommendedProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mapRecommendedProducts(recommendationWidget).toMutableList()
        val availableProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = createAvailableProducts().toMutableList()
        val recommendedProductsFiltered = filterProductRecommendationWithAvailableProduct(
            recommendedProducts = recommendedProducts,
            availableProducts = availableProducts
        )

        val wishlistProduct = recommendedProducts.first()

        val param = GetRecommendationRequestParam(
            userId = userId.toIntSafely(),
            productIds = listOf(productRecommendationTargetId),
            pageName = RECOMMENDATION_PAGE_NAME,
            xDevice = X_DEVICE_RECOMMENDATION_PARAM,
            xSource = X_SOURCE_RECOMMENDATION_PARAM,
            isTokonow = true
        )

        // stub section
        stubUserId(userId)

        stubProductRecommendation(
            param = param,
            response = recommendationWidget
        )

        stubAddToWishlist(
            response = Fail(throwable = Throwable())
        )

        // add available products
        viewModel
            .availableProducts
            .addAll(availableProducts)

        // filter product recommendation with available list, product layout type will be 'PRODUCT_RECOMMENDATION' if the product recommendation is not available in available list other wise will be 'PRODUCT_RECOMMENDATION_ADDED'
        viewModel
            .loadLayout(productId = productRecommendationTargetId)

        // add product recommendation to available list and re-filter
        viewModel
            .addToWishlist(wishlistProduct)

        // provide expected result
        val expectedResult = recommendedProductsFiltered

        // verify section
        verifyProductRecommendationUseCase(
            param = param
        )

        verifyAddToWishlistUseCase(
            productId = wishlistProduct.id,
            userId = userId
        )

        viewModel
            .layoutState
            .verifySuccess(expectedResult)

        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_add_product_to_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = Toaster.TYPE_ERROR,
                    event = ToasterModel.Event.ADD_WISHLIST,
                    any = wishlistProduct
                )
            )
    }

    @Test
    fun `When adding product to wishlist failed then should return the same layout as the previous one and failed toaster`() {
        // provide some variables
        val productRecommendationTargetId = "1231212"
        val userId = "1222121"
        val recommendationWidget: RecommendationWidget = createRecommendationWidget()

        val recommendedProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mapRecommendedProducts(recommendationWidget).toMutableList()
        val availableProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = createAvailableProducts().toMutableList()
        val recommendedProductsFiltered = filterProductRecommendationWithAvailableProduct(
            recommendedProducts = recommendedProducts,
            availableProducts = availableProducts
        )

        val wishlistProduct = recommendedProducts.first()

        val param = GetRecommendationRequestParam(
            userId = userId.toIntSafely(),
            productIds = listOf(productRecommendationTargetId),
            pageName = RECOMMENDATION_PAGE_NAME,
            xDevice = X_DEVICE_RECOMMENDATION_PARAM,
            xSource = X_SOURCE_RECOMMENDATION_PARAM,
            isTokonow = true
        )

        // stub section
        stubUserId(userId)

        stubProductRecommendation(
            param = param,
            response = recommendationWidget
        )

        stubAddToWishlist(
            throwable = Throwable()
        )

        // add available products
        viewModel
            .availableProducts
            .addAll(availableProducts)

        // filter product recommendation with available list, product layout type will be 'PRODUCT_RECOMMENDATION' if the product recommendation is not available in available list other wise will be 'PRODUCT_RECOMMENDATION_ADDED'
        viewModel
            .loadLayout(productId = productRecommendationTargetId)

        // add product recommendation to available list and re-filter
        viewModel
            .addToWishlist(wishlistProduct)

        // provide expected result
        val expectedResult = recommendedProductsFiltered

        // verify section
        verifyProductRecommendationUseCase(
            param = param
        )

        verifyAddToWishlistUseCase(
            productId = recommendedProductsFiltered.first().id,
            userId = userId
        )

        viewModel
            .layoutState
            .verifySuccess(expectedResult)

        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_add_product_to_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = Toaster.TYPE_ERROR,
                    event = ToasterModel.Event.ADD_WISHLIST,
                    any = wishlistProduct
                )
            )
    }
}
