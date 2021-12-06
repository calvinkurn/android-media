package com.tokopedia.buyerorderdetail.common.utils

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext private val context: Context?) {
    private fun getString(resId: Int, vararg args: Any): String? {
        return try {
            context?.getString(resId, *args)
        } catch (e: Resources.NotFoundException) {
            null
        }
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
        if (isNotBlank()) appendTextIfNotBlank(getString(R.string.break_line_html_format).orEmpty())
    }

    private fun StringBuilder.appendHtmlBoldText(text: String) {
        if (text.isNotBlank()) {
            appendTextIfNotBlank(getString(R.string.bold_text_html_format, text).orEmpty())
        }
    }

    private fun StringBuilder.appendHtmlLinkText(text: String, link: String) {
        appendTextIfNotBlank(getString(R.string.link_text_html_format, link, text).orEmpty())
    }

    fun getProductListSectionHeader(): String {
        return getString(R.string.header_section_product_list).orEmpty()
    }

    fun getShipmentInfoSectionHeader(): String {
        return getString(R.string.header_section_shipment_info).orEmpty()
    }

    fun getPaymentInfoSectionHeader(): String {
        return getString(R.string.header_section_payment_into).orEmpty()
    }

    fun getErrorMessageNoProduct(): String {
        return getString(R.string.buyer_order_detail_error_message_no_product).orEmpty()
    }

    fun getCopyMessageReceiverAddress(): String {
        return getString(R.string.message_receiver_address_copied).orEmpty()
    }

    fun getCopyMessageAwb(): String {
        return getString(R.string.message_awb_copied).orEmpty()
    }

    fun getReceiverAddressLabel(): String {
        return getString(R.string.label_address).orEmpty()
    }

    fun getAwbLabel(): String {
        return getString(R.string.label_awb_number).orEmpty()
    }

    fun getDropshipLabel(): String {
        return getString(R.string.label_dropshipper).orEmpty()
    }

    fun getCopyLabelReceiverAddress(): String {
        return getString(R.string.copy_label_receiver_address).orEmpty()
    }

    fun getCopyLabelAwb(): String {
        return getString(R.string.copy_label_awb_number).orEmpty()
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
                        appendHtmlLinkText(driverTippingInfo.action.name, driverTippingInfo.action.link)
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
        val receiverAddress = composeReceiverAddress(receiver.street, receiver.district, receiver.city, receiver.province, receiver.postal)
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