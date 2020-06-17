package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.product.addedit.variant.presentation.viewholder.SelectVariantMainViewHolder

class SelectVariantMainTypeAdapter: RecyclerView.Adapter<SelectVariantMainViewHolder>(),
        SelectVariantMainViewHolder.OnFieldClickListener {

    private var items: List<SelectionInputModel> = listOf()
    private var selectedIndices: MutableList<MutableList<Boolean>> = mutableListOf()
    private var selectedCombination: MutableList<Int> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectVariantMainViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_multiple_variant_edit_select, parent, false)
        return SelectVariantMainViewHolder(rootView, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SelectVariantMainViewHolder, position: Int) {
        holder.bindData(items[position], selectedIndices[position])
    }

    override fun onFieldClicked(selectionPosition: Int, optionPosition: Int, value: Boolean) {
        selectedIndices = initializeSelectedIndex(items)
        selectedIndices[selectionPosition][optionPosition] = value
        notifyDataSetChanged()
    }

    fun setData(variantInputModel: VariantInputModel) {
        items = variantInputModel.selections
        selectedIndices = initializeSelectedIndex(variantInputModel.selections)
        notifyDataSetChanged()
    }

    fun getSelectedData(): List<Int> {
        selectedIndices.forEachIndexed { selectionIndex, it ->
            it.forEachIndexed { optionIndex, isSelected ->
                if (isSelected) {
                    selectedCombination.add(selectionIndex, optionIndex)
                }
            }
        }
        return selectedCombination
    }

    private fun initializeSelectedIndex(selections: List<SelectionInputModel>) =
            selections.map {
                val result = mutableListOf<Boolean>()
                it.options.forEachIndexed { index, _ ->
                    result.add(index, false)
                }
                result
            }.toMutableList()

}