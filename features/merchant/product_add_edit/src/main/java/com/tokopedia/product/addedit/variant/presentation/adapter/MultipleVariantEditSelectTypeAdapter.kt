package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.product.addedit.variant.presentation.viewholder.MultipleVariantEditSelectViewHolder

class MultipleVariantEditSelectTypeAdapter: RecyclerView.Adapter<MultipleVariantEditSelectViewHolder>(),
        MultipleVariantEditSelectViewHolder.OnFieldClickListener {

    private var productVariants: List<ProductVariantInputModel> = listOf()
    private var selections: List<SelectionInputModel> = listOf()
    private var selectedIndex: MutableList<MutableList<Boolean>> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultipleVariantEditSelectViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_multiple_variant_edit_select, parent, false)
        return MultipleVariantEditSelectViewHolder(rootView, this)
    }

    override fun getItemCount(): Int {
        return selectedIndex.size
    }

    override fun onBindViewHolder(holder: MultipleVariantEditSelectViewHolder, position: Int) {
        holder.bindData(selectedIndex[position], selections)
    }

    override fun onFieldClicked(selectionPosition: Int, optionPosition: Int, value: Boolean) {
        selectedIndex[selectionPosition][optionPosition] = value
    }

    fun setData(variantInputModel: VariantInputModel) {
        selections = variantInputModel.selections
        productVariants = variantInputModel.products
        selectedIndex = initializeSelectedIndex(variantInputModel.products, false)
        notifyDataSetChanged()
    }

    fun setAllDataSelected(isSelected: Boolean) {
        selectedIndex = initializeSelectedIndex(productVariants, isSelected)
        notifyDataSetChanged()
    }

    fun getSelectedData(): List<HashMap<Int, Boolean>> {
        return emptyList()
    }

    private fun initializeSelectedIndex(
            productVariants: List<ProductVariantInputModel>,
            isSelected: Boolean
    ): MutableList<MutableList<Boolean>> {
        val groups = productVariants.groupBy{ it.combination.getOrNull(0) }
        return groups.map {
            it.value.map {
                isSelected
            }.toMutableList()
        }.toMutableList()
    }

}