package com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips

import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactory

data class InsightTypeChipsUiModel(val list: List<InsightTypeChipsItemUiModel>):
    GroupDetailDataModel {
    override fun type(): String {
        return  ""
    }

    override fun type(typeFactory: GroupDetailAdapterFactory): Int {
       return typeFactory.type(this)
    }

    override fun equalsWith(newItem: GroupDetailDataModel): Boolean {
        return this == newItem
    }
}
