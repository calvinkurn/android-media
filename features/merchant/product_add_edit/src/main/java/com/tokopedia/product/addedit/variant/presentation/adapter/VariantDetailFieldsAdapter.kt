package com.tokopedia.product.addedit.variant.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.product.addedit.variant.presentation.adapter.viewmodel.VariantDetailFieldsViewModel
import com.tokopedia.product.addedit.variant.presentation.adapter.viewmodel.VariantDetailHeaderViewModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel

class VariantDetailFieldsAdapter(variantDetailTypeFactoryImpl: VariantDetailInputTypeFactoryImpl) :
        BaseAdapter<VariantDetailInputTypeFactoryImpl>(variantDetailTypeFactoryImpl) {

    fun addUnitValueHeader(unitValueHeader: String, position: Int): Int {
        visitables.add(VariantDetailHeaderViewModel(unitValueHeader, position))
        notifyItemInserted(this.lastIndex)
        return this.lastIndex
    }

    fun addVariantDetailField(variantDetailInputLayoutModel: VariantDetailInputLayoutModel): Int {
        visitables.add(VariantDetailFieldsViewModel(variantDetailInputLayoutModel))
        val position = this.lastIndex
        notifyItemInserted(position)
        return position
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
        val viewModels = mutableListOf<VariantDetailFieldsViewModel>()
        inputLayoutModels.forEach { viewModels.add(VariantDetailFieldsViewModel(it)) }
        visitables.addAll(targetPosition, viewModels)
        notifyItemRangeInserted(targetPosition, viewModels.size)
    }

    fun updateSkuVisibilityStatus(variantDetailFieldMapLayout: Map<Int, VariantDetailInputLayoutModel>, isVisible: Boolean) {
        variantDetailFieldMapLayout.forEach { (adapterPosition, variantDetailInputModel) ->
            variantDetailInputModel.isSkuFieldVisible = isVisible
            val variantDetailFieldsViewModel = VariantDetailFieldsViewModel(variantDetailInputModel)
            notifyElement(adapterPosition, variantDetailFieldsViewModel)
        }
    }

    fun updatePriceEditingStatus(variantDetailFieldMapLayout: Map<Int, VariantDetailInputLayoutModel>, isEnabled: Boolean) {
        variantDetailFieldMapLayout.forEach { (adapterPosition, variantDetailInputModel) ->
            variantDetailInputModel.priceEditEnabled = isEnabled
            val variantDetailFieldsViewModel = VariantDetailFieldsViewModel(variantDetailInputModel)
            notifyElement(adapterPosition, variantDetailFieldsViewModel)
        }
    }

    fun getDetailInputLayoutList(): List<VariantDetailInputLayoutModel> {
        return list.filterIsInstance<VariantDetailFieldsViewModel>().map {
            it.variantDetailInputLayoutModel
        }
    }

    private fun notifyElement(adapterPosition: Int, element: Visitable<*>) {
        visitables[adapterPosition] = element
        notifyItemChanged(adapterPosition)
    }
}