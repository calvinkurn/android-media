package com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips

import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactory

class GroupDetailChipsUiModel(var isChipsAvailable:Boolean = true) :
    GroupDetailDataModel {

    override fun isAvailable(): Boolean {
        return isChipsAvailable
    }
    override fun type(): String {
        return "3"
    }

    override fun type(typeFactory: GroupDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newItem: GroupDetailDataModel): Boolean {
        return this == newItem
    }
}
