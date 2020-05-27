package com.tokopedia.thankyou_native.data.mapper

sealed class PaymentStatus
object PaymentActive : PaymentStatus()
object PaymentWaiting : PaymentStatus()
object PaymentVerified : PaymentStatus()
object PaymentCancelled : PaymentStatus()
object PaymentVoid : PaymentStatus()
object PaymentPreAuth : PaymentStatus()
object PaymentExpired : PaymentStatus()
object PaymentWaitingCOD : PaymentStatus()
object Invalid : PaymentStatus()

object PaymentStatusMapper {

    fun getPaymentStatusByInt(paymentStatusInt: Int): PaymentStatus {
        return when (paymentStatusInt) {
            0 -> PaymentExpired
            1 -> PaymentActive
            2 -> PaymentWaiting
            3 -> PaymentVerified
            4 -> PaymentCancelled
            5 -> PaymentVoid
            6 -> PaymentPreAuth
            7 -> PaymentWaitingCOD
            else -> Invalid
        }
    }
}
