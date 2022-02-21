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
        variantItemClickListener: OnVariantItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    interface OnVariantItemClickListener {
        fun onVariantCheckBoxClicked(isSelected: Boolean, variantIndex: Int)
    }

    var variantIndex: Int = 0

    init {
        binding.cbuVariantItem.setOnCheckedChangeListener { _, isChecked ->
            val productVariant = binding.root.getTag(R.id.product_variant) as VariantUiModel
            variantItemClickListener.onVariantCheckBoxClicked(isChecked, variantIndex)
        }
    }

    fun bindData(productVariant: VariantUiModel, variantIndex: Int) {
        this.variantIndex = variantIndex
        binding.root.setTag(R.id.product_variant, productVariant)
        binding.cbuVariantItem.isChecked = productVariant.isSelected
        binding.iuRemoveVariant.isVisible = productVariant.isEditing
        binding.tpgVariantName.text = productVariant.variantName
        binding.tpgSku.text = productVariant.sku
        binding.tpgProductPrice.text = productVariant.priceTxt
        binding.tpgSoldAndStock.text = productVariant.soldNStock
        if (productVariant.isError) {
            binding.tpgVariantError.text = productVariant.errorMessage
            binding.tpgVariantError.show()
        } else binding.tpgVariantError.hide()
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
    }
}