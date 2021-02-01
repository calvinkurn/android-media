package com.tokopedia.paylater.common

sealed class PaymentMode
object PayLater: PaymentMode()
object CreditCard: PaymentMode()

object PaymentModeMapper {

}