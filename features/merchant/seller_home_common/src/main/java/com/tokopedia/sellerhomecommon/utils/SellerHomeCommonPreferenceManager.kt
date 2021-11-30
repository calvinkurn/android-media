package com.tokopedia.sellerhomecommon.utils

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

class SellerHomeCommonPreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val SHARED_PREF_NAME = "seller_home_common_pref"
        private const val IS_SHOW_MILESTONE_WIDGET = "isShowMilestoneWidget"
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

    fun setShowMilestoneWidget(value: Boolean) {
        putBoolean(IS_SHOW_MILESTONE_WIDGET, value)
    }

    fun getIsShowMilestoneWidget() = getBoolean(IS_SHOW_MILESTONE_WIDGET, true)
}