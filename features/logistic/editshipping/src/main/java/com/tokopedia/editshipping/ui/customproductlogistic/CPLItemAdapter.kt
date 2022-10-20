package com.tokopedia.editshipping.ui.customproductlogistic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.databinding.ItemShippingEditorCardBinding
import com.tokopedia.logisticCommon.data.model.CPLProductModel
import com.tokopedia.logisticCommon.data.model.ShipperCPLModel

class CPLItemAdapter(private val listener: CPLItemAdapterListener) :
    RecyclerView.Adapter<CPLItemViewHolder>() {

    var cplItem = mutableListOf<ShipperCPLModel>()

    interface CPLItemAdapterListener {
        fun onShipperCheckboxClicked(shipperId: Long, check: Boolean)
        fun onShipperProductCheckboxClicked(spId: Long, check: Boolean)
        fun onWhitelabelServiceCheckboxClicked(spIds: List<Long>, check: Boolean)
    }

    var shipperServices = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CPLItemViewHolder {
        val binding = ItemShippingEditorCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CPLItemViewHolder.getViewHolder(binding, listener)
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
