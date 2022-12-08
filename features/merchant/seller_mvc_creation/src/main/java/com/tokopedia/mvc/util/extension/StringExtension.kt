package com.tokopedia.mvc.util.extension

import com.tokopedia.kotlin.extensions.view.EMPTY


private const val SPACE = " "

fun String.removeSpace(): String {
    return this.replace(SPACE, String.EMPTY)
}
