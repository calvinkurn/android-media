package com.tokopedia.sellerorder.waitingpaymentorder.presentation.model

import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.SOURCE)
@IntDef(ErrorType.NO_CONNECTION, ErrorType.SERVER_ERROR)
annotation class ErrorType {
    companion object {
        const val NO_CONNECTION = 0
        const val SERVER_ERROR = 1
    }
}