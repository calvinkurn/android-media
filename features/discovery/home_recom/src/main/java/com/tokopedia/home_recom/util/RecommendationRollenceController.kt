package com.tokopedia.home_recom.util

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import java.lang.Exception

/**
 * Created by yfsx on 18/08/21.
 */
object RecommendationRollenceController {

    private var rollenceRecommendationCPM: String = ""

    fun fetchRecommendationRollenceValue() {
        rollenceRecommendationCPM = try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.RECOM_PAGE_CPM_EXP, RollenceKey.RECOM_PAGE_CPM_OLD)
        } catch (e: Exception) {
            RollenceKey.RECOM_PAGE_CPM_OLD
        }
    }

    private fun getRollenceValueRecomPageCPM(): String {
        return if (rollenceRecommendationCPM.isNotEmpty()) rollenceRecommendationCPM else RollenceKey.RECOM_PAGE_CPM_OLD
    }

    fun isRecommendationCPMRollenceVariant(): Boolean {
        return getRollenceValueRecomPageCPM() == RollenceKey.RECOM_PAGE_CPM_VARIANT
    }
}