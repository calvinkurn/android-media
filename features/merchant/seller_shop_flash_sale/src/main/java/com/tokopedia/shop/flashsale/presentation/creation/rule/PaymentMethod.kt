package com.tokopedia.shop.flashsale.presentation.creation.rule

sealed class PaymentMethod(
    val type: Int,
) {
    companion object {
        const val NONE = -1
        const val INSTANT = 0
        const val REGULAR = 1

        const val PAYMENT_PROFILE_INSTANT = "TKP_FLASH"
        const val PAYMENT_PROFILE_REGULAR = "TKP_FLASH_REG"
        const val PAYMENT_PROFILE_OTHER = ""

        @JvmStatic
        fun mapFromInt(type: Int): PaymentMethod {
            return when (type) {
                INSTANT -> Instant
                REGULAR -> Regular
                else -> None
            }
        }
    }

    val paymentProfile: String
        get() = getPaymentProfileString()

    private fun getPaymentProfileString(): String {
        return when (this) {
            Instant -> PAYMENT_PROFILE_INSTANT
            Regular -> PAYMENT_PROFILE_REGULAR
            else -> PAYMENT_PROFILE_OTHER
        }
    }

    object None : PaymentMethod(NONE)
    object Instant : PaymentMethod(INSTANT)
    object Regular : PaymentMethod(REGULAR)
}
