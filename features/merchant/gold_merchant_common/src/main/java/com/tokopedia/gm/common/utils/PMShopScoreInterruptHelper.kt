package com.tokopedia.gm.common.utils

import com.tokopedia.gm.common.data.source.local.PMCommonPreferenceManager
import com.tokopedia.kotlin.extensions.orFalse
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/04/21
 */

class PMShopScoreInterruptHelper @Inject constructor(
    private val pmCommonPreferenceManager: PMCommonPreferenceManager
) {

    fun saveRecommendationCoachMarkFlag() {
        pmCommonPreferenceManager.putBoolean(
            PMCommonPreferenceManager.KEY_RECOMMENDATION_COACH_MARK,
            true
        )
        pmCommonPreferenceManager.apply()
    }

    fun getRecommendationCoachMarkStatus(): Boolean {
        return pmCommonPreferenceManager.getBoolean(
            PMCommonPreferenceManager.KEY_RECOMMENDATION_COACH_MARK,
            false
        ).orFalse()
    }
}