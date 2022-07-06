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

    private fun addToSelectedProduct(
        selectedItem: ReserveProductModel,
        isProductPreviouslySubmitted: Boolean
    ) {
        val isExist = selectedProduct.any { it.productId == selectedItem.productId }

        if (isExist) return // just add unique productId
        selectedItem.variant.forEach {
            selectedProduct.add(
                SelectedProductModel(
                    productId = it,
                    parentProductId = selectedItem.productId,
                    isProductPreviouslySubmitted = isProductPreviouslySubmitted)
            )
        }

        selectedProduct.add(
            SelectedProductModel(selectedItem.productId, parentProductId = null, isProductPreviouslySubmitted)
        )
    }

    private fun addPreselectedProduct(selectedItems: List<ReserveProductModel>) {
        selectedItems.forEach {
            if (it.isSelected) addToSelectedProduct(it, true)
        }
        onSelectedItemChanges(selectedProduct)
    }

    private fun notifyUnselectedItems() {
        items.forEachIndexed { index, item ->
            if (!item.isSelected) notifyItemChanged(index)
        }
    }

    fun setInputEnabled(enabled: Boolean) {
        val dataChanged = inputEnabled != enabled
        inputEnabled = enabled

        if (dataChanged) notifyUnselectedItems()
    }

    fun addItems(newItems: List<ReserveProductModel>) {
        val oldItemSize = items.size
        val newItemSize = newItems.size

        items.addAll(newItems)
        addPreselectedProduct(newItems)
        notifyItemRangeChanged(oldItemSize, newItemSize)
    }

    fun clearData() {
        items.clear()
    }

    fun getSelectedProduct(): List<SelectedProductModel> {
        return selectedProduct
    }
}