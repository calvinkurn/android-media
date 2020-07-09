package com.tokopedia.thankyou_native.data.mapper

sealed class PaymentType
object BankTransfer : PaymentType()
object VirtualAccount : PaymentType()
object Retail : PaymentType()
object CashOnDelivery : PaymentType()
object SmsPayment : PaymentType()
object Tokopedia : PaymentType()
object InstantPayment : PaymentType()
object HomeCredit : PaymentType()
object Kredivo : PaymentType()

object PaymentTypeMapper {

    fun getPaymentTypeByStr(paymentTypeStr: String): PaymentType? {
        return when (paymentTypeStr) {
            "BANKTRANSFER" -> BankTransfer
            "VA" -> VirtualAccount
            "RETAIL" -> Retail
            "COD" -> CashOnDelivery
            "SMSPAYMENT" -> SmsPayment
            "TOKOPEDIA" -> Tokopedia
            "INSTANT" -> InstantPayment
            "HCI" -> HomeCredit
            "KREDIVO" -> Kredivo
            else -> null
        }
    }

}