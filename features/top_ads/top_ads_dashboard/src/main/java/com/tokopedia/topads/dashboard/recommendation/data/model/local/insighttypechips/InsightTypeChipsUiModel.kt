package com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips

import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_INSIGHT
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactory

data class InsightTypeChipsUiModel(
    val chipsList: List<String> = mutableListOf(),
    val adGroupList: MutableList<InsightListUiModel> = mutableListOf()
) :
    GroupDetailDataModel {

    override fun type(): String {
        return TYPE_INSIGHT.toString()
    }

    override fun type(typeFactory: GroupDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newItem: GroupDetailDataModel): Boolean {
        return this == newItem
    }
}
