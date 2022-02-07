package com.tokopedia.vouchercreation.product.list.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemProductListVariantLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.model.VariantUiModel

class ProductItemVariantViewHolder(
        private val binding: ItemProductListVariantLayoutBinding,
        variantItemClickListener: OnVariantItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    interface OnVariantItemClickListener {
        fun onVariantCheckBoxClicked(isSelected: Boolean, productVariant: VariantUiModel)
    }

    init {
        binding.cbuVariantItem.setOnCheckedChangeListener { _, isChecked ->
            val productVariant = binding.root.getTag(R.id.product_variant) as VariantUiModel
            variantItemClickListener.onVariantCheckBoxClicked(isChecked, productVariant)
        }
    }

    fun bindData(productVariant: VariantUiModel) {
        binding.root.setTag(R.id.product_variant, productVariant)
        binding.tpgVariantName.text = productVariant.variantName
        binding.tpgSku.text = productVariant.sku
        binding.tpgProductPrice.text = productVariant.price
        binding.tpgSoldAndStock.text = productVariant.soldNStock
    }
}