package com.tokopedia.home.analytics.byteio

import android.content.Context
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero

internal fun sendEventShow(context: Context, element: HomeRecommendationItemDataModel) {
    if (element.recommendationProductItem.isTopAds) {
        AppLogTopAds.sendEventShow(
            context,
            element.recommendationProductItem.asAdsLogShowModel()
        )
    }
}

internal fun sendEventShowOver(context: Context, element: HomeRecommendationItemDataModel, maxPercentage: Int) {
    if(element.recommendationProductItem.isTopAds) {
        AppLogTopAds.sendEventShowOver(
            context,
            element.recommendationProductItem.asAdsLogShowOverModel(maxPercentage)
        )
    }
}

internal fun sendEventRealtimeClickAdsByteIo(context: Context, element: HomeRecommendationItemDataModel.HomeRecommendationProductItem?, refer: String) {
    element?.let {
        if (it.isTopAds) {
            AppLogTopAds.sendEventRealtimeClick(
                context,
                it.asAdsLogRealtimeClickModel(refer)
            )
        }
    }
}

internal fun HomeRecommendationItemDataModel.HomeRecommendationProductItem.asAdsLogShowModel(): AdsLogShowModel {
    return AdsLogShowModel(
        adsValue = recommendationAdsLog.creativeID.toLongOrZero(),
        logExtra = recommendationAdsLog.logExtra,
        AdsLogShowModel.AdExtraData(
            productId = id,
            productName = name
        )
    )
}

internal fun HomeRecommendationItemDataModel.HomeRecommendationProductItem.asAdsLogShowOverModel(sizePercent: Int): AdsLogShowOverModel {
    return AdsLogShowOverModel(
        adsValue = recommendationAdsLog.creativeID.toLongOrZero(),
        logExtra = recommendationAdsLog.logExtra,
        AdsLogShowOverModel.AdExtraData(
            productId = id,
            sizePercent = sizePercent.toString(),
            productName = name
        )
    )
}

internal fun HomeRecommendationItemDataModel.HomeRecommendationProductItem.asAdsLogRealtimeClickModel(refer: String): AdsLogRealtimeClickModel {
    return AdsLogRealtimeClickModel(
        refer,
        adsValue = recommendationAdsLog.creativeID.toLongOrZero(),
        logExtra = recommendationAdsLog.logExtra,
        AdsLogRealtimeClickModel.AdExtraData(
            productId = id,
            productName = name
        )
    )
}
