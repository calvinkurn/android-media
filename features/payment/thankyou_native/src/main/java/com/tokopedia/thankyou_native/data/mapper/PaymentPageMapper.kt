package com.tokopedia.thankyou_native.data.mapper

sealed class PageType
object InstantPaymentPage : PageType()
object WaitingPaymentPage : PageType()
object ProcessingPaymentPage : PageType()

object PaymentPageMapper {

    fun getPaymentPageType(pageTypeStr: String): PageType? {
        return when (pageTypeStr) {
            "Waiting" -> WaitingPaymentPage
            "Success" -> InstantPaymentPage
            "Processing" -> ProcessingPaymentPage
            else -> null
        }
    }
}