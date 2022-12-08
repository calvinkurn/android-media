package com.tokopedia.mvc.presentation.creation.step2.helper

object ErrorHelper {
    fun getVoucherInputErrorStatus(input: String): Boolean {
        return if (input.count() in 1..4) {
            true
        } else {
            input.isEmpty()
        }
    }
}
