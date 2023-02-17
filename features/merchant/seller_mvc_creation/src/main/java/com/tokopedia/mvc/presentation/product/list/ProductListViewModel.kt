package com.tokopedia.mvc.presentation.product.list

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.ProductListUseCase
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListEffect
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListEvent
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListUiState
import com.tokopedia.mvc.util.constant.NumberConstant
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase,
    private val productListUseCase: ProductListUseCase
) : BaseViewModel(dispatchers.main) {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ProductListEffect>(replay = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val currentState: ProductListUiState
        get() = _uiState.value

    fun processEvent(event: ProductListEvent) {
        when(event) {
            is ProductListEvent.FetchProducts -> {
                getProductsAndProductsMetadata(
                    event.pageMode,
                    event.voucherConfiguration,
                    event.selectedProducts,
                    event.showCtaUpdateProductOnToolbar,
                    event.isEntryPointFromVoucherSummaryPage,
                    event.selectedWarehouseId
                )
            }
            is ProductListEvent.MarkProductForDeletion -> handleMarkProductForDeletion(event.productId)
            ProductListEvent.TapContinueButton -> handleRedirection()
            ProductListEvent.DisableSelectAllCheckbox -> handleUncheckAllProduct()
            ProductListEvent.EnableSelectAllCheckbox -> handleCheckAllProduct()
            is ProductListEvent.TapVariant -> handleTapVariant(event.parentProduct)
            is ProductListEvent.VariantUpdated -> handleVariantUpdated(event.modifiedParentProductId, event.selectedVariantIds)
            is ProductListEvent.RemoveProductFromSelection -> handleRemoveProductFromSelection(event.productId)
            is ProductListEvent.TapRemoveProduct -> _uiEffect.tryEmit(ProductListEffect.ShowDeleteProductConfirmationDialog(event.productId))
            is ProductListEvent.ApplyRemoveProduct -> handleRemoveProduct(event.productId)
            ProductListEvent.TapBulkDeleteProduct -> {
                val productToDeleteCount = currentState.products.count { it.isSelected }
                _uiEffect.tryEmit(ProductListEffect.ShowBulkDeleteProductConfirmationDialog(productToDeleteCount))
            }
            ProductListEvent.ApplyBulkDeleteProduct -> handleBulkDeleteProducts()
            ProductListEvent.TapCtaChangeProduct -> handleSwitchPageMode()
            is ProductListEvent.AddNewProductToSelection -> handleAddNewProductToSelection(event.newProducts)
            ProductListEvent.TapCtaAddProduct -> handleCtaAddNewProduct()
            ProductListEvent.TapToolbarBackIcon -> handleTapToolbarBackIcon()
            ProductListEvent.TapBackButton -> handleTapBackButton()
        }
    }



    private fun getProductsAndProductsMetadata(
        pageMode: PageMode,
        voucherConfiguration: VoucherConfiguration,
        selectedProducts: List<SelectedProduct>,
        showCtaUpdateProductOnToolbar: Boolean,
        isEntryPointFromVoucherSummaryPage: Boolean,
        selectedWarehouseId: Long
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val action = if (pageMode == PageMode.CREATE) VoucherAction.CREATE else VoucherAction.UPDATE

                val metadataParam = GetInitiateVoucherPageUseCase.Param(action, voucherConfiguration.promoType, isVoucherProduct = true)
                val metadata = getInitiateVoucherPageUseCase.execute(metadataParam)

                val selectedParentProductIds = selectedProducts.map { it.parentProductId }
                val productListParam = ProductListUseCase.Param(
                    searchKeyword = "",
                    warehouseId = 0,
                    categoryIds = emptyList(),
                    showcaseIds = emptyList(),
                    page = NumberConstant.FIRST_PAGE,
                    pageSize = metadata.maxProduct,
                    sortId = "DEFAULT",
                    sortDirection = "DESC",
                    productIdInclude = selectedParentProductIds
                )

                val isCreateMode = pageMode == PageMode.CREATE

                //If previously selected products is empty, trigger empty state
                val updatedProducts = if (selectedProducts.isEmpty()) {
                    emptyList()
                } else {
                    val productsResponse = productListUseCase.execute(productListParam)
                    val updatedProducts = productsResponse.products.map { parentProduct ->
                        val variantIds = findVariantsIdsByParentId(parentProduct.id, selectedProducts)

                        parentProduct.copy(
                            originalVariants = toOriginalVariant(parentProduct.id, selectedProducts),
                            selectedVariantsIds = variantIds,
                            enableCheckbox = isCreateMode,
                            isDeletable = isCreateMode
                        )
                    }

                    updatedProducts
                }


                _uiState.update {
                    it.copy(
                        isLoading = false,
                        products = updatedProducts,
                        originalPageMode = pageMode,
                        currentPageMode = pageMode,
                        maxProductSelection = metadata.maxProduct,
                        voucherConfiguration = voucherConfiguration,
                        showCtaChangeProductOnToolbar = showCtaUpdateProductOnToolbar,
                        isEntryPointFromVoucherSummaryPage = isEntryPointFromVoucherSummaryPage,
                        selectedWarehouseId = selectedWarehouseId
                    )
                }

            },
            onError = { error ->
                _uiEffect.tryEmit(ProductListEffect.ShowError(error))
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        )
    }


    private fun toOriginalVariant(
        parentProductId: Long,
        selectedProducts: List<SelectedProduct>
    ): List<Product.Variant> {
        val matchedProduct = selectedProducts.find { it.parentProductId == parentProductId }
        return matchedProduct?.variantProductIds?.map { variantId ->
            Product.Variant(variantId, isEligible = true, reason = "", isSelected = true)
        }.orEmpty()
    }


    private fun findVariantsIdsByParentId(
        parentProductId: Long,
        selectedProducts: List<SelectedProduct>
    ): Set<Long> {
        val matchedProduct = selectedProducts.find { it.parentProductId == parentProductId }
        return matchedProduct?.variantProductIds?.toSet().orEmpty()
    }

    private fun handleCheckAllProduct() = launch(dispatchers.computation) {
        val selectedProducts = currentState.products.map { product ->
            val allVariantSelected = product.originalVariants.map { it.variantProductId }.toSet()
            product.copy(isSelected = true, selectedVariantsIds = allVariantSelected)
        }

        _uiState.update {
            it.copy(
                isSelectAllActive = true,
                selectedProductsIdsToBeRemoved = selectedProducts.selectedProductsOnly(),
                products = selectedProducts
            )
        }
    }

    private fun handleUncheckAllProduct() = launch(dispatchers.computation) {
        val disabledProducts = currentState.products.map { it.copy(isSelected = false) }
        _uiState.update {
            it.copy(
                isSelectAllActive = false,
                selectedProductsIdsToBeRemoved = emptySet(),
                products = disabledProducts
            )
        }
    }

    private fun handleMarkProductForDeletion(productIdToAdd: Long) = launch(dispatchers.computation) {
        val updatedProducts = updateProductAsSelected(productIdToAdd, currentState.products)

        _uiState.update {
            it.copy(
                selectedProductsIdsToBeRemoved = updatedProducts.selectedProductsOnly(),
                products = updatedProducts
            )
        }
    }

    private fun updateProductAsSelected(selectedProductId: Long, products: List<Product>) : List<Product> {
        return products.map {

            if (it.id == selectedProductId) {
                it.copy(isSelected = true)
            } else {
                it
            }
        }
    }

    private fun handleRemoveProductFromSelection(productIdToDelete: Long) {
        launch(dispatchers.computation) {
            val updatedProducts = currentState.products.map {
                if (it.id == productIdToDelete) {

                    val hasVariants = it.originalVariants.isNotEmpty()
                    if (hasVariants) {
                        it.copy(isSelected = false, selectedVariantsIds = it.originalVariants.map { it.variantProductId }.toSet())
                    } else {
                        it.copy(isSelected = false)
                    }

                } else {
                    it
                }
            }

            _uiState.update {
                it.copy(
                    isSelectAllActive = false,
                    products = updatedProducts,
                    selectedProductsIdsToBeRemoved = updatedProducts.selectedProductsOnly(),
                )
            }
        }
    }

    private fun handleRemoveProduct(productIdToDelete: Long) {
        launch(dispatchers.computation) {
            val updatedProducts = currentState.products.toMutableList()
            updatedProducts.removeFirst { it.id == productIdToDelete }

            _uiState.update {
                it.copy(
                    isSelectAllActive = false,
                    selectedProductsIdsToBeRemoved = updatedProducts.selectedProductsOnly(),
                    products = updatedProducts
                )
            }

            _uiEffect.tryEmit(ProductListEffect.ProductDeleted)
        }
    }

    private fun handleTapVariant(parentProduct: Product) {
        launch(dispatchers.computation) {
            val selectedVariantIds = parentProduct.originalVariants.map { it.variantProductId }.toList()
            val selectedProduct = SelectedProduct(parentProduct.id, parentProduct.selectedVariantsIds.toList())

            _uiEffect.tryEmit(
                ProductListEffect.ShowVariantBottomSheet(
                    parentProduct.isSelected,
                    selectedProduct,
                    selectedVariantIds,
                    currentState.currentPageMode
                )
            )
        }
    }

    private fun handleVariantUpdated(parentProductId: Long, newlySelectedVariantIds : Set<Long>) {
        launch(dispatchers.computation) {

            val isAllVariantRemoved = newlySelectedVariantIds.isEmpty()

            val updatedProducts = if (isAllVariantRemoved) {
                removeParentProduct(parentProductId)
            } else {
                updateVariants(parentProductId, currentState.products, newlySelectedVariantIds)
            }

            _uiState.update { it.copy(products = updatedProducts) }
        }
    }

    private fun removeParentProduct(parentProductIdToDelete: Long): List<Product> {
        val modifiedProducts = currentState.products.toMutableList()
        modifiedProducts.removeFirst { it.id == parentProductIdToDelete }
        return modifiedProducts
    }

    private fun updateVariants(
        parentProductIdToUpdate: Long,
        currentProducts: List<Product>,
        newlySelectedVariantIds: Set<Long>
    ): List<Product> {
        return currentProducts.map { parentProduct ->
            if (parentProduct.id == parentProductIdToUpdate) {
                parentProduct.copy(isSelected = true, selectedVariantsIds = newlySelectedVariantIds)
            } else {
                parentProduct
            }
        }
    }

    private fun handleRedirection() {
        launch(dispatchers.computation) {
            val selectedProducts = currentState.products.map { parentProduct ->
                SelectedProduct(parentProduct.id, parentProduct.selectedVariantsIds.toList())
            }

            val modifiedVoucherConfiguration = currentState.voucherConfiguration.copy(warehouseId = currentState.selectedWarehouseId)
            _uiEffect.tryEmit(
                ProductListEffect.ProceedToVoucherPreviewPage(
                    modifiedVoucherConfiguration,
                    selectedProducts,
                    currentState.originalPageMode
                )
            )

        }
    }


    private fun handleBulkDeleteProducts() {
        launch(dispatchers.computation) {
            val productIdsToDelete = currentState.products.selectedProductsOnly()

            val modifiedProducts = currentState.products.toMutableList()
            modifiedProducts.removeAll { it.id in productIdsToDelete }

            _uiState.update {
                it.copy(
                    products = modifiedProducts,
                    selectedProductsIdsToBeRemoved = modifiedProducts.selectedProductsOnly()
                )
            }

            _uiEffect.tryEmit(ProductListEffect.BulkDeleteProductSuccess(productIdsToDelete.count()))
        }
    }

    private fun handleSwitchPageMode() {
        launch(dispatchers.computation) {
            val modifiedProducts = currentState.products.map {
                it.copy(enableCheckbox = true, isDeletable = true)
            }
            _uiState.update { it.copy(products = modifiedProducts, currentPageMode = PageMode.CREATE) }
        }
    }

    private fun handleAddNewProductToSelection(newProducts: List<Product>) {
        launch(dispatchers.computation) {
            val toBeRemoveProductIds = currentState.selectedProductsIdsToBeRemoved

            val updatedProducts = newProducts.map { newProduct ->
                if (newProduct.id !in toBeRemoveProductIds) {
                    //Newly added product should not be marked for removal
                    newProduct.copy(isSelected = false)
                } else {
                    newProduct
                }
            }

            _uiState.update { it.copy(products = updatedProducts) }
        }
    }

    private fun List<Product>.selectedProductsOnly(): Set<Long> {
        return filter { it.isSelected }.map { it.id }.toSet()
    }


    private fun handleCtaAddNewProduct() {
        val originalPageMode = currentState.originalPageMode
        val isEntryPointFromVoucherSummaryPage = currentState.isEntryPointFromVoucherSummaryPage

        launch(dispatchers.computation) {
            if (originalPageMode == PageMode.CREATE && isEntryPointFromVoucherSummaryPage) {
                emitRedirectToAddProductPageEvent()
                return@launch
            }

            if (originalPageMode  == PageMode.CREATE) {
                _uiEffect.tryEmit(ProductListEffect.BackToPreviousPage)
                return@launch
            }

            if (originalPageMode == PageMode.EDIT) {
                emitRedirectToAddProductPageEvent()
            }
        }

    }

    private fun emitRedirectToAddProductPageEvent() {
        val currentlySelectedParentProduct = currentState.products.map { it.id }
        val modifiedVoucherConfiguration = currentState.voucherConfiguration.copy(productIds = currentlySelectedParentProduct)
        _uiEffect.tryEmit(ProductListEffect.RedirectToAddProductPage(modifiedVoucherConfiguration, currentState.products))
    }


    private fun handleTapToolbarBackIcon() {
        val selectedProductCount = uiState.value.products.count()
        _uiEffect.tryEmit(ProductListEffect.RedirectToPreviousPage(selectedProductCount, uiState.value.originalPageMode))
    }

    private fun handleTapBackButton() {
        _uiEffect.tryEmit(ProductListEffect.TapBackButton(uiState.value.originalPageMode))
    }
}
