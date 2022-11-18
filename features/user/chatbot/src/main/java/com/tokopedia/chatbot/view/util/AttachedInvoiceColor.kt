package com.tokopedia.chatbot.view.util

sealed class AttachedInvoiceColor {
    object InvoiceLabelRed : AttachedInvoiceColor()
    object InvoiceLabelGreen : AttachedInvoiceColor()
    object InvoiceLabelYellow : AttachedInvoiceColor()

    companion object {
        fun mapTextToInvoiceLabel(color: String): AttachedInvoiceColor {
            return when (color) {
                COLOR_GREEN -> InvoiceLabelGreen
                COLOR_RED -> InvoiceLabelRed
                COLOR_YELLOw -> InvoiceLabelYellow
                else -> InvoiceLabelRed
            }
        }
        const val COLOR_GREEN = "green"
        const val COLOR_RED = "red"
        const val COLOR_YELLOw = "yellow"
    }
}
