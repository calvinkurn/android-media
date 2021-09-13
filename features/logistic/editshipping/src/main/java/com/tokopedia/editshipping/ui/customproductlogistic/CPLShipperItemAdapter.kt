package com.tokopedia.editshipping.ui.customproductlogistic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.databinding.ItemShipperProductNameBinding
import com.tokopedia.editshipping.databinding.ItemShippingEditorCardBinding
import com.tokopedia.logisticCommon.data.model.ShipperProductCPLModel
import com.tokopedia.logisticCommon.data.response.customproductlogistic.ShipperProduct

class CPLShipperItemAdapter : RecyclerView.Adapter<CPLShipperItemAdapter.CPLShipperItemViewHolder>() {

    val cplShipperItem = mutableListOf<ShipperProductCPLModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CPLShipperItemViewHolder {
        val binding = ItemShipperProductNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CPLShipperItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CPLShipperItemViewHolder, position: Int) {
        holder.bindData(cplShipperItem[position])
    }

    override fun getItemCount(): Int {
        return cplShipperItem.size
    }

    fun addData(data: List<ShipperProductCPLModel>) {
        cplShipperItem.clear()
        cplShipperItem.addAll(data)
        notifyDataSetChanged()
    }

    fun updateChecked(checked: Boolean) {
        cplShipperItem.forEach {
            it.isActive = checked
        }
        notifyDataSetChanged()
    }

    inner class CPLShipperItemViewHolder(private val binding: ItemShipperProductNameBinding): RecyclerView.ViewHolder(binding.root) {

        fun bindData(data: ShipperProductCPLModel) {
            val lastItem = cplShipperItem.last()
            binding.shipperProductName.text = data.shipperProductName
            binding.shipperProductCb.isChecked = data.isActive

            if (data == lastItem) {
                binding.dividerShipment.visibility = View.GONE
            }

            binding.shipperProductCb.setOnCheckedChangeListener { _, isChecked ->
                data.isActive = isChecked
            }
        }
    }

}