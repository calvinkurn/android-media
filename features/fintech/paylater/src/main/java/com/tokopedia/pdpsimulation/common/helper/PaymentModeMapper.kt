package com.tokopedia.pdpsimulation.common.helper

sealed class PaymentMode
object PayLater: PaymentMode()
object CreditCard: PaymentMode()
