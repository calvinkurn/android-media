package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingTypeFactory

class ShippingDetailUiModel(
    val merchantName: String,
    val destinationName: String,
    val destinationPhone: String,
    val destinationAddress: String
) : BaseOrderTrackingTypeFactory {

    override fun type(typeFactory: OrderTrackingAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getFullDestination(): String {
        return StringBuilder().apply {
            appendHtmlBoldText(destinationName)
            if (destinationPhone.isNotBlank()) {
                appendHtmlBreakLineIfNotBlank()
                appendTextIfNotBlank(destinationPhone)
            }
            if (destinationAddress.isNotBlank()) {
                appendHtmlBreakLineIfNotBlank()
                appendTextIfNotBlank(destinationAddress)
            }
        }.toString()
    }

    private fun StringBuilder.appendHtmlBoldText(text: String) {
        if (text.isNotBlank()) {
            appendTextIfNotBlank(String.format(HTML_BOLD_FORMAT, text))
        }
    }

    private fun StringBuilder.appendTextIfNotBlank(text: String) {
        if (text.isNotBlank()) append(text)
    }

    private fun StringBuilder.appendHtmlBreakLineIfNotBlank() {
        if (isNotBlank()) appendTextIfNotBlank(HTML_BREAK_LINE)
    }

    companion object {
        private const val HTML_BOLD_FORMAT = "<b>%s</b>"
        private const val HTML_BREAK_LINE = "<br />"
    }
}