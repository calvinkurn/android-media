package com.tokopedia.editshipping.ui.customproductlogistic

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.editshipping.databinding.ItemShippingEditorCardBinding
import com.tokopedia.logisticCommon.data.model.ShipperCPLModel

class CPLItemViewHolder(private val binding: ItemShippingEditorCardBinding): RecyclerView.ViewHolder(binding.root) {

    private val cplShipperItamAdapter = CPLShipperItemAdapter()

    fun bindData(data: ShipperCPLModel) {
        val shipperProductData = data.shipperProduct
        val sb = StringBuilder()

        hideUnusedLayout()

        binding.imgShipmentItem.let {
            ImageHandler.loadImageFitCenter(binding.root.context, it, data.logo)
        }

        for (x in shipperProductData.indices) {
            sb.append(shipperProductData[x].shipperProductName).append(" | ")
        }
        binding.shipmentCategory.text = sb.substring(0, sb.length - 2)

        setAdapterData(data)
        setItemChecked(data)
    }

    private fun hideUnusedLayout() {
        binding.tickerShipper.visibility = View.GONE
        binding.layoutCoupon.root.visibility = View.GONE
        binding.rvShipmentLabel.visibility = View.GONE
        binding.btnInformation.visibility = View.GONE
    }

    private fun setAdapterData(data: ShipperCPLModel) {
        binding.shipmentItemList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cplShipperItamAdapter
        }

        cplShipperItamAdapter.addData(data.shipperProduct)
    }

    private fun setItemChecked(data: ShipperCPLModel) {

    }

    companion object {
        fun getViewHolder(binding: ItemShippingEditorCardBinding) = CPLItemViewHolder(binding)
    }
}