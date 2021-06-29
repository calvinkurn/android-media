package com.tokopedia.pms.howtopay_native.data.model

data class HowToPayInstruction(val htpData: HowToPayData)

sealed class PaymentType
object VirtualAccount : PaymentType()
object Syariah : PaymentType()
object BankTransfer : PaymentType()
object KlickBCA : PaymentType()
object Store : PaymentType()
