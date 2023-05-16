package com.tokopedia.media.loader.util

import android.content.Context

object AssetReader {

    fun get(context: Context, fileName: String): List<String> {
        val assets = context.resources.assets.open(fileName)
        val content = assets.bufferedReader().use { it.readText() }
        return content.split("\n")
    }
}
