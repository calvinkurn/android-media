package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import kotlinx.android.synthetic.main.item_buyer_order_detail_shipment_info_address.view.*

class ReceiverAddressInfoViewHolder(itemView: View?) : AbstractViewHolder<ShipmentInfoUiModel.ReceiverAddressInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_shipment_info_address
    }

    override fun bind(element: ShipmentInfoUiModel.ReceiverAddressInfoUiModel?) {
        element?.let {
            setupReceiverName(it.receiverName)
            setupReceiverPhoneNumber(it.receiverPhoneNumber)
            setupReceiverAddress(it.receiverAddress)
            setupReceiverAddressNote(it.receiverAddressNote)
        }
    }

    private fun setupReceiverName(receiverName: String) {
        itemView.tvBuyerOrderDetailReceiverNameValue?.text = receiverName
    }

    private fun setupReceiverPhoneNumber(receiverPhoneNumber: String) {
        itemView.tvBuyerOrderDetailReceiverPhoneNumberValue?.text = receiverPhoneNumber
    }

    private fun setupReceiverAddress(receiverAddress: String) {
        itemView.tvBuyerOrderDetailReceiverAddressValue?.text = receiverAddress
    }

    private fun setupReceiverAddressNote(receiverAddressNote: String) {
        itemView.tvBuyerOrderDetailReceiverAddressNoteValue?.text = receiverAddressNote
    }
}