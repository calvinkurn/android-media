package com.tokopedia.shop.score.common

import android.content.Context

class ShopScoreCoachMarkPrefs(private val context: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "shop_score_coach_mark_pref"
        const val HAS_FINISH_COACH_MARK = "finishCoachMarkShopScore"
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


    fun setFinishCoachMark(value: Boolean) {
        putBoolean(HAS_FINISH_COACH_MARK, value)
    }

    fun getFinishCoachMark() = getBoolean(HAS_FINISH_COACH_MARK)
}