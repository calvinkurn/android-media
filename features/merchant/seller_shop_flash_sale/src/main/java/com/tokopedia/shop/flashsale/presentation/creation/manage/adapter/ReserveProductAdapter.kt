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
    private var inputEnabled = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReserveProductViewHolder {
        val rootView = ReserveProductViewHolder.createRootView(parent)
        return ReserveProductViewHolder(rootView, ::itemOnClick)
    }

    override fun onBindViewHolder(holder: ReserveProductViewHolder, position: Int) {
        holder.bind(items[position], inputEnabled)
    }

    override fun getItemCount() = items.size

    private fun itemOnClick(position: Int, value: Boolean) {
        val selectedItem = items[position]
        selectedItem.isSelected = value
        if (value) {
            addToSelectedProduct(selectedItem, false)
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

    private fun addToSelectedProduct(selectedItem: ReserveProductModel, isPreselected: Boolean) {
        val isExist = selectedProduct.any { it.productId == selectedItem.productId }

        if (isExist) return // just add unique productId
        selectedItem.variant.forEach {
            selectedProduct.add(
                SelectedProductModel(
                    productId = it,
                    parentProductId = selectedItem.productId,
                    isPreselected = isPreselected)
            )
        }

        selectedProduct.add(
            SelectedProductModel(selectedItem.productId, parentProductId = null, isPreselected)
        )
    }

    private fun addPreselectedProduct(selectedItems: List<ReserveProductModel>) {
        selectedItems.forEach {
            if (it.isSelected) addToSelectedProduct(it, true)
        }
        onSelectedItemChanges(selectedProduct)
    }

    fun setInputEnabled(enabled: Boolean) {
        inputEnabled = enabled
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<ReserveProductModel>) {
        items.addAll(newItems)
        addPreselectedProduct(newItems)
        notifyDataSetChanged()
    }

    fun clearData() {
        items.clear()
        notifyDataSetChanged()
    }
}