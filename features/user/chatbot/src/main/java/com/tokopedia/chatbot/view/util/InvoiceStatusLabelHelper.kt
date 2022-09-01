package com.tokopedia.chatbot.view.util

import com.tokopedia.chat_common.data.OrderStatusCode
import com.tokopedia.unifycomponents.Label

object InvoiceStatusLabelHelper {
    fun getLabelType(statusColor: String?): Int {
        if(statusColor == null)
            return Label.HIGHLIGHT_LIGHT_ORANGE
        return when(AttachedInvoiceColor.mapTextToInvoiceLabel(statusColor)) {
            AttachedInvoiceColor.InvoiceLabelGreen -> Label.HIGHLIGHT_LIGHT_GREEN
            AttachedInvoiceColor.InvoiceLabelRed -> Label.HIGHLIGHT_LIGHT_RED
            AttachedInvoiceColor.InvoiceLabelYellow -> Label.HIGHLIGHT_LIGHT_ORANGE
        }
    }

    fun getLabelTypeWithStatusId(statusId: Int?): Int {
        if (statusId == null)
            return Label.HIGHLIGHT_DARK_GREY
        return when (OrderStatusCode.MAP[statusId]) {
            OrderStatusCode.COLOR_RED -> Label.HIGHLIGHT_LIGHT_RED
            OrderStatusCode.COLOR_GREEN -> Label.HIGHLIGHT_LIGHT_GREEN
            else -> Label.HIGHLIGHT_DARK_GREY
        }
    }

}