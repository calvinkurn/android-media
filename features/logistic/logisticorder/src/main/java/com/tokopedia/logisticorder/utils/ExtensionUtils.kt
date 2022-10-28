package com.tokopedia.logisticorder.utils

/**
 * Created by irpan on 12/07/22.
 */

/*
to Hypen  ( - ) if empty String or null
 */
fun String?.toHyphenIfEmptyOrNull(): String {
    return if (this == "" || this == null) "-" else this
}

fun String?.isHypen(): Boolean {
    return this == "-"
}