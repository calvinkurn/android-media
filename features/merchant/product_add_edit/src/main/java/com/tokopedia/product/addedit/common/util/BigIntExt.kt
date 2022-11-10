package com.tokopedia.product.addedit.common.util

import com.tokopedia.kotlin.extensions.view.ZERO

private val DEFAULT_BIGINT = Int.ZERO.toBigInteger()

fun String?.toBigIntegerSafely() = try {
    toBigIntegerOrZero()
} catch (e: Exception) {
    DEFAULT_BIGINT
}

fun String?.toBigIntegerOrZero() = this?.toBigIntegerOrNull() ?: DEFAULT_BIGINT
