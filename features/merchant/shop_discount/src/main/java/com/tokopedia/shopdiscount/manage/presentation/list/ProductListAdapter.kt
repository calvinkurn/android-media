package com.tokopedia.shopdiscount.manage.presentation.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.shopdiscount.databinding.SdItemProductBinding
import com.tokopedia.shopdiscount.manage.domain.entity.Product

class ProductListAdapter(
    private val onProductClicked: (Product) -> Unit,
    private val onUpdateDiscountButtonClicked: (Product) -> Unit,
    private val onOverflowMenuClicked: (Product) -> Unit,
) : RecyclerView.Adapter<ProductViewHolder>() {

    private var products: MutableList<Product> = mutableListOf()
    private var isLoading = false

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
            holder.bind(product, onProductClicked, onUpdateDiscountButtonClicked, onOverflowMenuClicked, isLoading)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(items: List<Product>) {
        this.products.addAll(items)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        this.products = mutableListOf()
        notifyDataSetChanged()
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
}