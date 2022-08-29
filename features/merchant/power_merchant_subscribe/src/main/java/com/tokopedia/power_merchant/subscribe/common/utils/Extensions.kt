package com.tokopedia.power_merchant.subscribe.common.utils

import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 21/04/22.
 */

private const val SPACE = " "

internal fun String.removeSpace(): String {
    return this.replace(SPACE, String.EMPTY)
}