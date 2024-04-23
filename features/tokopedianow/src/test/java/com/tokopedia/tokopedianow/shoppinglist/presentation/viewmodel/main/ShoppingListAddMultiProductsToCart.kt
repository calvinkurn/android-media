package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.main

import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.data.createMiniCartSimplifier
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.modifyExpandCollapseProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.modifyTopCheckAll
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.updateProductSelections
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel.Event.ADD_MULTI_PRODUCTS_TO_CART
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Test

class ShoppingListAddMultiProductsToCart: TokoNowShoppingListViewModelFixture() {
    private fun selectAllAvailableProducts() {
        val isSelected = true

        viewModel.selectAllAvailableProducts(
            isSelected = isSelected
        )

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
    }

    private fun mapMiniCartData(): MiniCartSimplifiedData {
        val miniCartItems = mutableMapOf<MiniCartItemKey, MiniCartItem.MiniCartItemProduct>()
        availableProducts.forEach { item ->
            miniCartItems[MiniCartItemKey(item.id)] = MiniCartItem.MiniCartItemProduct(
                productId = item.id,
                quantity = item.minOrder,
                cartId = item.id.toIntSafely().inc().toString(),
            )
        }
        val miniCartData = createMiniCartSimplifier()
            .copy(
                miniCartItems = miniCartItems
            )
        mMiniCartData = miniCartData
        return miniCartData
    }

    @Test
    fun `When adding multiple products to cart and getting mini cart data successfully then the result should return layout without those products and return success toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        selectAllAvailableProducts()

        val miniCartData = mapMiniCartData()

        // stub section
        stubAddMultiProductsToCart(
            response = Success(AtcMultiData(atcMulti = AtcMultiData.AtcMulti(buyAgainData = AtcMultiData.AtcMulti.BuyAgainData(success = 1))))
        )

        stubGetMiniCart(
            response = miniCartData
        )

        // add multi product
        viewModel.addMultiProductsToCart { /* nothing to do */ }

        // update expected layout
        updateLayout()

        // verify existing available products
        verifyIsTrue(
            expectedResult = !mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.productLayoutType == ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST }
        )

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_to_add_product_to_cart, filteredAvailableProducts.size),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_for_cta_okay),
                    type = Toaster.TYPE_NORMAL,
                    event = ADD_MULTI_PRODUCTS_TO_CART,
                    any = mMiniCartData
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
            .isProductAvailable
            .verifyValue(
                false
            )

        viewModel
            .isLoaderDialogShown
            .verifyValue(
                false
            )

        viewModel
            .miniCartState
            .verifySuccess(miniCartData)
    }

    @Test
    fun `When adding multiple products to cart and getting mini cart data successfully but mini cart data has the same data as the previous one then the result should return the same layout and return the success toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        selectAllAvailableProducts()

        // stub section
        stubAddMultiProductsToCart(
            response = Success(AtcMultiData(atcMulti = AtcMultiData.AtcMulti(buyAgainData = AtcMultiData.AtcMulti.BuyAgainData(success = 1))))
        )

        // add multi product
        viewModel.addMultiProductsToCart { /* nothing to do */ }

        // verify existing available products
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.productLayoutType == ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST }
        )

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_to_add_product_to_cart, filteredAvailableProducts.size),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_for_cta_okay),
                    type = Toaster.TYPE_NORMAL,
                    event = ADD_MULTI_PRODUCTS_TO_CART,
                    any = mMiniCartData
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
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .isLoaderDialogShown
            .verifyValue(
                false
            )

        viewModel
            .miniCartState
            .verifyIsError()
    }

    @Test
    fun `When adding multiple products to cart successfully but getting mini cart data failed message error then the result should be the same as before`() {
        loadLayout(
            needExpandCollapse = false
        )

        selectAllAvailableProducts()

        // stub section
        stubAddMultiProductsToCart(
            response = Success(AtcMultiData(atcMulti = AtcMultiData.AtcMulti(buyAgainData = AtcMultiData.AtcMulti.BuyAgainData(success = 1))))
        )

        stubGetMiniCart(
            throwable = MessageErrorException()
        )

        // add multi product
        viewModel.addMultiProductsToCart { /* nothing to do */ }

        // verify existing available products
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.productLayoutType == ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST }
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
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .isLoaderDialogShown
            .verifyValue(
                false
            )

        viewModel
            .miniCartState
            .verifyIsError()
    }

    @Test
    fun `When adding multiple products to cart successfully but getting mini cart data failed throwable then the result should be the same as before`() {
        loadLayout(
            needExpandCollapse = false
        )

        selectAllAvailableProducts()

        // stub section
        stubAddMultiProductsToCart(
            response = Success(AtcMultiData(atcMulti = AtcMultiData.AtcMulti(buyAgainData = AtcMultiData.AtcMulti.BuyAgainData(success = 1))))
        )

        stubGetMiniCart(
            throwable = Throwable()
        )

        // add multi product
        viewModel.addMultiProductsToCart { /* nothing to do */ }

        // verify product
        verifyIsTrue(
            expectedResult = mutableLayout.any { it is ShoppingListHorizontalProductCardItemUiModel && it.productLayoutType == ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST }
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
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .isLoaderDialogShown
            .verifyValue(
                false
            )

        viewModel
            .miniCartState
            .verifyIsError()
    }

    @Test
    fun `When adding multiple products to cart successfully but success value is zero then the result should be the same as before and return failed toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val errorMessage = String.EMPTY

        stubAddMultiProductsToCart(
            response = Success(AtcMultiData(atcMulti = AtcMultiData.AtcMulti(buyAgainData = AtcMultiData.AtcMulti.BuyAgainData(success = 0))))
        )

        viewModel.addMultiProductsToCart { /* nothing to do */ }

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = errorMessage,
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = Toaster.TYPE_ERROR,
                    event = ADD_MULTI_PRODUCTS_TO_CART
                )
            )

        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout,
                    isRequiredToScrollUp = true
                )
            )

        viewModel
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .isLoaderDialogShown
            .verifyValue(
                false
            )

        viewModel
            .miniCartState
            .verifyIsError()
    }

    @Test
    fun `When adding multiple products to cart successfully but success value is zero and message is not blank then the result should be the same as before and return failed toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        val errorMessage = "first error"

        stubAddMultiProductsToCart(
            response = Success(AtcMultiData(atcMulti = AtcMultiData.AtcMulti(buyAgainData = AtcMultiData.AtcMulti.BuyAgainData(success = 0, message = listOf(errorMessage)))))
        )

        viewModel.addMultiProductsToCart { /* nothing to do */ }

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = errorMessage,
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = Toaster.TYPE_ERROR,
                    event = ADD_MULTI_PRODUCTS_TO_CART
                )
            )

        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout,
                    isRequiredToScrollUp = true
                )
            )

        viewModel
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .isLoaderDialogShown
            .verifyValue(
                false
            )

        viewModel
            .miniCartState
            .verifyIsError()
    }

    @Test
    fun `When adding multiple products to cart failed from response then the result should be the same as before and return failed toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        stubAddMultiProductsToCart(
            response = Fail(throwable = Throwable())
        )

        viewModel.addMultiProductsToCart { /* nothing to do */ }

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_add_product_to_cart),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = Toaster.TYPE_ERROR,
                    event = ADD_MULTI_PRODUCTS_TO_CART
                )
            )

        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout,
                    isRequiredToScrollUp = true
                )
            )

        viewModel
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .isLoaderDialogShown
            .verifyValue(
                false
            )

        viewModel
            .miniCartState
            .verifyIsError()
    }

    @Test
    fun `When adding multiple products to cart failed then the result should be the same as before and return failed toaster`() {
        loadLayout(
            needExpandCollapse = false
        )

        stubAddMultiProductsToCart(
            throwable = Throwable()
        )

        viewModel.addMultiProductsToCart { /* nothing to do */ }

        // other verifications
        viewModel
            .toasterData
            .verifyValue(
                ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_add_product_to_cart),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = Toaster.TYPE_ERROR,
                    event = ADD_MULTI_PRODUCTS_TO_CART
                )
            )

        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout,
                    isRequiredToScrollUp = true
                )
            )

        viewModel
            .isProductAvailable
            .verifyValue(
                true
            )

        viewModel
            .isLoaderDialogShown
            .verifyValue(
                false
            )

        viewModel
            .miniCartState
            .verifyIsError()
    }
}
