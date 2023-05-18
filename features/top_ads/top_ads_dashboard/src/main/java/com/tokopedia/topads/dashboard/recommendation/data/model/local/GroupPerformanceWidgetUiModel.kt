package com.tokopedia.topads.dashboard.recommendation.data.model.local

import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactory

class GroupPerformanceWidgetUiModel(
    val impression: Int = 0,
    val topSlotImpression: Int = 0
) : GroupDetailDataModel {
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
