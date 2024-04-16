package com.tokopedia.home.util

import android.content.Context
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel

internal fun sendEventRealtimeClickAdsByteIo(context: Context, element: HomeRecommendationItemDataModel.HomeRecommendationProductItem?, refer: String) {
    element?.let {
        if (it.isTopAds) {
            AppLogTopAds.sendEventRealtimeClick(
                context,
                PageName.SEARCH_RESULT,
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
                PageName.SEARCH_RESULT,
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
