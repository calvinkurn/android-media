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
    private var selectedIndex: List<HashMap<Int, Boolean>> = listOf()
    private var selectedCombinations: List<Int> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectVariantMainViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_multiple_variant_edit_select, parent, false)
        return SelectVariantMainViewHolder(rootView, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SelectVariantMainViewHolder, position: Int) {
        holder.bindData(items[position], selectedIndex[position])
    }

    override fun onFieldClicked(selectionPosition: Int, optionPosition: Int, value: Boolean) {
        selectedIndex = initializeSelectedIndex(items)
        selectedIndex[selectionPosition][optionPosition] = value
        selectedCombinations = listOf(selectionPosition, optionPosition)
        notifyDataSetChanged()
    }

    fun setData(variantInputModel: VariantInputModel) {
        items = variantInputModel.selections
        selectedIndex = initializeSelectedIndex(variantInputModel.selections)
        notifyDataSetChanged()
    }

    fun getSelectedData(): List<Int> {
        return selectedCombinations
    }

    private fun initializeSelectedIndex(selections: List<SelectionInputModel>) =
            selections.map {
                val result = hashMapOf<Int, Boolean>()
                it.options.forEachIndexed { index, _ ->
                    result[index] = false
                }
                result
            }

}