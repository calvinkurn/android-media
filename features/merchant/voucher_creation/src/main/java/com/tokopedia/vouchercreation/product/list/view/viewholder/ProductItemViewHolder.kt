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
import com.tokopedia.vouchercreation.product.list.view.model.VariantUiModel

class ProductItemViewHolder(
        private val binding: ItemProductListLayoutBinding,
        private val productItemClickListener: OnProductItemClickListener
) : RecyclerView.ViewHolder(binding.root), ProductItemVariantViewHolder.OnVariantItemClickListener, VariantListAdapter.OnVariantSelectionRemovedListener {

    interface OnProductItemClickListener {
        fun onProductCheckBoxClicked(isSelected: Boolean, dataSetPosition: Int)
        fun onRemoveProductButtonClicked(adapterPosition: Int, dataSetPosition: Int)
        fun onProductVariantCheckBoxClicked(isSelected: Boolean, dataSetPosition: Int, variantIndex: Int): Int
        fun onProductVariantHeaderClicked(isExpanded: Boolean, dataSetPosition: Int)
        fun onProductVariantRemoved(variantList: List<VariantUiModel>, dataSetPosition: Int)
    }

    private var context: Context? = null
    private var variantListAdapter = VariantListAdapter(this, this)

    init {
        context = binding.root.context
    }

    fun bindData(productUiModel: ProductUiModel, dataSetPosition: Int) {

        resetListeners()

        // bind data set position
        binding.root.setTag(R.id.dataset_position, dataSetPosition)

        // enable / disable item list
        binding.root.isEnabled = productUiModel.isEnabled

        // product list item views
        binding.root.setTag(R.id.product, productUiModel)
        binding.cbuProductItem.isChecked = productUiModel.isSelected

        binding.iuProductImage.loadImage(productUiModel.imageUrl)
        binding.tpgProductName.text = productUiModel.productName
        binding.tpgSku.text = productUiModel.sku
        binding.tpgProductPrice.text = productUiModel.price
        binding.tpgSoldAndStock.text = productUiModel.soldNStock

        // disable product selection if all variants are errors
        binding.cbuProductItem.isEnabled = productUiModel.isSelectable

        // view mode
        val isViewing = productUiModel.isViewing
        if (isViewing) {
            binding.cbuProductItem.invisible()
            binding.iuRemoveProduct.hide()
        }
        // edit mode
        val isEditing = productUiModel.isEditing
        if (isEditing) {
            binding.iuRemoveProduct.show()
        }

        // variant layout
        binding.rvProductVariants.apply {
            adapter = variantListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
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
            binding.cbuProductItem.isClickable = false
            binding.errorLayout.show()
            if (productUiModel.errorMessage.isNotBlank()) {
                binding.iuRedExclamation.show()
                binding.tpgProductError.text = productUiModel.errorMessage
            } else {
                binding.iuRedExclamation.hide()
            }
        } else {
            binding.cbuProductItem.isClickable = true
            binding.errorLayout.hide()
        }

        setupListeners()
    }

    private fun resetListeners() {
        binding.cbuProductItem.setOnCheckedChangeListener { _, _ -> }
        binding.iuRemoveProduct.setOnClickListener { }
        binding.variantHeader.setOnClickListener { }
    }

    private fun setupListeners() {
        binding.cbuProductItem.setOnCheckedChangeListener { _, isChecked ->
            val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
            productItemClickListener.onProductCheckBoxClicked(isChecked, dataSetPosition)

            // only select all child variants when: 1. cbu is not selected, 2. not ind, 3. no variant selections
            val isIndeterminate = binding.cbuProductItem.getIndeterminate()
            if (!isIndeterminate) variantListAdapter.updateVariantSelections(isChecked)
            else if (!isChecked) { variantListAdapter.updateVariantSelections(isChecked) }
        }
        binding.iuRemoveProduct.setOnClickListener {
            val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
            productItemClickListener.onRemoveProductButtonClicked(adapterPosition, dataSetPosition)
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
    }

    override fun onVariantCheckBoxClicked(isSelected: Boolean, variantIndex: Int) {
        // variant selection exist within this product item
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

    override fun onVariantSelectionRemoved(variantList: List<VariantUiModel>) {
        val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
        productItemClickListener.onProductVariantRemoved(variantList, dataSetPosition)
    }

    override fun onVariantSelectionsEmpty() {
        val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
        productItemClickListener.onRemoveProductButtonClicked(adapterPosition, dataSetPosition)
    }
}