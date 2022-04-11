package com.tokopedia.shopdiscount.select.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.shopdiscount.databinding.SdItemSelectProductBinding
import com.tokopedia.shopdiscount.select.domain.entity.ReservableProduct

class SelectProductAdapter(
    private val onProductClicked: (ReservableProduct) -> Unit,
    private val onProductSelectionChange : (ReservableProduct, Boolean) -> Unit
) : RecyclerView.Adapter<SelectProductViewHolder>() {

    private var products: MutableList<ReservableProduct> = mutableListOf()
    private var isLoading = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectProductViewHolder {
        val binding =
            SdItemSelectProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: SelectProductViewHolder, position: Int) {
        products.getOrNull(position)?.let { product ->
            val isLoading = isLoading && (position == products.lastIndex)
            holder.bind(
                product,
                onProductClicked,
                onProductSelectionChange,
                isLoading
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(items: List<ReservableProduct>) {
        this.products.addAll(items)
        notifyDataSetChanged()
    }

    fun update(product: ReservableProduct, updatedProduct: ReservableProduct) {
        try {
            val position = products.indexOf(product)
            this.products[position] = updatedProduct
            notifyItemChanged(position)
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh(items: List<ReservableProduct>) {
        this.products.clear()
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

    fun getItems(): List<ReservableProduct> {
        return products
    }
}