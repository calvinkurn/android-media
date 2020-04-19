package com.tokopedia.analyticsdebugger.validator

import android.content.Context
import java.io.IOException


internal object Utils {

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun getAnalyticsName(item: Map<String, Any>): String {
        return if (item.containsKey("event")) item["event"] as String
        else if (item.isNotEmpty()) item[item.keys.first()] as String
        else ""
    }

    fun listAssetFiles(c: Context, rootPath: String): List<String> {
        val files: MutableList<String> = ArrayList()
        try {
            val paths = c.assets.list(rootPath)
            if (paths.size > 0) { // This is a folder
                for (file in paths) {
                    val path = "$rootPath/$file"
                    val temp = listAssetFiles(c, path)
                    if (temp.isEmpty()) { // not directory
                        files.add(path)
                    } else {
                        files.addAll(temp)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return files
    }

}