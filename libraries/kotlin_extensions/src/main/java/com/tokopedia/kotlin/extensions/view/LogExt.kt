package com.tokopedia.kotlin.extensions.view

import android.util.Log
import com.tokopedia.config.GlobalConfig.isAllowDebuggingTools

/**
 * @author : Steven 10/07/19
 */


fun Any.debug(message: String) {
    if(isAllowDebuggingTools()) {
        Log.d(this::class.java.simpleName, message)
    }
}

fun Any.debug(tag: String, message: String) {
    if(isAllowDebuggingTools()) {
        Log.d(tag, message)
    }
}

fun Any.debug(message: String, tr: Throwable) {
    if(isAllowDebuggingTools()) {
        Log.d(this::class.java.simpleName, message, tr)
    }
}

fun Any.error(message: String) {
    Log.e(this::class.java.simpleName, message)
}

fun Any.error(message: String, tr: Throwable) {
    Log.e(this::class.java.simpleName, message, tr)
}

fun Any.info(message: String) {
    Log.i(this::class.java.simpleName, message)
}

fun Any.info(message: String, tr: Throwable) {
    Log.i(this::class.java.simpleName, message, tr)
}

fun Any.verbose(message: String) {
    Log.v(this::class.java.simpleName, message)
}

fun Any.verbose(message: String, tr: Throwable) {
    Log.v(this::class.java.simpleName, message, tr)
}

fun Any.warn(message: String) {
    Log.w(this::class.java.simpleName, message)
}


fun Any.warn(message: String, tr: Throwable) {
    Log.w(this::class.java.simpleName, message, tr)
}

fun Any.warn(tr: Throwable) {
    Log.w(this::class.java.simpleName, tr)
}