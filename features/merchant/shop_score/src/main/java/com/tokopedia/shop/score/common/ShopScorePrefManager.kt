package com.tokopedia.shop.score.common

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext

class ShopScorePrefManager(@ApplicationContext private val context: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "shop_score_coach_mark_pref"
        private const val HAS_FINISH_COACH_MARK = "finishCoachMarkShopScore"
        private const val IS_SHOW_POP_UP_END_TENURE = "isShowPopupEndTenure"
    }

    private val sharedPref by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getBoolean(key: String, defaultValue: Boolean = true): Boolean {
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

    fun setIsShowPopupEndTenure(isShowPopEndTenure: Boolean) {
        putBoolean(IS_SHOW_POP_UP_END_TENURE, isShowPopEndTenure)
    }

    fun getIsShowPopupEndTenure() = getBoolean(IS_SHOW_POP_UP_END_TENURE)

    fun getFinishCoachMark() = getBoolean(HAS_FINISH_COACH_MARK, false)
}