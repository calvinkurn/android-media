package com.tokopedia.shop.flashsale.presentation.creation.manage.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.ReserveProductModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.SelectedProductModel

class ReserveProductAdapter(
    private val onSelectedItemChanges: (selectedProductId: MutableList<SelectedProductModel>) -> Unit
) : RecyclerView.Adapter<ReserveProductViewHolder>() {

    private var items: MutableList<ReserveProductModel> = mutableListOf()
    private var selectedProduct: MutableList<SelectedProductModel> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReserveProductViewHolder {
        val rootView = ReserveProductViewHolder.createRootView(parent)
        return ReserveProductViewHolder(rootView, ::itemOnClick)
    }

    override fun onBindViewHolder(holder: ReserveProductViewHolder, position: Int) {
        holder.bind(items[position])
    }

    private fun itemOnClick(position: Int, value: Boolean) {
        val selectedItem = items[position]
        selectedItem.isSelected = value
        if (value) {
            addToSelectedProduct(selectedItem)
        } else {
            removeFromSelectedProduct(selectedItem)
        }
        onSelectedItemChanges(selectedProduct)
    }

    private fun removeFromSelectedProduct(selectedItem: ReserveProductModel) {
        selectedProduct.removeAll {
            it.productId == selectedItem.productId || it.parentProductId == selectedItem.productId
        }
    }

    private fun addToSelectedProduct(selectedItem: ReserveProductModel) {
        val isExist = selectedProduct.any { it.productId == selectedItem.productId }

        if (isExist) return // just add unique productId
        selectedItem.variant.forEach {
            selectedProduct.add(
                SelectedProductModel(it, parentProductId = selectedItem.productId)
            )
        }

        selectedProduct.add(
            SelectedProductModel(selectedItem.productId, parentProductId = null)
        )
    }

    override fun getItemCount() = items.size

    fun addItems(newItems: List<ReserveProductModel>) {
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun clearData() {
        items.clear()
        notifyDataSetChanged()
    }
}