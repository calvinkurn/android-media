package com.tokopedia.mvc.presentation.product.variant

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.Variant
import com.tokopedia.mvc.domain.entity.VariantResult
import com.tokopedia.mvc.domain.usecase.ProductV3UseCase
import com.tokopedia.mvc.presentation.product.variant.uimodel.SelectVariantEffect
import com.tokopedia.mvc.presentation.product.variant.uimodel.SelectVariantEvent
import com.tokopedia.mvc.presentation.product.variant.uimodel.SelectVariantUiState
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectVariantViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val productV3UseCase: ProductV3UseCase
) : BaseViewModel(dispatchers.main) {

    private val _uiState = MutableStateFlow(SelectVariantUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SelectVariantEffect>(replay = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val currentState: SelectVariantUiState
        get() = _uiState.value

    fun processEvent(event: SelectVariantEvent) {
        when(event) {
            is SelectVariantEvent.FetchProductVariants -> {
                _uiState.update { it.copy(isLoading = true, parentProduct = event.selectedParentProduct) }
                getVariantDetail(event.selectedParentProduct)
            }
            is SelectVariantEvent.AddProductToSelection -> handleAddProductToSelection(event.variantProductId)
            SelectVariantEvent.DisableSelectAllCheckbox -> handleUncheckAllProduct()
            SelectVariantEvent.EnableSelectAllCheckbox -> handleCheckAllProduct()
            is SelectVariantEvent.RemoveProductFromSelection -> handleRemoveProductFromSelection(event.variantProductId)
            SelectVariantEvent.TapSelectButton -> {
                _uiEffect.tryEmit(SelectVariantEffect.ConfirmUpdateVariant(currentState.parentProduct.modifiedVariants))
            }
        }
    }



    private fun getVariantDetail(selectedParentProduct : Product) {
        launchCatchError(
            dispatchers.io,
            block = {
                val params = ProductV3UseCase.Param(selectedParentProduct.id, 0 )
                val response = productV3UseCase.execute(params)

                //Update variants of the selected parent product
                val modifiedVariants = findUpdatedVariantNames(selectedParentProduct, response)
                val modifiedParentProduct = selectedParentProduct.copy(modifiedVariants = modifiedVariants)

                _uiState.update { it.copy(isLoading = false, parentProduct = modifiedParentProduct) }
            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        )
    }

    private fun findUpdatedVariantNames(originalProduct: Product, remoteVariants: VariantResult): List<Product.Variant> {
        val selections = remoteVariants.selections
        val updatedVariantNames = remoteVariants.products.map { variant ->
            val variantName = variant.combinations.mapIndexed { index, combination ->
                selections[index].options[combination].value
            }
            val formattedVariantName = variantName.joinToString(separator = " | ") { it }
            variant.copy(variantName = formattedVariantName)
        }


        //Update variants of the selected parent product
        return originalProduct.modifiedVariants.map {
            val variantNewName = findUpdatedVariantName(it.variantProductId, updatedVariantNames)
            it.copy(productName = variantNewName)
        }

    }

    private fun findUpdatedVariantName(
        selectedVariantId: Long,
        updatedVariants: List<Variant>
    ): String {
        val matchedVariant = updatedVariants.find { selectedVariantId == it.variantId }
        return matchedVariant?.variantName.orEmpty()
    }

    private fun handleCheckAllProduct() = launch(dispatchers.computation) {
        val modifiedVariants = currentState.parentProduct.modifiedVariants
            .map { it.copy(isSelected = it.isEligible) }
        val modifiedParentProduct = currentState.parentProduct.copy(modifiedVariants = modifiedVariants)

        _uiState.update {
            it.copy(
                isSelectAllActive = true,
                parentProduct = modifiedParentProduct
            )
        }
    }

    private fun handleUncheckAllProduct() = launch(dispatchers.computation) {
        val modifiedVariants = currentState.parentProduct.modifiedVariants.map { it.copy(isSelected = false) }
        val modifiedParentProduct = currentState.parentProduct.copy(modifiedVariants = modifiedVariants)

        _uiState.update {
            it.copy(
                isSelectAllActive = false,
                parentProduct = modifiedParentProduct
            )
        }
    }

    private fun handleAddProductToSelection(variantProductIdToAdd: Long) {
        launch(dispatchers.computation) {
            val modifiedVariants = currentState.parentProduct.modifiedVariants.map { variant ->
                if (variantProductIdToAdd == variant.variantProductId) {
                    variant.copy(isSelected = true)
                } else {
                    variant
                }
            }
            val modifiedParentProduct = currentState.parentProduct.copy(modifiedVariants = modifiedVariants)
            _uiState.update { it.copy(parentProduct = modifiedParentProduct) }
        }
    }


    private fun handleRemoveProductFromSelection(variantProductIdToDelete: Long) {
        launch(dispatchers.computation) {
            val modifiedVariants = currentState.parentProduct.modifiedVariants.map { variant ->
                if (variantProductIdToDelete == variant.variantProductId) {
                    variant.copy(isSelected = false)
                } else {
                    variant
                }
            }
            val modifiedParentProduct = currentState.parentProduct.copy(modifiedVariants = modifiedVariants)
            _uiState.update { it.copy(parentProduct = modifiedParentProduct) }
        }
    }

}
