package com.tokopedia.product.detail.common.extensions

/**
 * Created by yovi.putra on 09/08/22"
 * Project name: android-tokopedia-core
 **/

/**
 * @return self if not null, and create new instance with block.invoke if null
 */
fun <T : Any> T?.ifNull(block: () -> T): T {
    return this ?: block.invoke()
}

fun String?.ifNullOrBlank(block: () -> String): String = if (this.isNullOrBlank()) {
    block.invoke()
} else {
    this
}