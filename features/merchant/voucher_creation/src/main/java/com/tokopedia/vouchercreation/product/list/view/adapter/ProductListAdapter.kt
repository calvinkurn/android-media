package com.tokopedia.vouchercreation.product.list.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.databinding.ItemProductListLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import com.tokopedia.vouchercreation.product.list.view.viewholder.ProductItemViewHolder
import com.tokopedia.vouchercreation.product.list.view.viewholder.ProductItemViewHolder.OnProductItemClickListener

@SuppressLint("NotifyDataSetChanged")
class ProductListAdapter(private val listener: OnProductItemClickListener)
    : RecyclerView.Adapter<ProductItemViewHolder>(), OnProductItemClickListener {

    interface OnProductItemClickListener {
        fun onProductCheckBoxClicked(isSelected: Boolean)
    }

    private var productUiModelList: MutableList<ProductUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val binding = ItemProductListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductItemViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        holder.bindData(productUiModelList[position], position)
    }

    override fun getItemCount(): Int {
        return productUiModelList.size
    }

    fun getSelectedProducts(): List<ProductUiModel> {
        return productUiModelList.filter {
            it.isSelected
        }
    }

    fun setProductList(productUiModelList: List<ProductUiModel>) {
        this.productUiModelList = productUiModelList.toMutableList()
        notifyDataSetChanged()
    }

    fun updateAllProductSelections(isSelectAll: Boolean) {
        this.productUiModelList.forEach {
            it.isSelectAll = isSelectAll
            it.isSelected = isSelectAll
        }
        notifyDataSetChanged()
    }

    fun isProductListEnabled(isEnabled: Boolean) {
        this.productUiModelList.forEach {
            it.isEnabled = isEnabled
        }
        notifyDataSetChanged()
    }

    fun addProducts(products : List<ProductUiModel>) {
        this.productUiModelList.addAll(products)
        notifyDataSetChanged()
    }

    fun clearData() {
        this.productUiModelList = mutableListOf()
        notifyDataSetChanged()
    }

    fun deleteSelectedProducts() {
        productUiModelList.removeAll { it.isSelected }
        notifyDataSetChanged()
    }

    override fun onProductCheckBoxClicked(isSelected: Boolean, dataSetPosition: Int) {
        productUiModelList[dataSetPosition].isSelected = isSelected
        listener.onProductCheckBoxClicked(isSelected)
    }

    override fun onRemoveProductButtonClicked(adapterPosition: Int, dataSetPosition: Int) {
        productUiModelList.removeAt(dataSetPosition)
        notifyItemRemoved(adapterPosition)
    }

    override fun onProductVariantCheckBoxClicked(isSelected: Boolean, dataSetPosition: Int, variantIndex: Int): Int {
        productUiModelList[dataSetPosition].variants[variantIndex].isSelected = isSelected
        return productUiModelList[dataSetPosition].getSelectedVariants().size
    }

    override fun onProductVariantHeaderClicked(isExpanded: Boolean, dataSetPosition: Int) {
        productUiModelList[dataSetPosition].isVariantHeaderExpanded = isExpanded
    }
}