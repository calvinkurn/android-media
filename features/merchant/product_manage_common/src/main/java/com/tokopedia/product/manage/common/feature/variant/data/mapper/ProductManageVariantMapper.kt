package com.tokopedia.product.manage.common.feature.variant.data.mapper

import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ShopParam
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.data.model.GetProductV3
import com.tokopedia.product.manage.common.feature.variant.presentation.data.GetVariantResult
import com.tokopedia.product.manage.common.feature.variant.data.model.Selection
import com.tokopedia.product.manage.common.feature.variant.data.model.param.UpdateVariantParam
import com.tokopedia.product.manage.common.feature.variant.data.model.param.VariantInputParam
import com.tokopedia.product.manage.common.feature.variant.data.model.param.VariantProductInput
import com.tokopedia.product.manage.common.feature.variant.data.model.param.VariantSelectionInput
import com.tokopedia.product.manage.common.feature.variant.data.model.param.VariantSizeChartInput
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult

object ProductManageVariantMapper {

    fun mapToVariantsResult(response: GetProductV3, access: ProductManageAccess): GetVariantResult {
        val variant = response.variant
        val variantSelections = variant.selections
        val variantSizeCharts = variant.sizeCharts
        val productName = response.productName
        val isAllStockEmpty = response.isAllStockEmpty()

        val variants = response.variant.products.map {
            val variantName = getVariantName(it.combination, variantSelections)
            ProductVariant(
                it.productID,
                variantName,
                it.status,
                it.combination,
                it.isPrimary,
                it.isCampaign,
                it.price,
                it.sku,
                it.stock,
                it.pictures,
                isAllStockEmpty,
                access
            )
        }

        return GetVariantResult(productName, variants, variantSelections, variantSizeCharts)
    }

    fun EditVariantResult.updateVariant(variantId: String, updateBlock: (ProductVariant) -> ProductVariant): EditVariantResult {
        val variantList = variants.toMutableList()
        val variant = variants.find { it.id == variantId }
        val index = variants.indexOf(variant)

        variantList[index] = updateBlock.invoke(variantList[index])
        return copy(variants = variantList)
    }

    fun EditVariantResult.setEditStockAndStatus(currentProductVariantList: List<ProductVariant>): EditVariantResult {
        var editStock = false
        var editStatus = false

        currentProductVariantList.forEachIndexed { index, variant ->
            val variantStockInput = variants.getOrNull(index)?.stock
            val variantStatusInput = variants.getOrNull(index)?.status

            if(variantStockInput != variant.stock) {
                editStock = true
            }

            if(variantStatusInput != variant.status) {
                editStatus = true
            }
        }

        return copy(editStock = editStock, editStatus = editStatus)
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

    fun mapVariantsToEditResult(productId: String, result: GetVariantResult): EditVariantResult {
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