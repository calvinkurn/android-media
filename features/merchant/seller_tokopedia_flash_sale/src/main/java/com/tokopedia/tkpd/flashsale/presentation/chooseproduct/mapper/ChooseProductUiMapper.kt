package com.tokopedia.tkpd.flashsale.presentation.chooseproduct.mapper

import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.domain.entity.Category
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaSelection
import com.tokopedia.tkpd.flashsale.domain.usecase.DoFlashSaleProductReserveUseCase

object ChooseProductUiMapper {

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

    fun getSelectedProduct(list: List<ChooseProductItem>): List<ChooseProductItem> {
        return list.filter { it.isSelected && it.isEnabled }
    }

    fun getSelectedProductCount(list: List<ChooseProductItem>) = getSelectedProduct(list).size

    fun getMaxSelectedProduct(criterias: List<CriteriaSelection>): Int {
        var max = 0
        criterias.forEach {
            max += it.selectionCountMax
        }
        return max
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

    fun validateSelection(productCount: Int, criteriaList: List<CriteriaSelection>): Boolean {
        val maxProduct = getMaxSelectedProduct(criteriaList)
        val productValidation = productCount <= maxProduct && productCount.isMoreThanZero()
        val criteriaValidation = criteriaList.validateMax()
        return productValidation && criteriaValidation
    }

    private fun List<CriteriaSelection>.validateMax(): Boolean {
        return !any { it.selectionCount > it.selectionCountMax }
    }
}