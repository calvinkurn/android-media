package com.tokopedia.mvc.presentation.product.variant.select

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.Variant
import com.tokopedia.mvc.domain.entity.VariantResult
import com.tokopedia.mvc.domain.usecase.ProductV3UseCase
import com.tokopedia.mvc.presentation.product.variant.select.uimodel.SelectVariantEffect
import com.tokopedia.mvc.presentation.product.variant.select.uimodel.SelectVariantEvent
import com.tokopedia.mvc.presentation.product.variant.select.uimodel.SelectVariantUiState
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
                _uiState.update { it.copy(isLoading = true, parentProductId = event.selectedParentProduct.id) }
                getAllVariantsByParentProductId(event.selectedParentProduct)
            }
            is SelectVariantEvent.AddProductToSelection -> handleAddProductToSelection(event.variantProductId)
            SelectVariantEvent.DisableSelectAllCheckbox -> handleUncheckAllProduct()
            SelectVariantEvent.EnableSelectAllCheckbox -> handleCheckAllProduct()
            is SelectVariantEvent.RemoveProductFromSelection -> handleRemoveProductFromSelection(event.variantProductId)
            SelectVariantEvent.TapSelectButton -> {
                _uiEffect.tryEmit(SelectVariantEffect.ConfirmUpdateVariant(currentState.selectedVariantIds))
            }
        }
    }


    private fun getAllVariantsByParentProductId(selectedParentProduct : Product) {
        launchCatchError(
            dispatchers.io,
            block = {
                val params = ProductV3UseCase.Param(selectedParentProduct.id)
                val response = productV3UseCase.execute(params)

                val modifiedVariantNames = findUpdatedVariantNames(response)

                val validatedVariants = modifiedVariantNames.map { variant ->
                    val (isEligible, reason) = isVariantEligible(variant.variantId, selectedParentProduct.originalVariants)
                    variant.copy(isEligible = isEligible, reason = reason)
                }

                val originalVariantIds = selectedParentProduct.originalVariants.map { it.variantProductId }
                val eligibleVariantsOnly = validatedVariants.filter { it.variantId in originalVariantIds }

                val updatedVariants = eligibleVariantsOnly.map {
                    if (it.variantId in selectedParentProduct.selectedVariantsIds && it.isEligible) {
                        it.copy(isSelected = true)
                    } else {
                        it
                    }
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        parentProductName = response.parentProductName,
                        parentProductStock = selectedParentProduct.stock,
                        parentProductPrice = response.parentProductPrice,
                        parentProductSoldCount = response.parentProductSoldCount,
                        parentProductImageUrl = response.parentProductImageUrl,
                        variants = updatedVariants,
                        selectedVariantIds = updatedVariants.selectedVariantsOnly()
                    )
                }
            },
            onError = { error ->
                _uiEffect.tryEmit(SelectVariantEffect.ShowError(error))
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        )
    }

    private fun isVariantEligible(variantId : Long, validatedVariants: List<Product.Variant>): Pair<Boolean, String> {
        val matchedVariant = validatedVariants.find { variant -> variant.variantProductId == variantId } ?: return Pair(false, "")
        return Pair(matchedVariant.isEligible, matchedVariant.reason)
    }

    private fun findUpdatedVariantNames(response: VariantResult): List<Variant> {
        val selections = response.selections
        return response.products.map { variant ->
            val variantName = variant.combinations.mapIndexed { index, combination ->
                selections[index].options[combination].value
            }
            val formattedVariantName = variantName.joinToString(separator = " | ") { it }
            variant.copy(variantName = formattedVariantName)
        }
    }

    private fun handleCheckAllProduct() = launch(dispatchers.computation) {
        val variants = currentState.variants.map { it.copy(isSelected = it.isEligible) }

        _uiState.update {
            it.copy(
                isSelectAllActive = true,
                variants = variants,
                selectedVariantIds = variants.selectedVariantsOnly()
            )
        }
    }

    private fun handleUncheckAllProduct() = launch(dispatchers.computation) {
        val variants = currentState.variants.map { it.copy(isSelected = false) }

        _uiState.update {
            it.copy(
                isSelectAllActive = false,
                variants = variants,
                selectedVariantIds = emptySet()
            )
        }
    }

    private fun handleAddProductToSelection(variantProductIdToAdd: Long) {
        launch(dispatchers.computation) {
            val modifiedVariants = currentState.variants.map { variant ->
                if (variantProductIdToAdd == variant.variantId) {
                    variant.copy(isSelected = true)
                } else {
                    variant
                }
            }
            _uiState.update { it.copy(variants = modifiedVariants, selectedVariantIds = modifiedVariants.selectedVariantsOnly()) }
        }
    }


    private fun handleRemoveProductFromSelection(variantProductIdToDelete: Long) {
        launch(dispatchers.computation) {
            val modifiedVariants = currentState.variants.map { variant ->
                if (variantProductIdToDelete == variant.variantId) {
                    variant.copy(isSelected = false)
                } else {
                    variant
                }
            }
            _uiState.update { it.copy(variants = modifiedVariants, selectedVariantIds = modifiedVariants.selectedVariantsOnly()) }
        }
    }

    private fun List<Variant>.selectedVariantsOnly(): Set<Long> {
        return filter { it.isSelected }.map { it.variantId }.toSet()
    }
}
