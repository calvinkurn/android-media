package com.tokopedia.vouchercreation.product.list.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.databinding.ItemMvcFilterLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.model.WarehouseLocationSelection
import com.tokopedia.vouchercreation.product.list.view.viewholder.WarehouseLocationItemViewHolder

@SuppressLint("NotifyDataSetChanged")
class LocationListAdapter :
        RecyclerView.Adapter<WarehouseLocationItemViewHolder>(),
        WarehouseLocationItemViewHolder.OnListItemClickListener {

    private var warehouseLocationSelections: MutableList<WarehouseLocationSelection> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarehouseLocationItemViewHolder {
        val binding = ItemMvcFilterLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WarehouseLocationItemViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: WarehouseLocationItemViewHolder, position: Int) {
        holder.bindData(warehouseLocationSelections[position])
    }

    override fun getItemCount(): Int {
        return warehouseLocationSelections.size
    }

    override fun onListItemClicked(position: Int) {
        val selectedWarehouseLocation = warehouseLocationSelections[position]
        warehouseLocationSelections.filter { warehouseLocationSelection ->
            warehouseLocationSelection.warehouseId != selectedWarehouseLocation.warehouseId
        }.forEach { selection -> selection.isSelected = false }
        warehouseLocationSelections[position].isSelected = true
        notifyDataSetChanged()
    }

    fun setWarehouseLocationSelections(warehouseLocationSelections: List<WarehouseLocationSelection>) {
        this.warehouseLocationSelections = warehouseLocationSelections.toMutableList()
        notifyDataSetChanged()
    }

    fun setSelectedWarehouseLocation(wareHouseId: Int?) {
        warehouseLocationSelections.filter { warehouseLocationSelection ->
            warehouseLocationSelection.warehouseId != wareHouseId
        }.forEach { selection -> selection.isSelected = false }
        warehouseLocationSelections.find { it.warehouseId == wareHouseId }?.isSelected = true
        notifyDataSetChanged()
    }

    fun getSelectedWarehouseLocation(): WarehouseLocationSelection? {
        val selectedWarehouseLocation = warehouseLocationSelections.find { warehouseLocation ->
            warehouseLocation.isSelected
        }
        return selectedWarehouseLocation
    }
}