package com.tokopedia.vouchercreation.product.list.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.databinding.ItemProductListLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import com.tokopedia.vouchercreation.product.list.view.model.VariantUiModel
import com.tokopedia.vouchercreation.product.list.view.viewholder.ProductItemViewHolder
import com.tokopedia.vouchercreation.product.list.view.viewholder.ProductItemViewHolder.OnProductItemClickListener

@SuppressLint("NotifyDataSetChanged")
class ProductListAdapter(private val listener: OnProductItemClickListener)
    : RecyclerView.Adapter<ProductItemViewHolder>(), OnProductItemClickListener {

    interface OnProductItemClickListener {
        fun onProductCheckBoxClicked(isSelected: Boolean, uiModel: ProductUiModel)
        fun onRemoveButtonClicked()
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

    fun getProductList(): MutableList<ProductUiModel> {
        return productUiModelList
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
            if (!it.isError && it.isSelectable) {
                it.isSelectAll = isSelectAll
                it.isSelected = isSelectAll
                it.variants.forEach { variantUiModel ->
                    if (!variantUiModel.isError) {
                        variantUiModel.isSelectAll = isSelectAll
                        variantUiModel.isSelected = isSelectAll
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    fun clearAllSelection() {
        this.productUiModelList.forEach {
            it.isSelected = false
        }
    }

    fun disableAllProductSelections() {
        this.productUiModelList.forEach {
            it.isError = true
            it.variants.forEach { variantUiModel ->
                variantUiModel.isError = true
            }
        }
    }

    fun enableAllSelectedProducts() {
        this.productUiModelList.forEach {
            val isSelected = it.isSelected
            if (isSelected)  {
                it.isError = false
            }
        }
    }

    fun enableAllProductSelections() {
        this.productUiModelList.forEach {
            if (it.errorMessage.isEmpty()) it.isError = false
            it.variants.forEach { variantUiModel ->
                if (variantUiModel.errorMessage.isEmpty()) variantUiModel.isError = false
            }
        }
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
        val uiModel = productUiModelList[dataSetPosition]
        uiModel.isSelected = isSelected
        listener.onProductCheckBoxClicked(isSelected, uiModel)
    }

    override fun onRemoveProductButtonClicked(adapterPosition: Int, dataSetPosition: Int) {
        try {
            productUiModelList.removeAt(dataSetPosition)
            notifyDataSetChanged()
            listener.onRemoveButtonClicked()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onProductVariantCheckBoxClicked(isSelected: Boolean, dataSetPosition: Int, variantIndex: Int): Int {
        var selectedVariantSize = 0
        try {
            productUiModelList[dataSetPosition].variants[variantIndex].isSelected = isSelected
            selectedVariantSize = productUiModelList[dataSetPosition].getSelectedVariants().size
        }  catch (e: Exception) {
            e.printStackTrace()
        }
        return selectedVariantSize
    }

    override fun onProductVariantHeaderClicked(isExpanded: Boolean, dataSetPosition: Int) {
        productUiModelList[dataSetPosition].isVariantHeaderExpanded = isExpanded
    }

    override fun onProductVariantRemoved(variantList: List<VariantUiModel>, dataSetPosition: Int) {
        productUiModelList[dataSetPosition].variants = variantList
    }
}