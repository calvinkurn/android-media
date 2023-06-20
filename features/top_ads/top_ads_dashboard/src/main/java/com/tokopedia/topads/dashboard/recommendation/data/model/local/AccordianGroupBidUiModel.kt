package com.tokopedia.topads.dashboard.recommendation.data.model.local

import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsAdGroupBidInsightResponse
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactory

data class AccordianGroupBidUiModel(
    val topAdsBatchGetAdGroupBidInsightByGroupID: TopAdsAdGroupBidInsightResponse.TopAdsBatchGetAdGroupBidInsightByGroupID,
    val input: TopadsManagePromoGroupProductInput?
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
