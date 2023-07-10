package com.tokopedia.topads.dashboard.recommendation.common

import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.HEADLINE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PRODUCT_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PRODUCT_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_SHOP_VALUE
import javax.inject.Inject

class Utils @Inject constructor() {
    fun toProgressPercent(count: Int): Int {
        return 100 - (count * 100) / 5
    }
    fun convertAdTypeToInt(adType: String?): Int {
        return if (HEADLINE_KEY == adType) TYPE_SHOP_VALUE else TYPE_PRODUCT_VALUE
    }

    fun convertAdTypeToString(adType: Int): String {
        return if (TYPE_PRODUCT_VALUE == adType) PRODUCT_KEY else HEADLINE_KEY
    }
}
