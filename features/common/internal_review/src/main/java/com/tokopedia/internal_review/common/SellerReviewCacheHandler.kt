package com.tokopedia.internal_review.common

import android.content.Context
import android.content.SharedPreferences

/**
 * Created By @ilhamsuaib on 09/02/21
 */

class SellerReviewCacheHandler constructor(
        context: Context
) {

    private val pref: SharedPreferences by lazy {
        context.getSharedPreferences(Const.SharedPrefKey.PREFERENCE_NAME, Context.MODE_PRIVATE)
    }
    private val editor: SharedPreferences.Editor

    init {
        editor = pref.edit()
    }

    fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
    }

    fun putLong(key: String, value: Long) {
        editor.putLong(key, value)
    }

    fun putStringSet(key: String, stringSet: Set<String>) {
        editor.putStringSet(key, stringSet)
    }

    fun getString(key: String, defValue: String): String {
        return pref.getString(key, defValue) ?: defValue
    }

    fun getLong(key: String, defValue: Long): Long {
        return pref.getLong(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return pref.getBoolean(key, defValue)
    }

    fun getStringSet(key: String, defValue: Set<String>): Set<String> {
        return pref.getStringSet(key, defValue) ?: defValue
    }

    fun applyEditor() {
        editor.apply()
    }
}