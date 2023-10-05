package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.databinding.ItemWarehouseInactiveBinding
import com.tokopedia.editshipping.domain.model.shippingEditor.WarehousesModel

class WarehouseInactiveAdapter :
    RecyclerView.Adapter<WarehouseInactiveAdapter.WarehouseInactiveViewHolder>() {

    private val warehouseData = mutableListOf<WarehousesModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarehouseInactiveViewHolder {
        return WarehouseInactiveViewHolder(
            ItemWarehouseInactiveBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return warehouseData.size
    }

    override fun onBindViewHolder(holder: WarehouseInactiveViewHolder, position: Int) {
        holder.bindData(warehouseData[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<WarehousesModel>) {
        warehouseData.clear()
        warehouseData.addAll(data)
        notifyDataSetChanged()
    }

    inner class WarehouseInactiveViewHolder(private val binding: ItemWarehouseInactiveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: WarehousesModel) {
            setItemData(data)
        }

        private fun setItemData(data: WarehousesModel) {
            binding.tvWarehouseName.text = data.warehouseName
            binding.tvWarehouseDistrict.text = data.districtName
            binding.tvWarehousePostalCode.text = data.postalCode
        }
    }
}
