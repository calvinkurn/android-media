package com.tokopedia.mvc.presentation.product.variant.review

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.mvc.domain.entity.Product
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
                _uiState.update { it.copy(isLoading = true, parentProductId = event.selectedParentProduct.id) }
                getVariantDetail(event.selectedParentProduct)
            }
            is ReviewVariantEvent.AddProductToSelection -> handleAddProductToSelection(event.variantProductId)
            ReviewVariantEvent.DisableSelectAllCheckbox -> handleUncheckAllProduct()
            ReviewVariantEvent.EnableSelectAllCheckbox -> handleCheckAllProduct()
            is ReviewVariantEvent.RemoveProductFromSelection -> handleRemoveProductFromSelection(event.variantProductId)
            ReviewVariantEvent.TapSelectButton -> {
                _uiEffect.tryEmit(ReviewVariantEffect.ConfirmUpdateVariant(currentState.selectedVariantIds))
            }
            is ReviewVariantEvent.RemoveProduct -> handleRemoveProduct(event.productId)
        }
    }


    private fun getVariantDetail(selectedParentProduct : Product) {
        launchCatchError(
            dispatchers.io,
            block = {
                val params = ProductV3UseCase.Param(selectedParentProduct.id, 0)
                val response = productV3UseCase.execute(params)

                val modifiedVariantNames = findUpdatedVariantNames(response)

                val validatedVariants = modifiedVariantNames.map { variant ->
                    val (isEligible, reason) = isVariantEligible(variant.variantId, selectedParentProduct.originalVariants)
                    variant.copy(isEligible = isEligible, reason = reason)
                }

                val updatedVariants = validatedVariants.map {
                    if (it.variantId in selectedParentProduct.selectedVariantsIds && it.isEligible) {
                        it.copy(isSelected = true)
                    } else {
                        it
                    }
                }

                val selectedVariantIds = updatedVariants.filter { it.isSelected }.map { it.variantId }.toSet()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        parentProductName = response.parentProductName,
                        parentProductStock = response.parentProductStock,
                        parentProductPrice = response.parentProductPrice,
                        parentProductSoldCount = response.parentProductSoldCount,
                        parentProductImageUrl = response.parentProductImageUrl,
                        variants = updatedVariants,
                        selectedVariantIds = selectedVariantIds
                    )
                }
            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        )
    }

    private fun isVariantEligible(variantId : Long, validatedVariants: List<Product.Variant>): Pair<Boolean, String> {
        val matchedVariant = validatedVariants.find { variant -> variant.variantProductId == variantId }
        return Pair(matchedVariant?.isEligible.orTrue(), matchedVariant?.reason.orEmpty())
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
        val selectedVariantIds = variants.filter { it.isSelected }.map { it.variantId }.toSet()

        _uiState.update {
            it.copy(
                isSelectAllActive = true,
                variants = variants,
                selectedVariantIds = selectedVariantIds
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
            val selectedVariants = modifiedVariants.filter { it.isSelected }.map { it.variantId }.toSet()
            _uiState.update { it.copy(variants = modifiedVariants, selectedVariantIds = selectedVariants) }
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
            val selectedVariants = modifiedVariants.filter { it.isSelected }.map { it.variantId }.toSet()
            _uiState.update { it.copy(variants = modifiedVariants, selectedVariantIds = selectedVariants) }
        }
    }

    private fun handleRemoveProduct(productIdToDelete: Long) = launch(dispatchers.computation) {
        val modifiedVariants = currentState.variants.toMutableList()
        modifiedVariants.removeFirst { it.variantId == productIdToDelete }

        val modifiedVariantProductIds = modifiedVariants.filter { it.isSelected }.map { it.variantId }.toMutableSet()
        modifiedVariantProductIds.remove(productIdToDelete)

        _uiState.update {
            it.copy(
                variants = modifiedVariants,
                selectedVariantIds = modifiedVariantProductIds
            )
        }
    }

}
