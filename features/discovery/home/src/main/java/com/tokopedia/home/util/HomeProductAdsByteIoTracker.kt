package com.tokopedia.home.util

import android.content.Context
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.productcard.ProductCardModel

internal fun sendEventRealtimeClickAdsByteIo(context: Context, element: HomeRecommendationItemDataModel.HomeRecommendationProductItem?, refer: String) {
    element?.let {
        if (it.isTopAds) {
            AppLogTopAds.sendEventRealtimeClick(
                context,
                PageName.SEARCH_RESULT,
                it.asAsLogRealtimeClickModel(refer)
            )
        }
    }
}

internal fun HomeRecommendationItemDataModel.HomeRecommendationProductItem.asAsLogShowModel(): AdsLogShowModel {
    return AdsLogShowModel(
        // todo this value from BE
        0,
        // todo this value from BE
        0,
        AdsLogShowModel.AdExtraData(
            productId = id
        )
    )
}

internal fun HomeRecommendationItemDataModel.HomeRecommendationProductItem.asAsLogShowOverModel(sizePercent: Int): AdsLogShowOverModel {
    return AdsLogShowOverModel(
        // todo this value from BE
        0,
        // todo this value from BE
        0,
        AdsLogShowOverModel.AdExtraData(
            productId = id,
            sizePercent = sizePercent.toString()
        )
    )
}

internal fun HomeRecommendationItemDataModel.HomeRecommendationProductItem.asAsLogRealtimeClickModel(refer: String): AdsLogRealtimeClickModel {
    return AdsLogRealtimeClickModel(
        refer,
        // todo this value from BE
        0,
        // todo this value from BE
        0,
        AdsLogRealtimeClickModel.AdExtraData(
            productId = id,
        )
    )
}
