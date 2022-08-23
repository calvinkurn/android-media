package com.tokopedia.shop.flashsale.presentation.creation.highlight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.shop.flashsale.common.tracker.ShopFlashSaleTracker
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
    private val highlightProductUiMapper: HighlightProductUiMapper,
    private val tracker: ShopFlashSaleTracker
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

    private var selectedProducts: MutableList<HighlightableProduct> = mutableListOf()

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
                val determinedProducts = determineShouldSelectProduct(mappedProducts, isFirstLoad)
                val appliedMaxSelectionRule = applyMaxSelectionRule(determinedProducts)
                val appliedProductSelectionRule = applyProductSelectionRule(appliedMaxSelectionRule)
                val highlightedProducts = applyProductHighlightPosition(appliedProductSelectionRule)
                _products.postValue(Success(highlightedProducts))
            },
            onError = { error ->
                _products.postValue(Fail(error))
            }
        )
    }


    private fun determineShouldSelectProduct(
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
            }
            .onEach { product ->
                if (isFirstLoad && product.isSelected) addProductIdToSelection(product)
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
            if (product.isVariant() && product.parentId in selectedParentProductIds && product.id !in selectedProductsIds) {
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

    private fun applyProductHighlightPosition(products: List<HighlightableProduct>): List<HighlightableProduct> {
        return products
            .map { product ->
                val position = findOrderPosition(product.id) + OFFSET_BY_ONE
                product.copy(position = position)
            }
    }


    private fun findOrderPosition(productId : Long): Int {
        return selectedProducts.indexOfFirst { product -> product.id == productId }
    }

    fun submitHighlightedProducts(campaignId: Long, products: List<HighlightableProduct>) {
        tracker.sendClickButtonProceedOnManageHighlightPageEvent()

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


    fun getSelectedProductIds(): List<HighlightableProduct> {
        return this.selectedProducts
    }

    fun removeProductIdFromSelection(product: HighlightableProduct) {
        this.selectedProducts.removeFirst { it.id == product.id }
    }

    fun markAsSelected(products: List<HighlightableProduct>): List<HighlightableProduct> {
        val selectedProductsIds = selectedProducts.map { selectedProduct -> selectedProduct.id }
        val selectedParentProductIds = selectedProducts.map { selectedProduct -> selectedProduct.parentId }

        return products
            .asSequence()
            .map { product -> enableProductIfParent(product, selectedProductsIds) }
            .map { product -> applyVariantProductSelectionRule(selectedProductsIds, selectedParentProductIds, product) }
            .map { product -> disableAllUnselectedProductIfMaxSelectionReached(product, selectedParentProductIds) }
            .sortedByDescending { it.isSelected }
            .mapIndexed { index, product -> product.copy(position = index + OFFSET_BY_ONE) }
            .toList()
    }

    private fun enableProductIfParent(product: HighlightableProduct, selectedProductsIds: List<Long>): HighlightableProduct {
        return if (product.isParent() && product.id in selectedProductsIds) {
            product.copy(
                isSelected = true,
                disabled = false,
                disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED
            )
        } else {
            product
        }

    }

    private fun applyVariantProductSelectionRule(
        selectedProductsIds: List<Long>,
        selectedParentProductIds: List<Long>,
        product: HighlightableProduct
    ): HighlightableProduct {
        return if (product.isVariant() && product.parentId in selectedParentProductIds && product.id in selectedProductsIds) {
            //First selected variant within same parent should be enabled
            product.copy(
                isSelected = true,
                disabled = false,
                disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED
            )
        } else if (product.isVariant() && product.parentId in selectedParentProductIds) {
            //The rest of the variant within same parent should be disabled
            product.copy(
                isSelected = false,
                disabled = true,
                disabledReason = HighlightableProduct.DisabledReason.OTHER_PRODUCT_WITH_SAME_PARENT_ID_ALREADY_SELECTED
            )
        }  else {
            product
        }
    }

    private fun disableAllUnselectedProductIfMaxSelectionReached(
        product: HighlightableProduct,
        selectedParentProductIds: List<Long>
    ): HighlightableProduct {
        return if (!product.isSelected && selectedParentProductIds.size >= MAX_PRODUCT_SELECTION) {
            product.copy(isSelected = false, disabled = true)
        } else {
            product
        }
    }

    fun markAsUnselected(
        currentlySelectedProduct: HighlightableProduct,
        products: List<HighlightableProduct>
    ): List<HighlightableProduct> {
        val selectedProductsIds = selectedProducts.map { selectedProduct -> selectedProduct.id }

        return products
            .map { product  -> disableSelectedProduct(currentlySelectedProduct, product) }
            .map { product -> enableAllProductExceptSelectedProducts(selectedProductsIds, product) }
            .sortedByDescending { it.isSelected }
            .mapIndexed { index, product -> product.copy(position = index + OFFSET_BY_ONE) }
    }

    private fun disableSelectedProduct(
        currentlySelectedProduct: HighlightableProduct,
        product: HighlightableProduct
    ): HighlightableProduct {
        return if (currentlySelectedProduct.id == product.id) {
            product.copy(isSelected = false)
        } else {
            product
        }
    }

    private fun enableAllProductExceptSelectedProducts(
        selectedProductsIds: List<Long>,
        product: HighlightableProduct
    ): HighlightableProduct {
        return if (product.id !in selectedProductsIds) {
            product.copy(
                disabled = false,
                disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED
            )
        } else {
            product
        }
    }

    private fun HighlightableProduct.isParent(): Boolean {
        return this.parentId == 0.toLong()
    }

    private fun HighlightableProduct.isVariant(): Boolean {
        return this.parentId != 0.toLong()
    }

}