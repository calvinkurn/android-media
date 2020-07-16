package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_COUNT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_COUNT
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.product.addedit.variant.presentation.viewholder.MultipleVariantEditSelectViewHolder

class MultipleVariantEditSelectAdapter: RecyclerView.Adapter<MultipleVariantEditSelectViewHolder>(),
        MultipleVariantEditSelectViewHolder.OnFieldClickListener {

    interface OnSelectionsDataListener {
        fun onSelectionsDataChanged(selectedCount: Int)
    }

    private var productVariants: List<ProductVariantInputModel> = listOf()
    private var selections: List<SelectionInputModel> = listOf()
    private var selectionIndex: MutableList<MutableList<Boolean>> = mutableListOf()
    private var onSelectionsDataListener: OnSelectionsDataListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultipleVariantEditSelectViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_multiple_variant_edit_select, parent, false)
        return MultipleVariantEditSelectViewHolder(rootView, this)
    }

    override fun getItemCount(): Int {
        return selectionIndex.size
    }

    override fun onBindViewHolder(holder: MultipleVariantEditSelectViewHolder, position: Int) {
        holder.bindData(selectionIndex[position], selections)
    }

    override fun onFieldClicked(selectionPosition: Int, optionPosition: Int, value: Boolean) {
        selectionIndex[selectionPosition][optionPosition] = value
        // add selection listener
        onSelectionsDataListener?.onSelectionsDataChanged(getSelectedData().size)
    }

    fun setOnSelectionsDataListener(listener: OnSelectionsDataListener) {
        onSelectionsDataListener = listener
    }

    fun setData(variantInputModel: VariantInputModel) {
        selections = variantInputModel.selections
        productVariants = variantInputModel.products
        selectionIndex = initializeSelectedIndex(variantInputModel.products, false)
        notifyDataSetChanged()
    }

    fun setAllDataSelected(isSelected: Boolean) {
        selectionIndex = initializeSelectedIndex(productVariants, isSelected)
        notifyDataSetChanged()
        // add selection listener
        onSelectionsDataListener?.onSelectionsDataChanged(getSelectedData().size)
    }

    fun getSelectedData(): MutableList<MutableList<Int>> {
        val levelCount = selections.size
        val selectedIndex = mutableListOf<MutableList<Int>>()
        selectionIndex.forEachIndexed { level1Index, level2Indices ->
            level2Indices.forEachIndexed { level2Index, isChecked ->
                if (isChecked) {
                    when (levelCount) {
                        VARIANT_VALUE_LEVEL_ONE_COUNT -> {
                            selectedIndex.add(mutableListOf(level1Index))
                        }
                        VARIANT_VALUE_LEVEL_TWO_COUNT -> {
                            selectedIndex.add(mutableListOf(level1Index, level2Index))
                        }
                    }
                }
            }
        }
        return selectedIndex
    }

    private fun initializeSelectedIndex(
            productVariants: List<ProductVariantInputModel>,
            isSelected: Boolean
    ): MutableList<MutableList<Boolean>> {
        val groups = productVariants.groupBy{ it.combination.getOrNull(VARIANT_VALUE_LEVEL_ONE_POSITION) }
        return groups.map {
            it.value.map {
                isSelected
            }.toMutableList()
        }.toMutableList()
    }

}