package com.tokopedia.topads.dashboard.recommendation.data.mapper

import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_ALL
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_ALL_INPUT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_DAILY_BUDGET
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_DAILY_BUDGET_INPUT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_GROUP_BID_INPUT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_KEYWORD_BID_INPUT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_NEGATIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_NEGATIVE_KEYWORD_INPUT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_POSITIVE_KEYWORD_INPUT
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightUiModel
import javax.inject.Inject

class InsightDataMapper @Inject constructor() {

    val insightDataMap: MutableMap<Int, InsightUiModel> = mutableMapOf(
        INSIGHT_TYPE_ALL to InsightUiModel(),
        INSIGHT_TYPE_POSITIVE_KEYWORD to InsightUiModel(),
        INSIGHT_TYPE_KEYWORD_BID to InsightUiModel(),
        INSIGHT_TYPE_GROUP_BID to InsightUiModel(),
        INSIGHT_TYPE_DAILY_BUDGET to InsightUiModel(),
        INSIGHT_TYPE_NEGATIVE_KEYWORD to InsightUiModel()
    )

    val insightTypeInputList = mutableListOf(
        INSIGHT_TYPE_ALL_INPUT,
        INSIGHT_TYPE_POSITIVE_KEYWORD_INPUT,
        INSIGHT_TYPE_KEYWORD_BID_INPUT,
        INSIGHT_TYPE_GROUP_BID_INPUT,
        INSIGHT_TYPE_DAILY_BUDGET_INPUT,
        INSIGHT_TYPE_NEGATIVE_KEYWORD_INPUT
    )
}
