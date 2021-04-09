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

        const val KEY_HAS_SHOW_COMMUNICATION_INTERRUPT_PAGE = "key_has_opened_communication_interrupt_page"
        const val KEY_HAS_SHOW_TRANSITION_INTERRUPT_POPUP = "key_has_show_transition_interrupt_popup"
        const val KEY_HAS_OPENED_COMMUNICATION_INTERRUPT_PAGE = "key_has_opened_communication_interrupt_page"
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

    fun apply() {
        spe.apply()
    }
}