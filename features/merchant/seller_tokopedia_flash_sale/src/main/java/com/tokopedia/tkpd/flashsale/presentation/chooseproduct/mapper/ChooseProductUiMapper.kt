package com.tokopedia.tkpd.flashsale.presentation.chooseproduct.mapper

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.tkpd.flashsale.domain.entity.Category
import com.tokopedia.tkpd.flashsale.domain.entity.CategorySelection

object ChooseProductUiMapper {

    fun collectAllCategory(categories: List<CategorySelection>): MutableList<Category> {
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

    fun getSelectedProduct(list: List<DelegateAdapterItem>): List<DelegateAdapterItem> {
        return getProductList(list).filter { it.isSelected && it.isEnabled }
    }

    fun getSelectedProductCount(list: List<DelegateAdapterItem>) = getSelectedProduct(list).size

    fun getMaxSelectedProduct(categories: List<CategorySelection>): Int {
        var max = 0
        categories.forEach {
            max += it.selectionCountMax
        }
        return max
    }
}