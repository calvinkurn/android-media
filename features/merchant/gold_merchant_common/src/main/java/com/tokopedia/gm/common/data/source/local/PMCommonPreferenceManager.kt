package com.tokopedia.gm.common.data.source.local

import android.content.Context
import android.content.SharedPreferences

/**
 * Created By @ilhamsuaib on 23/03/21
 */

class PMCommonPreferenceManager(
        private val appContext: Context
) {
    companion object {
        private const val PREF_NAME = "pm_common_shared_preferences"

        const val KEY_HAS_OPENED_NEW_SELLER_PM_IDLE_POPUP = "key_has_opened_communication_new_seller_pm_idle_popup"
        const val KEY_HAS_OPENED_COMMUNICATION_INTERRUPT_PAGE = "key_has_opened_communication_interrupt_page"

        const val KEY_SHOP_SCORE_CONSENT_CHECKED = "key_shop_score_consent_checked"
        const val KEY_NUMBER_OF_INTERRUPT_PAGE_OPENED = "key_number_of_interrupt_page_opened"
        const val KEY_RECOMMENDATION_COACH_MARK = "key_recommendation_coach_mark"
    }

    private val sp: SharedPreferences by lazy {
        appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    private val spe: SharedPreferences.Editor by lazy { sp.edit() }

    fun putBoolean(key: String, value: Boolean) {
        spe.putBoolean(key, value)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sp.getBoolean(key, defValue)
    }

    fun putInt(key: String, value: Int) {
        spe.putInt(key, value)
    }

    fun getInt(key: String, defValue: Int): Int {
        return sp.getInt(key, defValue)
    }

    fun apply() {
        spe.apply()
    }
}