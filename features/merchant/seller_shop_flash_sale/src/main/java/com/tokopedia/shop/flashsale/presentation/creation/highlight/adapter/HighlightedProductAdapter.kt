package com.tokopedia.shop.flashsale.presentation.creation.highlight.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemProductHighlightBinding
import com.tokopedia.shop.flashsale.common.extension.convertRupiah
import com.tokopedia.shop.flashsale.common.extension.disable
import com.tokopedia.shop.flashsale.common.extension.enable
import com.tokopedia.shop.flashsale.common.extension.strikethrough
import com.tokopedia.shop.flashsale.domain.entity.HighlightableProduct
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class HighlightedProductAdapter(
    private val onProductSelectionChange: (HighlightableProduct, Boolean) -> Unit
) : RecyclerView.Adapter<HighlightedProductAdapter.HighlightedProductViewHolder>() {

    companion object {
        private const val ALPHA_DISABLED = 0.5f
        private const val ALPHA_ENABLED = 1.0f
    }

    private var products: MutableList<HighlightableProduct> = mutableListOf()
    private var isLoading = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HighlightedProductViewHolder {
        val binding =
            SsfsItemProductHighlightBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HighlightedProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: HighlightedProductViewHolder, position: Int) {
        val isLoading = isLoading && (position == products.lastIndex)
        holder.bind(products[position], onProductSelectionChange, isLoading)
    }

    fun submit(products: List<HighlightableProduct>) {
        val diffCallback = DiffCallback(this.products, products)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.products.clear()
        this.products.addAll(products)
        diffResult.dispatchUpdatesTo(this)
    }

    fun showLoading() {
        isLoading = true
        if (itemCount.isMoreThanZero()) {
            notifyItemChanged(products.lastIndex)
        }
    }

    fun hideLoading() {
        isLoading = false
        if (itemCount.isMoreThanZero()) {
            notifyItemChanged(products.lastIndex)
        }
    }

    fun getItems(): List<HighlightableProduct> {
        return products
    }


    inner class HighlightedProductViewHolder(
        private val binding: SsfsItemProductHighlightBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            product: HighlightableProduct,
            onProductSelectionChange: (HighlightableProduct, Boolean) -> Unit,
            isLoading: Boolean
        ) {
            binding.imgProduct.loadImage(product.imageUrl)
            binding.tpgProductName.text = product.name
            binding.labelDiscountPercentage.setPercentage(product.discountPercentage)
            binding.tpgDiscountedPrice.setPrice(product.discountedPrice)
            binding.loader.isVisible = isLoading
            binding.tpgOriginalPrice.setPrice(product.originalPrice)
            binding.tpgOriginalPrice.strikethrough()
            handleDisplayErrorMessage(product)
            handleSwitchAppearance(product, onProductSelectionChange)
            handleCardSelectable(product.disabled)
            handleProductOrderNumber(product)
        }

        private fun handleDisplayErrorMessage(product: HighlightableProduct) {
            binding.tpgErrorMessage.isVisible = product.isOtherProductAlreadySelected()
        }

        private fun handleProductOrderNumber(product: HighlightableProduct) {
            if (product.isSelected) {
                binding.tpgProductOrder.setPosition(product.position)
                binding.tpgProductOrder.visible()
            } else {
                binding.tpgProductOrder.invisible()
            }
        }

        private fun handleSwitchAppearance(
            product: HighlightableProduct,
            onProductSelectionChange: (HighlightableProduct, Boolean) -> Unit
        ) {
            binding.switchUnify.setOnCheckedChangeListener(null)
            binding.switchUnify.isChecked = product.isSelected
            binding.switchUnify.isClickable = !product.disabled
            binding.switchUnify.setOnCheckedChangeListener { _, isChecked ->
                onProductSelectionChange(product, isChecked)
                binding.switchUnify.isChecked = !isChecked
            }
        }

        private fun handleCardSelectable(disabled: Boolean) {
            if (disabled) {
                binding.imgProduct.alpha = ALPHA_DISABLED
                binding.tpgProductName.disable()
                binding.tpgOriginalPrice.disable()
                binding.tpgDiscountedPrice.disable()
                binding.switchUnify.disable()
                binding.labelDiscountPercentage.opacityLevel = ALPHA_DISABLED
            } else {
                binding.imgProduct.alpha = ALPHA_ENABLED
                binding.tpgProductName.enable()
                binding.tpgOriginalPrice.enable()
                binding.tpgDiscountedPrice.enable()
                binding.switchUnify.enable()
                binding.labelDiscountPercentage.opacityLevel = ALPHA_ENABLED
            }
        }

        private fun Label.setPercentage(percentage: Int) {
            val formattedPercentage = String.format(this.context.getString(R.string.sfs_placeholder_percent), percentage)
            this.text = formattedPercentage
        }

        private fun Typography.setPrice(price: Long) {
            this.text = price.convertRupiah()
        }

        private fun Typography.setPosition(position: Int) {
            val formattedPosition = String.format(this.context.getString(R.string.sfs_placeholder_product_order), position)
            this.text = formattedPosition
        }

        private fun HighlightableProduct.isOtherProductAlreadySelected(): Boolean {
            return this.disabledReason == HighlightableProduct.DisabledReason.OTHER_PRODUCT_WITH_SAME_PARENT_ID_ALREADY_SELECTED
        }

    }

    inner class DiffCallback(
        private val oldProductList: List<HighlightableProduct>,
        private val newProductList: List<HighlightableProduct>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldProductList.size
        override fun getNewListSize() = newProductList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldProductList[oldItemPosition].id == newProductList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return  oldProductList[oldItemPosition] == newProductList[newItemPosition]
        }

    }
}