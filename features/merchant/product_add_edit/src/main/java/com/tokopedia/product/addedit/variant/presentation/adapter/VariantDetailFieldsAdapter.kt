package com.tokopedia.product.addedit.variant.presentation.adapter

import android.annotation.SuppressLint
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.variant.presentation.adapter.uimodel.VariantDetailFieldsUiModel
import com.tokopedia.product.addedit.variant.presentation.adapter.uimodel.VariantDetailHeaderUiModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel

class VariantDetailFieldsAdapter(variantDetailTypeFactoryImpl: VariantDetailInputTypeFactoryImpl) :
        BaseAdapter<VariantDetailInputTypeFactoryImpl>(variantDetailTypeFactoryImpl) {

    fun addUnitValueHeader(unitValueHeader: String): Int {
        val variantDetailHeaderUiModel = VariantDetailHeaderUiModel(unitValueHeader, lastIndex + 1)
        visitables.add(variantDetailHeaderUiModel)
        notifyItemInserted(this.lastIndex)
        return this.lastIndex
    }

    fun addVariantDetailField(
        variantDetailInputLayoutModel: VariantDetailInputLayoutModel,
        displayWeightCoachmark: Boolean = false
    ): Int {
        variantDetailInputLayoutModel.visitablePosition = lastIndex + 1
        visitables.add(VariantDetailFieldsUiModel(
            variantDetailInputLayoutModel,
            displayWeightCoachmark
        ))
        notifyItemInserted(this.lastIndex)
        return this.lastIndex
    }

    fun collapseUnitValueHeader(visitablePosition: Int, fieldSize: Int) {
        (visitables[visitablePosition] as? VariantDetailHeaderUiModel)?.isCollapsed = true
        val targetPosition = visitablePosition + 1
        for (i in 1..fieldSize) {
            visitables.removeAt(targetPosition)
            notifyItemRemoved(targetPosition)
        }
    }

    fun expandDetailFields(visitablePosition: Int, inputLayoutModels: List<VariantDetailInputLayoutModel>) {
        (visitables[visitablePosition] as? VariantDetailHeaderUiModel)?.isCollapsed = false
        val targetPosition = visitablePosition + 1
        val viewModels = mutableListOf<VariantDetailFieldsUiModel>()
        inputLayoutModels.forEach { viewModels.add(VariantDetailFieldsUiModel(it)) }
        visitables.addAll(targetPosition, viewModels)
        notifyItemRangeInserted(targetPosition, viewModels.size)
    }

    fun updatePriceEditingStatus(isEnabled: Boolean) {
        list.forEachIndexed { index, visitable ->
            if (visitable is VariantDetailFieldsUiModel) {
                visitable.variantDetailInputLayoutModel.priceEditEnabled = isEnabled
                val variantDetailFieldsViewModel = VariantDetailFieldsUiModel(
                    visitable.variantDetailInputLayoutModel, visitable.displayWeightCoachmark)
                notifyElement(index, variantDetailFieldsViewModel)
            }
        }
    }

    fun updateMaxStockThreshold(maxStockThreshold: String) {
        list.forEachIndexed { index, visitable ->
            (visitable as? VariantDetailFieldsUiModel)?.variantDetailInputLayoutModel?.let { detailModel ->
                val currentStock = detailModel.stock.orZero()
                val maxStock = maxStockThreshold.toInt().orZero()
                if (currentStock > maxStock) {
                    notifyItemChanged(index)
                }
            }
        }
    }

    fun activateVariantStatus(combination: List<Int>) {
        list.forEachIndexed { position, visitable ->
            (visitable as? VariantDetailFieldsUiModel)?.variantDetailInputLayoutModel?.apply {
                if (this.combination == combination) {
                    isActive = true
                    notifyItemChanged(position)
                }
            }
        }
    }

    fun deactivateVariantStatus(position: Int) {
        (list.getOrNull(position) as? VariantDetailFieldsUiModel)?.
            variantDetailInputLayoutModel?.apply {
                if (this.combination == combination) {
                    isActive = false
                    notifyItemChanged(position)
                }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyElement(adapterPosition: Int, element: Visitable<*>) {
        try {
            visitables[adapterPosition] = element
            notifyItemChanged(adapterPosition)
        } catch (e: Exception) {
            notifyDataSetChanged()
            AddEditProductErrorHandler.logExceptionToCrashlytics(e)
        }
    }

    fun getHeaderAtPosition(position: Int): VariantDetailHeaderUiModel? {
        return list.firstOrNull {
            (it as? VariantDetailHeaderUiModel)?.position == position
        } as? VariantDetailHeaderUiModel
    }
}
