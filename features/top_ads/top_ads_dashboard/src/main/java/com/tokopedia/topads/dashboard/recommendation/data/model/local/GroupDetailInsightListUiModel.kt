package com.tokopedia.topads.dashboard.recommendation.data.model.local

import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_EMPTY_STATE
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactory

data class GroupDetailInsightListUiModel(
    val adGroups: MutableList<InsightListUiModel> = mutableListOf(),
    var insightType: Int = 0
) :
    GroupDetailDataModel {

    override fun type(): String {
        return TYPE_EMPTY_STATE.toString()
    }

    override fun type(typeFactory: GroupDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newItem: GroupDetailDataModel): Boolean {
        return this == newItem
    }
}
