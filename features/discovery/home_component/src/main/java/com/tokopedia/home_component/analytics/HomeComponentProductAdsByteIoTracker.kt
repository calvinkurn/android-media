package com.tokopedia.home_component.analytics

import android.content.Context
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.home_component.analytics.TrackRecommendationMapper.asAdsLogRealtimeClickModel
import com.tokopedia.home_component.analytics.TrackRecommendationMapper.asAdsLogShowModel
import com.tokopedia.home_component.analytics.TrackRecommendationMapper.asAdsLogShowOverModel
import com.tokopedia.home_component.model.ChannelGrid

internal fun ChannelGrid?.sendEventRealtimeClickAdsByteIo(context: Context, refer: String) {
    this?.let {
        if (it.isTopads) {
            AppLogTopAds.sendEventRealtimeClick(
                context,
                PageName.HOME,
                it.asAdsLogRealtimeClickModel(refer)
            )
        }
    }
}

internal fun ChannelGrid?.sendEventShowAdsByteIo(context: Context) {
    this?.let {
        if (it.isTopads) {
            AppLogTopAds.sendEventShow(
                context,
                PageName.HOME,
                it.asAdsLogShowModel()
            )
        }
    }
}

internal fun ChannelGrid?.sendEventShowOverAdsByteIo(context: Context, visiblePercentage: Int) {
    this?.let {
        if (it.isTopads) {
            AppLogTopAds.sendEventShowOver(
                context,
                PageName.HOME,
                it.asAdsLogShowOverModel(visiblePercentage)
            )
        }
    }
}
