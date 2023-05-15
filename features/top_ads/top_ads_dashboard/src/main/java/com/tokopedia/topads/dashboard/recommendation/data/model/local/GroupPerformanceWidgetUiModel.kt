package com.tokopedia.topads.dashboard.recommendation.data.model.local

import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactory

class GroupPerformanceWidgetUiModel : GroupDetailDataModel {
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
