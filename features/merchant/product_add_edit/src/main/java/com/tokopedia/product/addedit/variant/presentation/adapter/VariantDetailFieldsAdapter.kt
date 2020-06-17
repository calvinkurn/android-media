package com.tokopedia.product.addedit.variant.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.product.addedit.variant.presentation.adapter.viewmodel.VariantDetailFieldsViewModel
import com.tokopedia.product.addedit.variant.presentation.adapter.viewmodel.VariantDetailHeaderViewModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel

class VariantDetailFieldsAdapter(variantDetailTypeFactoryImpl: VariantDetailInputTypeFactoryImpl) :
        BaseAdapter<VariantDetailInputTypeFactoryImpl>(variantDetailTypeFactoryImpl) {

    fun addUnitValueHeader(unitValueHeader: String): Int {
        visitables.add(VariantDetailHeaderViewModel(unitValueHeader))
        val position = this.lastIndex
        notifyItemInserted(position)
        return position
    }

    fun addVariantDetailField(variantDetailInputLayoutModel: VariantDetailInputLayoutModel): Int {
        visitables.add(VariantDetailFieldsViewModel(variantDetailInputLayoutModel))
        val position = this.lastIndex
        notifyItemInserted(position)
        return position
    }

    fun updateAllField(variantDetailFieldMapLayout: HashMap<Int, VariantDetailInputLayoutModel>) {
        variantDetailFieldMapLayout.forEach { (adapterPosition, variantDetailInputModel) ->
            val variantDetailFieldsViewModel = VariantDetailFieldsViewModel(variantDetailInputModel)
            notifyElement(adapterPosition, variantDetailFieldsViewModel)
        }
    }

    private fun notifyElement(adapterPosition: Int, element: Visitable<*>) {
        visitables[adapterPosition] = element
        notifyItemChanged(adapterPosition)
    }
}