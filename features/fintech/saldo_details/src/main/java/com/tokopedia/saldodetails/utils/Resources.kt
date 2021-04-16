package com.tokopedia.saldodetails.utils

import androidx.annotation.StringDef

sealed class Resources<T>
class Loading<T> : Resources<T>()
data class Success<T>(val data : T) : Resources<T>()
data class ErrorMessage<T,E>(val data : E , @ErrorTypes val type : String = NORMAL) : Resources<T>()

data class AddElements<T>(val data : T) : Resources<T>()


sealed class Errors

data class ErrorType<T>(val data : T , @ErrorTypes val type : String = NORMAL) : Errors()




const val NORMAL = "normal"
const val IN_VALID_DATE_ERROR = "invaliddateError"

@StringDef(
        NORMAL,
        IN_VALID_DATE_ERROR
)
annotation class ErrorTypes