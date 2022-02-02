package com.tokopedia.minicart.common.simplified

data class MiniCartSimplifiedState(
        var state: Int = 0,
        var data: Any? = null,
        var throwable: Throwable? = null
) {
    companion object {
        const val STATE_MOVE_TO_CART = 11
        const val STATE_FAILED_VALIDATE_USE = 22
        const val STATE_FAILED_MINICART = 33
    }
}