package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
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
        }
    }

    override fun bind(element: ShipmentInfoUiModel.ReceiverAddressInfoUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val oldItem = it.first
                val newItem = it.second
                if (oldItem is ShipmentInfoUiModel.ReceiverAddressInfoUiModel && newItem is ShipmentInfoUiModel.ReceiverAddressInfoUiModel) {
                    itemView.container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    if (oldItem.receiverName != newItem.receiverName) {
                        setupReceiverName(newItem.receiverName)
                    }
                    if (oldItem.receiverPhoneNumber != newItem.receiverPhoneNumber) {
                        setupReceiverPhoneNumber(newItem.receiverPhoneNumber)
                    }
                    if (oldItem.receiverAddress != newItem.receiverAddress) {
                        setupReceiverAddress(newItem.receiverAddress)
                    }
                    itemView.container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
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

    private fun composeReceiverAddressNote(receiverAddressNote: String): SpannableString {
        return SpannableString(receiverAddressNote).apply {
            setSpan(StyleSpan(android.graphics.Typeface.ITALIC), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}