package com.tokopedia.creation.common.upload.util

/**
 * Created By : Jonathan Darwin on November 06, 2023
 */

operator fun Throwable.plus(anotherThrowable: Throwable) = Exception(this.stackTraceToString() + "\n\n" + anotherThrowable.stackTraceToString())
