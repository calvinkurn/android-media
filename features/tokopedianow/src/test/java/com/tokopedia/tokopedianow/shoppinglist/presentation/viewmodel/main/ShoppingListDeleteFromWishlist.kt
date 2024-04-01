package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.main

import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.SHOW
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.countSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.modifyProduct
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.sumPriceSelectedItems
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.removeProduct
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.BottomBulkAtcModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel.Event.DELETE_WISHLIST
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.UNAVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import org.junit.Test

class ShoppingListDeleteFromWishlist: TokoNowShoppingListViewModelFixture() {
    private fun onSuccessDeletingFromWishlist(
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        if (product.productLayoutType == AVAILABLE_SHOPPING_LIST) availableProducts.removeProduct(product.id) else unavailableProducts.removeProduct(product.id)

        updateLayout()
    }

    private fun onErrorDeletingFromWishlist(
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        mutableLayout
            .modifyProduct(
                productId = product.id,
                state = SHOW
            )
    }

    @Test
    fun `When removing an available product from wishlist successfully then the result should return layout without that available product and success toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val productLayoutType = AVAILABLE_SHOPPING_LIST
        val product = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first { it.productLayoutType == productLayoutType }

        stubDeleteFromWishlist(
            response = Success(data = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true))
        )

        viewModel.deleteFromWishlist(
            product = product
        )

        onSuccessDeletingFromWishlist(
            product = product
        )

        // verify product
        verifyIsTrue(
            expectedResult = !mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.id == product.id && it.productLayoutType == productLayoutType }
        )

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_to_delete_product_from_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_for_cta_cancel),
                    type = Toaster.TYPE_NORMAL,
                    event = DELETE_WISHLIST,
                    any = product
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
    fun `When removing an available product from wishlist successfully but getting false for success then the result should return layout with that available product and failed toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val productLayoutType = AVAILABLE_SHOPPING_LIST
        val product = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first { it.productLayoutType == productLayoutType }

        stubDeleteFromWishlist(
            response = Success(data = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = false))
        )

        viewModel.deleteFromWishlist(
            product = product
        )

        onErrorDeletingFromWishlist(
            product = product
        )

        // verify product
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.id == product.id && it.productLayoutType == productLayoutType }
        )

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_delete_product_from_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = Toaster.TYPE_ERROR,
                    event = DELETE_WISHLIST,
                    any = product
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
    fun `When removing an available product from wishlist failed from usecase then the result should return layout with that available product and failed toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val productLayoutType = AVAILABLE_SHOPPING_LIST
        val product = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first { it.productLayoutType == productLayoutType }

        stubDeleteFromWishlist(
            response = Fail(Throwable())
        )

        viewModel.deleteFromWishlist(
            product = product
        )

        onErrorDeletingFromWishlist(
            product = product
        )

        // verify product
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.id == product.id && it.productLayoutType == productLayoutType }
        )

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_delete_product_from_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = Toaster.TYPE_ERROR,
                    event = DELETE_WISHLIST,
                    any = product
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
    fun `When removing an available product from wishlist failed then the result should return layout with that available product and failed toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val productLayoutType = AVAILABLE_SHOPPING_LIST
        val product = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first { it.productLayoutType == productLayoutType }

        stubDeleteFromWishlist(
            throwable = Throwable()
        )

        viewModel.deleteFromWishlist(
            product = product
        )

        onErrorDeletingFromWishlist(
            product = product
        )

        // verify product
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.id == product.id && it.productLayoutType == productLayoutType }
        )

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_delete_product_from_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = Toaster.TYPE_ERROR,
                    event = DELETE_WISHLIST,
                    any = product
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
    fun `When removing an unavailable product from wishlist successfully then the result should return layout without that available product and success toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val productLayoutType = UNAVAILABLE_SHOPPING_LIST
        val product = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first { it.productLayoutType == productLayoutType }

        stubDeleteFromWishlist(
            response = Success(data = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true))
        )

        viewModel.deleteFromWishlist(
            product = product
        )

        onSuccessDeletingFromWishlist(
            product = product
        )

        // verify product
        verifyIsTrue(
            expectedResult = !mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.id == product.id && it.productLayoutType == productLayoutType }
        )

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_to_delete_product_from_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_for_cta_cancel),
                    type = Toaster.TYPE_NORMAL,
                    event = DELETE_WISHLIST,
                    any = product
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
    fun `When removing an unavailable product from wishlist successfully but getting false for success then the result should return layout with that available product and failed toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val productLayoutType = UNAVAILABLE_SHOPPING_LIST
        val product = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first { it.productLayoutType == productLayoutType }

        stubDeleteFromWishlist(
            response = Success(data = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = false))
        )

        viewModel.deleteFromWishlist(
            product = product
        )

        onErrorDeletingFromWishlist(
            product = product
        )

        // verify product
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.id == product.id && it.productLayoutType == productLayoutType }
        )

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_delete_product_from_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = Toaster.TYPE_ERROR,
                    event = DELETE_WISHLIST,
                    any = product
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
    fun `When removing an unavailable product from wishlist failed from usecase then the result should return layout with that available product and failed toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val productLayoutType = UNAVAILABLE_SHOPPING_LIST
        val product = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first { it.productLayoutType == productLayoutType }

        stubDeleteFromWishlist(
            response = Fail(Throwable())
        )

        viewModel.deleteFromWishlist(
            product = product
        )

        onErrorDeletingFromWishlist(
            product = product
        )

        // verify product
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.id == product.id && it.productLayoutType == productLayoutType }
        )

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_delete_product_from_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = Toaster.TYPE_ERROR,
                    event = DELETE_WISHLIST,
                    any = product
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
    fun `When removing an unavailable product from wishlist failed then the result should return layout with that available product and failed toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val productLayoutType = UNAVAILABLE_SHOPPING_LIST
        val product = mutableLayout.filterIsInstance<ShoppingListHorizontalProductCardItemUiModel>().first { it.productLayoutType == productLayoutType }

        stubDeleteFromWishlist(
            throwable = Throwable()
        )

        viewModel.deleteFromWishlist(
            product = product
        )

        onErrorDeletingFromWishlist(
            product = product
        )

        // verify product
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.id == product.id && it.productLayoutType == productLayoutType }
        )

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_delete_product_from_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = Toaster.TYPE_ERROR,
                    event = DELETE_WISHLIST,
                    any = product
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
}
