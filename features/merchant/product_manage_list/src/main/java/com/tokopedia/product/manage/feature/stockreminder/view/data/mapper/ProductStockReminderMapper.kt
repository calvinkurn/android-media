package com.tokopedia.product.manage.feature.stockreminder.view.data.mapper

import com.tokopedia.product.manage.common.feature.variant.data.model.GetProductV3
import com.tokopedia.product.manage.common.feature.variant.data.model.Selection
import com.tokopedia.product.manage.feature.stockreminder.view.data.ProductStockReminderUiModel

object ProductStockReminderMapper {

    fun mapToProductResult(response: GetProductV3): List<ProductStockReminderUiModel> {
        if (response.variant.products.isEmpty()){
            val stockAlertCount = if (response.stockAlertCount.isEmpty())  0 else response.stockAlertCount.toInt()
            return listOf(
                ProductStockReminderUiModel(
                    response.productID,
                    response.productName,
                    stockAlertCount,
                    response.stockAlertStatus,
                    response.stock
                )
            )
        }else{
            val variant = response.variant
            val variantSelections = variant.selections
            val variants = response.variant.products.map {
                val variantName = getVariantName(it.combination, variantSelections)
                val stockAlertCount = if (it.stockAlertCount.isEmpty())  0 else it.stockAlertCount.toInt()
                ProductStockReminderUiModel(
                    it.productID,
                    variantName,
                    stockAlertCount,
                    it.stockAlertStatus,
                    it.stock
                )
            }
            return variants
        }
    }

    private fun getVariantName(optionIndexList: List<Int>, selections: List<Selection>): String {
        var variantName = ""
        val firstIndex = 0
        val firstOptionIndex = optionIndexList.firstOrNull()

        firstOptionIndex?.let {
            variantName = selections.getVariantName(firstIndex, it)

            optionIndexList.drop(1).mapIndexed { index, optionIndex ->
                val selectionIndex = index + 1
                val optionName = selections.getVariantName(selectionIndex, optionIndex)
                variantName += " | $optionName"
            }
        }

        return variantName
    }

    private fun List<Selection>.getVariantName(selectionIndex: Int, optionIndex: Int): String {
        return getOrNull(selectionIndex)?.options?.getOrNull(optionIndex)?.value.orEmpty()
    }
}