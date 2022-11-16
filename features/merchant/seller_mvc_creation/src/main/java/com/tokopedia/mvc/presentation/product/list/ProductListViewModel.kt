package com.tokopedia.mvc.presentation.product.list

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.ProductListUseCase
import com.tokopedia.mvc.domain.usecase.ShopBasicDataUseCase
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
    private val shopBasicDataUseCase: ShopBasicDataUseCase,
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
            is ProductListEvent.FetchProducts -> getProductsAndProductsMetadata(event.action, event.promoType, event.selectedProductIds)
            is ProductListEvent.AddProductToSelection -> handleAddProductToSelection(event.productId)
            ProductListEvent.ConfirmAddProduct -> handleConfirmAddProduct()
            ProductListEvent.DisableSelectAllCheckbox -> handleUncheckAllProduct()
            ProductListEvent.EnableSelectAllCheckbox -> handleCheckAllProduct()
            is ProductListEvent.TapVariant -> handleTapVariant(event.parentProduct)
            is ProductListEvent.VariantUpdated -> handleVariantUpdated(event.modifiedParentProductId, event.selectedVariantIds)
            is ProductListEvent.RemoveProductFromSelection -> handleRemoveProductFromSelection(event.productId)
            is ProductListEvent.RemoveProduct -> handleRemoveProduct(event.productId)
        }
    }

    private fun getProductsAndProductsMetadata(
        action: VoucherAction,
        promoType: PromoType,
        selectedProductIds: List<Long>
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val metadataParam = GetInitiateVoucherPageUseCase.Param(action, promoType, isVoucherProduct = true)
                val metadata = getInitiateVoucherPageUseCase.execute(metadataParam)

                val productListParam = ProductListUseCase.Param(
                    searchKeyword = "",
                    warehouseId = 0,
                    categoryIds = emptyList(),
                    showcaseIds = emptyList(),
                    page = NumberConstant.FIRST_PAGE,
                    pageSize = metadata.maxProduct,
                    sortId = "DEFAULT",
                    sortDirection = "DESC",
                    productIdInclude = selectedProductIds
                )

                val productsResponse = productListUseCase.execute(productListParam)

                _uiState.update {
                    it.copy(isLoading = false, products = productsResponse.products)
                }

            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        )
    }


    private fun handleCheckAllProduct() = launch(dispatchers.computation) {
        val selectedProducts = currentState.products.map { product ->
            product.copy(isSelected = true)
        }

        val selectedProductIds = selectedProducts.filter { it.isSelected }.map { it.id }.toSet()

        _uiState.update {
            it.copy(
                isSelectAllActive = true,
                selectedProductsIds = selectedProductIds,
                products = selectedProducts
            )
        }
    }

    private fun handleUncheckAllProduct() = launch(dispatchers.computation) {
        val disabledProducts = currentState.products.map { it.copy(isSelected = false) }
        _uiState.update {
            it.copy(
                isSelectAllActive = false,
                selectedProductsIds = emptySet(),
                products = disabledProducts
            )
        }
    }

    private fun handleAddProductToSelection(productIdToAdd: Long) = launch(dispatchers.computation) {
        val updatedProducts = updateProductAsSelected(productIdToAdd, currentState.products)
        val updatedProductIds = updatedProducts.filter { it.isSelected }.map { it.id }.toSet()

        _uiState.update {
            it.copy(
                selectedProductsIds = updatedProductIds,
                products = updatedProducts
            )
        }
    }

    private fun updateProductAsSelected(selectedProductId: Long, products: List<Product>) : List<Product> {
        return products.map {

            if (it.id == selectedProductId) {

                val hasVariants = it.originalVariants.isNotEmpty()
                if (hasVariants) {
                    val eligibleVariantsOnly = it.originalVariants.filter { it.isEligible }.map { it.variantProductId }.toSet()
                    it.copy(isSelected = true, enableCheckbox = true, originalVariants = it.originalVariants, selectedVariantsIds = eligibleVariantsOnly)
                } else {
                    it.copy(isSelected = true, enableCheckbox = true)
                }

            } else {
                it
            }
        }
    }

    private fun handleRemoveProductFromSelection(productIdToDelete: Long) = launch(dispatchers.computation) {
        val updatedProducts = currentState.products.map {
            if (it.id == productIdToDelete) {

                val hasVariants = it.originalVariants.isNotEmpty()
                if (hasVariants) {
                    it.copy(isSelected = false, enableCheckbox = true, originalVariants = it.originalVariants, selectedVariantsIds = it.originalVariants.map { it.variantProductId }.toSet())
                } else {
                    it.copy(isSelected = false, enableCheckbox = true)
                }

            } else {
                it.copy(enableCheckbox = true)
            }
        }

        val updatedSelectedProducts = currentState.selectedProductsIds.toMutableSet()
        updatedSelectedProducts.remove(productIdToDelete)

        _uiState.update {
            it.copy(
                isSelectAllActive = false,
                selectedProductsIds = updatedSelectedProducts,
                products = updatedProducts
            )
        }
    }

    private fun handleRemoveProduct(productIdToDelete: Long) = launch(dispatchers.computation) {
        val updatedProducts = currentState.products.toMutableList()
        updatedProducts.removeFirst { it.id == productIdToDelete }

        val updatedSelectedProductIds = updatedProducts.filter { it.isSelected }.map { it.id }.toMutableSet()
        updatedSelectedProductIds.remove(productIdToDelete)

        _uiState.update {
            it.copy(
                isSelectAllActive = false,
                selectedProductsIds = updatedSelectedProductIds,
                products = updatedProducts
            )
        }
    }

    private fun handleTapVariant(parentProduct: Product) {
        _uiEffect.tryEmit(ProductListEffect.ShowVariantBottomSheet(parentProduct))
    }

    private fun handleVariantUpdated(parentProductId: Long, selectedVariantIds : Set<Long>) {
        launch(dispatchers.computation) {

            val updatedProducts = currentState.products.map { parentProduct ->
                if (parentProduct.id == parentProductId) {
                    parentProduct.copy(selectedVariantsIds = selectedVariantIds)
                } else {
                    parentProduct
                }
            }

            _uiState.update { it.copy(products = updatedProducts) }
        }
    }


    private fun handleConfirmAddProduct() {
        launchCatchError(
            dispatchers.io,
            block = {
                val response = shopBasicDataUseCase.execute()

                val selectedProducts = currentState.products.filter { it.isSelected }
                val topSellingProductImageUrls = selectedProducts
                    .filter { it.isSelected }
                    .sortedByDescending { it.txStats.sold }
                    .map { it.picture }

                _uiEffect.tryEmit(
                    ProductListEffect.ConfirmAddProduct(
                        selectedProducts,
                        topSellingProductImageUrls,
                        response
                    )
                )

            },
            onError = { error ->
                _uiState.update { it.copy(error = error) }
            }
        )
    }

}
