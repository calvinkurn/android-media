package com.tokopedia.shop.score.common

import android.content.Context

class ShopScoreCoachMarkPrefs(private val context: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "shop_score_coach_mark_pref"
        const val HAS_SHOP_HEADER = "hasShownItemShopHeader"
        const val HAS_ITEM_PERFORMANCE_DETAIL = "isShownItemPerformanceDetail"
        const val HAS_ITEM_PM = "isShownItemPM"
        const val HAS_ITEM_RM = "isShownItemRM"
    }

    private val sharedPref by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPref.getBoolean(key, defaultValue)
    }

    private fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun setHasShownHeaderPerformanceDetail(value: Boolean) {
        putBoolean(HAS_SHOP_HEADER, value)
    }

    fun setHasShownItemPerformanceDetail(value: Boolean) {
        putBoolean(HAS_ITEM_PERFORMANCE_DETAIL, value)
    }

    fun setHasShownItemPM(value: Boolean) {
        putBoolean(HAS_ITEM_PM, value)
    }

    fun setHasShownItemRM(value: Boolean) {
        putBoolean(HAS_ITEM_RM, value)
    }

    fun getHasShownHeaderPerformanceDetail() = getBoolean(HAS_SHOP_HEADER)

    fun getHasShownItemPerformanceDetail() = getBoolean(HAS_ITEM_PERFORMANCE_DETAIL)

    fun getHasShownItemPM() = getBoolean(HAS_ITEM_PM)

    fun getHasShownItemRM() = getBoolean(HAS_ITEM_RM)
}