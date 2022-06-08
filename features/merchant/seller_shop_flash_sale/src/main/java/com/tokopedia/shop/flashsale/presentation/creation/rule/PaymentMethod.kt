package com.tokopedia.shop.flashsale.presentation.creation.rule

sealed class PaymentMethod(
    val type: Int,
) {
    companion object {
        const val NONE = -1
        const val INSTANT = 0
        const val REGULAR = 1

        @JvmStatic
        fun mapFromInt(type: Int): PaymentMethod {
            return when (type) {
                INSTANT -> Instant
                REGULAR -> Regular
                else -> None
            }
        }
    }

    object None : PaymentMethod(NONE)
    object Instant : PaymentMethod(INSTANT)
    object Regular : PaymentMethod(REGULAR)
}
