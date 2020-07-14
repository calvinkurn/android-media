package com.tokopedia.product.manage.feature.quickedit.variant.data.mapper

import com.tokopedia.product.manage.feature.multiedit.data.param.ShopParam
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.GetProductV3
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.data.GetVariantResult
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Selection
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.param.UpdateVariantParam
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.param.VariantInputParam
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.param.VariantProductInput
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.param.VariantSelectionInput
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.param.VariantSizeChartInput
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.data.EditVariantResult

object ProductManageVariantMapper {

    fun mapToVariantsResult(response: GetProductV3): GetVariantResult {
        val variant = response.variant
        val variantSelections = variant.selections
        val variantSizeCharts = variant.sizeCharts
        val productName = response.productName

        val variants = response.variant.products.map {
            val variantName = getVariantName(it.combination, variantSelections)
            ProductVariant(
                it.productID,
                variantName,
                it.status,
                it.combination,
                it.isPrimary,
                it.price,
                it.sku,
                it.stock,
                it.pictures,
                response.isAllStockEmpty()
            )
        }

        return GetVariantResult(productName, variants, variantSelections, variantSizeCharts)
    }

    fun EditVariantResult.updateVariant(
        variantId: String,
        updateBlock: (ProductVariant) -> ProductVariant
    ): EditVariantResult {
        val variantList = variants.toMutableList()
        val variant = variants.find { it.id == variantId }
        val index = variants.indexOf(variant)

        variantList[index] = updateBlock.invoke(variantList[index])
        return EditVariantResult(productId, productName, variantList, selections, sizeCharts)
    }

    fun mapResultToUpdateParam(shopId: String, result: EditVariantResult): UpdateVariantParam {
        val productInput = result.variants.map {
            VariantProductInput(
                it.status,
                it.combination,
                it.isPrimary,
                it.price,
                it.sku,
                it.stock,
                it.pictures
            )
        }

        val selectionInput = result.selections.map {
            VariantSelectionInput(it.variantID, it.unitID, it.options)
        }

        val sizeChartInput = result.sizeCharts.map {
            VariantSizeChartInput(
                it.picId,
                it.description,
                it.filePath,
                it.fileName,
                it.width,
                it.height
            )
        }

        val shopParam = ShopParam(shopId)
        val variantInputParam = VariantInputParam(productInput, selectionInput, sizeChartInput)

        return UpdateVariantParam(shopParam, result.productId, variantInputParam)
    }

    fun mapVariantsToEditResult(productId: String, result: GetVariantResult): EditVariantResult? {
        val productName = result.productName
        val variants = result.variants
        val selections = result.selections
        val sizeCharts = result.sizeCharts

        return EditVariantResult(productId, productName, variants, selections, sizeCharts)
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
                variantName+= " | $optionName"
            }
        }

        return variantName
    }

    private fun List<Selection>.getVariantName(selectionIndex: Int, optionIndex: Int): String {
        return getOrNull(selectionIndex)?.options?.getOrNull(optionIndex)?.value.orEmpty()
    }
}