package com.tokopedia.kotlin.extensions.view

/**
 * Created by jegul on 2019-08-05.
 */
fun <V> Map<String, V>.toUrlParams(): String {
    return entries.joinToString("&") { "${it.key}=${it.value}" }
}