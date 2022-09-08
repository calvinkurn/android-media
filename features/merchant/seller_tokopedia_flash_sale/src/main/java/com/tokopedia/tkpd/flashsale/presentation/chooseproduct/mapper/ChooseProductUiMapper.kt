package com.tokopedia.tkpd.flashsale.presentation.chooseproduct.mapper

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.entity.ChooseProductItem
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

    fun getProductList(list: List<DelegateAdapterItem>) = list.filterIsInstance<ChooseProductItem>()

    fun getSelectedProduct(list: List<DelegateAdapterItem>): List<ChooseProductItem> {
        return getProductList(list).filter { it.isSelected && it.isEnabled }
    }

    fun getSelectedProductCount(list: List<DelegateAdapterItem>) = getSelectedProduct(list).size

    fun getMaxSelectedProduct(categories: List<CriteriaSelection>): Int {
        var max = 0
        categories.forEach {
            max += it.selectionCountMax
        }
        return max
    }

    fun mapToReserveParam(campaignId: Long, reservationId: String, selectedProducts: List<DelegateAdapterItem>?): DoFlashSaleProductReserveUseCase.Param {
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
}