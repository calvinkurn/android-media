package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.product.addedit.variant.presentation.viewholder.MultipleVariantEditSelectViewHolder

class MultipleVariantEditSelectTypeAdapter: RecyclerView.Adapter<MultipleVariantEditSelectViewHolder>(),
        MultipleVariantEditSelectViewHolder.OnFieldClickListener {

    private var items: List<SelectionInputModel> = listOf()
    private var selectedIndex: List<HashMap<Int, Boolean>> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultipleVariantEditSelectViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_multiple_variant_edit_select, parent, false)
        return MultipleVariantEditSelectViewHolder(rootView, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MultipleVariantEditSelectViewHolder, position: Int) {
        holder.bindData(items[position], selectedIndex[position])
    }

    override fun onFieldClicked(selectionPosition: Int, optionPosition: Int, value: Boolean) {
        selectedIndex[selectionPosition][optionPosition] = value
    }

    fun setData(variantInputModel: VariantInputModel) {
        items = variantInputModel.selections
        selectedIndex = initializeSelectedIndex(variantInputModel.selections, false)
        notifyDataSetChanged()
    }

    fun setAllDataSelected(isSelected: Boolean) {
        selectedIndex = initializeSelectedIndex(items, isSelected)
        notifyDataSetChanged()
    }

    fun getSelectedData(): List<HashMap<Int, Boolean>> {
        return selectedIndex
    }

    private fun initializeSelectedIndex(selections: List<SelectionInputModel>, isSelected: Boolean) =
            selections.map {
                val result = hashMapOf<Int, Boolean>()
                it.options.forEachIndexed { index, _ ->
                    result[index] = isSelected
                }
                result
            }

}