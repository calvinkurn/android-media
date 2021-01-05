package com.tokopedia.paylater.data.mapper

sealed class PaymentMode
object PayLater: PaymentMode()
object CreditCard: PaymentMode()

object PaymentModeMapper {

}