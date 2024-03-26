package com.tokopedia.home.beranda.presentation.view.helper

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object HomeRecommendationController {

    var recommendationCardValue = ""

    fun fetchRecommendationCardRollence() {
        recommendationCardValue = try {
             RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.FOR_YOU_FEATURE_FLAG,
                ""
            )
        } catch (e: Exception) {
            ""
        }
    }

    fun isUsingRecommendationCard(): Boolean {
        return true
    }
}
