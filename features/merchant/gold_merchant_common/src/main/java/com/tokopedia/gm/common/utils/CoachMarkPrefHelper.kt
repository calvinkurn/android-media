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
        const val REBATE_MVP_DATA_KEY = "rebateMVPAnnouncement"
        const val REBATE_ULTIMATE_DATA_KEY = "rebateWidget"
        const val KEY_REBATE_MVP_COACH_MARK = "rebate_mvp_coach_mark_key"
        const val KEY_REBATE_ULTIMATE_COACH_MARK = "rebate_ultimate_coach_mark_key"
        const val KEY_UNIFICATION_WIDGET_COACH_MARK = "unification_widget_coach_mark_key"
    }

    fun saveRebateCoachMarkMvpFlag() {
        saveBoolean(KEY_REBATE_MVP_COACH_MARK, true)
    }

    fun getRebateCoachMarkMvpStatus(): Boolean {
        return getBoolean(KEY_REBATE_MVP_COACH_MARK, false)
    }

    fun getUnificationCoachMarkStatus(): Boolean {
        return getBoolean(KEY_UNIFICATION_WIDGET_COACH_MARK, false)
    }

    fun saveUnificationMarkFlag() {
        saveBoolean(KEY_UNIFICATION_WIDGET_COACH_MARK, true)
    }

    fun saveRebateCoachMarkUltimateFlag() {
        saveBoolean(KEY_REBATE_ULTIMATE_COACH_MARK, true)
    }

    fun getRebateCoachMarkUltimateStatus(): Boolean {
        return getBoolean(KEY_REBATE_ULTIMATE_COACH_MARK, false)
    }

    private fun saveBoolean(key: String, value: Boolean) {
        pmCommonPreferenceManager.putBoolean(key, value)
        pmCommonPreferenceManager.apply()
    }

    private fun getBoolean(key: String, defValue: Boolean): Boolean {
        return pmCommonPreferenceManager.getBoolean(
            key,
            defValue
        ).orFalse()
    }
}