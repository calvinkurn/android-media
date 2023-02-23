package com.tokopedia.editshipping.ui.customproductlogistic

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.databinding.ItemShippingEditorCardBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.model.ShipperCPLModel
import com.tokopedia.media.loader.loadImage

class CPLItemViewHolder(
    private val binding: ItemShippingEditorCardBinding,
    private val listener: CPLItemAdapter.CPLItemAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    private val cplShipperItemAdapter = CPLShipperItemAdapter()

    fun bindData(data: ShipperCPLModel) {
        hideUnusedLayout()
        bindShipperData(data)
        bindCheckbox(data)
    }

    private fun bindShipperData(data: ShipperCPLModel) {
        binding.shipmentName.text = data.shipperName
        binding.shipmentCategory.text = data.description
        bindLogo(data)
        setAdapterData(data)
        setShipperProductLayout(data)
    }

    private fun bindCheckbox(data: ShipperCPLModel) {
        binding.cbShipmentItem.setOnCheckedChangeListener(null)
        binding.cbShipmentItem.isChecked = data.isActive
        binding.cbShipmentItem.setOnCheckedChangeListener { _, isChecked ->
            if (data.isWhitelabel) {
                listener.onWhitelabelServiceCheckboxClicked(data.shipperProduct.map { it.shipperProductId }, isChecked)
            } else {
                listener.onShipperCheckboxClicked(data.shipperId, isChecked)
            }
        }
    }

    private fun bindLogo(data: ShipperCPLModel) {
        if (data.isWhitelabel || data.logo.isEmpty()) {
            binding.imgShipmentItem.gone()
        } else {
            binding.imgShipmentItem.loadImage(data.logo)
        }

    }

    private fun hideUnusedLayout() {
        binding.tickerShipper.visibility = View.GONE
        binding.layoutCoupon.root.visibility = View.GONE
        binding.rvShipmentLabel.visibility = View.GONE
        binding.btnInformation.visibility = View.GONE
    }

    private fun setAdapterData(data: ShipperCPLModel) {
        if (data.isWhitelabel) {
            binding.shipmentItemList.gone()
            binding.dividerShipment.gone()
        } else {
            binding.shipmentItemList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = cplShipperItemAdapter
            }

            cplShipperItemAdapter.addData(data.shipperProduct)
            cplShipperItemAdapter.setupListener(object :
                CPLShipperItemAdapter.CPLShipperItemAdapterListener {
                override fun onCheckboxProductClicked(shipperProductId: Long, checked: Boolean) {
                    listener.onShipperProductCheckboxClicked(shipperProductId, checked)
                }
            })
        }
    }

    private fun setShipperProductLayout(data: ShipperCPLModel) {
        if (data.isWhitelabel || !data.isActive) {
            binding.itemChildLayout.gone()
        } else {
            binding.itemChildLayout.visible()
        }
    }

    companion object {
        fun getViewHolder(
            binding: ItemShippingEditorCardBinding,
            listener: CPLItemAdapter.CPLItemAdapterListener
        ) = CPLItemViewHolder(binding, listener)
    }
}
