package com.tokopedia.vouchercreation.product.list.view.viewholder

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemProductListLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.adapter.ProductVariantListAdapter
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel

class ProductItemViewHolder(
        private val binding: ItemProductListLayoutBinding,
        private val productItemClickListener: OnProductItemClickListener,
        variantItemClickListener: ProductItemVariantViewHolder.OnVariantItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    interface OnProductItemClickListener {
        fun onProductCheckBoxClicked(isSelected: Boolean, productUiModel: ProductUiModel, adapterPosition: Int)
        fun onVariantAccordionClicked(isVariantEmpty: Boolean, productId: String)
    }

    private var context: Context? = null
    private var variantListAdapter = ProductVariantListAdapter(variantItemClickListener)

    init {
        context = binding.root.context
        binding.cbuProductItem.setOnCheckedChangeListener { _, isChecked ->
            val product = binding.root.getTag(R.id.product) as ProductUiModel
            if (!product.isSelectAll) productItemClickListener.onProductCheckBoxClicked(isChecked, product, adapterPosition)
        }
        binding.productVariantLayout.setOnClickListener {
            val product = binding.root.getTag(R.id.product) as ProductUiModel
            productItemClickListener.onVariantAccordionClicked(product.variants.isEmpty(), product.id)
        }
        binding.rvProductVariants.apply {
            adapter = variantListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun bindData(productUiModel: ProductUiModel) {
        // product list item views
        binding.root.setTag(R.id.product, productUiModel)
        binding.cbuProductItem.isChecked = productUiModel.isSelected
        binding.iuProductImage.loadImage(productUiModel.imageUrl)
        binding.tpgProductName.text = productUiModel.productName
        binding.tpgSku.text = productUiModel.sku
        binding.tpgProductPrice.text = productUiModel.price
        binding.tpgSoldAndStock.text = productUiModel.soldNStock

        // variant layout
        if (productUiModel.hasVariant) {
            variantListAdapter.setVariantList(productUiModel.variants)
            binding.variantHeader.visible()
            if (productUiModel.isVariantsEmpty) binding.variantDivider.gone()
            else binding.variantDivider.visible()
            binding.rvProductVariants.visible()
        } else {
            binding.variantHeader.gone()
            binding.variantDivider.gone()
            binding.rvProductVariants.gone()
        }
    }
}