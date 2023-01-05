package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.product.addedit.variant.presentation.viewholder.SelectVariantMainViewHolder

class SelectVariantMainAdapter :
    RecyclerView.Adapter<SelectVariantMainViewHolder>(),
    SelectVariantMainViewHolder.OnFieldClickListener {

    private var variantInputModel: VariantInputModel = VariantInputModel()
    private var selectionIndices: MutableList<MutableList<Boolean>> = mutableListOf()
    private var selectedCombination: List<Int> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectVariantMainViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_multiple_variant_edit_select, parent, false)
        return SelectVariantMainViewHolder(rootView, this)
    }

    override fun getItemCount(): Int {
        return selectionIndices.size
    }

    override fun onBindViewHolder(holder: SelectVariantMainViewHolder, position: Int) {
        holder.bindData(selectionIndices[position], variantInputModel.selections)
    }

    override fun onFieldClicked(level1Position: Int, level2Position: Int, value: Boolean) {
        setSelectedIndex(level1Position, level2Position, value)
        updateSelectedCombination(level1Position, level2Position)
        notifyDataSetChanged()
    }

    private fun setSelectedIndex(level1Position: Int, level2Position: Int, value: Boolean) {
        selectionIndices.forEachIndexed { level1Index, level2Indices ->
            level2Indices.forEachIndexed { level2Index, _ ->
                selectionIndices.getOrNull(level1Index)?.getOrNull(level2Index)?.run {
                    selectionIndices[level1Index][level2Index] = false
                }
            }
        }
        selectionIndices.getOrNull(level1Position)?.getOrNull(level2Position)?.run {
            selectionIndices[level1Position][level2Position] = value
        }
    }

    fun setData(variantInputModel: VariantInputModel) {
        this.variantInputModel = variantInputModel
        selectionIndices = initializeSelectedIndex(variantInputModel.products)
        notifyDataSetChanged()
    }

    fun getSelectedData(): List<Int> {
        return selectedCombination
    }

    private fun initializeSelectedIndex(
        productVariants: List<ProductVariantInputModel>
    ): MutableList<MutableList<Boolean>> {
        val groups = productVariants.groupBy { it.combination.getOrNull(VARIANT_VALUE_LEVEL_ONE_POSITION) }
        return groups.map {
            it.value.map { productVariant ->
                productVariant.isPrimary
            }.toMutableList()
        }.toMutableList()
    }

    private fun updateSelectedCombination(level1Position: Int, level2Position: Int) {
        val result = mutableListOf<Int>()
        val levelCount = variantInputModel.selections.size
        result.add(level1Position)
        if (levelCount > 1) {
            result.add(level2Position)
        }

        selectedCombination = result
    }
}
