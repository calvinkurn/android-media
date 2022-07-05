package com.tokopedia.chatbot.view.util

sealed class AttachedInvoiceColor {
    object InvoiceLabelRed : AttachedInvoiceColor()
    object InvoiceLabelGreen : AttachedInvoiceColor()
    object InvoiceLabelYellow : AttachedInvoiceColor()

    companion object {
        fun mapTextToInvoiceLabel(color: String): AttachedInvoiceColor {
            return when (color) {
                "green" -> InvoiceLabelGreen
                "red" -> InvoiceLabelRed
                "yellow" -> InvoiceLabelYellow
                else -> InvoiceLabelRed
            }
        }
    }
}
