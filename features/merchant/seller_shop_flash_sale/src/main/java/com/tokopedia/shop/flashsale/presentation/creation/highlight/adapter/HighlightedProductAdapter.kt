package com.tokopedia.shop.flashsale.presentation.creation.highlight.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemProductHighlightBinding
import com.tokopedia.shop.flashsale.common.extension.convertRupiah
import com.tokopedia.shop.flashsale.common.extension.disable
import com.tokopedia.shop.flashsale.common.extension.enable
import com.tokopedia.shop.flashsale.domain.entity.HighlightableProduct
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class HighlightedProductAdapter(
    private val onProductClicked: (HighlightableProduct, Int) -> Unit,
    private val onProductSelectionChange: (HighlightableProduct, Boolean) -> Unit
) : RecyclerView.Adapter<HighlightedProductAdapter.HighlightedProductViewHolder>() {

    companion object {
        private const val ALPHA_DISABLED = 0.5f
        private const val ALPHA_ENABLED = 1.0f
        private const val ONE = 1
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
        holder.bind(
            position,
            products[position],
            onProductClicked,
            onProductSelectionChange,
            isLoading
        )
    }

    fun addData(items: List<HighlightableProduct>) {
        val previousProductsSize = items.size
        this.products.addAll(items)
        notifyItemRangeChanged(previousProductsSize, this.products.size)
    }

    fun update(product: HighlightableProduct, updatedProduct: HighlightableProduct) {
        try {
            val position = products.indexOf(product)
            this.products[position] = updatedProduct
            notifyItemChanged(position)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun submit(products: List<HighlightableProduct>) {
        this.products = products.toMutableList()
        notifyItemRangeChanged(Int.ZERO, products.size)
    }


    fun clearData() {
        this.products = mutableListOf()
        notifyItemRangeChanged(Int.ZERO, this.products.size)
    }

    fun showLoading() {
        if (itemCount.isMoreThanZero()) {
            isLoading = true
            notifyItemChanged(products.lastIndex)
        }
    }

    fun hideLoading() {
        isLoading = false
    }

    fun getItems(): List<HighlightableProduct> {
        return products
    }


    inner class HighlightedProductViewHolder(
        private val binding: SsfsItemProductHighlightBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            product: HighlightableProduct,
            onProductClicked: (HighlightableProduct, Int) -> Unit,
            onProductSelectionChange: (HighlightableProduct, Boolean) -> Unit,
            isLoading: Boolean
        ) {
            binding.imgProduct.loadImage(product.imageUrl)
            binding.tpgProductName.text = product.name
            binding.labelDiscountPercentage.setPercentage(product.discountPercentage)
            binding.tpgDiscountedPrice.setPrice(product.discountedPrice)
            binding.root.setOnClickListener { onProductClicked(product, position) }
            binding.loader.isVisible = isLoading
            binding.tpgOriginalPrice.setPrice(product.originalPrice)
            binding.tpgProductOrder.isVisible = product.isSelected
            binding.tpgProductOrder.setPosition(position)
            handleSwitchAppearance(product, onProductSelectionChange)
            handleCardSelectable(product.disableClick, product.disabled)
        }

        private fun handleSwitchAppearance(
            product: HighlightableProduct,
            onProductSelectionChange: (HighlightableProduct, Boolean) -> Unit
        ) {
            binding.switchUnify.setOnCheckedChangeListener(null)
            binding.switchUnify.isChecked = product.isSelected
            binding.switchUnify.isClickable = !product.disableClick && !product.disabled
            binding.switchUnify.setOnCheckedChangeListener { _, isSelected ->
                onProductSelectionChange(
                    product,
                    isSelected
                )
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
            val incrementedPosition = position + ONE
            val formattedPosition = String.format(this.context.getString(R.string.sfs_placeholder_product_order), incrementedPosition)
            this.text = formattedPosition
        }

        private fun handleCardSelectable(disableClick: Boolean, disabled: Boolean) {
            if (disableClick || disabled) {
                binding.imgProduct.alpha = ALPHA_DISABLED
                binding.tpgProductName.disable()
                binding.tpgOriginalPrice.disable()
                binding.switchUnify.disable()
            } else {
                binding.imgProduct.alpha = ALPHA_ENABLED
                binding.tpgProductName.enable()
                binding.tpgOriginalPrice.enable()
                binding.switchUnify.enable()
            }
        }

    }
}