package com.tokopedia.topads.dashboard.recommendation.data.model.local

import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PERFORMANCE
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactory

data class GroupPerformanceWidgetUiModel(
    val impression: Int = 0,
    val topSlotImpression: Int = 0,
) : GroupDetailDataModel {
    override fun type(): String {
        return TYPE_PERFORMANCE.toString()
    }

    override fun type(typeFactory: GroupDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newItem: GroupDetailDataModel): Boolean {
        return this == newItem
    }
}
