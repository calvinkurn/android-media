package com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips

import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_CHIPS
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactory

data class GroupDetailChipsUiModel(var isChipsAvailable: Boolean = true) :
    GroupDetailDataModel {

    override fun isAvailable(): Boolean {
        return isChipsAvailable
    }
    override fun type(): String {
        return TYPE_CHIPS.toString()
    }

    override fun type(typeFactory: GroupDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newItem: GroupDetailDataModel): Boolean {
        return this == newItem
    }
}
