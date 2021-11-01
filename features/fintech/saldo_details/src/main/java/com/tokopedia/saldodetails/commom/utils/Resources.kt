package com.tokopedia.saldodetails.commom.utils

import androidx.annotation.StringDef

sealed class Resources<T>
data class Success<T>(val data : T) : Resources<T>()
data class ErrorMessage<T,E>(val data : E , @ErrorTypes val type : String = NORMAL) : Resources<T>()




const val NORMAL = "normal"
const val IN_VALID_DATE_ERROR = "invaliddateError"

@StringDef(
        NORMAL,
        IN_VALID_DATE_ERROR
)
annotation class ErrorTypes