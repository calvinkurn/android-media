package com.tokopedia.topads.dashboard.recommendation.data.model.local.data

import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_ALL_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_DAILY_BUDGET_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_GROUP_BID_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_KEYWORD_BID_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_NEGATIVE_KEYWORD_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_POSITIVE_KEYWORD_NAME
import com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips.GroupDetailChipsItemUiModel

object ChipsData {
    val chipsList = listOf(
        GroupDetailChipsItemUiModel(INSIGHT_TYPE_ALL_NAME, true),
        GroupDetailChipsItemUiModel(INSIGHT_TYPE_POSITIVE_KEYWORD_NAME),
        GroupDetailChipsItemUiModel(INSIGHT_TYPE_KEYWORD_BID_NAME),
        GroupDetailChipsItemUiModel(INSIGHT_TYPE_GROUP_BID_NAME),
        GroupDetailChipsItemUiModel(INSIGHT_TYPE_DAILY_BUDGET_NAME),
        GroupDetailChipsItemUiModel(INSIGHT_TYPE_NEGATIVE_KEYWORD_NAME)
    )
}
