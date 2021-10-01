package com.tokopedia.analytics.performance.util

import android.os.Build
import android.os.Trace
import kotlin.reflect.KFunction

object PerformanceCustomTrace {
    var id = 101
    fun launchFunctionWithTrace(targetFunction: () -> Unit, lambdaName: String="") {
        val functionAsKFunction: KFunction<*>? = targetFunction as? KFunction<*>
        val functionName = functionAsKFunction?.name?:if (lambdaName.isNotEmpty())lambdaName else "Unknown function"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            id++
            Trace.beginAsyncSection(functionName, id)
            targetFunction.invoke()
            Trace.endAsyncSection(functionName, id)
        }
    }

    fun beginMethodTracing(name: String, cookie: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Trace.beginAsyncSection(name, cookie)
        }
    }

    fun endMethodTracing(name: String, cookie: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Trace.endAsyncSection(name, cookie)
        }
    }
}