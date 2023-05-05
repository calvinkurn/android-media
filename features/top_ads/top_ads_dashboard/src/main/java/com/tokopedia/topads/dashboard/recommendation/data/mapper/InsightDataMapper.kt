package com.tokopedia.topads.dashboard.recommendation.data.mapper

import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightUiModel
import javax.inject.Inject

class InsightDataMapper @Inject constructor() {

    val insightDataMap: MutableMap<Int, InsightUiModel> = mutableMapOf(
        0 to InsightUiModel(),
        1 to InsightUiModel(),
        2 to InsightUiModel(),
        3 to InsightUiModel(),
        4 to InsightUiModel(),
        5 to InsightUiModel(),
    )

    val insightTypeInputList = mutableListOf(
        "Semua",
        "keyword_bid",
        "group_daily_budget",
        "group_bid",
        "keyword_new_negative",
        "keyword_new_positive",
    )
}
