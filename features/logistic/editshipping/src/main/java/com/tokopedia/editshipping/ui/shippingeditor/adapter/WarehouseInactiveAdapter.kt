package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.WarehousesModel
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.unifyprinciples.Typography

class WarehouseInactiveAdapter: RecyclerView.Adapter<WarehouseInactiveAdapter.WarehouseInactiveViewHolder>() {

    private val warehouseData = mutableListOf<WarehousesModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarehouseInactiveViewHolder {
        return WarehouseInactiveViewHolder(parent.inflateLayout(R.layout.item_warehouse_inactive))
    }

    override fun getItemCount(): Int {
        return warehouseData.size
    }

    override fun onBindViewHolder(holder: WarehouseInactiveViewHolder, position: Int) {
        holder.bindData(warehouseData[position])
    }

    fun setData(data: List<WarehousesModel>) {
        warehouseData.clear()
        warehouseData.addAll(data)
        notifyDataSetChanged()
    }

   inner class WarehouseInactiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
       private val imgLocationWarehouse = itemView.findViewById<ImageView>(R.id.img_location)
       private val tvWarehouseName = itemView.findViewById<Typography>(R.id.tv_warehouse_name)
       private val tvWarehouseDistrict = itemView.findViewById<Typography>(R.id.tv_warehouse_district)
       private val tvPostalCode = itemView.findViewById<Typography>(R.id.tv_warehouse_postal_code)

       fun bindData(data: WarehousesModel) {
           setItemData(data)
       }

       private fun setItemData(data: WarehousesModel) {
           val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_location_warehouse)
           imgLocationWarehouse.setImageDrawable(icon)
           tvWarehouseName.text = data.warehouseName
           tvWarehouseDistrict.text = data.districtName
           tvPostalCode.text = data.postalCode
        }
    }
}