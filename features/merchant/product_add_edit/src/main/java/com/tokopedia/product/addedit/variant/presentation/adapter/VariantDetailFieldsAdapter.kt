package com.tokopedia.product.addedit.variant.presentation.adapter

import android.annotation.SuppressLint
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
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

    fun addVariantDetailField(variantDetailInputLayoutModel: VariantDetailInputLayoutModel): Int {
        variantDetailInputLayoutModel.visitablePosition = lastIndex + 1
        visitables.add(VariantDetailFieldsUiModel(variantDetailInputLayoutModel))
        notifyItemInserted(this.lastIndex)
        return this.lastIndex
    }

    fun collapseUnitValueHeader(visitablePosition: Int, fieldSize: Int) {
        val targetPosition = visitablePosition + 1
        for (i in 1..fieldSize) {
            visitables.removeAt(targetPosition)
            notifyItemRemoved(targetPosition)
        }
    }

    fun expandDetailFields(visitablePosition: Int, inputLayoutModels: List<VariantDetailInputLayoutModel>) {
        val targetPosition = visitablePosition + 1
        val viewModels = mutableListOf<VariantDetailFieldsUiModel>()
        inputLayoutModels.forEach { viewModels.add(VariantDetailFieldsUiModel(it)) }
        visitables.addAll(targetPosition, viewModels)
        notifyItemRangeInserted(targetPosition, viewModels.size)
    }

    fun updateSkuVisibilityStatus(variantDetailFieldMapLayout: Map<Int, VariantDetailInputLayoutModel>, isVisible: Boolean) {
        variantDetailFieldMapLayout.forEach { (adapterPosition, variantDetailInputModel) ->
            variantDetailInputModel.isSkuFieldVisible = isVisible
            val variantDetailFieldsViewModel = VariantDetailFieldsUiModel(variantDetailInputModel)
            notifyElement(adapterPosition, variantDetailFieldsViewModel)
        }
    }

    fun updatePriceEditingStatus(variantDetailFieldMapLayout: Map<Int, VariantDetailInputLayoutModel>, isEnabled: Boolean) {
        variantDetailFieldMapLayout.forEach { (adapterPosition, variantDetailInputModel) ->
            variantDetailInputModel.priceEditEnabled = isEnabled
            val variantDetailFieldsViewModel = VariantDetailFieldsUiModel(variantDetailInputModel)
            notifyElement(adapterPosition, variantDetailFieldsViewModel)
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

    fun getDetailInputLayoutList(): List<VariantDetailInputLayoutModel> {
        return list.filterIsInstance<VariantDetailFieldsUiModel>().map {
            it.variantDetailInputLayoutModel
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
}