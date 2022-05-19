package com.tokopedia.buyerorderdetail.common.utils

import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import javax.inject.Inject

class ResourceProvider @Inject constructor() {

    companion object {
        private const val HTML_BREAK_LINE = "<br />"
        private const val HTML_BOLD_FORMAT = "<b>%s</b>"
        private const val HTML_LINK_FORMAT = "<a href=\"%s\">%s</a>"
    }

    private fun composeReceiverAddress(vararg chunks: String): String {
        return StringBuilder().apply {
            chunks.forEach {
                appendSpaceIfNotBlank()
                appendTextIfNotBlank(it)
            }
        }.toString()
    }

    private fun StringBuilder.appendTextIfNotBlank(text: String) {
        if (text.isNotBlank()) append(text)
    }

    private fun StringBuilder.appendSpaceIfNotBlank() {
        if (isNotBlank()) append(" ")
    }

    private fun StringBuilder.appendHtmlBreakLineIfNotBlank() {
        if (isNotBlank()) appendTextIfNotBlank(HTML_BREAK_LINE)
    }

    private fun StringBuilder.appendHtmlBoldText(text: String) {
        if (text.isNotBlank()) {
            appendTextIfNotBlank(String.format(HTML_BOLD_FORMAT, text))
        }
    }

    private fun StringBuilder.appendHtmlLinkText(text: String, link: String) {
        appendTextIfNotBlank(String.format(HTML_LINK_FORMAT, link, text))
    }

    fun getShipmentInfoSectionHeader(): Int {
        return R.string.header_section_shipment_info
    }

    fun getPaymentInfoSectionHeader(): Int {
        return R.string.header_section_payment_into
    }

    fun getErrorMessageNoProduct(): Int {
        return R.string.buyer_order_detail_error_message_no_product
    }

    fun getCopyMessageReceiverAddress(): Int {
        return R.string.message_receiver_address_copied
    }

    fun getCopyMessageAwb(): Int {
        return R.string.message_awb_copied
    }

    fun getCopyMessageDropshipper(): Int {
        return R.string.message_dropshipper_copied
    }

    fun getReceiverAddressLabel(): Int {
        return R.string.label_address
    }

    fun getAwbLabel(): Int {
        return R.string.label_awb_number
    }

    fun getDropshipperLabel(): Int {
        return R.string.label_dropshipper
    }

    fun getCopyLabelReceiverAddress(): Int {
        return R.string.copy_label_receiver_address
    }

    fun getCopyLabelAwb(): Int {
        return R.string.copy_label_awb_number
    }

    fun getCopyLabelDropshipper(): Int {
        return R.string.copy_label_dropship
    }

    fun composeDriverTippingInfoDescription(
        driverTippingInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.LogisticSectionInfo?
    ): String {
        return if (driverTippingInfo == null) {
            ""
        } else {
            StringBuilder().apply {
                appendTextIfNotBlank(driverTippingInfo.subtitle)
                if (driverTippingInfo.action.name.isNotBlank()) {
                    appendSpaceIfNotBlank()
                    if (driverTippingInfo.action.link.isNotBlank()) {
                        appendHtmlLinkText(
                            driverTippingInfo.action.name,
                            driverTippingInfo.action.link
                        )
                    } else {
                        append(driverTippingInfo.action.name)
                    }
                }
            }.toString()
        }
    }

    fun composeDropshipperValue(
        dropshipper: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Dropship
    ): String {
        return StringBuilder().apply {
            appendHtmlBoldText(dropshipper.name)
            if (dropshipper.phoneNumber.isNotBlank()) {
                appendHtmlBreakLineIfNotBlank()
                append(dropshipper.phoneNumber)
            }
        }.toString()
    }

    fun composeReceiverAddressValue(
        receiver: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment.Receiver
    ): String {
        val receiverAddress = composeReceiverAddress(
            receiver.street,
            receiver.district,
            receiver.city,
            receiver.province,
            receiver.postal
        )
        return StringBuilder().apply {
            appendHtmlBoldText(receiver.name)
            if (receiver.phone.isNotBlank()) {
                appendHtmlBreakLineIfNotBlank()
                append(receiver.phone)
            }
            if (receiverAddress.isNotBlank()) {
                appendHtmlBreakLineIfNotBlank()
                append(receiverAddress)
            }
        }.toString()
    }
}