package com.tokopedia.editshipping.ui.customproductlogistic

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.databinding.ItemShipperProductNameBinding
import com.tokopedia.logisticCommon.data.response.customproductlogistic.ShipperProduct

class CPLShipperItemViewHolder(private val binding: ItemShipperProductNameBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(data: ShipperProduct) {
        binding.shipperProductName.text = data.shipperProductName

    }
}
