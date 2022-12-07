package com.tokopedia.mvc.presentation.creation.step2.helper

object ErrorHelper {
    fun getVoucherNameErrorStatus(voucherName: String) : Boolean {
        return if (voucherName.count() in 1 .. 4 ) {
            true
        } else voucherName.isEmpty()
    }
}
