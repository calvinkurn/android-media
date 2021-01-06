package com.tokopedia.pms.howtopay_native.data.model

sealed class HowToPayInstruction(type : PaymentType)
data class MultiOptionInstructions(val type: PaymentType,
                                   val options : ArrayList<ArrayList<String>>) : HowToPayInstruction(type)
data class Instructions(val type: PaymentType,
                                   val instructions : ArrayList<String>) : HowToPayInstruction(type)


sealed class PaymentType
object VirtualAccount : PaymentType()
object Syariah : PaymentType()
object BankTransfer : PaymentType()
object KlickBCA : PaymentType()
object Store : PaymentType()
