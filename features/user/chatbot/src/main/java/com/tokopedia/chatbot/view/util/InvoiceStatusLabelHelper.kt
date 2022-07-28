package com.tokopedia.chatbot.view.util

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

}