package com.tokopedia.shop.flashsale.presentation.creation.highlight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignProductListRequest
import com.tokopedia.shop.flashsale.domain.entity.HighlightableProduct
import com.tokopedia.shop.flashsale.domain.entity.ProductSubmissionResult
import com.tokopedia.shop.flashsale.domain.entity.enums.ProductionSubmissionAction
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignProductSubmissionUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignProductListUseCase
import com.tokopedia.shop.flashsale.presentation.creation.highlight.mapper.HighlightProductUiMapper
import com.tokopedia.shop.flashsale.presentation.creation.highlight.mapper.HighlightableProductRequestMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ManageHighlightedProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignProductListUseCase: GetSellerCampaignProductListUseCase,
    private val doSellerCampaignProductSubmissionUseCase: DoSellerCampaignProductSubmissionUseCase,
    private val mapper: HighlightableProductRequestMapper,
    private val highlightProductUiMapper: HighlightProductUiMapper
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val PRODUCT_LIST_TYPE_ID = 0
        private const val OFFSET_BY_ONE = 1
        private const val MAX_PRODUCT_SELECTION = 5
    }

    private val _products = MutableLiveData<Result<List<HighlightableProduct>>>()
    val products: LiveData<Result<List<HighlightableProduct>>>
        get() = _products

    private val _submit = MutableLiveData<Result<ProductSubmissionResult>>()
    val submit: LiveData<Result<ProductSubmissionResult>>
        get() = _submit

    private val _saveDraft = MutableLiveData<Result<ProductSubmissionResult>>()
    val saveDraft: LiveData<Result<ProductSubmissionResult>>
        get() = _saveDraft

    private var selectedProducts: MutableSet<HighlightableProduct> = mutableSetOf()

    private var isFirstLoad = true

    fun setIsFirstLoad(isFirstLoad: Boolean) {
        this.isFirstLoad = isFirstLoad
    }

    fun getProducts(
        campaignId: Long,
        productName: String,
        pageSize: Int,
        offset: Int
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val products = getSellerCampaignProductListUseCase.execute(
                    campaignId = campaignId,
                    productName = productName,
                    listType = PRODUCT_LIST_TYPE_ID,
                    pagination = GetSellerCampaignProductListRequest.Pagination(pageSize, offset)
                )

                val mappedProducts = highlightProductUiMapper.map(products)
                val appliedSelectedProductRule = applySelectedProductRule(mappedProducts, isFirstLoad)
                val appliedMaxSelectionRule = applyMaxSelectionRule(appliedSelectedProductRule)
                val appliedProductSelectionRule = applyProductSelectionRule(appliedMaxSelectionRule)
                val sortedProducts = applySortRule(appliedProductSelectionRule)
                _products.postValue(Success(sortedProducts))
            },
            onError = { error ->
                _products.postValue(Fail(error))
            }
        )
    }


    private fun applySelectedProductRule(
        products: List<HighlightableProduct>,
        isFirstLoad: Boolean
    ): List<HighlightableProduct> {
        return products
            .map { product ->
                val selectedProductIds = selectedProducts.map { it.id }

                val isSelected = if (isFirstLoad) {
                    product.highlightProductWording.isNotEmpty()
                } else {
                    product.id in selectedProductIds
                }

                product.copy(isSelected = isSelected)
            }.onEach { product ->
                if (product.isSelected) addProductIdToSelection(product)
            }
    }

    private fun applyMaxSelectionRule(
        currentPageProducts: List<HighlightableProduct>
    ): List<HighlightableProduct> {
        return currentPageProducts.map { product ->
            if (selectedProducts.size == MAX_PRODUCT_SELECTION && !product.isSelected) {
                product.copy(disabled = true, disabledReason = HighlightableProduct.DisabledReason.MAX_PRODUCT_REACHED)
            } else {
                product
            }
        }
    }

    private fun applyProductSelectionRule(currentPageProducts: List<HighlightableProduct>): List<HighlightableProduct> {
        val selectedProductsIds = selectedProducts.map { selectedProduct -> selectedProduct.id }
        val selectedParentProductIds = selectedProducts.map { selectedProduct -> selectedProduct.parentId }

        return currentPageProducts.map { product ->
            if (product.parentId in selectedParentProductIds && product.id !in selectedProductsIds) {
                product.copy(
                    isSelected = false,
                    disabled = true,
                    disabledReason = HighlightableProduct.DisabledReason.OTHER_PRODUCT_WITH_SAME_PARENT_ID_ALREADY_SELECTED
                )
            } else {
                product
            }
        }
    }

    private fun applySortRule(products: List<HighlightableProduct>): List<HighlightableProduct> {
        return products
            .sortedByDescending { it.isSelected }
            .mapIndexed { index, product -> product.copy(position = index + OFFSET_BY_ONE) }
    }


    fun submitHighlightedProducts(campaignId: Long, products: List<HighlightableProduct>) {
        launchCatchError(
            dispatchers.io,
            block = {
                val mappedProducts = mapper.map(products)
                val result = doSellerCampaignProductSubmissionUseCase.execute(
                    campaignId.toString(),
                    ProductionSubmissionAction.SUBMIT,
                    mappedProducts
                )
                _submit.postValue(Success(result))
            },
            onError = { error ->
                _submit.postValue(Fail(error))
            }
        )
    }

    fun saveDraft(campaignId: Long, products: List<HighlightableProduct>) {
        launchCatchError(
            dispatchers.io,
            block = {
                val mappedProducts = mapper.map(products)
                val result = doSellerCampaignProductSubmissionUseCase.execute(
                    campaignId.toString(),
                    ProductionSubmissionAction.SUBMIT,
                    mappedProducts
                )
                _saveDraft.postValue(Success(result))
            },
            onError = { error ->
                _saveDraft.postValue(Fail(error))
            }
        )
    }

    fun addProductIdToSelection(product: HighlightableProduct) {
        this.selectedProducts.add(product)
    }


    fun getSelectedProductIds(): Set<HighlightableProduct> {
        return this.selectedProducts
    }

    fun removeProductIdFromSelection(product: HighlightableProduct) {
        this.selectedProducts.removeFirst { it.id == product.id }
    }

    fun markAsSelected(products: List<HighlightableProduct>): List<HighlightableProduct> {
        val selectedProductsIds = selectedProducts.map { selectedProduct -> selectedProduct.id }
        val selectedParentProductIds = selectedProducts.map { selectedProduct -> selectedProduct.parentId }

        return products
            .mapIndexed { index, product ->
                //Same parent, but different variant
                if (product.parentId in selectedParentProductIds && product.id !in selectedProductsIds) {
                    product.copy(
                        isSelected = false,
                        disabled = true,
                        disabledReason = HighlightableProduct.DisabledReason.OTHER_PRODUCT_WITH_SAME_PARENT_ID_ALREADY_SELECTED
                    )
                } else if (product.parentId in selectedParentProductIds && product.id in selectedProductsIds) {
                    product.copy(
                        isSelected = true,
                        disabled = false,
                        disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED
                    )
                } else {
                    product
                }
            }
            .map { product ->
                if (!product.isSelected && selectedParentProductIds.size >= MAX_PRODUCT_SELECTION) {
                    product.copy(isSelected = false, disabled = true)
                } else {
                    product
                }
            }
            .sortedByDescending { it.isSelected }
            .mapIndexed { index, product -> product.copy(position = index + OFFSET_BY_ONE) }
    }

    fun markAsUnselected(
        currentlySelectedProduct: HighlightableProduct,
        products: List<HighlightableProduct>
    ): List<HighlightableProduct> {
        return products
            .mapIndexed { index, product ->
                if (currentlySelectedProduct.id == product.id) {
                    product.copy(isSelected = false)
                } else {
                    product
                }
            }
            .map { product ->
                if (product.parentId == currentlySelectedProduct.parentId) {
                    product.copy(disabled = false, disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED)
                } else {
                    product
                }
            }
            .sortedByDescending { it.isSelected }
            .mapIndexed { index, product -> product.copy(position = index + OFFSET_BY_ONE) }
    }
}