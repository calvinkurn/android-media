package com.tokopedia.kotlin.extensions.view

import com.tokopedia.abstraction.common.utils.GlobalConfig

/**
 * @author by milhamj on 30/11/18.
 */

fun Throwable.debugTrace() {
    if (GlobalConfig.isAllowDebuggingTools()) printStackTrace()
}