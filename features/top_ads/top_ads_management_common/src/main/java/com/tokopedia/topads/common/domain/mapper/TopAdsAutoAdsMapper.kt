package com.tokopedia.topads.common.domain.mapper

import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.topads.common.data.response.TopAdsAutoAdsData
import com.tokopedia.topads.common.data.response.TopAdsAutoAdsV2
import com.tokopedia.topads.common.domain.model.TopAdsAutoAdsModel

object TopAdsAutoAdsMapper {

    val TopAdsAutoAdsV2.TopAdsAutoAdsData.mapToDomain: TopAdsAutoAdsModel
        get() = TopAdsAutoAdsModel(
            shopId, status, statusDesc, dailyBudget, dailyUsage, adsInfo?.mapToDomain
        )

    val TopAdsAutoAdsData.mapToDomain: TopAdsAutoAdsModel
        get() = TopAdsAutoAdsModel(
            shopId.toString(), status, statusDesc, dailyBudget, dailyUsage, adsInfo?.mapToDomain
        )

    val TopAdsAutoAdsV2.TopAdsAutoAdsData.TopAdsAutoAdsInfo.mapToDomain
        get() = TopAdsAutoAdsModel.TopAdsAutoAdsInfo(reason, message)

    val TopAdsAutoAdsData.TopAdsAutoAdsInfo.mapToDomain
        get() = TopAdsAutoAdsModel.TopAdsAutoAdsInfo(reason, message)

    val AutoAdsResponse.TopAdsGetAutoAds.Data.mapToDomain: TopAdsAutoAdsModel
        get() = TopAdsAutoAdsModel(
            shopId, status, statusDesc, dailyBudget, dailyUsage, info.mapToDomain
        )

    val AutoAdsResponse.TopAdsGetAutoAds.Data.Info.mapToDomain
        get() = TopAdsAutoAdsModel.TopAdsAutoAdsInfo(reason, message)
}