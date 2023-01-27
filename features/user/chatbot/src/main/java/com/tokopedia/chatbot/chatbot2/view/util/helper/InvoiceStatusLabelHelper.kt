package com.tokopedia.chatbot.chatbot2.view.util.helper

import com.tokopedia.unifycomponents.Label

object InvoiceStatusLabelHelper {
    fun getLabelType(statusColor: String?): Int {
        if (statusColor == null) {
            return Label.HIGHLIGHT_LIGHT_ORANGE
        }
        return when (AttachedInvoiceColor.mapTextToInvoiceLabel(statusColor)) {
            AttachedInvoiceColor.InvoiceLabelGreen -> Label.HIGHLIGHT_LIGHT_GREEN
            AttachedInvoiceColor.InvoiceLabelRed -> Label.HIGHLIGHT_LIGHT_RED
            AttachedInvoiceColor.InvoiceLabelYellow -> Label.HIGHLIGHT_LIGHT_ORANGE
        }
    }

    fun getLabelTypeWithStatusId(statusId: Long?): Int {
        if (statusId == null) {
            return Label.HIGHLIGHT_LIGHT_ORANGE
        }
        return when (ChatbotOrderStatusCode.MAP[statusId]) {
            ChatbotOrderStatusCode.COLOR_RED -> Label.HIGHLIGHT_LIGHT_RED
            ChatbotOrderStatusCode.COLOR_GREEN -> Label.HIGHLIGHT_LIGHT_GREEN
            else -> Label.HIGHLIGHT_LIGHT_ORANGE
        }
    }
}
