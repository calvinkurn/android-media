package com.tokopedia.product.manage.common.session

import android.content.Context

class ProductManageSession(private val context: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "product_manage_session_shared_pref"
        const val HAS_TICKER_BROADCAST_CHAT = "hasTickerBroadcastChat"
        const val HAS_DATE_TICKER_BC = "hasDateTickerBC"
    }

    private val sharedPref by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPref.getBoolean(key, defaultValue)
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPref.getString(key, defaultValue).orEmpty()
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun putString(key: String, value: String) {
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }
}