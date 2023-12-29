package com.tokopedia.gm.common.utils

import com.tokopedia.gm.common.data.source.local.PMCommonPreferenceManager
import com.tokopedia.kotlin.extensions.orFalse
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/04/21
 */

class CoachMarkPrefHelper @Inject constructor(
    private val pmCommonPreferenceManager: PMCommonPreferenceManager
) {

    companion object {
        const val KEY_UNIFICATION_WIDGET_COACH_MARK = "unification_widget_coach_mark_key"
    }

    val hasUnificationCoachMarkBeenShown: Boolean
        get() = getBoolean(KEY_UNIFICATION_WIDGET_COACH_MARK, false)

    fun saveUnificationMarkFlag() {
        saveBoolean(KEY_UNIFICATION_WIDGET_COACH_MARK, true)
    }

    private fun saveBoolean(key: String, value: Boolean) {
        pmCommonPreferenceManager.putBoolean(key, value)
        pmCommonPreferenceManager.apply()
    }

    private fun getBoolean(key: String, defValue: Boolean): Boolean {
        return pmCommonPreferenceManager.getBoolean(
            key, defValue
        ).orFalse()
    }
}