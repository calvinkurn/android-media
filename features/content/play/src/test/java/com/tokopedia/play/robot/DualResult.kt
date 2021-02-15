package com.tokopedia.play.robot

/**
 * Created by jegul on 15/02/21
 */
sealed class DualResult<T> {

    data class HasValue<T>(val result: T) : DualResult<T>()
    data class NoValue(val reason: Throwable) : DualResult<Nothing>()
}

val errorNoResult = "Result has no value"
val errorHasResult = "Result has value"