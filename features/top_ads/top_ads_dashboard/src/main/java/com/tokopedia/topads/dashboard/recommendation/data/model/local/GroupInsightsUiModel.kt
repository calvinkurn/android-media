package com.tokopedia.topads.dashboard.recommendation.data.model.local

import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactory

data class GroupInsightsUiModel(
    var type: Int = 0,
    val title: String = "",
    val subTitleValue: String = "",
    val isInsightAvailable: Boolean = false,
    val expandItemDataModel: GroupDetailDataModel? = null,
    var isExpanded: Boolean = false
) :
    GroupDetailDataModel {

    override fun isAvailable(): Boolean {
        return isInsightAvailable
    }

    override fun type(): String {
        return ""
    }

    override fun type(typeFactory: GroupDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newItem: GroupDetailDataModel): Boolean {
        return this == newItem
    }
}
