package com.tokopedia.shop.flashsale.presentation.creation.highlight.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemProductHighlightBinding
import com.tokopedia.shop.flashsale.domain.entity.HighlightableProduct

class HighlightedProductAdapter(
    private val onProductClicked: (HighlightableProduct, Int) -> Unit,
    private val onProductSelectionChange : (HighlightableProduct, Boolean) -> Unit
) : RecyclerView.Adapter<HighlightedProductViewHolder>() {

    private var products: MutableList<HighlightableProduct> = mutableListOf()
    private var isLoading = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighlightedProductViewHolder {
        val binding =
            SsfsItemProductHighlightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HighlightedProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: HighlightedProductViewHolder, position: Int) {
        products.getOrNull(position)?.let { product ->
            val isLoading = isLoading && (position == products.lastIndex)
            holder.bind(
                position,
                product,
                onProductClicked,
                onProductSelectionChange,
                isLoading
            )
        }
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
        } catch (e : Exception) {
            e.printStackTrace()
        }
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

}