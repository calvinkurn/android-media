package com.tokopedia.shop.flashsale.presentation.creation.manage.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.ReserveProductModel

class ReserveProductAdapter(
    private val onSelectedItemChanges: (selectedProductId: MutableList<String>) -> Unit
) : RecyclerView.Adapter<ReserveProductViewHolder>() {

    private var items: MutableList<ReserveProductModel> = mutableListOf()
    private var selectedProductId: MutableList<String> = mutableListOf()

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
            val isExist = selectedProductId.any { it == selectedItem.productName }
            if (!isExist) selectedProductId.add(selectedItem.productName)
        } else {
            selectedProductId.remove(selectedItem.productName)
        }
        onSelectedItemChanges(selectedProductId)
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