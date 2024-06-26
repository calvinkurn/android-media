package com.tokopedia.analytics.byteio.util

import java.util.*

/**
 * Created by @milhamj on on 3/1/24.
 **/

private val alphanumericRegex = Regex("[^A-Za-z0-9_]")

fun String.spacelessParam(): String {
    val withoutSpace = this.lowercase(Locale.getDefault()).replace(" ", "")
    return alphanumericRegex.replace(withoutSpace, "")
}

fun String.underscoredParam(): String {
    val withoutSpace = this.lowercase(Locale.getDefault()).replace(" ", "_")
    return alphanumericRegex.replace(withoutSpace, "")
}
