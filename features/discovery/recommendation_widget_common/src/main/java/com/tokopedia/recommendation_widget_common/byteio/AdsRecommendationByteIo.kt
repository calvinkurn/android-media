package com.tokopedia.recommendation_widget_common.byteio

import android.content.Context
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogRealtimeClickModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogShowModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogShowOverModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

fun RecommendationItem.sendShowAdsByteIo(context: Context?) {
    context?.let {
        if (isTopAds) {
            AppLogTopAds.sendEventShow(
                it,
                asAdsLogShowModel()
            )
        }
    }
}

fun RecommendationItem.sendShowOverAdsByteIo(context: Context?, visiblePercentage: Int) {
    context?.let {
        if (isTopAds) {
            AppLogTopAds.sendEventShowOver(
                it,
                asAdsLogShowOverModel(visiblePercentage)
            )
        }
    }
}

fun RecommendationItem.sendRealtimeClickAdsByteIo(context: Context?, refer: String) {
    context?.let {
        if (isTopAds) {
            AppLogTopAds.sendEventRealtimeClick(
                it,
                asAdsLogRealtimeClickModel(refer)
            )
        }
    }
}
