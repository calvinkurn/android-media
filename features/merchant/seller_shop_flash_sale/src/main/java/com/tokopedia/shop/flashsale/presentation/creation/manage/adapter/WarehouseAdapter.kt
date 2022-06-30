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

    private fun itemOnClick(position: Int) {
        items.forEachIndexed { index, warehouseUiModel ->
            warehouseUiModel.isSelected = (index == position)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: WarehouseViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(warehouses: List<WarehouseUiModel>) {
        items = warehouses
    }

    fun getItems(): List<WarehouseUiModel> {
        return items
    }
}