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
) : RecyclerView.ViewHolder(binding.root),
        ProductItemVariantViewHolder.OnVariantItemClickListener,
        VariantListAdapter.OnVariantSelectionRemovedListener {

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
        // reset all listeners because selection state will be bind from data
        resetListeners()

        // bind data set position
        binding.root.setTag(R.id.dataset_position, dataSetPosition)

        // enable / disable item list
        binding.root.isEnabled = productUiModel.isEnabled

        // product list item views
        binding.root.setTag(R.id.product, productUiModel)
        binding.cbuProductItem.setIndeterminate(productUiModel.hasVariant)
        binding.cbuProductItem.isChecked = productUiModel.isSelected
        binding.iuProductImage.loadImage(productUiModel.imageUrl)
        binding.tpgProductName.text = productUiModel.productName
        binding.tpgSku.text = productUiModel.sku
        binding.tpgProductPrice.text = productUiModel.price
        binding.tpgSoldAndStock.text = productUiModel.soldNStock

        // disable product selection if all variants are errors
        binding.cbuProductItem.isEnabled = productUiModel.isSelectable

        // view mode - no selection, no deletion
        val isViewing = productUiModel.isViewing
        if (isViewing) {
            binding.cbuProductItem.invisible()
            binding.iuRemoveProduct.hide()
        }
        // edit mode - visible delete button
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
                if (isVariantHeaderExpanded) {
                    context?.run { binding.iuChevron.setImageDrawable(getDrawable(R.drawable.ic_mvc_chevron_up)) }
                } else {
                    context?.run { binding.iuChevron.setImageDrawable(getDrawable(R.drawable.ic_mvc_chevron_down)) }
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
            variantListAdapter.updateVariantSelections(isChecked)
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
            if (!isVariantHeaderExpanded) {
                context?.run { binding.iuChevron.setImageDrawable(getDrawable(R.drawable.ic_mvc_chevron_up)) }
            } else {
                context?.run { binding.iuChevron.setImageDrawable(getDrawable(R.drawable.ic_mvc_chevron_down)) }
            }
            productItemClickListener.onProductVariantHeaderClicked(!isVariantHeaderExpanded, dataSetPosition)
        }
    }

    override fun onVariantCheckBoxClicked(isSelected: Boolean, variantIndex: Int) {
        // update variant selection to product data in product parent adapter
        val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
        val selectedVariantSize = productItemClickListener.onProductVariantCheckBoxClicked(
                isSelected,
                dataSetPosition,
                variantIndex
        )
        // remove listeners to prevent unwanted behaviors
        resetListeners()
        // change parent product selection start here
        // if variant product selected
        if (isSelected) {
            // select parent product if one of its variant got selected
            val isParentProductSelected = binding.cbuProductItem.isChecked
            if (!isParentProductSelected) {
                binding.cbuProductItem.isChecked = true
                productItemClickListener.onProductCheckBoxClicked(true, dataSetPosition)
            }
        }
        // if no variant item got selected set uncheck parent selection
        else {
            if (selectedVariantSize.isZero()) {
                binding.cbuProductItem.isChecked = false
                productItemClickListener.onProductCheckBoxClicked(false, dataSetPosition)
            }
        }
        // return ui listeners
        setupListeners()
    }

    override fun onVariantSelectionRemoved(variantList: List<VariantUiModel>) {
        // update variant selection to product data in product parent adapter
        val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
        productItemClickListener.onProductVariantRemoved(variantList, dataSetPosition)
    }

    override fun onVariantSelectionsEmpty() {
        // manage product purpose - delete product selection if there is no variant selection
        val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
        productItemClickListener.onRemoveProductButtonClicked(adapterPosition, dataSetPosition)
    }
}