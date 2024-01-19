package com.tokopedia.discovery2.viewcontrollers.customview.automatecoupon

sealed class ButtonState(
    open val text: String,
    open val action: () -> Unit
) {
    data class Claim(
        override val action: () -> Unit
    ) : ButtonState(KLAIM, action)

    data class Redirection(
        override val action: () -> Unit
    ) : ButtonState(EXPLORE, action)

    object OutOfStock : ButtonState(OUT_OF_STOCK, { })

    data class Custom(
        override val text: String,
        override val action: () -> Unit
    ) : ButtonState(text, action)

    companion object {
        private const val KLAIM = "Klaim"
        private const val EXPLORE = "Mulai Belanja"
        private const val OUT_OF_STOCK = "Habis"
    }
}
