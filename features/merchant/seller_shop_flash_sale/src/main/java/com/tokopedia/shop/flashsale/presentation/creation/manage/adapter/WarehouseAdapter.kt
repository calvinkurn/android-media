package com.tokopedia.shop.flashsale.presentation.creation.manage.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.WarehouseUiModel

class WarehouseAdapter: RecyclerView.Adapter<WarehouseViewHolder>() {

    private var items: List<WarehouseUiModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarehouseViewHolder {
        val rootView = WarehouseViewHolder.createRootView(parent)
        return WarehouseViewHolder(rootView, ::itemOnClick)
    }

    override fun onBindViewHolder(holder: WarehouseViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    private fun itemOnClick(position: Int) {
        items.forEachIndexed { index, warehouseUiModel ->
            if (warehouseUiModel.isSelected) setItemSelected(index, false)
            if (index == position) setItemSelected(index, true)
        }
    }

    private fun setItemSelected(index: Int, isSelected: Boolean) {
        items.getOrNull(index)?.isSelected = isSelected
        notifyItemChanged(index)
    }

    fun setItems(warehouses: List<WarehouseUiModel>) {
        items = warehouses
    }

    fun getItems(): List<WarehouseUiModel> {
        return items
    }
}