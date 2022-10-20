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
        binding.shipmentName.text = data.shipperName
        binding.shipmentCategory.text = data.description
        if (data.isWhitelabel) {
            bindWhitelabelShipment(data)
        } else {
            bindNormalShipment(data)
        }
    }

    private fun bindNormalShipment(data: ShipperCPLModel) {
        binding.imgShipmentItem.loadImage(data.logo)
        setAdapterData(data)
        setItemChecked(data)
    }

    private fun bindWhitelabelShipment(data: ShipperCPLModel) {
        binding.shipmentItemList.gone()
        binding.imgShipmentItem.gone()
        binding.cbShipmentItem.setOnCheckedChangeListener { _, isChecked ->
            listener.onWhitelabelServiceCheckboxClicked(data.shipperProduct.map { it.shipperProductId }, isChecked)
        }
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
        cplShipperItemAdapter.setupListener(object :
            CPLShipperItemAdapter.CPLShipperItemAdapterListener {
            override fun onCheckboxProductClicked(shipperProductId: Long, checked: Boolean) {
                listener.onShipperProductCheckboxClicked(shipperProductId, checked)
            }
        })
    }

    private fun setItemChecked(data: ShipperCPLModel) {
        binding.cbShipmentItem.isChecked = data.isActive

        if (data.isActive) {
            binding.itemChildLayout.visible()
        } else {
            binding.itemChildLayout.gone()
        }

        binding.cbShipmentItem.setOnCheckedChangeListener { _, isChecked ->
            listener.onShipperCheckboxClicked(data.shipperId, isChecked)
//            cplShipperItemAdapter.updateChecked(isChecked)
//            if (isChecked) {
//                binding.itemChildLayout.visible()
//            } else {
//                binding.itemChildLayout.gone()
//            }
        }
    }

    companion object {
        fun getViewHolder(
            binding: ItemShippingEditorCardBinding,
            listener: CPLItemAdapter.CPLItemAdapterListener
        ) = CPLItemViewHolder(binding, listener)
    }
}
