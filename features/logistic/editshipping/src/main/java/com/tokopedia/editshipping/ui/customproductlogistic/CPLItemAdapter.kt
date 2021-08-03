package com.tokopedia.editshipping.ui.customproductlogistic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.databinding.ItemShippingEditorCardBinding
import com.tokopedia.logisticCommon.data.model.ShipperCPLModel
import com.tokopedia.logisticCommon.data.response.customproductlogistic.Shipper
import com.tokopedia.logisticCommon.data.response.customproductlogistic.ShipperProduct

class CPLItemAdapter : RecyclerView.Adapter<CPLItemViewHolder>() {

    var cplItem = mutableListOf<ShipperCPLModel>()

    var shipperServices = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CPLItemViewHolder {
        val binding = ItemShippingEditorCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CPLItemViewHolder.getViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CPLItemViewHolder, position: Int) {
        holder.bindData(cplItem[position])
    }

    override fun getItemCount(): Int {
        return cplItem.size
    }

    fun addData(data: List<ShipperCPLModel>) {
        cplItem.clear()
        cplItem.addAll(data)
        notifyDataSetChanged()
    }

}