package com.tokopedia.product.manage.common.session

import android.content.Context

class ProductManageSession(private val context: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "product_manage_session_shared_pref"
        private const val HAS_TICKER_BROADCAST_CHAT = "hasTickerBroadcastChat"
        private const val HAS_TICKER_DATE_BC = "hasTickerDateBC"
    }

    private val sharedPref by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPref.getBoolean(key, defaultValue)
    }

    private fun getString(key: String, defaultValue: String = ""): String {
        return sharedPref.getString(key, defaultValue).orEmpty()
    }

    private fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun putString(key: String, value: String) {
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setHasTickerBroadcastChat(value: Boolean) {
        putBoolean(HAS_TICKER_BROADCAST_CHAT, value)
    }

    fun getHasTickerBroadcastChat() = getBoolean(HAS_TICKER_BROADCAST_CHAT)

    fun setHasTickerDateBC(value: String) {
        putString(HAS_TICKER_DATE_BC, value)
    }

    fun getHasTickerDateBC() = getString(HAS_TICKER_DATE_BC)
}