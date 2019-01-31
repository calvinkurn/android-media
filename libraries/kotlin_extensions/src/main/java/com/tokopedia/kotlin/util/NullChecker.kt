package com.tokopedia.kotlin.util

import android.util.Log
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.utils.GlobalConfig

/**
 * @author by milhamj on 29/01/19.
 */

private const val NULL_PATTERN = """\n\s*"\S+":\s*null"""

@JvmOverloads
fun isContainNull(`object`: Any?, actionWhenNull: (String) -> Unit = { }): Boolean {
    val whenNull = { errorMessage: String ->
        printDebug(errorMessage)
        actionWhenNull(errorMessage)
    }

    if (`object` == null) {
        whenNull("Object is null")
        return true
    }

    val gsonBuilder = GsonBuilder()
    gsonBuilder.serializeNulls()
    gsonBuilder.setPrettyPrinting()
    val gson = gsonBuilder.create()

    val objectInString = gson.toJson(`object`).toLowerCase()
    val firstNullOccurrence = Regex(NULL_PATTERN).find(objectInString)

    return if (firstNullOccurrence == null) {
        false
    } else {
        whenNull(firstNullOccurrence.value)
        true
    }
}

@JvmOverloads
fun throwExceptionWhenNull(`object`: Any?, actionWhenNull: (String) -> Unit = { }) {
    if (isContainNull(`object`, actionWhenNull)) {
        throw ContainNullException()
    }
}

private fun printDebug(errorMessage: String) {
    if (GlobalConfig.isAllowDebuggingTools()) {
        Log.e("NullChecker", errorMessage)
    }
}
