package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.product.addedit.databinding.AddEditProductItemVariantUnitBinding
import com.tokopedia.product.addedit.variant.data.model.Unit

class VariantUnitViewHolder(
    private val binding: AddEditProductItemVariantUnitBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(itemData: Unit, selectedVariantUnit: Unit?) {
        val isSelected = selectedVariantUnit?.variantUnitID == itemData.variantUnitID
        binding.tfVariantName.text = itemData.unitName
        binding.iconCheck.isVisible = isSelected
        val textColor = if (isSelected) {
            com.tokopedia.unifyprinciples.R.color.Unify_NN950
        } else {
            com.tokopedia.unifyprinciples.R.color.Unify_NN400
        }
        binding.tfVariantName.setTextColor(MethodChecker.getColor(
            binding.root.context,
            textColor
        ))
    }
}
