package com.tokopedia.mvc.presentation.product.variant.review

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.Variant
import com.tokopedia.mvc.domain.entity.VariantResult
import com.tokopedia.mvc.domain.usecase.ProductV3UseCase
import com.tokopedia.mvc.presentation.product.variant.review.uimodel.ReviewVariantEffect
import com.tokopedia.mvc.presentation.product.variant.review.uimodel.ReviewVariantEvent
import com.tokopedia.mvc.presentation.product.variant.review.uimodel.ReviewVariantUiState
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReviewVariantViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val productV3UseCase: ProductV3UseCase
) : BaseViewModel(dispatchers.main) {

    private val _uiState = MutableStateFlow(ReviewVariantUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ReviewVariantEffect>(replay = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val currentState: ReviewVariantUiState
        get() = _uiState.value

    fun processEvent(event: ReviewVariantEvent) {
        when(event) {
            is ReviewVariantEvent.FetchProductVariants -> {
                handleFetchProductVariants(
                    event.originalVariantIds,
                    event.isParentProductSelected,
                    event.selectedProduct
                )
            }
            is ReviewVariantEvent.AddVariantToSelection -> handleAddVariantToSelection(event.variantProductId)
            is ReviewVariantEvent.RemoveVariantFromSelection -> handleRemoveVariantFromSelection(event.variantProductId)
            ReviewVariantEvent.DisableSelectAllCheckbox -> handleUncheckAllVariant()
            ReviewVariantEvent.EnableSelectAllCheckbox -> handleCheckAllVariant()
            ReviewVariantEvent.TapSelectButton -> _uiEffect.tryEmit(ReviewVariantEffect.ConfirmUpdateVariant(currentState.selectedVariantIds))
            is ReviewVariantEvent.RemoveVariant -> handleRemoveVariant(event.productId)
            is ReviewVariantEvent.BulkDeleteVariant -> handleBulkDeleteVariant()
        }
    }

    private fun handleFetchProductVariants(
        originalVariantIds: List<Long>,
        isParentProductSelected: Boolean,
        selectedProduct: SelectedProduct
    ) {
        _uiState.update {
            it.copy(
                isLoading = true,
                parentProductId = selectedProduct.parentProductId,
                originalVariantIds = originalVariantIds
            )
        }
        getVariantDetail(selectedProduct, isParentProductSelected, originalVariantIds)
    }

    private fun getVariantDetail(
        selectedProduct: SelectedProduct,
        isParentProductSelected: Boolean,
        originalVariantIds: List<Long>
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val params = ProductV3UseCase.Param(selectedProduct.parentProductId, 0)
                val response = productV3UseCase.execute(params)
                val allVariantsFromRemote = formatVariantNames(response)

                val updatedVariants = allVariantsFromRemote.map { variant ->
                    shouldSelectVariant(variant, isParentProductSelected, selectedProduct.variantProductIds)
                }

                val userSelectedVariantsOnly = updatedVariants.filter { it.variantId in originalVariantIds }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        parentProductName = response.parentProductName,
                        parentProductStock = response.parentProductStock,
                        parentProductPrice = response.parentProductPrice,
                        parentProductSoldCount = response.parentProductSoldCount,
                        parentProductImageUrl = response.parentProductImageUrl,
                        variants = userSelectedVariantsOnly,
                        selectedVariantIds = userSelectedVariantsOnly.selectedVariantsOnly()
                    )
                }
            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        )
    }

    private fun formatVariantNames(response: VariantResult): List<Variant> {
        val selections = response.selections
        return response.products.map { variant ->
            val variantName = variant.combinations.mapIndexed { index, combination ->
                selections[index].options[combination].value
            }
            val formattedVariantName = variantName.joinToString(separator = " | ") { it }
            variant.copy(variantName = formattedVariantName)
        }
    }

    private fun handleCheckAllVariant() {
        launch(dispatchers.computation) {
            val variants = currentState.variants.map { it.copy(isSelected = it.isEligible) }

            _uiState.update {
                it.copy(
                    isSelectAllActive = true,
                    variants = variants,
                    selectedVariantIds = variants.selectedVariantsOnly()
                )
            }
        }
    }

    private fun handleUncheckAllVariant() {
        launch(dispatchers.computation) {
            val variants = currentState.variants.map { it.copy(isSelected = false) }

            _uiState.update {
                it.copy(
                    isSelectAllActive = false,
                    variants = variants,
                    selectedVariantIds = emptySet()
                )
            }
        }
    }

    private fun handleAddVariantToSelection(variantProductIdToAdd: Long) {
        launch(dispatchers.computation) {
            val modifiedVariants = currentState.variants.map { variant ->
                if (variantProductIdToAdd == variant.variantId) {
                    variant.copy(isSelected = true)
                } else {
                    variant
                }
            }
            _uiState.update {
                it.copy(
                    variants = modifiedVariants,
                    selectedVariantIds = modifiedVariants.selectedVariantsOnly()
                )
            }
        }
    }


    private fun handleRemoveVariantFromSelection(variantProductIdToDelete: Long) {
        launch(dispatchers.computation) {
            val modifiedVariants = currentState.variants.map { variant ->
                if (variantProductIdToDelete == variant.variantId) {
                    variant.copy(isSelected = false)
                } else {
                    variant
                }
            }

            _uiState.update {
                it.copy(
                    variants = modifiedVariants,
                    selectedVariantIds = modifiedVariants.selectedVariantsOnly()
                )
            }
        }
    }

    private fun handleRemoveVariant(variantIdToDelete: Long) {
        launch(dispatchers.computation) {
            val modifiedVariants = currentState.variants.toMutableList()
            modifiedVariants.removeFirst { it.variantId == variantIdToDelete }

            val modifiedVariantProductIds =
                modifiedVariants.filter { it.isSelected }.map { it.variantId }.toMutableSet()

            _uiState.update {
                it.copy(
                    variants = modifiedVariants,
                    selectedVariantIds = modifiedVariantProductIds
                )
            }
        }
    }

    private fun handleBulkDeleteVariant() {
        launch(dispatchers.computation) {
            val toBeRemovedVariantIds = currentState.variants.filter { it.isSelected }.map { it.variantId }

            val modifiedVariants = currentState.variants.toMutableList()
            modifiedVariants.removeAll { it.variantId in toBeRemovedVariantIds }

            val modifiedVariantProductIds = modifiedVariants.filter { it.isSelected }.map { it.variantId }.toMutableSet()

            _uiState.update {
                it.copy(
                    variants = modifiedVariants,
                    selectedVariantIds = modifiedVariantProductIds
                )
            }
        }
    }

    private fun shouldSelectVariant(
        variant: Variant,
        isParentProductSelected: Boolean,
        selectedVariantIds: List<Long>
    ): Variant {
        val isSelected = if (isParentProductSelected) {
            //Select all variant if parent product is selected
            true
        } else {
            //Select variant only if it's previously selected
            variant.variantId in selectedVariantIds
        }

        return variant.copy(isSelected = isSelected)
    }

    private fun List<Variant>.selectedVariantsOnly(): Set<Long> {
       return filter { it.isSelected }.map { it.variantId }.toSet()
    }
}
