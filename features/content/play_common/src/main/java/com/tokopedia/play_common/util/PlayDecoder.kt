package com.tokopedia.play_common.util

import com.tokopedia.abstraction.common.utils.view.MethodChecker

/**
 * @author by astidhiyaa on 24/05/22
 */

fun String.decodeFromNetwork() : String {
    return MethodChecker.fromHtml(this).toString()
}