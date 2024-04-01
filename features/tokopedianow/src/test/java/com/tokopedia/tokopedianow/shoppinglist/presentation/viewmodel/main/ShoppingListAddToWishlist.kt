package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.main

import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel.Event.ADD_WISHLIST
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.SHOW
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.addProduct
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.countSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.modifyProduct
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.sumPriceSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.removeProduct
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.BottomBulkAtcModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.UNAVAILABLE_SHOPPING_LIST
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import org.junit.Test

class ShoppingListAddToWishlist: TokoNowShoppingListViewModelFixture() {
    private fun onSuccessAddingToWishlist(
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        when(product.productLayoutType) {
            UNAVAILABLE_SHOPPING_LIST -> {
                unavailableProducts
                    .addProduct(product)
            }
            AVAILABLE_SHOPPING_LIST -> {
                availableProducts
                    .addProduct(
                        product.copy(
                            isSelected = true,
                            state = SHOW
                        )
                    )
            }
            else -> {
                availableProducts
                    .addProduct(
                        product.copy(
                            productLayoutType = AVAILABLE_SHOPPING_LIST,
                            isSelected = true,
                            state = SHOW
                        )
                    )

                recommendedProducts
                    .removeProduct(product.id)
            }
        }
        updateLayout()
    }

    private fun onErrorAddingToWishlist(
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        mutableLayout
            .modifyProduct(
                productId = product.id,
                state = SHOW
            )
    }

    @Test
    fun `When adding an available product to wishlist successfully then the result should return layout with new that available product and success toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val productLayoutType = AVAILABLE_SHOPPING_LIST
        val product = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first { it.productLayoutType == productLayoutType }

        stubAddToWishlist(
            response = Success(data = AddToWishlistV2Response.Data.WishlistAddV2(success = true))
        )

        viewModel.addToWishlist(
            product = product
        )

        onSuccessAddingToWishlist(
            product = product
        )

        // verify product
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.id == product.id && it.productLayoutType == AVAILABLE_SHOPPING_LIST }
        )

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_to_add_product_to_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_for_cta_okay),
                    type = TYPE_NORMAL,
                    event = ADD_WISHLIST
                )
            )

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
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .miniCartState
            .verifyIsError()
    }

    @Test
    fun `When adding a product recommendation to wishlist successfully then the result should return layout with new available product and success toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val productLayoutType = PRODUCT_RECOMMENDATION
        val product = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first { it.productLayoutType == productLayoutType }

        stubAddToWishlist(
            response = Success(data = AddToWishlistV2Response.Data.WishlistAddV2(success = true))
        )

        viewModel.addToWishlist(
            product = product
        )

        onSuccessAddingToWishlist(
            product = product
        )

        // verify product
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.id == product.id && it.productLayoutType == AVAILABLE_SHOPPING_LIST }
        )

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_to_add_product_to_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_for_cta_okay),
                    type = TYPE_NORMAL,
                    event = ADD_WISHLIST
                )
            )

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
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .miniCartState
            .verifyIsError()
    }

    @Test
    fun `When adding an unavailable product to wishlist successfully then the result should return layout with new that unavailable product and success toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val productLayoutType = UNAVAILABLE_SHOPPING_LIST
        val product = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first { it.productLayoutType == productLayoutType }

        stubAddToWishlist(
            response = Success(data = AddToWishlistV2Response.Data.WishlistAddV2(success = true))
        )

        viewModel.addToWishlist(
            product = product
        )

        onSuccessAddingToWishlist(
            product = product
        )

        // verify product
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.id == product.id && it.productLayoutType == UNAVAILABLE_SHOPPING_LIST }
        )

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_to_add_product_to_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_for_cta_okay),
                    type = TYPE_NORMAL,
                    event = ADD_WISHLIST
                )
            )

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
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .miniCartState
            .verifyIsError()
    }

    @Test
    fun `When adding a product recommendation to wishlist successfully but getting false for success then the result should return the same layout as before and failed toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val productLayoutType = PRODUCT_RECOMMENDATION
        val product = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first { it.productLayoutType == productLayoutType }

        stubAddToWishlist(
            response = Success(data = AddToWishlistV2Response.Data.WishlistAddV2(success = false))
        )

        viewModel.addToWishlist(
            product = product
        )

        onErrorAddingToWishlist(
            product = product
        )

        // verify product
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.id == product.id && it.productLayoutType == PRODUCT_RECOMMENDATION }
        )

        // other verifications
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_add_product_to_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = TYPE_ERROR,
                    event = ADD_WISHLIST,
                    any = product
                )
            )
    }

    @Test
    fun `When adding a product recommendation to wishlist failed from usecase then the result should return the same layout as before and failed toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val productLayoutType = PRODUCT_RECOMMENDATION
        val product = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first { it.productLayoutType == productLayoutType }

        stubAddToWishlist(
            response = Fail(throwable = Throwable())
        )

        viewModel.addToWishlist(
            product = product
        )

        onErrorAddingToWishlist(
            product = product
        )

        // verify product
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.id == product.id && it.productLayoutType == PRODUCT_RECOMMENDATION }
        )

        // other verifications
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_add_product_to_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = TYPE_ERROR,
                    event = ADD_WISHLIST,
                    any = product
                )
            )
    }

    @Test
    fun `When adding a product recommendation to wishlist failed then the result should return the same layout as before and failed toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val productLayoutType = PRODUCT_RECOMMENDATION
        val product = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first { it.productLayoutType == productLayoutType }

        stubAddToWishlist(
            throwable = Throwable()
        )

        viewModel.addToWishlist(
            product = product
        )

        onErrorAddingToWishlist(
            product = product
        )

        // verify product
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.id == product.id && it.productLayoutType == PRODUCT_RECOMMENDATION }
        )

        // other verifications
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_add_product_to_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = TYPE_ERROR,
                    event = ADD_WISHLIST,
                    any = product
                )
            )
    }
}
