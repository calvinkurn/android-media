package com.tokopedia.topads.common.data.util

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.RECOMMENDATION_BUDGET_MULTIPLIER

object TopAdsEditUtils {
    fun calculateDailyBudget(pencarian: Int?, rekomendasi: Int?): Int {
        return pencarian.orZero().coerceAtLeast(rekomendasi.orZero()) * RECOMMENDATION_BUDGET_MULTIPLIER
    }
}