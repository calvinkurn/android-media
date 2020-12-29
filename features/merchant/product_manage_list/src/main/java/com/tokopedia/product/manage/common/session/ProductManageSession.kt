package com.tokopedia.product.manage.common.session

import android.content.Context

class ProductManageSession(private val context: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "product_manage_session_shared_pref"

        private const val KEY_SHOW_STOCK_LOCATION_BOTTOM_SHEET = "key_show_stock_location_bottom_sheet"
        private const val KEY_HAS_TICKER_BROADCAST_CHAT = "hasTickerBroadcastChat"
        private const val KEY_HAS_TICKER_DATE_BC = "hasTickerDateBC"
    }

    private val sharedPref by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getShowStockLocationBottomSheet(): Boolean {
        return getBoolean(KEY_SHOW_STOCK_LOCATION_BOTTOM_SHEET, true)
    }

    fun setShowStockLocationBottomSheet(value: Boolean) {
        putBoolean(KEY_SHOW_STOCK_LOCATION_BOTTOM_SHEET, value)
    }

    fun setHasTickerBroadcastChat(value: Boolean) {
        putBoolean(KEY_HAS_TICKER_BROADCAST_CHAT, value)
    }

    fun getHasTickerBroadcastChat() = getBoolean(KEY_HAS_TICKER_BROADCAST_CHAT)

    fun setHasTickerDateBC(value: String) {
        putString(KEY_HAS_TICKER_DATE_BC, value)
    }

    fun getHasTickerDateBC() = getString(KEY_HAS_TICKER_DATE_BC)

    private fun getString(key: String, defaultValue: String = ""): String {
        return sharedPref.getString(key, defaultValue).orEmpty()
    }

    private fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPref.getBoolean(key, defaultValue)
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
}