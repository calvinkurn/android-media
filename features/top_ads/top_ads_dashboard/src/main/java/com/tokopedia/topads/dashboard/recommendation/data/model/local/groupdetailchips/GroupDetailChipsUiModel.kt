package com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips

import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactory

class GroupDetailChipsUiModel() :
    GroupDetailDataModel {
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
