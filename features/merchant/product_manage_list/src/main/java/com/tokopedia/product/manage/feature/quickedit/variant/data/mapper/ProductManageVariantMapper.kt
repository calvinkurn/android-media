package com.tokopedia.product.manage.feature.quickedit.variant.data.mapper

import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Selection
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Variant

object ProductManageVariantMapper {

    fun mapToProductVariants(variant: Variant): List<ProductVariant> {
        return variant.products.map {
            val variantName = getVariantName(it.combination, variant.selections)
            ProductVariant(it.productID, variantName, it.price)
        }
    }

    private fun getVariantName(optionIndexList: List<Int>, selections: List<Selection>): String {
        val firstIndex = 0
        val firstOptionIndex = optionIndexList.first()
        var variantName = selections.getVariantName(firstIndex, firstOptionIndex)

        optionIndexList.drop(1).mapIndexed { index, optionIndex ->
            val selectionIndex = index + 1
            val optionName = selections.getVariantName(selectionIndex, optionIndex)
            variantName+= " | $optionName"
        }

        return variantName
    }

    private fun List<Selection>.getVariantName(selectionIndex: Int, optionIndex: Int): String {
        return getOrNull(selectionIndex)?.options?.getOrNull(optionIndex)?.value.orEmpty()
    }
}