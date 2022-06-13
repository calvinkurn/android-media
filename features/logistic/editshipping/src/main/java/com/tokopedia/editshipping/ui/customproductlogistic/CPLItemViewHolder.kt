package com.tokopedia.editshipping.ui.customproductlogistic

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.editshipping.databinding.ItemShippingEditorCardBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.model.ShipperCPLModel

class CPLItemViewHolder(private val binding: ItemShippingEditorCardBinding, private val listener: CPLItemAdapter.CPLItemAdapterListener): RecyclerView.ViewHolder(binding.root) {

    private val cplShipperItemAdapter = CPLShipperItemAdapter()

    fun bindData(data: ShipperCPLModel) {
        val shipperProductData = data.shipperProduct

        hideUnusedLayout()

        binding.shipmentName.text = data.shipperName
        binding.imgShipmentItem.let {
            ImageHandler.loadImageFitCenter(binding.root.context, it, data.logo)
        }

        binding.shipmentCategory.text =
            shipperProductData.joinToString(" | ") { it.shipperProductName }

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
            adapter = cplShipperItemAdapter
        }

        cplShipperItemAdapter.addData(data.shipperProduct)
    }

    private fun setItemChecked(data: ShipperCPLModel) {
        data.shipperProduct.forEach {
            if (it.isActive) {
                data.isActive = true
            }
        }

        binding.cbShipmentItem.isChecked = data.isActive

        if (data.isActive) {
            binding.itemChildLayout.visible()
        } else {
            binding.itemChildLayout.gone()
        }

        binding.cbShipmentItem.setOnCheckedChangeListener { _, isChecked ->
            listener.onCheckboxItemClicked()
            cplShipperItemAdapter.updateChecked(isChecked)
            if (isChecked) {
                binding.itemChildLayout.visible()
            } else {
                binding.itemChildLayout.gone()
            }
        }
    }

    companion object {
        fun getViewHolder(binding: ItemShippingEditorCardBinding, listener: CPLItemAdapter.CPLItemAdapterListener) = CPLItemViewHolder(binding, listener)
    }
}