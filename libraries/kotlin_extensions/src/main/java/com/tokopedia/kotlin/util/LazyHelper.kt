package com.tokopedia.kotlin.util

/**
 * Created by yovi.putra on 14/12/22"
 * Project name: android-tokopedia-core
 **/

fun <T> lazyThreadSafetyNone(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)
