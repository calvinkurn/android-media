package com.tokopedia.pms.howtopay_native.data.model

sealed class HowToPayInstruction
data class SingleChannelGatewayResult(val type: PaymentType,
                                      val instructions : ArrayList<String>) : HowToPayInstruction()
data class MultiChannelGatewayResult(val type: PaymentType,
                                     val gateway: MultiChannelGateway) : HowToPayInstruction()


sealed class PaymentType
object VirtualAccount : PaymentType()
object Syariah : PaymentType()
object BankTransfer : PaymentType()
object KlickBCA : PaymentType()
object Store : PaymentType()
