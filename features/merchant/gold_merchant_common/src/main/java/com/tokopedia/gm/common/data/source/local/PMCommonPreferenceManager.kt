package com.tokopedia.gm.common.data.source.local

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 23/03/21
 */

class PMCommonPreferenceManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) {
    companion object {
        private const val PREF_NAME = "pm_common_shared_preferences"

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