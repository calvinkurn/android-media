package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.databinding.ItemShipperDetailChildBinding
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperProductDetailsModel

class ShipperDetailsChildAdapter :
    RecyclerView.Adapter<ShipperDetailsChildAdapter.ShipperDetailsChildViewHolder>() {

    private var shipperDetailsChild = mutableListOf<ShipperProductDetailsModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShipperDetailsChildViewHolder {
        return ShipperDetailsChildViewHolder(
            ItemShipperDetailChildBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return shipperDetailsChild.size
    }

    override fun onBindViewHolder(holder: ShipperDetailsChildViewHolder, position: Int) {
        holder.bindData(shipperDetailsChild[position])
    }

    fun addData(data: List<ShipperProductDetailsModel>) {
        shipperDetailsChild.addAll(data)
        notifyDataSetChanged()
    }

    inner class ShipperDetailsChildViewHolder(private val binding: ItemShipperDetailChildBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: ShipperProductDetailsModel) {
            binding.tvDetailShipperFeatureName.text = data.name
            binding.tvShipperFeatureDesc.text = data.description
        }
    }
}
