package com.tokopedia.sellerappwidget.data.local

/**
 * Created By @ilhamsuaib on 22/11/20
 */

interface SellerAppWidgetPreferences {

    fun putString(key: String, value: String)

    fun getString(key: String, defValue: String): String

    fun putLong(key: String, value: Long)

    fun getLong(key: String, defValue: Long): Long
}