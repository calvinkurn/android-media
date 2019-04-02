package com.tokopedia.instantloan.ddcollector

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

import java.util.ArrayList
import java.util.HashMap

abstract class BaseContentCollector(private val mContentResolver: ContentResolver) : BaseCollector() {

    abstract fun getParameters(): List<String>

    abstract fun getLimit(): Int

    override fun getData() = this.collect(this.buildUri())

    abstract fun buildUri(): Uri

    fun collect(uri: Uri): List<Map<String, String>> {
        val phoneInfoColumn = mutableListOf<Map<String, String>>()
        var sortOrder: String? = null

        if (this.getLimit() > 0) {
            sortOrder = String.format("%s limit ${getLimit()}", BaseColumns._ID)
        }

        val cursor = this.mContentResolver.query(uri, this.getParameters().toTypedArray(), null, null, sortOrder)
        if (cursor != null && cursor.count > 0) {
            val paramList = this.getParameters()
            cursor.moveToFirst()

            do {
                val phoneInfoMap = HashMap<String, String>()

                for (columnName in paramList) {
                    val value = cursor.getString(cursor.getColumnIndex(columnName))
                    phoneInfoMap[columnName] = value
                }

                phoneInfoColumn.add(phoneInfoMap)
            } while (cursor.moveToNext())
        }

        cursor?.close()

        return phoneInfoColumn
    }
}
