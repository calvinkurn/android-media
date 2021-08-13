package com.tokopedia.product.manage.common.session

import android.content.Context

class ProductManageSession(private val context: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "product_manage_session_shared_pref"

        private const val KEY_SHOW_STOCK_LOCATION_BOTTOM_SHEET = "key_show_stock_location_bottom_sheet"
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

    private fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPref.getBoolean(key, defaultValue)
    }

    private fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
}