package com.tokopedia.libra.utils

fun <T> invokeSafe(default: T, action: () -> T?): T {
    return try {
        action() ?: default
    } catch (_: Throwable) {
        default
    }
}
