package com.tokopedia.pdpsimulation.common

sealed class PaymentMode
object PayLater: PaymentMode()
object CreditCard: PaymentMode()
