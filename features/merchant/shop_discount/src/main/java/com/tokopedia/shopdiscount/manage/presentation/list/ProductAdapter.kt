package com.tokopedia.shopdiscount.manage.presentation.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.shopdiscount.databinding.SdItemProductBinding
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import com.tokopedia.shopdiscount.utils.constant.ZERO

class ProductAdapter(
    private val onProductClicked: (Product) -> Unit,
    private val onUpdateDiscountButtonClicked: (Product) -> Unit,
    private val onOverflowMenuClicked: (Product) -> Unit,
    private val onVariantInfoClicked : (Product) -> Unit,
    private val onProductSelectionChange : (Product, Boolean) -> Unit
) : RecyclerView.Adapter<ProductViewHolder>() {

    private var products: MutableList<Product> = mutableListOf()
    private var isLoading = false

    companion object {
        private const val FIRST_ITEM = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            SdItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        products.getOrNull(position)?.let { product ->
            val isLoading = isLoading && (position == products.lastIndex)
            holder.bind(
                product,
                onProductClicked,
                onUpdateDiscountButtonClicked,
                onOverflowMenuClicked,
                onVariantInfoClicked,
                onProductSelectionChange,
                isLoading
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(items: List<Product>) {
        this.products.addAll(items)
        notifyDataSetChanged()
    }

    fun update(product: Product, updatedProduct: Product) {
        try {
            val position = products.indexOf(product)
            this.products[position] = updatedProduct
            notifyItemChanged(position)
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    fun updateAll(items: List<Product>) {
        this.products.clear()
        this.products.addAll(items)
        notifyItemRangeChanged(FIRST_ITEM, items.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh(items: List<Product>) {
        this.products.clear()
        this.products.addAll(items)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        this.products = mutableListOf()
        notifyDataSetChanged()
    }

    fun delete(product: Product) {
        val index = products.indexOf(product)
        products.remove(product)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    fun bulkDelete(deletedProductId: List<String>) {
        val products = this.products.toMutableList()
        products
            .filter { it.id in deletedProductId }
            .forEach { product ->
                val index = products.indexOf(product)
                this.products.remove(product)
                notifyItemRemoved(index)
            }
        notifyItemRangeChanged(ZERO, itemCount)
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

    fun getItems(): List<Product> {
        return products
    }
}