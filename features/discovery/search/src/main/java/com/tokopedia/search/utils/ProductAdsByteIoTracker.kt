package com.tokopedia.search.utils

import android.content.Context
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.search.result.presentation.model.ProductItemDataView

internal fun sendEventRealtimeClickAdsByteIo(context: Context, element: ProductItemDataView?, refer: String) {
    element?.let {
        if (it.isAds) {
            AppLogTopAds.sendEventRealtimeClick(
                context,
                it.asAdsLogRealtimeClickModel(refer)
            )
        }
    }
}

internal fun sendEventShow(context: Context, element: ProductItemDataView?) {
    element?.let {
        if (it.isAds) {
            AppLogTopAds.sendEventShow(
                context,
                it.asAdsLogShowModel()
            )
        }
    }
}

internal fun sendEventShowOver(context: Context, element: ProductItemDataView?, maxPercentage: Int) {
    element?.let {
        if (it.isAds) {
            AppLogTopAds.sendEventShowOver(
                context,
                it.asAdsLogShowOverModel(maxPercentage)
            )
        }
    }
}
