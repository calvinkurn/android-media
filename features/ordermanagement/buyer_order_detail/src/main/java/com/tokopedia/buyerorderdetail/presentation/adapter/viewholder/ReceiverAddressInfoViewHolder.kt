package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifyprinciples.Typography

class ReceiverAddressInfoViewHolder(itemView: View?) : BaseToasterViewHolder<ShipmentInfoUiModel.ReceiverAddressInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_shipment_info_address

        private const val LABEL_RECEIVER_ADDRESS = "receiverAddress"
    }

    private val icBuyerOrderDetailCopyReceiverAddress = itemView?.findViewById<IconUnify>(R.id.icBuyerOrderDetailCopyReceiverAddress)
    private val tvBuyerOrderDetailReceiverNameValue = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailReceiverNameValue)
    private val tvBuyerOrderDetailReceiverPhoneNumberValue = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailReceiverPhoneNumberValue)
    private val tvBuyerOrderDetailReceiverAddressValue = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailReceiverAddressValue)
    private val container = itemView?.findViewById<ConstraintLayout>(R.id.container)

    init {
        setupListeners()
    }

    private var element: ShipmentInfoUiModel.ReceiverAddressInfoUiModel? = null

    private fun setupListeners() {
        icBuyerOrderDetailCopyReceiverAddress?.setOnClickListener {
            copyReceiverAddress()
        }
    }

    private fun copyReceiverAddress() {
        element?.let {
            Utils.copyText(itemView.context, LABEL_RECEIVER_ADDRESS, copyAllReceiverData())
            showToaster(itemView.context.getString(R.string.message_receiver_address_copied))
        }
    }

    private fun copyAllReceiverData(): String {
        val receiverData = StringBuilder()
        val element = element
        if (element != null) {
            if (element.receiverName.isNotBlank()) {
                receiverData.append(element.receiverName)
            }
            if (element.receiverPhoneNumber.isNotBlank()) {
                if (receiverData.isNotBlank()) receiverData.appendLine()
                receiverData.append(element.receiverPhoneNumber)
            }
            if (element.receiverAddress.isNotBlank()) {
                if (receiverData.isNotBlank()) receiverData.appendLine()
                receiverData.append(element.receiverAddress)
            }
        }
        return receiverData.toString()
    }

    override fun bind(element: ShipmentInfoUiModel.ReceiverAddressInfoUiModel?) {
        element?.let {
            this.element = it
            setupReceiverName(it.receiverName)
            setupReceiverPhoneNumber(it.receiverPhoneNumber)
            setupReceiverAddress(it.receiverAddress)
        }
    }

    override fun bind(element: ShipmentInfoUiModel.ReceiverAddressInfoUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is ShipmentInfoUiModel.ReceiverAddressInfoUiModel && newItem is ShipmentInfoUiModel.ReceiverAddressInfoUiModel) {
                    this.element = newItem
                    container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    if (oldItem.receiverName != newItem.receiverName) {
                        setupReceiverName(newItem.receiverName)
                    }
                    if (oldItem.receiverPhoneNumber != newItem.receiverPhoneNumber) {
                        setupReceiverPhoneNumber(newItem.receiverPhoneNumber)
                    }
                    if (oldItem.receiverAddress != newItem.receiverAddress) {
                        setupReceiverAddress(newItem.receiverAddress)
                    }
                    container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupReceiverName(receiverName: String) {
        tvBuyerOrderDetailReceiverNameValue?.text = receiverName
    }

    private fun setupReceiverPhoneNumber(receiverPhoneNumber: String) {
        tvBuyerOrderDetailReceiverPhoneNumberValue?.text = receiverPhoneNumber
    }

    private fun setupReceiverAddress(receiverAddress: String) {
        tvBuyerOrderDetailReceiverAddressValue?.text = receiverAddress
    }
}