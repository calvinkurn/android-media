package com.tokopedia.vouchercreation.product.list.view.viewholder

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemProductListLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.adapter.VariantListAdapter
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel

class ProductItemViewHolder(
        private val binding: ItemProductListLayoutBinding,
        private val productItemClickListener: OnProductItemClickListener
) : RecyclerView.ViewHolder(binding.root), ProductItemVariantViewHolder.OnVariantItemClickListener {

    interface OnProductItemClickListener {
        fun onProductCheckBoxClicked(isSelected: Boolean, dataSetPosition: Int)
        fun onProductVariantCheckBoxClicked(isSelected: Boolean, dataSetPosition: Int, variantIndex: Int): Int
        fun onProductVariantHeaderClicked(isExpanded: Boolean, dataSetPosition: Int)
    }

    private var context: Context? = null
    private var variantListAdapter = VariantListAdapter(this)

    init {
        context = binding.root.context
        binding.cbuProductItem.setOnCheckedChangeListener { _, isChecked ->
            val product = binding.root.getTag(R.id.product) as ProductUiModel
            val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
            if (!product.isSelectAll) productItemClickListener.onProductCheckBoxClicked(isChecked, dataSetPosition)
        }
        binding.variantHeader.setOnClickListener {
            val product = binding.root.getTag(R.id.product) as ProductUiModel
            val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
            val isVariantHeaderExpanded = product.isVariantHeaderExpanded
            // !isVariantHeaderExpanded => current condition after clicked
            binding.variantDivider.isVisible = !isVariantHeaderExpanded
            binding.rvProductVariants.isVisible = !isVariantHeaderExpanded
            if (!isVariantHeaderExpanded) { context?.run { binding.iuChevron.setImageDrawable(getDrawable(R.drawable.ic_mvc_chevron_up)) } }
            else { context?.run { binding.iuChevron.setImageDrawable(getDrawable(R.drawable.ic_mvc_chevron_down)) } }
            productItemClickListener.onProductVariantHeaderClicked(!isVariantHeaderExpanded, dataSetPosition)
        }
        binding.rvProductVariants.apply {
            adapter = variantListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun bindData(productUiModel: ProductUiModel, dataSetPosition: Int) {

        // bind data set position
        binding.root.setTag(R.id.dataset_position, dataSetPosition)

        // enable / disable item list
        binding.root.isEnabled = productUiModel.isEnabled

        // product list item views
        binding.root.setTag(R.id.product, productUiModel)
        binding.cbuProductItem.isChecked = productUiModel.isSelected
        binding.iuRemoveProduct.isVisible = productUiModel.isEditing
        binding.iuProductImage.loadImage(productUiModel.imageUrl)
        binding.tpgProductName.text = productUiModel.productName
        binding.tpgSku.text = productUiModel.sku
        binding.tpgProductPrice.text = productUiModel.price
        binding.tpgSoldAndStock.text = productUiModel.soldNStock

        // variant layout
        if (productUiModel.hasVariant && !productUiModel.isError) {
            variantListAdapter.setVariantList(productUiModel.variants)
            variantListAdapter.setDataSetPosition(dataSetPosition)
            variantListAdapter.setParentAdapterPosition(adapterPosition)
            binding.variantHeader.visible()
            if (productUiModel.variants.isEmpty()) {
                binding.variantDivider.gone()
                context?.run { binding.iuChevron.setImageDrawable(getDrawable(R.drawable.ic_mvc_chevron_down)) }
            } else {
                val isVariantHeaderExpanded = productUiModel.isVariantHeaderExpanded
                binding.variantDivider.isVisible = isVariantHeaderExpanded
                binding.rvProductVariants.isVisible = isVariantHeaderExpanded
                if (isVariantHeaderExpanded) { context?.run { binding.iuChevron.setImageDrawable(getDrawable(R.drawable.ic_mvc_chevron_up)) } }
                else { context?.run { binding.iuChevron.setImageDrawable(getDrawable(R.drawable.ic_mvc_chevron_down)) }
                }
            }
        } else {
            binding.variantHeader.gone()
            binding.variantDivider.gone()
            binding.rvProductVariants.gone()
        }

        // error layout
        if (productUiModel.isError) {
            binding.root.isEnabled = false
            binding.errorLayout.show()
            binding.tpgProductError.text = productUiModel.errorMessage
        } else {
            binding.root.isEnabled = true
            binding.errorLayout.hide()
        }
    }

    override fun onVariantCheckBoxClicked(isSelected: Boolean, variantIndex: Int) {
        val isIndeterminate = binding.cbuProductItem.getIndeterminate()
        val isChecked = binding.cbuProductItem.isChecked
        val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
        val selectedVariantSize = productItemClickListener.onProductVariantCheckBoxClicked(isSelected, dataSetPosition, variantIndex)
        if (isSelected) {
            if (!isIndeterminate) binding.cbuProductItem.setIndeterminate(true)
            if (!isChecked) binding.cbuProductItem.isChecked = true
        } else {
            if (selectedVariantSize.isZero()) {
                if (isIndeterminate) binding.cbuProductItem.setIndeterminate(false)
                if (selectedVariantSize.isZero()) binding.cbuProductItem.isChecked = false
            }
        }
    }
}