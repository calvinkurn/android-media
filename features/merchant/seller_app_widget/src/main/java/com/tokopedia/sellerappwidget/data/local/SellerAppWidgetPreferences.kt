package com.tokopedia.sellerappwidget.data.local

import android.content.Context

/**
 * Created By @ilhamsuaib on 22/11/20
 */

interface SellerAppWidgetPreferences {

    companion object {
        fun getInstance(context: Context): SellerAppWidgetPreferences {
            return SellerAppWidgetPreferencesImpl(context)
        }
    }

    fun putString(key: String, value: String)

    fun getString(key: String, defValue: String): String

    fun putLong(key: String, value: Long)

    fun getLong(key: String, defValue: Long): Long

    fun putInt(key: String, value: Int)

    fun getInt(key: String, defValue: Int): Int
}