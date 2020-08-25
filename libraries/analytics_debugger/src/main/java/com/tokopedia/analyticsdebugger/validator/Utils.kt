package com.tokopedia.analyticsdebugger.validator

import android.content.Context
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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

    //todo Rahul - need better logic to support branch-io and appsflyer events
    fun getAnalyticsName(item: Map<String, Any>): String {
        return if (item.containsKey("eventAction")) item["eventAction"] as String
        else if (item.isNotEmpty()) {
            val key = item.keys.find { key ->
                item[key] is String
            }.orEmpty()

            if (key.isNotEmpty()) {
                item[key] as String
            } else ""

        } else ""
    }

    fun listAssetFiles(c: Context, rootPath: String): List<String> {
        val files: MutableList<String> = ArrayList()
        try {
            val paths = c.assets.list(rootPath) ?: arrayOf()
            if (paths.isNotEmpty()) { // This is a folder
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

    fun getTimeStampFormat(ts: Long?): String {
        if (ts == null) return ""
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        return dateFormat.format(Date(ts))
    }

}