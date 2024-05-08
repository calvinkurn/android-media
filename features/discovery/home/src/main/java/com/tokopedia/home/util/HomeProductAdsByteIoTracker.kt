package com.tokopedia.home.util

import android.content.Context
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel

internal fun sendEventShow(context: Context, element: HomeRecommendationItemDataModel) {
    if (element.recommendationProductItem.isTopAds) {
        AppLogTopAds.sendEventShow(
            context,
            element.recommendationProductItem.asAdsLogShowModel()
        )
    }
}

internal fun sendEventShow(context: Context, element: DynamicHomeChannel.Grid) {
    if (element.isTopads) {
        AppLogTopAds.sendEventShow(
            context,
            element.asAdsLogShowModel()
        )
    }
}

internal fun sendEventShowOver(context: Context, element: DynamicHomeChannel.Grid, maxPercentage: Int) {
    if(element.isTopads) {
        AppLogTopAds.sendEventShowOver(
            context,
            element.asAdsLogShowOverModel(maxPercentage)
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

internal fun sendEventRealtimeClickAdsByteIo(context: Context, element: DynamicHomeChannel.Grid?, refer: String) {
    element?.let {
        if (it.isTopads) {
            AppLogTopAds.sendEventRealtimeClick(
                context,
                it.asAdsLogRealtimeClickModel(refer)
            )
        }
    }
}

internal fun HomeRecommendationItemDataModel.HomeRecommendationProductItem.asAdsLogShowModel(): AdsLogShowModel {
    return AdsLogShowModel(
        // todo this value from BE
        0,
        // todo this value from BE
        "",
        AdsLogShowModel.AdExtraData(
            productId = id,
            productName = name
        )
    )
}

internal fun HomeRecommendationItemDataModel.HomeRecommendationProductItem.asAdsLogShowOverModel(sizePercent: Int): AdsLogShowOverModel {
    return AdsLogShowOverModel(
        // todo this value from BE
        0,
        // todo this value from BE
        "",
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
        // todo this value from BE
        0,
        // todo this value from BE
        "",
        AdsLogRealtimeClickModel.AdExtraData(
            productId = id,
            productName = name
        )
    )
}

internal fun DynamicHomeChannel.Grid.asAdsLogShowModel(): AdsLogShowModel {
    return AdsLogShowModel(
        // todo this value from BE
        0,
        // todo this value from BE
        "",
        AdsLogShowModel.AdExtraData(
            productId = id,
            productName = name
        )
    )
}

internal fun DynamicHomeChannel.Grid.asAdsLogShowOverModel(sizePercent: Int): AdsLogShowOverModel {
    return AdsLogShowOverModel(
        // todo this value from BE
        0,
        // todo this value from BE
        "",
        AdsLogShowOverModel.AdExtraData(
            productId = id,
            productName = name,
            sizePercent = sizePercent.toString()
        )
    )
}

internal fun DynamicHomeChannel.Grid.asAdsLogRealtimeClickModel(refer: String): AdsLogRealtimeClickModel {
    return AdsLogRealtimeClickModel(
        refer,
        // todo this value from BE
        0,
        // todo this value from BE
        "",
        AdsLogRealtimeClickModel.AdExtraData(
            productId = id,
            productName = name
        )
    )
}
