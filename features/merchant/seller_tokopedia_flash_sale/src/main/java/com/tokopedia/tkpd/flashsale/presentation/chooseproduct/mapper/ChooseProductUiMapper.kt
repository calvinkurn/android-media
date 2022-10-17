package com.tokopedia.tkpd.flashsale.presentation.chooseproduct.mapper

import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.domain.entity.Category
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaSelection
import com.tokopedia.tkpd.flashsale.domain.usecase.DoFlashSaleProductReserveUseCase

object ChooseProductUiMapper {

    private const val MAX_PRODUCT_SELECTION = 40

    private fun List<CriteriaSelection>.validateMax(): Boolean {
        return !any { it.selectionCount >= it.selectionCountMax }
    }

    private fun List<CriteriaSelection>.getDisabledCriteriaIds(): List<Long> {
        val result = mutableListOf<Long>()
        forEach {
            if (it.selectionCount >= it.selectionCountMax) result.add(it.criteriaId)
        }
        return result
    }

    private fun getSelectedProduct(list: List<ChooseProductItem>): List<ChooseProductItem> {
        return list.filter { it.isSelected && it.isEnabled }
    }

    fun collectAllCategory(categories: List<CriteriaSelection>): MutableList<Category> {
        val result = mutableListOf<Category>()
        categories.forEach { category ->
            category.categoryList.forEach { categoryData ->
                if (!result.any { it.categoryId == categoryData.categoryId}) {
                    result.add(categoryData)
                }
            }
        }
        return result
    }

    // limit max selected to MAX_PRODUCT_SELECTION due to server limitation
    fun getMaxSelectedProduct(maximumFromRemote: Int): Int {
        return if (maximumFromRemote < MAX_PRODUCT_SELECTION) maximumFromRemote
        else MAX_PRODUCT_SELECTION
    }

    fun mapToReserveParam(campaignId: Long, reservationId: String, selectedProducts: List<ChooseProductItem>?): DoFlashSaleProductReserveUseCase.Param {
        val productList = getSelectedProduct(selectedProducts.orEmpty())
        return DoFlashSaleProductReserveUseCase.Param(
            campaignId,
            reservationId,
            productList.map {
                Pair(it.productId.toLongOrZero(), it.criteriaId)
            }
        )
    }

    fun chooseCriteria(
        criteriaList: List<CriteriaSelection>?,
        item: ChooseProductItem,
    ): List<CriteriaSelection> {
        criteriaList?.firstOrNull { it.criteriaId == item.criteriaId }?.apply {
            if (item.isSelected) selectionCount++ else selectionCount--
        }
        return criteriaList.orEmpty()
    }

    fun validateSelection(
        productCount: Int,
        maxProduct: Int,
        criteriaList: List<CriteriaSelection>
    ): Boolean {
        val productValidation = productCount < maxProduct && productCount.isMoreThanZero()
        val criteriaValidation = criteriaList.validateMax()
        return productValidation && criteriaValidation
    }

    fun getSelectedProductList(
        selectedProductList: List<ChooseProductItem>?,
        remoteProductList: List<ChooseProductItem>,
    ): List<ChooseProductItem> {
        return remoteProductList.onEach { product ->
            product.isSelected = product.isSelected
                    || selectedProductList?.any { it.productId == product.productId }.orFalse()
        }
    }

    fun isExceedMaxProduct(productCount: Int): Boolean {
        return productCount.orZero() >= MAX_PRODUCT_SELECTION
    }

    fun isExceedMaxQuota(productCount: Int, maxProduct: Int): Boolean {
        return productCount.orZero() >= maxProduct
    }

    fun getSelectionValidationResult(
        selectedProductCount: Int,
        criteriaList: List<CriteriaSelection>,
        maxSelectedProduct: Int
    ): SelectionValidationResult {
        val isExceedMaxProduct = isExceedMaxProduct(selectedProductCount)
        val isExceedMaxQuota = isExceedMaxQuota(selectedProductCount, maxSelectedProduct)
        val disabledCriteriaIds = criteriaList.getDisabledCriteriaIds()
        return SelectionValidationResult(isExceedMaxProduct, isExceedMaxQuota, disabledCriteriaIds)
    }

    data class SelectionValidationResult (
        val isExceedMaxProduct: Boolean = false,
        val isExceedMaxQuota: Boolean = false,
        val disabledCriteriaIds: List<Long> = emptyList()
    )
}
