package com.tokopedia.editshipping.ui.customproductlogistic

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.databinding.ItemShippingEditorCardBinding
import com.tokopedia.logisticCommon.data.model.ShipperCPLModel

class CPLItemAdapter(private val listener: CPLItemAdapterListener) :
    RecyclerView.Adapter<CPLItemViewHolder>() {

    private val cplItem = mutableListOf<ShipperCPLModel>()

    interface CPLItemAdapterListener {
        fun onShipperCheckboxClicked(shipperId: Long, check: Boolean)
        fun onShipperProductCheckboxClicked(spId: Long, check: Boolean)
        fun onWhitelabelServiceCheckboxClicked(spIds: List<Long>, check: Boolean)
    }

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

    fun getSamedayServicePosition(): Int {
        for (i in cplItem.indices) {
            if (cplItem[i].isWhitelabel && cplItem[i].shipperName == "Sameday 8 Jam") {
                return i
            }
        }
        return RecyclerView.NO_POSITION
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(data: List<ShipperCPLModel>) {
        cplItem.clear()
        cplItem.addAll(data)
        notifyDataSetChanged()
    }

    fun modifyData(shipper: ShipperCPLModel) {
        val index =
            cplItem.indexOfFirst { shipperCPLModel -> shipperCPLModel.shipperId == shipper.shipperId }
        if (index != -1 && cplItem.elementAtOrNull(index) != null) {
            cplItem[index] = shipper
            notifyItemChanged(index)
        }
    }
}
