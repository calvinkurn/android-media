package com.tokopedia.kotlin.extensions.view

/**
 * Created by yovi.putra on 13/06/23"
 * Project name: android-tokopedia-core
 **/

fun <T : Any> T?.ifNull(block: () -> T): T {
    return this ?: block.invoke()
}