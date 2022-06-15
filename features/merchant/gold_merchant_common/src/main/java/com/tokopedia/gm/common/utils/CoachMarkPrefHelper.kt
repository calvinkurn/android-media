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
        const val REBATE_ANNOUNCEMENT_DATA_KEY = "communicationPeriodAnnouncement"
        const val KEY_RECOMMENDATION_COACH_MARK = "key_recommendation_coach_mark"
        const val KEY_REBATE_COACH_MARK = "key_rebate_coach_mark"
    }

    fun saveRecommendationCoachMarkFlag() {
        saveBoolean(KEY_RECOMMENDATION_COACH_MARK, true)
    }

    fun getRecommendationCoachMarkStatus(): Boolean {
        return getBoolean(KEY_RECOMMENDATION_COACH_MARK, false)
    }

    fun saveRebateCoachMarkFlag() {
        saveBoolean(KEY_REBATE_COACH_MARK, true)
    }

    fun getRebateCoachMarkStatus(): Boolean {
        return getBoolean(KEY_REBATE_COACH_MARK, false)
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