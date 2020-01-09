package com.tokopedia.kotlin.extensions.view

import timber.log.Timber

/**
 * @author by milhamj on 30/11/18.
 */

fun Throwable.debugTrace() {
    Timber.d(this)
}