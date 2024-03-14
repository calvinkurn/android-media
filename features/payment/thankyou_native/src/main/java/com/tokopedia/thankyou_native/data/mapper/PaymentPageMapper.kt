package com.tokopedia.thankyou_native.data.mapper
import com.tokopedia.thankyou_native.data.mapper.PaymentStatusMapper
import com.tokopedia.thankyou_native.data.mapper.PaymentWaitingCOD

sealed class PageType
object InstantPaymentPage : PageType()
object WaitingPaymentPage : PageType()
object ProcessingPaymentPage : PageType()

object PaymentPageMapper {
    fun getPaymentPageType(pageTypeStr: String, paymentStatusInt: Int = Int.ZERO): PageType? {
        return when (pageTypeStr) {
            "Waiting" -> {
                if (PaymentStatusMapper.getPaymentStatusByInt(paymentStatusInt) == PaymentWaitingCOD)
                    InstantPaymentPage
                else
                    WaitingPaymentPage
            }
            "Success" -> InstantPaymentPage
            "Processing" -> ProcessingPaymentPage
            else -> null
        }
    }
}
