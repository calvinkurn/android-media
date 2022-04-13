package com.tokopedia.vouchercreation.product.list.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemProductListVariantLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.model.VariantUiModel

class ProductItemVariantViewHolder(
        private val binding: ItemProductListVariantLayoutBinding,
        private val variantItemClickListener: OnVariantItemClickListener,
        private val deleteButtonClickListener: OnDeleteButtonClickListener
) : RecyclerView.ViewHolder(binding.root) {

    interface OnVariantItemClickListener {
        fun onVariantCheckBoxClicked(isSelected: Boolean, variantIndex: Int)
    }

    interface OnDeleteButtonClickListener {
        fun onDeleteButtonClicked(variantIndex: Int)
    }

    var variantIndex: Int = 0

    fun bindData(productVariant: VariantUiModel, variantIndex: Int) {
        binding.root.setTag(R.id.product_variant, productVariant)
        this.variantIndex = variantIndex
        binding.root.setTag(R.id.product_variant, productVariant)
        binding.cbuVariantItem.isChecked = productVariant.isSelected
        binding.iuRemoveVariant.isVisible = productVariant.isEditing
        binding.tpgVariantName.text = productVariant.variantName
        binding.tpgSku.text = productVariant.sku
        binding.tpgProductPrice.text = productVariant.priceTxt
        binding.tpgSoldAndStock.text = productVariant.soldNStock
        if (productVariant.isError) {
            binding.cbuVariantItem.isClickable = false
            if (productVariant.errorMessage.isNotBlank()) binding.tpgVariantError.text = productVariant.errorMessage
            binding.tpgVariantError.show()
        } else {
            binding.cbuVariantItem.isClickable = true
            binding.tpgVariantError.hide()
        }
        // view mode
        val isViewing = productVariant.isViewing
        if (isViewing) {
            binding.cbuVariantItem.invisible()
            binding.iuRemoveVariant.hide()
        }
        // edit mode
        val isEditing = productVariant.isEditing
        if (isEditing) {
            binding.iuRemoveVariant.show()
        }

        binding.cbuVariantItem.setOnCheckedChangeListener { _, isChecked ->
            val productVariant = binding.root.getTag(R.id.product_variant) as VariantUiModel
            variantItemClickListener.onVariantCheckBoxClicked(isChecked, variantIndex)
        }

        binding.iuRemoveVariant.setOnClickListener {
            deleteButtonClickListener.onDeleteButtonClicked(variantIndex)
        }
    }
}